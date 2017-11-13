package de.unileipzig.analyzewikipedia.crawler.controller;

import static de.unileipzig.analyzewikipedia.crawler.controller.Fetcher.download_html;
import static de.unileipzig.analyzewikipedia.dumpreader.constants.Components.getWikiURL;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread.unescapeString;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.ThreadController.getUrl;
import static de.unileipzig.analyzewikipedia.dumpreader.controller.TransmitorThread.searchOrCreateEntity;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubExternServiceImpl;

import java.net.URL;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Danilo Morgado
 */
public class WebCrawler {
    
    private static final boolean DEBUG = false;
    
    public static void inWork(){
        
        String search = "Alan_Smithee";
        String title = unescapeString(search);
        
        ArticleObject article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, title);
        // DO -> ANYTHING WITH ARTICLE
        
        URL article_url = getUrl(getWikiURL() + title);
        if (article_url == null){
            // DO "article url" failed
            return;
        }
        
        Map<String, Long> weblinks_string_map = getWeblinks(title);
        if (DEBUG) {
            System.out.format("URL-Strings of %s:%n", title);
            for (Map.Entry<String, Long> entry : weblinks_string_map.entrySet()) {
                System.out.format("\t%s\t%06d%n", entry.getKey(), entry.getValue());
            }
        }
        
        Map<URL, Long> weblinks_url_map = createUrlMap(weblinks_string_map);
        if (DEBUG) {
            System.out.format("URLs of %s:%n", title);
            for (Map.Entry<URL, Long> entry : weblinks_url_map.entrySet()) {
                System.out.format("\t%s\t%06d%n", entry.getKey().toString(), entry.getValue());
            }
        }
        
        List<CrawledElement> crawls = createCrawlElements(weblinks_url_map);
        for (CrawledElement crawl : crawls){
            System.out.format("========================= %s (ID:%d) =========================%n", crawl.getUrl(), crawl.getID());
            System.out.println(crawl.getContent());
        }
        
    // TODO
        // CHECK STATUS OF WEBLINK
        
    }
    
    private static List<CrawledElement> createCrawlElements(Map<URL, Long> map){
        
        List<CrawledElement> list = new LinkedList();
        
        for (Map.Entry<URL, Long> entry : map.entrySet()) {
            list.add(createCrawlElement(entry.getKey(), entry.getValue()));
        }
        
        return list;
    }
    
    private static CrawledElement createCrawlElement(URL url, Long id){
        
        CrawledElement crawl = download_html(url);
        crawl.setID(id);
        return crawl;
        
    }
    
    private static Map<URL, Long> createUrlMap(Map<String, Long> map){
        
        Map<URL, Long> url_map = new HashMap();
        
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            URL url = getUrl(entry.getKey());
            if (url != null) url_map.put(url, entry.getValue());
        }
        
        return url_map;
        
    }
    
    private static Map<String, Long> getWeblinks(String title){
        
        ArticleServiceImpl service_art = new ArticleServiceImpl();
        SubExternServiceImpl service_subext = new SubExternServiceImpl();
        
        Map<String, Long> map = new HashMap();
        
        Iterable<Entity> result = service_art.getWeblinks(title);
        for (Entity ent : result){
            Iterable<Entity> doms = service_subext.getDomain(ent.getId());
            for (Entity dom : doms){
                map.put(dom.getTitle() + ent.getTitle(), ent.getId());
            }
        }
        
        return map;
                
    }
    
}
