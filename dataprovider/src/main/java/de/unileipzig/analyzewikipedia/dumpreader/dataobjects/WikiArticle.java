package de.unileipzig.analyzewikipedia.dumpreader.dataobjects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Danilo Morgado
 * 
 * model for an article in wikipedia page
 */
public class WikiArticle {
    
    private final String articlename;
    
    private WikiArticle parent;
    
    private Queue<String> text;
    private final List<WikiArticle> subarticles;
    
    private final List<String[]> wiki_articlelinks;
    private final List<String[]> wiki_sublinks;
    private final List<String[]> wiki_unknownsublinks;
    private final List<String[]> externlinks;
    private final Map<String, List<String>> categories;
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public WikiArticle(){
        
        this("");
        
    }
    
    /**
     * KONSTRUCTOR: set page name
     * 
     * @param name as string
     */
    public WikiArticle(String name){
        
        this.articlename = name;
        this.parent = null;
        this.text = new LinkedList();
        this.subarticles = new LinkedList();
        this.wiki_articlelinks = new LinkedList();
        this.wiki_sublinks = new LinkedList();
        this.wiki_unknownsublinks = new LinkedList();
        this.externlinks = new LinkedList();
        this.categories = new HashMap();
                
    }
    
    /**
     * METHOD: add subarticle to article
     * 
     * @param subarticle as object
     */
    public final void addSubArticle(WikiArticle subarticle){
        
        this.subarticles.add(subarticle);
        
    }
    
    /**
     * METHOD: add wiki link to list
     * 
     * @param link as string
     * @param name as string
     */
    public final void addWikiLink(String link, String name){
        
        this.wiki_articlelinks.add(new String[]{link, name});
        
    }
    
    /**
     * METHOD: add wiki sub link to list
     * 
     * @param title as string
     * @param sub as string
     * @param name as string
     */
    public final void addWikiSubLink(String title, String sub, String name){
        
        this.wiki_sublinks.add(new String[]{title, sub, name});
        
    }
    
    /**
     * METHOD: add wiki unknown sub link to list
     * 
     * @param title as string
     * @param sub as string
     * @param name as string
     */
    public final void addWikiUnknownSubLink(String title, String sub, String name){
        
        this.wiki_unknownsublinks.add(new String[]{title, sub, name});
        
    }
    
    /**
     * METHOD: add extern link to list
     * 
     * @param link as string
     * @param filetype as string
     */
    public final void addExternLink(String link, String filetype){
        
        this.externlinks.add(new String[]{link, filetype});
        
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
     * GETTER: get subarticle list
     * 
     * @return wiki subarticles
     */
    public final List<WikiArticle> getSubArticles(){
        
        return this.subarticles;
        
    }
    
    /**
     * GETTER: get article link list
     * 
     * @return wiki intern link list
     */
    public final List<String[]> getWikiLinks(){
        
        return this.wiki_articlelinks;
        
    }
    
    /**
     * GETTER: get article sub link list
     * 
     * @return wiki sub link list
     */
    public final List<String[]> getWikiSubLinks(){
        
        return this.wiki_sublinks;
        
    }
    
    /**
     * GETTER: get article unknown sub link list
     * 
     * @return wiki unknown sub link list
     */
    public final List<String[]> getWikiUnknownSubLinks(){
        
        return this.wiki_unknownsublinks;
        
    }
    
    /**
     * GETTER: get extern link list
     * 
     * @return extern link list
     */
    public final List<String[]> getExternLinks(){
        
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
    
    /**
     * METHOD: get parent of article
     * 
     * @return parent article
     */
    public final WikiArticle getParent(){
        
        return this.parent;
        
    }
    
    /**
     * METHOD: get text of article
     * 
     * @return text of article
     */
    public final Queue<String> getText(){
        
        return this.text;
        
    }
    
    /**
     * METHOD: set parent of article
     * 
     * @param parent as wikiarticle
     */
    public final void setParent(WikiArticle parent){
        
        this.parent = parent;
        
    }
    
    /**
     * METHOD: set text of article
     * 
     * @param list as object
     */
    public final void setText(Queue<String> list){
        
        this.text = list;
        
    }
    
}
