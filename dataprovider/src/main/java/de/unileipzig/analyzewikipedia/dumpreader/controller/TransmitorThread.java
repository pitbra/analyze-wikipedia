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
    
    private final boolean debug = false;
    
    private final String activeTitle;
    
    private final ActiveServiceImpl activeService;
    private final ArticleServiceImpl articleService;
    private final SubArticleServiceImpl subArticleService;
    private final ExternServiceImpl externService;
    private final SubExternServiceImpl subExternService;
    private final CategorieServiceImpl categorieService;
    private final SubCategorieServiceImpl subCategorieService;
        
    /**
     * KONSTRUCTOR: default
     * 
     * @throws java.lang.Exception
     */
    public TransmitorThread() throws Exception {
        
        // create article Service for article operations
        activeService = new ActiveServiceImpl();
        articleService = new ArticleServiceImpl();
        subArticleService = new SubArticleServiceImpl();
        externService = new ExternServiceImpl();
        subExternService = new SubExternServiceImpl();
        categorieService = new CategorieServiceImpl();
        subCategorieService = new SubCategorieServiceImpl();
        
        ActiveNode active = (ActiveNode) searchOrCreateEntity(new ActiveNode(), "Active");
        activeTitle = active.getTitle();
        activeService.createOrUpdate(active);
        
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
            if (debug) {
                s_lin += dump_article.getWikiLinks().size();
                s_sub += dump_article.getWikiSubLinks().size();
                s_ext += dump_article.getExternLinks().size();
                s_cat += dump_article.getCategories().size();
            }
            
            // TEST article name
            if (debug) System.out.println("Article : " + dump_article.getName());
            
            main_article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), dump_article.getName());
            
            if (!main_article.isActive()){
                ActiveNode active = (ActiveNode) searchOrCreateEntity(new ActiveNode(), "");
                main_article.SetActive(active);
                articleService.createOrUpdate(main_article);
            }
            
            // travers each link to article
            for (String[] linkToArticle:dump_article.getWikiLinks()){
                // TEST article link
                if (debug) System.out.println("A-Link : " + Arrays.toString(linkToArticle));
                
                ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToArticle[0]);
                
                main_article.addLinkToArticle(article);
                articleService.createOrUpdate(main_article);
            }
            
            // travers each link to subarticle
            for (String[] linkToSubArticle:dump_article.getWikiSubLinks()){
                // TEST article link
                if (debug) System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));
                
                ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToSubArticle[0]);                
                SubArticleObject subarticle = (SubArticleObject) searchOrCreateEntity(new SubArticleObject(), linkToSubArticle[1]);
    
                if (!article.getOwnSubArticles().contains(subarticle)){
                    article.addSubArticle(subarticle);
                    articleService.createOrUpdate(article);
                }
                
                main_article.addLinkToSubArticle(subarticle);
                articleService.createOrUpdate(main_article);
            }
            
            // travers each link to extern
            for (String linkToExtern:dump_article.getExternLinks()){
                // TEST extern link
                if (debug) System.out.println("E-Link : " + linkToExtern); 
                
                String[] urlSplit = splitUrl(linkToExtern);
                ExternObject extern = (ExternObject) searchOrCreateEntity(new ExternObject(), urlSplit[0]);
                
                if (urlSplit[1].length() > 0) {
                    // TEST extern link
                    if (debug) System.out.println("sE-Link: " + urlSplit[1]);
                        
                    SubExternObject subextern = (SubExternObject) searchOrCreateEntity(new SubExternObject(), urlSplit[1]);
                    
                    extern.addSubExtern(subextern);
                    externService.createOrUpdate(extern);
                    
                    main_article.addLinkToSubExtern(subextern);
                    articleService.createOrUpdate(main_article);
                } else {
                    main_article.addLinkToExtern(extern);
                    articleService.createOrUpdate(main_article);
                }
            }
            
            // travers each link to categorie
            for (Map.Entry<String, List<String>> cat_entry : dump_article.getCategories().entrySet()) {
                CategorieObject cat = (CategorieObject) searchOrCreateEntity(new CategorieObject(), cat_entry.getKey());
                
                // travers each link to subcategorie
                for (String sub:cat_entry.getValue()){
                    // TEST categorie link
                    if (debug) System.out.println("C-Link : " + cat_entry.getKey() + " : " + sub);
                    
                    SubCategorieObject sub_cat = (SubCategorieObject) searchOrCreateEntity(new SubCategorieObject(), sub);
                    
                    if (!cat.getSubCategorie().contains(sub_cat)){
                        cat.addSubCategorie(sub_cat);
                        categorieService.createOrUpdate(cat);
                    }
                    
                    main_article.addLinkToCategorie(sub_cat);
                    articleService.createOrUpdate(main_article);
                }
                        
            }
            
        }
        
        // travers each subarticle
        if (main_article != null){
            for (int i = 1; i < dump_page.getArticles().size(); i++){

                WikiArticle dump_subarticle = dump_page.getArticles().get(i);

                // TEST count the link sizes
                if (debug) {
                    s_lin += dump_subarticle.getWikiLinks().size();
                    s_sub += dump_subarticle.getWikiSubLinks().size();
                    s_ext += dump_subarticle.getExternLinks().size();
                    s_cat += dump_subarticle.getCategories().size();
                }

                // TEST article name
                if (debug) System.out.println("Subarticle: " + dump_subarticle.getName());
                
                // add subarticle to article
                SubArticleObject sub = (SubArticleObject) searchOrCreateEntity(new SubArticleObject(), dump_subarticle.getName());
                
                if (!main_article.getOwnSubArticles().contains(sub)){
                    main_article.addSubArticle(sub);
                    articleService.createOrUpdate(main_article);
                }
                
                // travers each link to article
                for (String[] linkToArticle:dump_subarticle.getWikiLinks()){
                    // TEST article link
                    if (debug) System.out.println("A-Link : " + Arrays.toString(linkToArticle));
                    
                    ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToArticle[0]);
                
                    sub.addLinkToArticle(article);
                    subArticleService.createOrUpdate(sub);
                }

                // travers each link to subarticle
                for (String[] linkToSubArticle:dump_subarticle.getWikiSubLinks()){
                    // TEST article link
                    if (debug) System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));
                    
                    ArticleObject article = (ArticleObject) searchOrCreateEntity(new ArticleObject(), linkToSubArticle[0]);
                    SubArticleObject subarticle = (SubArticleObject) searchOrCreateEntity(new SubArticleObject(), linkToSubArticle[1]);

                    if (!article.getOwnSubArticles().contains(subarticle)){
                        article.addSubArticle(subarticle);
                        articleService.createOrUpdate(article);
                    }
                    
                    sub.addLinkToSubArticle(subarticle);
                    subArticleService.createOrUpdate(sub);
                }

                // travers each link to extern
                for (String linkToExtern:dump_subarticle.getExternLinks()){
                    // TEST extern link
                    if (debug) System.out.println("E-Link : " + linkToExtern);
                    
                    String[] urlSplit = splitUrl(linkToExtern);
                    ExternObject extern = (ExternObject) searchOrCreateEntity(new ExternObject(), urlSplit[0]);

                    if (urlSplit[1].length() > 0) {
                        // TEST extern link
                        if (debug) System.out.println("sE-Link: " + urlSplit[1]);
                    
                        SubExternObject subextern = (SubExternObject) searchOrCreateEntity(new SubExternObject(), urlSplit[1]);

                        extern.addSubExtern(subextern);
                        externService.createOrUpdate(extern);

                        sub.addLinkToSubExtern(subextern);
                        subArticleService.createOrUpdate(sub);
                    } else {
                        sub.addLinkToExtern(extern);
                        subArticleService.createOrUpdate(sub);
                    }
                }

                // travers each link to categorie, add it to main article
                for (Map.Entry<String, List<String>> cat_entry : dump_subarticle.getCategories().entrySet()) {
                    CategorieObject cat = (CategorieObject) searchOrCreateEntity(new CategorieObject(), cat_entry.getKey());
                    
                    // travers each link to subcategorie
                    for (String sc:cat_entry.getValue()){
                        // TEST categorie link
                        if (debug) System.out.println("C-Link : " + cat_entry.getKey() + " : " + sc);
                        
                        SubCategorieObject sub_cat = (SubCategorieObject) searchOrCreateEntity(new SubCategorieObject(), sc);
                        
                        if (!cat.getSubCategorie().contains(sub_cat)){
                            cat.addSubCategorie(sub_cat);
                            categorieService.createOrUpdate(cat);
                        }
                        
                        main_article.addLinkToCategorie(sub_cat);
                        articleService.createOrUpdate(main_article);
                    }

                }

            }
            
        }
        
        // TEST out the page link stacks
        if (debug) {
            System.out.println("STACK -> L-S-E-C: " + 
                                        String.format("%04d", s_lin) + "-" + 
                                        String.format("%02d", s_sub) + "-" + 
                                        String.format("%03d", s_ext) + "-" + 
                                        String.format("%02d", s_cat) + "\t");
        }
              
    }
    
    private Entity searchOrCreateEntity(Entity node, String title){
        Entity search = null;
        
        if (node instanceof ActiveNode){
            search = activeService.findByTitle(activeTitle);
            if (search == null) {
                search = new ActiveNode();
                activeService.createOrUpdate((ActiveNode)search);
            }
        }
        else if (node instanceof ArticleObject){
            search = articleService.findByTitle(title);
            if (search == null) {
                search = new ArticleObject(title);
                articleService.createOrUpdate((ArticleObject)search);
            }
        }
        else if (node instanceof SubArticleObject){
            search = subArticleService.findByTitle(title);
            if (search == null) {
                search = new SubArticleObject(title);
                subArticleService.createOrUpdate((SubArticleObject)search);
            }
        }
        else if (node instanceof ExternObject){
            search = externService.findByTitle(title);
            if (search == null) {
                search = new ExternObject(title);
                externService.createOrUpdate((ExternObject)search);
            }
        }
        else if (node instanceof SubExternObject){
            search = subExternService.findByTitle(title);
            if (search == null) {
                search = new SubExternObject(title);
                subExternService.createOrUpdate((SubExternObject)search);
            }
        }
        else if (node instanceof CategorieObject){
            search = categorieService.findByTitle(title);
            if (search == null) {
                search = new CategorieObject(title);
                categorieService.createOrUpdate((CategorieObject)search);
            }
        }
        else if (node instanceof SubCategorieObject){
            search = subCategorieService.findByTitle(title);
            if (search == null) {
                search = new SubCategorieObject(title);
                subCategorieService.createOrUpdate((SubCategorieObject)search);
            }
        }
                
        return search;
    }
    
}
