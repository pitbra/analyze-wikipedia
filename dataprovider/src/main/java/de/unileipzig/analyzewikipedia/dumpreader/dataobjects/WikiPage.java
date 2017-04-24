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
    
    private final List<String> linklist_toArticle;
    private final List<String[]> linklist_toSubArticle;
    private final List<String> linklist_toExtern;
    
    private final List<String[]> categorylist;
    
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
        
        this.linklist_toArticle = new LinkedList();
        this.linklist_toSubArticle = new LinkedList();
        this.linklist_toExtern = new LinkedList();
        
        this.categorylist = new LinkedList();
        
    }
    
    /**
     * METHODE: add link to wiki intern link list
     * 
     * @param link as string
     */
    public final void addIntLink(String link){
        
        this.linklist_toArticle.add(link);
        
    }
    
    /**
     * METHODE: add link to wiki intern sublink list
     * 
     * @param link as string
     * @param sublink as string
     */
    public final void addIntSubLink(String link, String sublink){
        
        this.linklist_toSubArticle.add(new String[]{link, sublink});
        
    }
    
    /**
     * METHODE: add link to extern link list
     * 
     * @param link as string
     */
    public final void addExtLink(String link){
        
        this.linklist_toExtern.add(link);
        
    }
    
    /**
     * METHODE: add category wiki category list
     * 
     * @param category as string
     * @param name as string
     */
    public final void addCategory(String category, String name){
        
        this.categorylist.add(new String[]{category, name});
        
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
     * GETTER: get wiki intern link list
     * 
     * @return wiki intern link list
     */
    public final List<String> getIntLinks(){
        
        return this.linklist_toArticle;
        
    }
    
    /**
     * GETTER: get wiki intern sublink list
     * 
     * @return wiki intern link list
     */
    public final List<String[]> getIntSubLinks(){
        
        return this.linklist_toSubArticle;
        
    }
    
    /**
     * GETTER: get extern link list
     * 
     * @return extern link list
     */
    public final List<String> getExtLinks(){
        
        return this.linklist_toExtern;
        
    }
    
    /**
     * GETTER: get wiki category list
     * 
     * @return wiki category list
     */
    public final List<String[]> getCategories(){
        
        return this.categorylist;
        
    }
    
}
