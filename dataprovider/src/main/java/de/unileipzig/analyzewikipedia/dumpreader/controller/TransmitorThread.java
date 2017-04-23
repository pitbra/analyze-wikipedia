package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neo4j.driver.v1.*;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class TransmitorThread implements Runnable {
    
    private Driver driver;
    private DataProvider prov;
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public TransmitorThread(){
        
        createNeo4jDriver();
        
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
        
        if (driver != null) driver.close();
        
    }
    
    /**
     * METHODE: create neo4j driver for direct access
     * 
     */
    public final void createNeo4jDriver() {
        
        URI boltUri;
        
        try {
            
            boltUri = new URI(Components.getNeo4jLink());
            
            driver = GraphDatabase.driver(boltUri, AuthTokens.basic(Components.getNeo4jUser(), Components.getNeo4jPass()));
        
        } catch (URISyntaxException ex) {
            Logger.getLogger(TransmitorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        
//        Session session = driver.session();
//        session.run(...).consume();

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
            String title = "Page:" + page.getName();

            int length = 7 - (int)(title.length()/8);
            for (int i = 0; i < length; i++) title = title + "\t";

            length = page.getIntLinks().size();
            title = title + "\tLinks:" + length;

            if (length < 10) title = title + "\t\tExt:" + page.getExtLinks().size(); else title = title + "\tExt:" + page.getExtLinks().size();

            // TEST out the page data
            System.out.println(title);
        }
              
    }
    
}
