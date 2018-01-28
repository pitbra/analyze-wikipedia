package de.unileipzig.analyzewikipedia.crawler.controller;

import static de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread.unescapeString;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread.unreplaceText;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.ThreadController.getUrl;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.TransmitorThread.searchOrCreateEntity;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.Measurement;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.SectionElement;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.WebFile;
import de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread;
import de.unileipzig.analyzewikipedia.dumpreader.controller.TransmitorThread;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.textanalyse.MediaWikiLanguageHelper;
import de.unileipzig.analyzewikipedia.textanalyse.MediaWikiLanguageHelper.Language;
import de.unileipzig.analyzewikipedia.textanalyse.TextConverterHelper;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.textanalyse.StringSimiliarityHelper;

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

    private static final CrawlDB CRAWLDB = new CrawlDB();
    
    public static String getArticleText(Language language, String neo4j_title){
        
        return getArticleText(language, neo4j_title, neo4j_title);
        
    }
    
    public static String getArticleText(Language language, String neo4j_title, String neo4j_subtitle){
        
        CrawledElement element = CRAWLDB.get(neo4j_title);
        
        if (element == null) plagiarismDetection(language, neo4j_title);
        
        element = CRAWLDB.get(neo4j_title);
        
        if (element == null) return "FAILURE";
        
        return getSectionText(element.getSections(), neo4j_subtitle);
        
    }
    
    private static String getSectionText(SectionElement section, String title){
        
        if (section.getTitle().equals(title)) return section.getText();
                
        for (SectionElement subsection : section.getSections()){
            
            return getSectionText(subsection, title);
            
        }
        
        return "";
        
    }
    
    private static void plagiarismDetection(Language language, String neo4j_title){
        
        String title = unescapeString(neo4j_title);
        String origin_title = title;
        origin_title = unreplaceText(origin_title);
        origin_title = origin_title.replace(" ", "%20");

        ArticleObject article = null;
        if (false) article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, title);

        String article_language = language.toString().toLowerCase();
        
        String api_article_string = "https://" + article_language + ".wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=xml&titles=" + origin_title + "&redirects";
        URL api_article_url = getUrl(api_article_string);
        if (api_article_url == null){

            //DO ANYTHING BECAUSE ARTICLE API URL CREATING FAILED
            return;

        }

        //GET ARTICLE VIA WIKI API
        CrawledElement api_article = Fetcher.downloadAsCrawledElement(api_article_url);

        api_article.setTitle(neo4j_title);
        api_article.setWebtitle(origin_title);
        api_article.setLanguage(article_language);
        
        if (!Fetcher.checkStatus(api_article.getRequestCode())){

            //DO ANYTHING BECAUSE ARTICLE WAS NOT DOWNLOADABLE
            return;

        }

        if (api_article.getContent() == null){

            //DO ANYTHING BECAUSE ARTICLE CONTENT IS EMPTY
            return;

        }
        
        //GET WEBLINKS VIA WIKI API
        api_article.putReflist(getAPIexternlinks(api_article));
        
        transcodeOriginArticle(api_article);

        normaliseArticleSections(api_article.getLanguage(), api_article.getSections());
        
        CRAWLDB.add(api_article);
        
        //GET WEBLINKS VIA HTML
