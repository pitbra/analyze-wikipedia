package de.unileipzig.analyzewikipedia.dumpreader.dataobjects;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Danilo Morgado
 * 
 * model for one wikipedia page
 */
public class WikiPage {
    
    private final String page_name;
    
    private final List<WikiArticle> articles;
        
    /**
     * KONSTRUCTOR: default
     * 
     */
    public WikiPage(){
        
        this("");
        
    }
    
    /**
     * KONSTRUCTOR: set page name
     * 
     * @param name as string
     */
    public WikiPage(String name){
        
        this.page_name = name;
        
        this.articles = new LinkedList();
                
    }
    
    /**
     * METHOD: add article to page
     * 
     * @param article as object
     */
    public final void addArticle(WikiArticle article){
        
        this.articles.add(article);
        
    }
         
    /**
     * GETTER: get name of page
     * 
     * @return pagename
     */
    public final String getName(){
        
        return this.page_name;
        
    }
    
    /**
     * GETTER: get article list
     * 
     * @return wiki intern link list
     */
    public final List<WikiArticle> getArticles(){
        
        return this.articles;
        
    }
     
}
