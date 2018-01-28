package de.unileipzig.analyzewikipedia.crawler.controller;

import static de.unileipzig.analyzewikipedia.crawler.controller.WebCrawler.checkSectionByReferences;
import static de.unileipzig.analyzewikipedia.crawler.controller.WebCrawler.loadWebfiles;
import static de.unileipzig.analyzewikipedia.crawler.controller.WebCrawler.normaliseWebfiles;
import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class WebfileThread implements Runnable {
    
    private final CrawledElement api_article;
    
    /**
     * KONSTRUCTOR: default
     * 
     * @param api_article
     */
    public WebfileThread(CrawledElement api_article){
        
        this.api_article = api_article;
        
    }
            
    /**
     * METHOD: execution of thread file reader
     */
    @Override
    public void run() {
        
        api_article.putWebfiles(loadWebfiles(api_article));
        
        normaliseWebfiles(api_article);

        checkSectionByReferences(api_article, api_article.getSections());
        
    }
    
}
