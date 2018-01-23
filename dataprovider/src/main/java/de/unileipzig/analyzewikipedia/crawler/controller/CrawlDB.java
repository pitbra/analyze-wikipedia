package de.unileipzig.analyzewikipedia.crawler.controller;

import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Danilo Morgado
 */
public class CrawlDB {
    
    private static final Integer MAXIMUM_QUEUE_ELEMENTS = 100;
    
    private final Queue<CrawledElement> ELEMENTS = new ConcurrentLinkedQueue<>();
    
    public void add(CrawledElement element){
        
        ELEMENTS.add(element);
        
        if (ELEMENTS.size() > MAXIMUM_QUEUE_ELEMENTS) ELEMENTS.remove();
                
    }
    
    public CrawledElement get(String title){
        
        for(CrawledElement element : ELEMENTS) {
            if (element.getTitle().equals(title)) return element;
        }
        
        return null;
        
    }
    
}
