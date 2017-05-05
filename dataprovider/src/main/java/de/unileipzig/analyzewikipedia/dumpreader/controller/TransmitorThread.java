package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.INodeObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubCategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class TransmitorThread implements Runnable {
    
    private final Integer ID;
    
    private final DataProvider prov;
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public TransmitorThread(){
        
        this(-1);
                
    }
    
    /**
     * KONSTRUCTOR: set id
     * 
     * @param id as integer
     */
    public TransmitorThread(Integer id){
        
        this.ID = id + Components.getNeo4jPort();
        
        // create neo4j provider for network access
        prov = new DataProvider(Components.getNeo4jLink() + ":" + ID + "/", Components.getNeo4jUser(), Components.getNeo4jPass());
        
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
     * METHOD: search article node in neo4j graphDB
     * 
     * @param name as string
     * @return article as object
     */
    private INodeObject searchArticleInDB(String name){
        
        return null;
        
    }
    
    /**
     * METHOD: search subarticle node in neo4j graphDB
     * 
     * @param article as string
     * @param sub as string
     * @return article as object
     */
    private INodeObject searchSubArticleInDB(INodeObject article, String sub){
        
        return null;
        
    }
    
    /**
     * METHOD: search extern node in neo4j graphDB
     * 
     * @param url as string
     * @return article as object
     */
    private INodeObject searchExternInDB(String url){
        
        String dom = splitUrl(url)[0];
        
        return null;
        
    }
    
    /**
     * METHOD: search extern node in neo4j graphDB
     * 
     * @param url as string
     * @return article as object
     */
    private INodeObject searchSubExternInDB(String url){
        
        String sub = splitUrl(url)[1];
        
        return null;
        
    }
    
    /**
     * METHOD: search categorie in neo4j graphDB
     * 
     * @param cat as string
     * @return article as object
     */
    private INodeObject searchCategorieInDB(String cat){
        
        return null;
        
    }
    
    /**
     * METHOD: search sub categorie in neo4j graphDB
     * 
     * @param cat as object
     * @param subcat as string
     * @return article as object
     */
    private INodeObject searchSubCategorieInDB(INodeObject cat, String subcat){
        
        return null;
        
    }
    
    /**
     * METHOD: create article in neo4j database, if exist, return it
     * 
     * @param title as string
     * @return article as object
     */
    private INodeObject createArticleNodeInDB(String title){
        
        INodeObject article = searchArticleInDB(title);
        
        if (article == null){
            article = ArticleObject.CreateArticleObject();
            article.AddAnnotation("title", title);

            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateArticle((ArticleObject) article)){

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
    private INodeObject createSubArticleNodeInDB(String art, String sub){
        
        INodeObject article = createArticleNodeInDB(art);
        
        INodeObject subarticle = searchSubArticleInDB(article, sub);
        
        if (subarticle == null){
            subarticle = SubArticleObject.CreateSubArticleObject();
            subarticle.AddAnnotation("title", sub);
            
            prov.CreateRelationship(RelationshipType.HAS, article, subarticle);
            
            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateSubArticle((SubArticleObject) subarticle)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
        }
        
        return subarticle;
                
    }
    
    /**
     * METHOD: create sub article in neo4j database, if exist, return it
     * 
     * @param article as object
     * @param title as stringarray
     * @return article as object
     */
    private INodeObject createSubArticleNodeInDB(INodeObject article, String sub){
                
        INodeObject subarticle = searchSubArticleInDB(article, sub);
        
        if (subarticle == null){
            subarticle = SubArticleObject.CreateSubArticleObject();
            subarticle.AddAnnotation("title", sub);
            
            prov.CreateRelationship(RelationshipType.HAS, article, subarticle);
            
            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateSubArticle((SubArticleObject) subarticle)){

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
    private INodeObject createExternNodeInDB(String url){
        
        INodeObject extern = searchExternInDB(url);
        
        if (extern == null){
            extern = ExternObject.CreateExternObject();
            extern.AddAnnotation("title", splitUrl(url)[0]);

            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateExtern((ExternObject) extern)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
        }
        
        return extern;
                
    }
    
    /**
     * METHOD: create node for sub extern link in neo4j database, if exist, return it
     * 
     * @param url as string
     * @return article as object
     */
    private INodeObject createSubExternNodeInDB(String url){
        
        INodeObject extern = createExternNodeInDB(url);
        
        INodeObject subextern = searchSubExternInDB(url);
        
        if (subextern == null){
            subextern = SubExternObject.CreateSubExternObject();
            subextern.AddAnnotation("title", splitUrl(url)[1]);

            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateSubExtern((SubExternObject) subextern)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
            prov.CreateRelationship(RelationshipType.HAS, extern, subextern);
            
        }
        
        return subextern;
                
    }
    
    /**
     * METHOD: create node for categorie in neo4j database, if exist, return it
     * 
     * @param categorie as string
     * @return article as object
     */
    private INodeObject createCategorieNodeInDB(String categorie){
        
        INodeObject cat = searchCategorieInDB(categorie);
        
        if (cat == null){
            cat = CategorieObject.CreateCategorieObject();
            cat.AddAnnotation("title", categorie);

            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateCategorie((CategorieObject) cat)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
        }
        
        return cat;
                
    }
    
    /**
     * METHOD: create node for categorie in neo4j database, if exist, return it
     * 
     * @param categorie as string
     * @param subcategorie as string
     * @return article as object
     */
    private INodeObject createSubCategorieNodeInDB(INodeObject categorie, String subcategorie){
        
        INodeObject sub = searchSubCategorieInDB(categorie, subcategorie);
                
        if (sub == null){
            sub = SubCategorieObject.CreateSubCategorieObject();
            sub.AddAnnotation("title", subcategorie);

            // break thread for a fix time, if areticle could'n be created
            while (!prov.CreateSubCategorie((SubCategorieObject) sub)){

                ThreadController.waitMillis(Components.getThreadSleepTime());
                
            }
            
        }
        
        return sub;
                
    }
    
    /**
     * METHODE: get domain and the suffix of url
     * 
     * @param url
     * @return string arraywith top level domain and suffix
     */
    private static String[] splitUrl(String url){
        
        URL t_url;
        try {
            t_url = new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
        
        return new String[]{t_url.getHost(), t_url.getPath()};

        // EXAMPLE url = "http://example.com:80/docs/books/tutorial/index.html?name=networking#DOWNLOADING"
        // url.getProtocol());      //  http
        // url.getAuthority());     //  example.com:80
        // url.getHost());          //  example.com
        // url.getPort());          //  80
        // url.getPath());          //  /docs/books/tutorial/index.html
        // url.getQuery());         //  name=networking
        // url.getFile());          //  /docs/books/tutorial/index.html?name=networking
        // url.getRef());           //  DOWNLOADING
        
    }
    
    /**
     * METHOD: send wikipage content to neo4j graph database
     * 
     * @param page as object
     */
    private void sendPageContent(WikiPage page){
        
        // TEST out the page title
        String title = page.getName();
        if (title.length() > 50) title = title.substring(0, 50);
        System.out.println("PAGE   : " + String.format("%-50s", title));
        
        // TEST stack sizes for output
        int s_lin = 0, s_sub = 0, s_ext = 0, s_cat = 0;
        
        // create artikel
        INodeObject article = createArticleNodeInDB(page.getName());
//        article.setStatus();
        
        // travers each article
        for (WikiArticle s_art : page.getArticles()){
            
            INodeObject subarticle;
            
            if (s_art.getName().equals(page.getName())){
                subarticle = article;
            } else {
                subarticle = createSubArticleNodeInDB(article, s_art.getName());
                prov.CreateRelationship(RelationshipType.HAS, article, subarticle);
            }
            
            // wiki title links
            s_art.getWikiLinks().stream().map((link) -> createArticleNodeInDB(link)).forEach((linkArticle) -> {
                prov.CreateRelationship(RelationshipType.LINK, subarticle, linkArticle);
            });
                                    
            // wiki article links
            s_art.getSubLinks().stream().map((sub) -> createSubArticleNodeInDB(sub[0], sub[1])).forEachOrdered((linkArticle) -> {
                prov.CreateRelationship(RelationshipType.LINK, subarticle, linkArticle);
            });
                        
            // extern links
            s_art.getExternLinks().stream().map((link) -> createSubExternNodeInDB(link)).forEach((linkArticle) -> {
                prov.CreateRelationship(RelationshipType.LINK, subarticle, linkArticle);
            });

            // categories
            s_art.getCategories().entrySet().forEach((cat) -> {
                INodeObject categorie = createCategorieNodeInDB(cat.getKey());
                prov.CreateRelationship(RelationshipType.LINK, article, categorie);
                
                cat.getValue().stream().map((list_element) -> createSubCategorieNodeInDB(categorie, list_element)).forEachOrdered((subcategorie) -> {
                    prov.CreateRelationship(RelationshipType.HAS, categorie, subcategorie);
                    prov.CreateRelationship(RelationshipType.LINK, article, subcategorie);
                });
            });
            
            // TEST count the link sizes
            s_lin += s_art.getWikiLinks().size();
            s_sub += s_art.getSubLinks().size();
            s_ext += s_art.getExternLinks().size();
            s_cat += s_art.getCategories().size();

        }
        
        // TEST out the page link stacks
        System.out.println("STACK -> L-S-E-C: " + 
                                        String.format("%04d", s_lin) + "-" + 
                                        String.format("%02d", s_sub) + "-" + 
                                        String.format("%03d", s_ext) + "-" + 
                                        String.format("%02d", s_cat) + "\t");
        
        //TEST
        for (WikiArticle o_article : page.getArticles()){
            System.out.println("Article: " + o_article.getName());
            o_article.getWikiLinks().forEach((t_lin) ->     { System.out.println("Link   : " + t_lin);                          });
            o_article.getSubLinks().forEach((t_sub) ->      { System.out.println("Sub    : " + t_sub[0] + " :#: " + t_sub[1]);  });
            o_article.getExternLinks().forEach((t_ext) ->   { System.out.println("Ext    : " + t_ext);                          });
            o_article.getCategories().entrySet().forEach((t_cat) -> {
                t_cat.getValue().forEach((tmp) ->           { System.out.println("Cat    : " + t_cat.getKey() + " : " + tmp);    });
            });  
        }
              
    }
    
}
