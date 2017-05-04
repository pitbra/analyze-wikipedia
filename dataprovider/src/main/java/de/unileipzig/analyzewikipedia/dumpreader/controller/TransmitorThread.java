package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;

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
     * METHOD: execution of transmitor thread
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
            
        } while(ThreadController.getReaderIsAlive() || ThreadController.getSeekersAreAlive() || (!ThreadController.getSeekersAreAlive() && !ThreadController.pageIsEmpty()) );
        
    }
        
    /**
     * METHOD: create neo4j provider for direct access
     * 
     */
    private void createNeo4jProvider() {
        
        prov = new DataProvider(Components.getNeo4jLink(), Components.getNeo4jUser(), Components.getNeo4jPass());
        
    }
    
    /**
     * METHOD: search articlenode in neo4j graphDB
     * 
     * @param name as string
     * @return article as object
     */
    private ArticleObject searchArticleInDB(String name){
        
        return null;
        
    }
    
    /**
     * METHOD: search subarticlenode in neo4j graphDB
     * 
     * @param name as string
     * @return article as object
     */
    private SubArticleObject searchSubArticleInDB(String name){
        
        return null;
        
    }
    
    /**
     * METHOD: search subarticlenode in neo4j graphDB
     * 
     * @param url as string
     * @return article as object
     */
    private ExternObject searchExternInDB(String url){
        
        return null;
        
    }
    
    /**
     * METHOD: create article in neo4j database, if exist, return it
     * 
     * @param title as string
     * @return article as object
     */
    private ArticleObject createArticleInDB(String title){
        
        ArticleObject article = searchArticleInDB(title);
        
        if (article == null){
            article = ArticleObject.CreateArticleObject();
            article.AddAnnotation("title", title);

            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateArticle(article)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
        }
        
        return article;
                
    }
    
    /**
     * METHOD: create sub article in neo4j database, if exist, return it
     * 
     * @param title as stringarray
     * @return article as object
     */
    private SubArticleObject createSubArticleInDB(String[] title){
        
        ArticleObject article = createArticleInDB(title[0]);
        
        SubArticleObject subarticle = searchSubArticleInDB(title[1]);
        
        if (subarticle == null){
            subarticle = SubArticleObject.CreateSubArticleObject();
            subarticle.AddAnnotation("title", title[1]);
            
            prov.CreateRelationship(RelationshipType.HAS, article, subarticle);
            
            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateSubArticle(subarticle)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
        }
        
        return subarticle;
                
    }
    
    /**
     * METHOD: create node for extern link in neo4j database, if exist, return it
     * 
     * @param url as string
     * @return article as object
     */
    private ExternObject createExternNodeInDB(String url){
        
        ExternObject extern = searchExternInDB(url);
        
        if (extern == null){
            extern = ExternObject.CreateExternObject();
            extern.AddAnnotation("title", url);

            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateExternArticle(extern)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
        }
        
        return extern;
                
    }
    
    /**
     * METHOD: send wikipage content to neo4j graph database
     * 
     * @param page as object
     */
    private void sendPageContent(WikiPage page){
        
//        // create artikel
//        ArticleObject article = createArticleInDB(page.getName());
//        
//        // travers each article
//        for (WikiArticle t_art : page.getArticles()){
//            
//            // wiki title links
//            t_art.getWikiLinks().stream().map((link) -> createArticleInDB(link)).forEach((linkArticle) -> {
//                prov.CreateRelationship(RelationshipType.LINK, article, linkArticle);
//            });
//                        
//            // wiki article links
//            t_art.getSubLinks().stream().map((link) -> createSubArticleInDB(link)).forEach((linkArticle) -> {
//                prov.CreateRelationship(RelationshipType.LINK, article, linkArticle);
//            });
//            
//            // extern links
//            t_art.getExternLinks().stream().map((link) -> createExternNodeInDB(link)).forEach((linkArticle) -> {
//                prov.CreateRelationship(RelationshipType.LINK, article, linkArticle);
//            });
//
//            // categories
////            t_art.getCategories().entrySet().stream().map((cat) -> cat.getValue()).forEach((list) -> {
////                for (String entry : list){
////                    // do for each entry ...
////                }
////            });
//            
//        }
        
        //TEST
        {
            int lin = 0;
            int sub = 0;
            int ext = 0;
            int cat = 0;
            
            for (WikiArticle o_article : page.getArticles()){
                lin += o_article.getWikiLinks().size();
                sub += o_article.getSubLinks().size();
                ext += o_article.getExternLinks().size();
                cat += o_article.getCategories().size();
            }
            
            // TEST out the page data
            String title = page.getName();
            if (title.length() > 50) title = title.substring(0, 50);
            System.out.println("PAGE   : " + String.format("%-50s", title) + "\tL-S-E-C: " + 
                                            String.format("%04d", lin) + "-" + 
                                            String.format("%02d", sub) + "-" + 
                                            String.format("%03d", ext) + "-" + 
                                            String.format("%02d", cat));
            
//            for (WikiArticle o_article : page.getArticles()){
//                System.out.println("Article: " + o_article.getName());
//                o_article.getWikiLinks().forEach((t_lin) ->     { System.out.println("Link   : " + t_lin);                          });
//                o_article.getSubLinks().forEach((t_sub) ->      { System.out.println("Sub    : " + t_sub[0] + " :#: " + t_sub[1]);  });
//                o_article.getExternLinks().forEach((t_ext) ->   { System.out.println("Ext    : " + t_ext);                          });
//                o_article.getCategories().entrySet().forEach((t_cat) -> {
//                    t_cat.getValue().forEach((tmp) ->           { System.out.println("Cat    : " + t_cat.getKey() + " : " + tmp);    });
//                });  
//            }
            
        }
              
    }
    
}
