package de.unileipzig.analyzewikipedia.dumpreader.dataobjects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Danilo Morgado
 * 
 * model for an article in wikipedia page
 */
public class WikiArticle {
    
    private final String articlename;
    
    private final List<String> wiki_articlelinks;
    private final List<String[]> wiki_sublinks;
    private final List<String> externlinks;
    private final Map<String, List<String>> categories;
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public WikiArticle(){
        
        this("empty");
        
    }
    
    /**
     * KONSTRUCTOR: set page name
     * 
     * @param name as string
     */
    public WikiArticle(String name){
        
        this.articlename = name;
        
        this.wiki_articlelinks = new LinkedList();
        this.wiki_sublinks = new LinkedList();
        this.externlinks = new LinkedList();
        this.categories = new HashMap();
                
    }
    
    /**
     * METHOD: add wiki link to list
     * 
     * @param link as string
     */
    public final void addWikiLink(String link){
        
        this.wiki_articlelinks.add(link);
        
    }
    
    /**
     * METHOD: add wiki sub link to list
     * 
     * @param title as string
     * @param sub as string
     */
    public final void addSubLink(String title, String sub){
        
        this.wiki_sublinks.add(new String[]{title, sub});
        
    }
    
    /**
     * METHOD: add extern link to list
     * 
     * @param link as string
     */
    public final void addExternLink(String link){
        
        this.externlinks.add(link);
        
    }
    
    /**
     * METHOD: add categorie to map
     * 
     * @param categorie as string
     * @param name as string
     */
    public final void addCategorieName(String categorie, String name){
        
        List list = this.categories.get(categorie);
        
        // if categorie not exist, create it
        if (list == null){
            
            this.categories.put(categorie, new LinkedList());
            
        }
        
        // add name to categorie list
        categories.get(categorie).add(name);
                
    }
    
    /**
     * GETTER: get name of article
     * 
     * @return articlename
     */
    public final String getName(){
        
        return this.articlename;
        
    }
    
    /**
     * GETTER: get article link list
     * 
     * @return wiki intern link list
     */
    public final List<String> getWikiLinks(){
        
        return this.wiki_articlelinks;
        
    }
    
    /**
     * GETTER: get article sub link list
     * 
     * @return wiki sub link list
     */
    public final List<String[]> getSubLinks(){
        
        return this.wiki_sublinks;
        
    }
    
    /**
     * GETTER: get extern link list
     * 
     * @return extern link list
     */
    public final List<String> getExternLinks(){
        
        return this.externlinks;
        
    }
    
    /**
     * GETTER: get categorie map
     * 
     * @return categorie map
     */
    public final Map<String, List<String>> getCategories(){
        
        return this.categories;
        
    }
    
}
