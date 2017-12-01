package de.unileipzig.analyzewikipedia.crawler.controller;

import de.unileipzig.analyzewikipedia.textanalyse.MediaWikiLanguageHelper;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread.unescapeString;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread.unreplaceText;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.ThreadController.getUrl;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.TransmitorThread.searchOrCreateEntity;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.SectionElement;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.WebFile;
import de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread;
import de.unileipzig.analyzewikipedia.dumpreader.controller.TransmitorThread;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.textanalyse.MediaWikiLanguageHelper.Language;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.textanalyse.TextConverterHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import java.net.URL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Danilo Morgado
 */
public class WebCrawler {

    private static final boolean NETWORK_AVAILABLE = false;

    //FOR TESTING
    public static void main(String[] args){
        
        plagiarismDetection(Language.DE, "Alan_Smithee");
//        plagiarismDetection(Language.DE, "Zweiter_Weltkrieg");
        
    }
    
    public static void plagiarismDetection(Language language, String neo4j_title){
        
        String title = unescapeString(neo4j_title);
        String origin_title = title;
        origin_title = unreplaceText(origin_title);
        origin_title = origin_title.replace(" ", "%20");

        ArticleObject article = null;
        if (NETWORK_AVAILABLE) article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, title);

        Fetcher.init();

        String article_language = language.toString().toLowerCase();
        
        String api_article_string = "https://" + article_language + ".wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=" + origin_title + "&redirects";
        URL api_article_url = getUrl(api_article_string);
        if (api_article_url == null){

            //DO ANYTHING BECAUSE ARTICLE API URL CREATING FAILED
            return;

        }

        //GET ARTICLE VIA WIKI API
        CrawledElement api_article;
        if (NETWORK_AVAILABLE) api_article = Fetcher.downloadAsCrawledElement(api_article_url);
        else api_article = LimitOnlineHelper.getOriginalArticleAsCrawlElement();

        api_article.setTitle(neo4j_title);
        api_article.setWebtitle(origin_title);
        api_article.setLanguage(article_language);
        
        //GET WEBLINKS VIA WIKI API
        if (NETWORK_AVAILABLE) api_article.putReflist(getAPIexternlinks(api_article));
        else api_article.putReflist(LimitOnlineHelper.getExternLinks());

        if (!Fetcher.checkStatus(api_article.getRequestCode())){

            //DO ANYTHING BECAUSE ARTICLE WAS NOT DOWNLOADABLE
            return;

        }

        if (api_article.getContent() == null){

            //DO ANYTHING BECAUSE ARTICLE CONTENT IS EMPTY
            return;

        }

        transcodeOriginArticle(api_article);

        normaliseArticleSections(api_article.getLanguage(), api_article.getSections());
        
        //GET WEBLINKS VIA HTML
        if (NETWORK_AVAILABLE) api_article.putWebfiles(loadWebfiles(api_article));
        else api_article.putWebfiles(LimitOnlineHelper.getWebfiles());

        normaliseWebfiles(api_article);

