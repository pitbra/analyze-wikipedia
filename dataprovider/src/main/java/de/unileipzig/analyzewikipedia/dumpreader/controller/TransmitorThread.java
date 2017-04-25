package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;

import java.util.List;
import java.util.Map;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class TransmitorThread implements Runnable {
    
    private DataProvider prov;
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public TransmitorThread(){
        
        createNeo4jProvider();
        
    }
        
    /**
     * METHODE: execution of thread
     * 
     */
    @Override
    public void run() {
        
        WikiPage page;
        
        do{
            
            page = ThreadController.removePage();
            
            if (page != null){
                
                sendPageContent(page);
                
            }
            
        } while(ThreadController.getSeekersAreAlive() || !ThreadController.pageIsEmpty());
        
    }
        
    /**
     * METHODE: create neo4j provider for direct access
     */
    public final void createNeo4jProvider() {
        
        prov = new DataProvider(Components.getNeo4jLink(), Components.getNeo4jUser(), Components.getNeo4jPass());
        
    }
    
    /**
     * METHODE: send wikipage content to neo4j graph database
     * 
     * @param page as object
     */
    protected final void sendPageContent(WikiPage page){
        
//        // create artikel
//        ArticleObject article = ArticleObject.CreateArticleObject();		
//        article.AddAnnotation("title", page.getName());
//        prov.CreateArticle(article);
//
//        for (String link : page.getIntLinks()){
//            
//            SubArticleObject subArticle = new SubArticleObject();
//            subArticle.AddAnnotation("title", link);
//            prov.CreateSubArticle(subArticle);
//            
//            prov.CreateRelationship(RelationshipType.LINK, article, subArticle);
//            
//        }
//        
//        for (String[] link : page.getExtLinks()){
//            
//            SubArticleObject subArticle = new SubArticleObject();
//            subArticle.AddAnnotation("external", link[0]);
//            prov.CreateSubArticle(subArticle);
//            
//            prov.CreateRelationship(RelationshipType.LINK, article, subArticle);
//            
//        }       
                
        //TEST
        {
            String title = "Page: " + page.getName();

            int length = 7 - (int)(title.length()/8);
            for (int i = 0; i < length; i++) title = title + "\t";
            
            int lin = 0;
            int sub = 0;
            int ext = 0;
            int cat = 0;
            
            for (WikiArticle article : page.getArticles()){
                lin += article.getWikiLinks().size();
                sub += article.getSubLinks().size();
                ext += article.getExternLinks().size();
                cat += article.getCategories().size();
            }
            
            title = title + "\tLinks:" + lin;

            if (lin < 10) title = title + "\t\tSub:" + sub; else title = title + "\tSub:" + sub;
            if (sub < 10) title = title + "\t\tExt:" + ext; else title = title + "\tExt:" + ext;
            if (ext < 10) title = title + "\t\tCat:" + cat; else title = title + "\tCat:" + cat;

            // TEST out the page data
            System.out.println(title);
            for (WikiArticle article : page.getArticles()){
                System.out.println("Article: " + article.getName());
                for (String t_lin : article.getWikiLinks()) System.out.println("Link: " + t_lin);
                for (String[] t_sub : article.getSubLinks()) System.out.println("Sub : " + t_sub[0] + " :#: " + t_sub[1]);
                for (String t_ext : article.getExternLinks()) System.out.println("Ext : " + t_ext);
                for (Map.Entry<String, List<String>> t_cat : article.getCategories().entrySet()){
                    List<String> list = t_cat.getValue();
                    
                    for (String tmp : list){
                        
                        System.out.println("Cat :" + t_cat.getKey() + " : " + tmp);
                        
                    }
                    
                }
                
            }
            
        }
              
    }
    
}
