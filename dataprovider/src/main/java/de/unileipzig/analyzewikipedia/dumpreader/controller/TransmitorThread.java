package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ActiveNode;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubCategorieObject;

import de.unileipzig.analyzewikipedia.neo4j.service.ActiveServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubArticleServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.ExternServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubExternServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.CategorieServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubCategorieServiceImpl;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class TransmitorThread implements Runnable {
    
    private static final boolean DEBUG = true;
    
    private static String activeTitle;
    
    private static final ActiveServiceImpl ACTIVE_SERVICE  = new ActiveServiceImpl();;
    private static final ArticleServiceImpl ARTICLE_SERVICE = new ArticleServiceImpl();
    private static final SubArticleServiceImpl SUBARTICLE_SERVICE = new SubArticleServiceImpl();
    private static final ExternServiceImpl EXTERN_SERVICE = new ExternServiceImpl();
    private static final SubExternServiceImpl SUBEXTERN_SERVICE = new SubExternServiceImpl();
    private static final CategorieServiceImpl CATEGORIE_SERVICE = new CategorieServiceImpl();
    private static final SubCategorieServiceImpl SUBCATEGORIE_SERVICE = new SubCategorieServiceImpl();
        
    /**
     * KONSTRUCTOR: default
     * 
     * @throws java.lang.Exception
     */
    public TransmitorThread() throws Exception {
        
        ActiveNode active = (ActiveNode) searchOrCreateEntity(new ActiveNode(), "Active");
        activeTitle = active.getTitle();
        ACTIVE_SERVICE.createOrUpdate(active);
        
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
     * METHODE: get domain and the suffix of url
     * 
     * @param url
     * @return string arraywith top level domain and suffix
     */
    public static String[] splitUrl(String url){
        
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
    private void sendPageContent(WikiPage dump_page){
        
        // TEST stack sizes for output
        int s_lin = 0, s_sub = 0, s_ext = 0, s_cat = 0;
        
        ArticleObject main_article = null;
        
        // create main article
        if (dump_page.getArticles().size() > 0){
            
            WikiArticle dump_article = dump_page.getArticles().get(0);
            
            // TEST count the link sizes
            if (DEBUG) {
                s_lin += dump_article.getWikiLinks().size();
                s_sub += dump_article.getWikiSubLinks().size();
                s_ext += dump_article.getExternLinks().size();
                s_cat += dump_article.getCategories().size();
            }
            
            // TEST article name
            if (DEBUG) System.out.println("Article : " + dump_article.getName());
            
            main_article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), dump_article.getName());
            
            if (!main_article.isActive()){
                ActiveNode active = (ActiveNode) searchOrCreateEntity(new ActiveNode(), null);
                main_article.setActive(active);
                ARTICLE_SERVICE.createOrUpdate(main_article);
            }
            
            // travers each link to article
            for (String[] linkToArticle:dump_article.getWikiLinks()){
                // TEST article link
                if (DEBUG) System.out.println("A-Link : " + Arrays.toString(linkToArticle));
                
                ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToArticle[0]);
                
                main_article.addLinkToArticle(article, linkToArticle[1]);
                ARTICLE_SERVICE.createOrUpdate(main_article);
            }
            
            // travers each link to subarticle
            for (String[] linkToSubArticle:dump_article.getWikiSubLinks()){
                // TEST subarticle link
                if (DEBUG) System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));
                
                ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToSubArticle[0]);                
                SubArticleObject subarticle = (SubArticleObject) searchOrCreateEntity(new SubArticleObject(), linkToSubArticle[1], linkToSubArticle[0]);
    
                article.addSubArticle(subarticle);
                ARTICLE_SERVICE.createOrUpdate(article);
                
                main_article.addLinkToSubArticle(subarticle, linkToSubArticle[2]);
                ARTICLE_SERVICE.createOrUpdate(main_article);
            }
            
            // travers each link to extern
            for (String[] linkToExtern:dump_article.getExternLinks()){
                // TEST extern link
                if (DEBUG) System.out.println("E-Link : " + Arrays.toString(linkToExtern)); 
                
                String[] urlSplit = splitUrl(linkToExtern[0]);
                ExternObject extern = (ExternObject) searchOrCreateEntity(new ExternObject(), urlSplit[0]);
                
                if (urlSplit[1].length() > 0) {
                    // TEST subextern link
                    if (DEBUG) System.out.println("sE-Link: " + urlSplit[1]);
                        
                    SubExternObject subextern = (SubExternObject) searchOrCreateEntity(new SubExternObject(), urlSplit[1], urlSplit[0]);
                    
                    extern.addSubExtern(subextern, linkToExtern[1]);
                    EXTERN_SERVICE.createOrUpdate(extern);
                    
                    // make no sence set the linktype
                    main_article.addLinkToSubExtern(subextern);
                    ARTICLE_SERVICE.createOrUpdate(main_article);
                } else {
                    
                    main_article.addLinkToExtern(extern, linkToExtern[1]);
                    ARTICLE_SERVICE.createOrUpdate(main_article);
                }
            }
            
            // travers each link to categorie
            for (Map.Entry<String, List<String>> cat_entry : dump_article.getCategories().entrySet()) {
                CategorieObject categorie = (CategorieObject) searchOrCreateEntity(new CategorieObject(), cat_entry.getKey());
                
                // travers each link to subcategorie
                for (String sub:cat_entry.getValue()){
                    // TEST categorie link
                    if (DEBUG) System.out.println("C-Link : " + cat_entry.getKey() + " : " + sub);
                    
                    SubCategorieObject subcategorie = (SubCategorieObject) searchOrCreateEntity(new SubCategorieObject(), sub, cat_entry.getKey());
                    
//                  // we can change the name to the categorie, if it makes sence
                    categorie.addSubCategorie(subcategorie);
                    CATEGORIE_SERVICE.createOrUpdate(categorie);
                    
                    // we can change the name to the categorie, if it makes sence
                    subcategorie.addContained(main_article);
                    SUBCATEGORIE_SERVICE.createOrUpdate(subcategorie);
                }
                        
            }
            
        }
        
        // travers each subarticle
        if (main_article != null){
            for (int i = 1; i < dump_page.getArticles().size(); i++){

                WikiArticle dump_subarticle = dump_page.getArticles().get(i);

                // TEST count the link sizes
                if (DEBUG) {
                    s_lin += dump_subarticle.getWikiLinks().size();
                    s_sub += dump_subarticle.getWikiSubLinks().size();
                    s_ext += dump_subarticle.getExternLinks().size();
                    s_cat += dump_subarticle.getCategories().size();
                }

                // TEST subarticle name
                if (DEBUG) System.out.println("Subarticle: " + dump_subarticle.getName());
                
                // add subarticle to article
                SubArticleObject main_subarticle = (SubArticleObject) searchOrCreateEntity(new SubArticleObject(), dump_subarticle.getName(), main_article.getTitle());
                
                main_article.addSubArticle(main_subarticle);
                ARTICLE_SERVICE.createOrUpdate(main_article);
                
                // travers each link to article
                for (String[] linkToArticle:dump_subarticle.getWikiLinks()){
                    // TEST article link
                    if (DEBUG) System.out.println("A-Link : " + Arrays.toString(linkToArticle));
                    
                    ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToArticle[0]);
                
                    main_subarticle.addLinkToArticle(article, linkToArticle[1]);
                    SUBARTICLE_SERVICE.createOrUpdate(main_subarticle);
                }

                // travers each link to subarticle
                for (String[] linkToSubArticle:dump_subarticle.getWikiSubLinks()){
                    // TEST subarticle link
                    if (DEBUG) System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));
                    
                    ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToSubArticle[0]);
                    SubArticleObject subarticle = (SubArticleObject) searchOrCreateEntity(new SubArticleObject(), linkToSubArticle[1], linkToSubArticle[0]);

                    article.addSubArticle(subarticle);
                    ARTICLE_SERVICE.createOrUpdate(article);
                    
                    main_subarticle.addLinkToSubArticle(subarticle, linkToSubArticle[2]);
                    SUBARTICLE_SERVICE.createOrUpdate(main_subarticle);
                }

                // travers each link to extern
                for (String[] linkToExtern:dump_subarticle.getExternLinks()){
                    // TEST extern link
                    if (DEBUG) System.out.println("E-Link : " + Arrays.toString(linkToExtern));
                    
                    String[] urlSplit = splitUrl(linkToExtern[0]);
                    ExternObject extern = (ExternObject) searchOrCreateEntity(new ExternObject(), urlSplit[0]);

                    if (urlSplit[1].length() > 0) {
                        // TEST subextern link
                        if (DEBUG) System.out.println("sE-Link: " + urlSplit[1]);
                    
                        SubExternObject subextern = (SubExternObject) searchOrCreateEntity(new SubExternObject(), urlSplit[1], urlSplit[0]);

                        extern.addSubExtern(subextern, linkToExtern[1]);
                        EXTERN_SERVICE.createOrUpdate(extern);

                        // make no sence set the linktype
                        main_subarticle.addLinkToSubExtern(subextern);
                        SUBARTICLE_SERVICE.createOrUpdate(main_subarticle);
                    } else {
                        main_subarticle.addLinkToExtern(extern, linkToExtern[1]);
                        SUBARTICLE_SERVICE.createOrUpdate(main_subarticle);
                    }
                }

                // travers each link to categorie, add it to main article
                for (Map.Entry<String, List<String>> cat_entry : dump_subarticle.getCategories().entrySet()) {
                    CategorieObject categorie = (CategorieObject) searchOrCreateEntity(new CategorieObject(), cat_entry.getKey());
                    
                    // travers each link to subcategorie
                    for (String sc:cat_entry.getValue()){
                        // TEST categorie link
                        if (DEBUG) System.out.println("C-Link : " + cat_entry.getKey() + " : " + sc);
                        
                        SubCategorieObject subcategorie = (SubCategorieObject) searchOrCreateEntity(new SubCategorieObject(), sc, cat_entry.getKey());
                        
                        // we can change the name to the categorie, if it makes sence
                        categorie.addSubCategorie(subcategorie);
                        CATEGORIE_SERVICE.createOrUpdate(categorie);
                        
                        // we can change the name to the categorie, if it makes sence
                        subcategorie.addContained(main_article);
                        SUBCATEGORIE_SERVICE.createOrUpdate(subcategorie);
                    }

                }

            }
            
        }
        
        // TEST out the page link stacks
        if (DEBUG) {
            System.out.println("STACK -> L-S-E-C: " + 
                                        String.format("%04d", s_lin) + "-" + 
                                        String.format("%02d", s_sub) + "-" + 
                                        String.format("%03d", s_ext) + "-" + 
                                        String.format("%02d", s_cat) + "\t");
        }
              
    }
    
    private Entity searchOrCreateEntity(Entity node, String title){
        return searchOrCreateEntity(node, title, null);
    }
    
    private Entity searchOrCreateEntity(Entity node, String title, String parent){
        Entity search = null;
        if (node instanceof ActiveNode){
            String active = title;
            if (active == null) active = activeTitle;
            search = ACTIVE_SERVICE.findByTitle(active);
            if (search == null) {
                search = new ActiveNode();
                ACTIVE_SERVICE.createOrUpdate((ActiveNode)search);
            }
        }
        else if (node instanceof ArticleObject){
            search = ARTICLE_SERVICE.findByTitle(title);
            if (search == null) {
                search = new ArticleObject(title);
                ARTICLE_SERVICE.createOrUpdate((ArticleObject)search);
            }
        }
        else if (node instanceof SubArticleObject){
            if (parent == null) search = SUBARTICLE_SERVICE.findByTitle(title); else search = SUBARTICLE_SERVICE.findSubTitleNode(parent, title);
            if (search == null) {
                search = new SubArticleObject(title);
                SUBARTICLE_SERVICE.createOrUpdate((SubArticleObject)search);
            }
        }
        else if (node instanceof ExternObject){
            search = EXTERN_SERVICE.findByTitle(title);
            if (search == null) {
                search = new ExternObject(title);
                EXTERN_SERVICE.createOrUpdate((ExternObject)search);
            }
        }
        else if (node instanceof SubExternObject){
            if (parent == null) search = SUBEXTERN_SERVICE.findByTitle(title); else search = SUBEXTERN_SERVICE.findSubTitleNode(parent, title);
            if (search == null) {
                search = new SubExternObject(title);
                SUBEXTERN_SERVICE.createOrUpdate((SubExternObject)search);
            }
        }
        else if (node instanceof CategorieObject){
            search = CATEGORIE_SERVICE.findByTitle(title);
            if (search == null) {
                search = new CategorieObject(title);
                CATEGORIE_SERVICE.createOrUpdate((CategorieObject)search);
            }
        }
        else if (node instanceof SubCategorieObject){
            if (parent == null) search = SUBCATEGORIE_SERVICE.findByTitle(title); else search = SUBCATEGORIE_SERVICE.findSubTitleNode(parent, title);
            if (search == null) {
                search = new SubCategorieObject(title);
                SUBCATEGORIE_SERVICE.createOrUpdate((SubCategorieObject)search);
            }
        }
                
        return search;
    }
    
}