        checkSectionByReferences(api_article);

    }

    private static void checkSectionByReferences(CrawledElement article){
        
        // TODO
        System.out.println();
        
    }

    private static void normaliseArticleSections(String lang, SectionElement section){

        List<String> stem = new LinkedList();

        for (String str : section.getText()){
            stem.add((String) TextConverterHelper.normaliseText(lang, str)[0]);
        }

        section.setNormalized(stem);

        for (SectionElement sec : section.getSections()){
            normaliseArticleSections(lang, sec);
        }

    }

    private static void normaliseWebfiles(CrawledElement article){

        for (Map.Entry<URL, WebFile> entry : article.getWebfiles().entrySet()) {

            WebFile webfile = entry.getValue();

            // if status not ok, abort
            if (!Fetcher.checkStatus(webfile.getStatus()) || webfile.getOriginText().length() == 0) continue;

            Object[] obj = TextConverterHelper.normaliseText(null, webfile.getOriginText());

            webfile.setLanguage((String) obj[1]);
            webfile.setFinalText((String) obj[0]);

        }

    }

    private static Map<URL, WebFile> loadWebfiles(CrawledElement api_article){

        Map<URL, WebFile> webfiles = new HashMap();

        for (String ref : api_article.getReflist()){

            URL url = getUrl(ref);
            if (url != null) {
                Object[] download = Fetcher.download_html(url);
                WebFile webfile = new WebFile(url);
                webfile.setStatus((Integer) download[0]);
                webfile.setOriginText((String) download[1]);
                webfiles.put(url, webfile);
            }

        }

        return webfiles;

    }

    private static void transcodeOriginArticle(CrawledElement api_article){

        String revtext = getRevisionText(api_article);

        WikiArticle wiki_article = SeekerThread.generateSectionArticle(api_article.getTitle(), revtext);

        if (NETWORK_AVAILABLE) TransmitorThread.sendArticle(wiki_article);
        
        SectionElement sec_element = convertArticleToSectionElement(wiki_article);

        api_article.putSection(sec_element);

    }

    private static List<String> convertApiText(List<String> list){

        List<String> clean = new LinkedList();
        
        for(String line : list){

            line = StringEscapeUtils.unescapeHtml3(line);
            line = StringEscapeUtils.unescapeHtml4(line);

            List<String[]> cuts = new LinkedList();
            cuts.add(new String[]{"<ref", "</ref>"});
            cuts.add(new String[]{"{{", "}}"});
            cuts.add(new String[]{"{", "}"});
            
            for (String[] cut : cuts) line = TextConverterHelper.cutByTag(line, cut);

            List<String[]> reps = new LinkedList();
            reps.add(new String[]{"<u>", "</u>"});
            
            for (String[] rep : reps) line = TextConverterHelper.cleanTag(line, rep);
            
            List<String[]> inserts = new LinkedList();
            inserts.add(new String[]{"[[", "]]", "|"});
            inserts.add(new String[]{"[", "]", " "});

            for (String[] insert : inserts) line = TextConverterHelper.insertDuringTag(line, insert);

            clean.add(line);

        }

        return clean;

    }

    private static List<WebFile> searchReferences(List<String> list){

        List<WebFile> references = new LinkedList();

        for (String line : list){

            if (line.contains("<ref") && line.contains("</ref>")){

                line = line.substring(line.indexOf("<ref"), line.indexOf("</ref>"));

                line = line.replace("[", "").replace("]", "");

                String[] split = line.split("\\s+");

                for (String sp : split){
                    if (sp.contains(">")) sp = sp.substring(sp.indexOf(">")+1);
                    URL url = getUrl(sp);
                    if (url != null) {
                        references.add(new WebFile(url));
                    }
                }

            }

        }

        return references;

    }

    private static SectionElement convertArticleToSectionElement(WikiArticle article){

        SectionElement section = new SectionElement(article.getName());

        for (String text : article.getText()){

            section.addText(text);

        }

        section.setReferences(searchReferences(section.getText()));

        section.setText(convertApiText(section.getText()));

        for (WikiArticle sub_article : article.getSubArticles()){

            if ( !MediaWikiLanguageHelper.isNotMediawikiBaseTitle(sub_article.getName())){
                section.addSection(convertArticleToSectionElement(sub_article));
            }

        }

        return section;

    }

    private static String getRevisionText(CrawledElement article){

        List<String> rev = new LinkedList();

        BufferedReader br;

        try {

            br = new BufferedReader(new StringReader(article.getContent()));

            String line;

            do {
                line = br.readLine();

                if (line.trim().contains("<rev ")) {

                    String stop = "xml:space=\"preserve\">";
                    line = line.substring(line.indexOf(stop) + stop.length());

                    do {
                        if (line.trim().contains("</rev>")) {
                            rev.add(line.substring(0, line.indexOf("</rev>")));
                            break;
                        }

                        rev.add(line);

                        line = br.readLine();

                    } while(line != null);

                }

                break;

            } while(line != null);

            br.close();

        } catch (IOException ex) {
            return "";
        }

        String out = "";
        if (rev.size() == 1){

            String old_lines = rev.get(0);
            rev.clear();

            while(old_lines.length() > 0){

                if (!old_lines.contains("==")) {
                    rev.add(old_lines);
                    break;
                }

                String line_up = old_lines.substring(0, old_lines.indexOf("=="));
                if (line_up.length() > 0) rev.add(line_up);

                String start_search = old_lines.substring(old_lines.indexOf("=="));
                Integer count = 0;
                do {
                    if (start_search.substring(count, count + 1).equals("=")){
                        count++;
                    } else {
                        break;
                    }
                } while(true);

                String tmp = start_search.substring(count);
                String search_pattern = "";
                for(int i = 0; i < count ; i++) search_pattern = search_pattern + "=";

                String line_down = search_pattern + " " + tmp.substring(0, tmp.indexOf(search_pattern) + search_pattern.length());
                if (line_down.length() > 0) rev.add(line_down);

                old_lines = tmp.substring(tmp.indexOf(search_pattern) + search_pattern.length());

            }

        }

        for(String str : rev) out = out + "\n" + str;

        return out;

    }

    private static List<String> getAPIexternlinks(CrawledElement article){

        List<String> list = new LinkedList<>();

        Integer site = 0;

        String search = "extlinks";

        while (true){
            String api_extlinks_string = "https://" + article.getLanguage() + ".wikipedia.org/w/api.php?action=query&titles=" + article.getWebtitle() + "&format=xml&prop=" + search + "&eloffset=" + site;
            URL api_extlinks_url = getUrl(api_extlinks_string);
            if (api_extlinks_url == null) continue;

            List<String> search_list = Fetcher.download_xmllist(api_extlinks_url, search);

            if (search_list.isEmpty()) break;

            list.addAll(search_list);

            site += 10;

        }

        return list;

    }

}
