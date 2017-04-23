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
    private final List<String> in_linklist;
    private final List<String[]> ex_linklist;
    
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
        this.in_linklist = new LinkedList();
        this.ex_linklist = new LinkedList();
        
    }
    
    /**
     * METHODE: add link to wiki intern link list
     * 
     * @param link as string
     */
    public final void addIntLink(String link){
        
        this.in_linklist.add(link);
        
    }
    
    /**
     * METHODE: add link to extern link list
     * 
     * @param link as string
     * @param description as string
     */
    public final void addExtLink(String link, String description){
        
        this.ex_linklist.add(new String[]{link, description});
        
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
        
        return this.in_linklist;
        
    }
    
    /**
     * GETTER: get extern link list
     * 
     * @return extern link list
     */
    public final List<String[]> getExtLinks(){
        
        return this.ex_linklist;
        
    }
    
}