//        Thread webfileThread = new Thread(new WebfileThread(api_article));
//        webfileThread.start();
        api_article.putWebfiles(loadWebfiles(api_article));
        normaliseWebfiles(api_article);
        checkSectionByReferences(api_article, api_article.getSections());
        
    }
    
    protected static void checkSectionByReferences(CrawledElement article, SectionElement section){

        for (WebFile wf : section.getReferences()){
        
            // check available size and correct language
            if (Fetcher.checkStatus(wf.getStatus()) && article.getLanguage().equals(wf.getLanguage())) {
            
                Measurement m = wordOverlap(section.getNormalized(), wf.getNormalized());
                wf.addMeasurmant(section, m);
                
                // highlighting the text
                if (    m.getLongestWordSequenze()  > 5     ||
                        m.getJarowinklerDistance()  > 0.7   ||
                        m.getLevensteinDistance()   > 0.3   ||
                        m.getNgramDistance()        > 0.6   ||
                        m.getNgramFrequenze()       > 0.3){
                    
                }
                
            }
            
        }
        
        for(SectionElement subsection : section.getSections()){
            checkSectionByReferences(article, subsection);
        }
        
    }
    
    private static Measurement wordOverlap(String origin, String compare){
        
        Measurement m = new Measurement();
        
        // if the text is to long, the ngram algorithm will be to long
        if (origin.split("\\s+").length <= 300){
            // 0,2s for  300 words
            // 0,4s for  400 words
            // 0,8s for  500 words
            m.setNgramFrequenze(StringSimiliarityHelper.getNgramFrequenze(origin, compare));
        }
        
        // if the text is to long, the ngram algorithm will be to long
        if (origin.split("\\s+").length <= 40 && compare.split("\\s+").length <= 30){
            //  0,3s for   30 words
            //  0,8s for   40 words
            //  1,7s for   50 words
            m.setNgramDistance(StringSimiliarityHelper.getNgramDistance(origin, compare));
        }
        
        m.setJarowinklerDistance(StringSimiliarityHelper.getJarowinklerDistance(origin, compare));
        m.setLevensteinDistance(StringSimiliarityHelper.getLevensteinDistance(origin, compare));
                
        m.setWordoverlaptext(StringSimiliarityHelper.getWordOverlapText(origin, compare)); 
        m.setLongestWordSequenze(StringSimiliarityHelper.getLongestWordSequenze(m.getWordoverlaptext()));
        
        return m;
        
    }

    private static void normaliseArticleSections(String lang, SectionElement section){

        String stem = "";

        for (String str : section.getReftext()){
            stem = stem + " " + (String) TextConverterHelper.normaliseText(lang, str)[2];
        }

        section.setNormalized(stem.trim());

        for (SectionElement sec : section.getSections()){
            normaliseArticleSections(lang, sec);
        }

    }

    protected static void normaliseWebfiles(CrawledElement article){

        for (Map.Entry<URL, WebFile> entry : article.getWebfiles().entrySet()) {

            WebFile webfile = entry.getValue();

            // if status not ok, abort
            if (!Fetcher.checkStatus(webfile.getStatus()) || webfile.getOrigin().length() == 0) continue;

            Object[] obj = TextConverterHelper.normaliseText(null, webfile.getOrigin());

            webfile.setLanguage((String) obj[1]);
            webfile.setCleaned((String) obj[0]);
            webfile.setNormalized((String) obj[2]);

        }

    }

    private static WebFile findWebfileInSections(SectionElement section, URL url){
        
        for (WebFile section_webfile : section.getReferences()){
            if (section_webfile.getUrl().equals(url)){
                return section_webfile;
            }
        }
        
        for (SectionElement sub_section : section.getSections()){
            return findWebfileInSections(sub_section, url);
        }
        
        return new WebFile(url);
        
    }
    
    protected static Map<URL, WebFile> loadWebfiles(CrawledElement api_article){

        Map<URL, WebFile> webfiles = new HashMap();

        for (String ref : api_article.getReflist()){

            while (ref.startsWith(":") || ref.startsWith("/")) ref = ref.substring(1);
            
            URL url = getUrl(ref);
            
            if (url == null) url = getUrl("http://" + ref);
            
            if (url != null) {
                Object[] download = Fetcher.download_html(url);
                
                WebFile webfile = findWebfileInSections(api_article.getSections(), url);
                webfile.setStatus((Integer) download[0]);
                webfile.setOrigin((String) download[1]);
                
                webfiles.put(url, webfile);
            }

        }

        return webfiles;

    }

    private static void transcodeOriginArticle(CrawledElement api_article){

        String revtext = getRevisionText(api_article);

        WikiArticle wiki_article = SeekerThread.generateSectionArticle(api_article.getTitle(), revtext);

        if (false) TransmitorThread.sendArticle(wiki_article);
        
        SectionElement sec_element = convertArticleToSectionElement(wiki_article);

        api_article.putSection(sec_element);

    }

    private static String convertApiText(List<String> list){

        String clean = "";
        
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
            
            clean = clean + " " + line;
            
        }
        
        return clean.trim();

    }

    private static List<WebFile> searchReferences(List<String> list){

        List<WebFile> references = new LinkedList();

        String ref_s = "<ref";
        String ref_e = "</ref>";
        
        String cit_s = "{{";
        String cit_e = "}}";
        
        for (String line : list){

            line = StringEscapeUtils.unescapeHtml3(line);
            line = StringEscapeUtils.unescapeHtml4(line);
            
            // References
            while(line.contains(ref_s) && line.contains(ref_e)){

                String ref = line.substring(line.indexOf(ref_s) + ref_s.length(), line.indexOf(ref_e));
                
                // citacion in reference
                while(ref.contains(cit_s) && ref.contains(cit_e)){
                    
                    String sub = ref.substring(ref.indexOf(cit_s) + cit_s.length(), ref.indexOf(cit_e));
                    
                    ref = ref.substring(0, ref.indexOf(cit_s)) + ref.substring(ref.indexOf(cit_e) + cit_e.length());
                    
                    String[] split = sub.split("|");
                    for (String sp : split){
                        if (sp.contains("=")) {
                            sp = sp.substring(sp.indexOf("=") + 1);
                            sp = sp.trim();
                            if (sp.contains(" ")) sp = sp.substring(0, sp.indexOf(" "));
                            URL url = getUrl(sp);
                            if (url != null) {
                                references.add(new WebFile(url));
                            }
                        }
                    }
                }
                
                line = line.substring(0, line.indexOf(ref_s)) + line.substring(line.indexOf(ref_e) + ref_e.length());
                
                ref = ref.replace("[", "").replace("]", "");

                String[] split = ref.split("\\s+");

                for (String sp : split){
                    if (sp.contains(">")) sp = sp.substring(sp.indexOf(">")+1);
                    URL url = getUrl(sp);
                    if (url != null) {
                        references.add(new WebFile(url));
                    }
                }

            }
            
            // Citate
            while(line.contains(cit_s) && line.contains(cit_e)){
                
                String cit = line.substring(line.indexOf(cit_s) + ref_s.length(), line.indexOf(cit_e));
                
                line = line.substring(0, line.indexOf(cit_s)) + line.substring(line.indexOf(cit_e) + cit_e.length());
                
                cit = cit.replace("[[", "").replace("]]", "");
                
                String[] split = cit.split("\\s+");
                
                for (String sp : split){
                    URL url = getUrl(sp);
                    if (url != null) {
                        references.add(new WebFile(url));
                        continue;
                    }
                    if (sp.contains("=")) sp = sp.substring(sp.indexOf("=")+1);
                    url = getUrl(sp);
                    if (url != null) {
                        references.add(new WebFile(url));
                        continue;
                    }
                    if (sp.contains("[")) sp = sp.substring(sp.indexOf("[")+1);
                    url = getUrl(sp);
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

            section.addReftext(text);

        }

        section.setReferences(searchReferences(section.getReftext()));

        section.setText(convertApiText(section.getReftext()));
        
        section.setHighlighted(section.getText());

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
