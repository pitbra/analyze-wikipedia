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

import java.util.Arrays;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class TransmitorThread implements Runnable {
    
    private final DataProvider prov;
    
    /**
     * KONSTRUCTOR: default
     * 
     * @throws java.lang.Exception
     */
    public TransmitorThread() throws Exception{
        
        // create neo4j provider for network access
        prov = new DataProvider(Components.getNeo4jLink(), Components.getNeo4jUser(), Components.getNeo4jPass());                
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
     * METHOD: search typed node in neo4j graphDB, return null if not exist
     * 
     * @param name as string
     * @return article as object
     */
    private INodeObject searchNodeInDB(String type, Object obj, String name){
        
        INodeObject node = null;
        
        switch(type){
            case "article":
//// ####
////                node = (ArticleObject) ...;
                break;
            case "subarticle":
                ArticleObject article = (ArticleObject) obj;
//// ####
////                node = (SubArticleObject) ...;
                break;
            case "extern":
//// ####
////                node = (ExternObject) ...;
                break;
            case "subextern":
                ExternObject extern = (ExternObject) obj;
//// ####
////                node = (SubExternObject) ...;
                break;
            case "categorie":
//// ####
////                node = (CategorieObject) ...;
                break;
            case "subcategorie":
                CategorieObject categorie = (CategorieObject) obj;
//// ####
////                node = (SubCategorieObject) ...;
                break;
        }
        
        return node;
        
    }
    
    /**
     * METHOD: create specific node in neo4j database and return it
     * 
     * @param name as string
     * @return article as object
     */
    private INodeObject createNodeInDB(String type, Object obj, String name){
        
        INodeObject node = searchNodeInDB(type, obj, name);
                
        if (node == null){
            switch(type){
                case "article":
                    node = ArticleObject.CreateArticleObject();
                    break;
                case "subarticle":
                    node = SubArticleObject.CreateSubArticleObject();
                    break;
                case "extern":
                    node = ExternObject.CreateExternObject();
                    break;
                case "subextern":
                    node = SubExternObject.CreateSubExternObject();
                    break;
                case "categorie":
                    node = CategorieObject.CreateCategorieObject();
                    break;
                case "subcategorie":
                    node = SubCategorieObject.CreateSubCategorieObject();
                    break;
            }
            
            if (node != null) node.AddAnnotation("title", name);

            // break thread for a fix time, if areticle could'n be created
            boolean created = false;
            do{
                switch(type){
                    case "article":
                        created = prov.CreateArticle((ArticleObject) node);
                        break;
                    case "subarticle":
                        created = prov.CreateSubArticle((SubArticleObject) node);
                        break;
                    case "extern":
                        created = prov.CreateExtern((ExternObject) node);
                        break;
                    case "subextern":
                        created = prov.CreateSubExtern((SubExternObject) node);
                        break;
                    case "categorie":
                        created = prov.CreateCategorie((CategorieObject) node);
                        break;
                    case "subcategorie":
                        created = prov.CreateSubCategorie((SubCategorieObject) node);
                        break;
                }
            } while (created == false);
            
        }
        
        return node;
                
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
        INodeObject article = createNodeInDB("article", null, page.getName());
//// ####
////        article.setStatus();
        
        // travers each article
        for (WikiArticle s_art : page.getArticles()){
            
            INodeObject subarticle;
            
            if (s_art.getName().equals(page.getName())){
                subarticle = article;
            } else {
                subarticle = createNodeInDB("subarticle", article, s_art.getName());
                prov.CreateRelationship(RelationshipType.HAS, article, subarticle);
            }
            
            // wiki title links
            s_art.getWikiLinks().stream().map((link) -> createNodeInDB("article", null, link[0])).forEach((linkArticle) -> {
                prov.CreateRelationship(RelationshipType.LINK, subarticle, linkArticle);
//// ####                
////                if (link[1] != null) setLinkdDescription(link[1]);
            });
                                    
            // wiki article links
            s_art.getWikiSubLinks().stream().map((sub) -> createNodeInDB("subarticle", searchNodeInDB("subarticle", null, sub[0]), sub[1])).forEachOrdered((linkArticle) -> {
                prov.CreateRelationship(RelationshipType.LINK, subarticle, linkArticle);
            });
                        
            // extern links
            for (String ext : s_art.getExternLinks()){
                INodeObject suffix = createNodeInDB("subextern", null, splitUrl(ext)[1]);
                prov.CreateRelationship(RelationshipType.HAS, createNodeInDB("extern", null, splitUrl(ext)[0]), suffix);
                prov.CreateRelationship(RelationshipType.LINK, subarticle, suffix);
            }

            // categories
            s_art.getCategories().entrySet().forEach((cat) -> {
                INodeObject categorie = createNodeInDB("categorie", null, cat.getKey());
                
                cat.getValue().stream().map((list_element) -> createNodeInDB("subcategorie", categorie, list_element)).forEachOrdered((subcategorie) -> {
                    prov.CreateRelationship(RelationshipType.HAS, categorie, subcategorie);
                    prov.CreateRelationship(RelationshipType.LINK, article, subcategorie);
                });
            });
            
            // TEST count the link sizes
            s_lin += s_art.getWikiLinks().size();
            s_sub += s_art.getWikiSubLinks().size();
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
            o_article.getWikiLinks().forEach((t_lin) ->     { System.out.println("Link   : " + Arrays.toString(t_lin));             });
            o_article.getWikiSubLinks().forEach((t_sub) ->      { System.out.println("Sub    : " + t_sub[0] + " :#: " + t_sub[1]);  });
            o_article.getExternLinks().forEach((t_ext) ->   { System.out.println("Ext    : " + t_ext);                              });
            o_article.getCategories().entrySet().forEach((t_cat) -> {
                t_cat.getValue().forEach((tmp) ->           { System.out.println("Cat    : " + t_cat.getKey() + " : " + tmp);       });
            });  
        }
              
    }
    
}
