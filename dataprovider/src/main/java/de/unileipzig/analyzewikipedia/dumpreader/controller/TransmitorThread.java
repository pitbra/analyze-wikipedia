package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ActiveNode;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.PageObject;
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
    
    private ActiveNode activeNode = new ActiveNode();;
    
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
        
        // create page
        PageObject page = new PageObject();
        page.setTitle(dump_page.getName());
        
        // travers each article
        for (WikiArticle dump_article : dump_page.getArticles()){
            
            // TEST count the link sizes
            s_lin += dump_article.getWikiLinks().size();
            s_sub += dump_article.getWikiSubLinks().size();
            s_ext += dump_article.getExternLinks().size();
            s_cat += dump_article.getCategories().size();
            
            // add article to page
            ArticleObject article = new ArticleObject();
            article.setTitle(dump_article.getName());
            article.setPage(page);
            
            article.SetActive(activeNode);
            
            // travers each link to article
            for (String[] linkToArticle:dump_article.getWikiLinks()){
                ArticleObject article_s = new ArticleObject();
                article_s.setTitle(linkToArticle[0]);
                
                article.addLinkToArticle(article_s);
                
                articleService.createOrUpdate(article_s);
            }
            
            // travers each link to subarticle
            for (String[] linkToSubArticle:dump_article.getWikiSubLinks()){
                SubArticleObject subArticle = new SubArticleObject();
                subArticle.setTitle(linkToSubArticle[0]);
                
                article.addLinkToSubArticle(subArticle);
                
                subArticleService.createOrUpdate(subArticle);
            }
            
            // travers each link to extern
            for (String linkToExtern:dump_article.getExternLinks()){
                ExternObject extern = new ExternObject();
                extern.setTitle(linkToExtern);
                
                article.addLinkToExtern(extern);
                
                externService.createOrUpdate(extern);
            }
            
            // travers each link to categorie
            for (Map.Entry<String, List<String>> cat_entry : dump_article.getCategories().entrySet()) {

                CategorieObject cat = new CategorieObject();
                cat.setTitle(cat_entry.getKey());
                
                categorieService.createOrUpdate(cat);
                
                // travers each link to subcategorie
                for (String sub:cat_entry.getValue()){
                    SubCategorieObject sub_cat = new SubCategorieObject();
                    sub_cat.setTitle(sub);
                    
                    article.addLinkToCategorie(sub_cat);
                    sub_cat.setParentCategorie(cat);
                    
                    subCategorieService.createOrUpdate(sub_cat);
                }
                        
            }
            
            // update in DB
            articleService.createOrUpdate(article);
            
        }
        
        // TEST out the page title
        String title = dump_page.getName();
        if (title.length() > 50) title = title.substring(0, 50);
        System.out.println("PAGE   : " + String.format("%-50s", title));
        
        // TEST out the page link stacks
        System.out.println("STACK -> L-S-E-C: " + 
                                        String.format("%04d", s_lin) + "-" + 
                                        String.format("%02d", s_sub) + "-" + 
                                        String.format("%03d", s_ext) + "-" + 
                                        String.format("%02d", s_cat) + "\t");
        
        //TEST
        for (WikiArticle o_article : dump_page.getArticles()){
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
