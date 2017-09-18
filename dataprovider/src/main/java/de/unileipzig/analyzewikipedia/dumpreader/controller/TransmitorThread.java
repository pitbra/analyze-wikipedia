package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ActiveNode;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubCategorieObject;

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
    
    private final  ArticleServiceImpl articleService;
    private final  SubArticleServiceImpl subArticleService;
    private final  ExternServiceImpl externService;
    private final  SubExternServiceImpl subExternService;
    private final  CategorieServiceImpl categorieService;
    private final  SubCategorieServiceImpl subCategorieService;
    
    private final ActiveNode ACTIVENODE = new ActiveNode();;
    
    /**
     * KONSTRUCTOR: default
     * 
     * @throws java.lang.Exception
     */
    public TransmitorThread() throws Exception {
        
        // create article Service for article operations
        articleService = new ArticleServiceImpl();
        subArticleService = new SubArticleServiceImpl();
        externService = new ExternServiceImpl();
        subExternService = new SubExternServiceImpl();
        categorieService = new CategorieServiceImpl();
        subCategorieService = new SubCategorieServiceImpl();
        
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
            s_lin += dump_article.getWikiLinks().size();
            s_sub += dump_article.getWikiSubLinks().size();
            s_ext += dump_article.getExternLinks().size();
            s_cat += dump_article.getCategories().size();
            
            // TEST article name
            System.out.println("Article   : " + dump_article.getName());
            
            main_article = new ArticleObject();
            main_article.setTitle(dump_article.getName());
            main_article.SetActive(ACTIVENODE);
            articleService.createOrUpdate(main_article);
            
            // travers each link to article
            for (String[] linkToArticle:dump_article.getWikiLinks()){
                // TEST article link
                System.out.println("A-Link : " + Arrays.toString(linkToArticle));
                
                ArticleObject article = new ArticleObject();
                article.setTitle(linkToArticle[0]);
                main_article.addLinkToArticle(article);
                articleService.createOrUpdate(main_article);
                articleService.createOrUpdate(article);
            }
            
            // travers each link to subarticle
            for (String[] linkToSubArticle:dump_article.getWikiSubLinks()){
                // TEST article link
                System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));
                
                ArticleObject article = new ArticleObject();
                article.setTitle(linkToSubArticle[0]);
                articleService.createOrUpdate(article);
                
                SubArticleObject subarticle = new SubArticleObject();
                subarticle.setTitle(linkToSubArticle[1]);
                subarticle.setParentArticle(article);
                subArticleService.createOrUpdate(subarticle);
                
                main_article.addLinkToSubArticle(subarticle);
                articleService.createOrUpdate(main_article);
            }
            
            // travers each link to extern
            for (String linkToExtern:dump_article.getExternLinks()){
                // TEST extern link
                System.out.println("E-Link : " + linkToExtern); 
                
                ExternObject extern = new ExternObject();
                extern.setTitle(linkToExtern);
                main_article.addLinkToExtern(extern);
                articleService.createOrUpdate(main_article);
                externService.createOrUpdate(extern);
            }
            
            // travers each link to categorie
            for (Map.Entry<String, List<String>> cat_entry : dump_article.getCategories().entrySet()) {
                CategorieObject cat = new CategorieObject();
                cat.setTitle(cat_entry.getKey());
                categorieService.createOrUpdate(cat);
                
                // travers each link to subcategorie
                for (String sub:cat_entry.getValue()){
                    // TEST categorie link
                    System.out.println("C-Link : " + cat_entry.getKey() + " : " + sub);
                    
                    SubCategorieObject sub_cat = new SubCategorieObject();
                    sub_cat.setTitle(sub);
                    sub_cat.setParentCategorie(cat);
                    main_article.addLinkToCategorie(sub_cat);
                    articleService.createOrUpdate(main_article);
                    subCategorieService.createOrUpdate(sub_cat);
                }
                        
            }
            
        }
        
        // travers each subarticle
        if (main_article != null){
            for (int i = 1; i < dump_page.getArticles().size(); i++){

                WikiArticle dump_subarticle = dump_page.getArticles().get(i);

                // TEST count the link sizes
                s_lin += dump_subarticle.getWikiLinks().size();
                s_sub += dump_subarticle.getWikiSubLinks().size();
                s_ext += dump_subarticle.getExternLinks().size();
                s_cat += dump_subarticle.getCategories().size();

                // TEST article name
                System.out.println("Subarticle: " + dump_subarticle.getName());
                
                // add subarticle to article
                SubArticleObject sub = new SubArticleObject();
                sub.setTitle(dump_subarticle.getName());
                sub.setParentArticle(main_article);
                subArticleService.createOrUpdate(sub);

                // travers each link to article
                for (String[] linkToArticle:dump_subarticle.getWikiLinks()){
                    // TEST article link
                    System.out.println("A-Link : " + Arrays.toString(linkToArticle));
                    
                    ArticleObject article = new ArticleObject();
                    article.setTitle(linkToArticle[0]);
                    articleService.createOrUpdate(article);
                    sub.addLinkToArticle(article);
                    subArticleService.createOrUpdate(sub);
                }

                // travers each link to subarticle
                for (String[] linkToSubArticle:dump_subarticle.getWikiSubLinks()){
                    // TEST article link
                    System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));
                    
                    ArticleObject article = new ArticleObject();
                    article.setTitle(linkToSubArticle[0]);
                    articleService.createOrUpdate(article);

                    SubArticleObject subarticle = new SubArticleObject();
                    subarticle.setTitle(linkToSubArticle[1]);
                    subarticle.setParentArticle(article);
                    subArticleService.createOrUpdate(subarticle);

                    sub.addLinkToSubArticle(subarticle);
                    subArticleService.createOrUpdate(sub);
                }

                // travers each link to extern
                for (String linkToExtern:dump_subarticle.getExternLinks()){
                    // TEST extern link
                    System.out.println("E-Link : " + linkToExtern);
                    
                    ExternObject extern = new ExternObject();
                    extern.setTitle(linkToExtern);
                    sub.addLinkToExtern(extern);
                    subArticleService.createOrUpdate(sub);
                    externService.createOrUpdate(extern);
                }

                // travers each link to categorie, add it to main article
                for (Map.Entry<String, List<String>> cat_entry : dump_subarticle.getCategories().entrySet()) {
                    CategorieObject cat = new CategorieObject();
                    cat.setTitle(cat_entry.getKey());
                    categorieService.createOrUpdate(cat);

                    // travers each link to subcategorie
                    for (String sc:cat_entry.getValue()){
                        // TEST categorie link
                        System.out.println("C-Link : " + cat_entry.getKey() + " : " + sc);
                        
                        SubCategorieObject sub_cat = new SubCategorieObject();
                        sub_cat.setTitle(sc);
                        sub_cat.setParentCategorie(cat);
                        main_article.addLinkToCategorie(sub_cat);
                        articleService.createOrUpdate(main_article);
                        subCategorieService.createOrUpdate(sub_cat);
                    }

                }

            }
            
        }
        
        // TEST out the page link stacks
        System.out.println("STACK -> L-S-E-C: " + 
                                        String.format("%04d", s_lin) + "-" + 
                                        String.format("%02d", s_sub) + "-" + 
                                        String.format("%03d", s_ext) + "-" + 
                                        String.format("%02d", s_cat) + "\t");
              
    }
    
}
