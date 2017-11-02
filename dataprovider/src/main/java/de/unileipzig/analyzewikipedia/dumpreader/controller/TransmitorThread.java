package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class TransmitorThread implements Runnable {
    
    private static final boolean DEBUG = false;
    
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
        
        ActiveNode active = (ActiveNode) searchOrCreateEntity(ActiveNode.class, "Active");
        activeTitle = active.getTitle();
        ACTIVE_SERVICE.createOrUpdate(active);
        
    }
        
    /**
     * METHOD: execution of transmitor thread
     * 
     */
    @Override
    public void run() {
        
        WikiArticle article;
        
        do{
            
            article = ThreadController.removeArticle();

            if (article != null){
                
                System.out.println("ArticleStackSize: " + ThreadController.getArticleStackSize() + "   Removed: " + article.getName());
                sendArticle(article);

            }
            
        } while(ThreadController.getReaderIsAlive() || ThreadController.getSeekersAreAlive() || (!ThreadController.getSeekersAreAlive() && !ThreadController.articleIsEmpty()) );

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
     * METHOD: seperate the wiki main article
     * 
     * @param article as object
     */
    private void sendArticle(WikiArticle dump_article){
        
        // TEST article name
        if (DEBUG) System.out.println("Article : " + dump_article.getName());

        ArticleObject main_article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, dump_article.getName());

        if (dump_article.getParent() == null && !main_article.isActive()){
            ActiveNode active = (ActiveNode) searchOrCreateEntity(ActiveNode.class, null);
            main_article.setActive(active);
            ARTICLE_SERVICE.createOrUpdate(main_article);
        }

        // travers each link to article
        for (String[] linkToArticle:dump_article.getWikiLinks()){
            // TEST article link
            if (DEBUG) System.out.println("A-Link : " + Arrays.toString(linkToArticle));

            ArticleObject article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, linkToArticle[0]);

            main_article.addLinkToArticle(article, linkToArticle[1]);
            ARTICLE_SERVICE.createOrUpdate(main_article);
        }
        
        // travers each link to subarticle
        for (String[] linkToSubArticle:dump_article.getWikiSubLinks()){
            
            // TEST subarticle link
            if (DEBUG) System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));

            ArticleObject article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, linkToSubArticle[0]);
            SubArticleObject subarticle = article.findSub(linkToSubArticle[1]);
            if (subarticle == null){
                dump_article.addWikiUnknownSubLink(linkToSubArticle[0], linkToSubArticle[1], linkToSubArticle[2]);
                continue;
            }
            
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
            ExternObject extern = (ExternObject) searchOrCreateEntity(ExternObject.class, urlSplit[0]);

            if (urlSplit[1].length() > 0) {
                // TEST subextern link
                if (DEBUG) System.out.println("sE-Link: " + urlSplit[1]);

                SubExternObject subextern = (SubExternObject) searchOrCreateEntity(SubExternObject.class, urlSplit[1], urlSplit[0]);

                extern.addSubExtern(subextern);
                EXTERN_SERVICE.createOrUpdate(extern);

                main_article.addLinkToSubExtern(subextern, linkToExtern[1]);
                ARTICLE_SERVICE.createOrUpdate(main_article);
            } else {

                main_article.addLinkToExtern(extern, linkToExtern[1]);
                ARTICLE_SERVICE.createOrUpdate(main_article);
            }
        }

        // travers each link to categorie
        for (Map.Entry<String, List<String>> cat_entry : dump_article.getCategories().entrySet()) {
            CategorieObject categorie = (CategorieObject) searchOrCreateEntity(CategorieObject.class, cat_entry.getKey());

            // travers each link to subcategorie
            for (String sub:cat_entry.getValue()){
                
                // TEST categorie link
                if (DEBUG) System.out.println("C-Link : " + cat_entry.getKey() + " : " + sub);

                SubCategorieObject subcategorie = (SubCategorieObject) searchOrCreateEntity(SubCategorieObject.class, sub, cat_entry.getKey());

                // we can change the name to the categorie, if it makes sence
                categorie.addSubCategorie(subcategorie);
                CATEGORIE_SERVICE.createOrUpdate(categorie);
                
                // we can change the name to the categorie, if it makes sence
                subcategorie.addContained(main_article);
                SUBCATEGORIE_SERVICE.createOrUpdate(subcategorie);
            }

        }
        
        // travers each subarticle
        for (WikiArticle dumd_sub_article:dump_article.getSubArticles()){
            
            sendArticle(dumd_sub_article, main_article);
            
        }
        
        // travers each unknown link to subarticle
        for (String[] unknownLinkToSubArticle:dump_article.getWikiUnknownSubLinks()){
            
            ArticleObject article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, unknownLinkToSubArticle[0]);
            
            SubArticleObject subarticle = article.findSub(unknownLinkToSubArticle[1]);
            if (subarticle != null) {
                article.addLinkToSubArticle(subarticle, unknownLinkToSubArticle[2]);
                ARTICLE_SERVICE.createOrUpdate(article);
            }
            
        }
        
        // travers each subarticle unknownlink
        for (WikiArticle dumd_sub_article:dump_article.getSubArticles()){
            
            unknownLinks(dumd_sub_article, main_article);
            
        }
            
    }
    
    /**
     * METHOD: seperate the wiki sub articles
     * 
     * @param article as object
     */
    private void unknownLinks(WikiArticle dump_sub_article, ArticleObject root_article){
        
        // travers each unknown link to subarticle
        for (String[] unknownLinkToSubArticle:dump_sub_article.getWikiUnknownSubLinks()){
            
            ArticleObject article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, unknownLinkToSubArticle[0]);
            
            SubArticleObject subarticle = article.findSub(unknownLinkToSubArticle[1]);
            if (subarticle != null) {
                article.addLinkToSubArticle(subarticle, unknownLinkToSubArticle[2]);
                ARTICLE_SERVICE.createOrUpdate(article);
            }
            
        }
        
        // travers each unknown subarticle
        for (WikiArticle dumd_sub_sub_article : dump_sub_article.getSubArticles()){
            
            unknownLinks(dumd_sub_sub_article, root_article);
            
        }
        
    }
     
    /**
     * METHOD: seperate the wiki sub articles
     * 
     * @param article as object
     */
    private void sendArticle(WikiArticle dump_sub_article, ArticleObject root_article){
        
        // TEST article name
        if (DEBUG) System.out.println("Article : " + dump_sub_article.getName());
        
        SubArticleObject sub_article = root_article.findSub(dump_sub_article.getName());
        if (sub_article == null) sub_article = (SubArticleObject) updateSubarticle(dump_sub_article);
        
        // travers each link to article
        for (String[] linkToArticle:dump_sub_article.getWikiLinks()){
            // TEST article link
            if (DEBUG) System.out.println("A-Link : " + Arrays.toString(linkToArticle));

            ArticleObject article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, linkToArticle[0]);

            sub_article.addLinkToArticle(article, linkToArticle[1]);
            SUBARTICLE_SERVICE.createOrUpdate(sub_article);
        }

        // travers each link to subarticle
        for (String[] linkToSubArticle:dump_sub_article.getWikiSubLinks()){
            // TEST subarticle link
            if (DEBUG) System.out.println("S-Link : " + Arrays.toString(linkToSubArticle));

            ArticleObject article = (ArticleObject) searchOrCreateEntity(ArticleObject.class, linkToSubArticle[0]);
            SubArticleObject subarticle = article.findSub(linkToSubArticle[1]);
            if (subarticle == null){
                dump_sub_article.addWikiUnknownSubLink(linkToSubArticle[0], linkToSubArticle[1], linkToSubArticle[2]);
                continue;
            }
            
            sub_article.addLinkToSubArticle(subarticle, linkToSubArticle[2]);
            SUBARTICLE_SERVICE.createOrUpdate(sub_article);
                        
        }

        // travers each link to extern
        for (String[] linkToExtern:dump_sub_article.getExternLinks()){
            // TEST extern link
            if (DEBUG) System.out.println("E-Link : " + Arrays.toString(linkToExtern)); 

            String[] urlSplit = splitUrl(linkToExtern[0]);
            ExternObject extern = (ExternObject) searchOrCreateEntity(ExternObject.class, urlSplit[0]);

            if (urlSplit[1].length() > 0) {
                // TEST subextern link
                if (DEBUG) System.out.println("sE-Link: " + urlSplit[1]);

                SubExternObject subextern = (SubExternObject) searchOrCreateEntity(SubExternObject.class, urlSplit[1], urlSplit[0]);

                extern.addSubExtern(subextern);
                EXTERN_SERVICE.createOrUpdate(extern);

                sub_article.addLinkToSubExtern(subextern, linkToExtern[1]);
                SUBARTICLE_SERVICE.createOrUpdate(sub_article);
            } else {

                sub_article.addLinkToExtern(extern, linkToExtern[1]);
                SUBARTICLE_SERVICE.createOrUpdate(sub_article);
            }
        }

        // travers each link to categorie
        for (Map.Entry<String, List<String>> cat_entry : dump_sub_article.getCategories().entrySet()) {
            
            CategorieObject categorie = (CategorieObject) searchOrCreateEntity(CategorieObject.class, cat_entry.getKey());

            // travers each link to subcategorie
            for (String sub:cat_entry.getValue()){
                
                // TEST categorie link
                if (DEBUG) System.out.println("C-Link : " + cat_entry.getKey() + " : " + sub);

                SubCategorieObject subcategorie = (SubCategorieObject) searchOrCreateEntity(SubCategorieObject.class, sub, cat_entry.getKey());

                // we can change the name to the categorie, if it makes sence
                categorie.addSubCategorie(subcategorie);
                CATEGORIE_SERVICE.createOrUpdate(categorie);

                // we can change the name to the categorie, if it makes sence
                subcategorie.addContained(root_article);
                SUBCATEGORIE_SERVICE.createOrUpdate(subcategorie);
            }

        }
        
        // travers each subarticle
        for (WikiArticle dumd_sub_sub_article : dump_sub_article.getSubArticles()){
            
            sendArticle(dumd_sub_sub_article, root_article);
            
        }
            
    }       
        
    private Entity searchOrCreateEntity(Class cl, String title){
        return searchOrCreateEntity(cl, title, null);
    }
    
    private Entity searchOrCreateEntity(Class cl, String title, String parent){
        Entity search = null;
        
        if (DEBUG) System.out.println("searchOrCreateEntity: " + cl.getSimpleName() + " : " + title + " : " + parent);
        
        if (cl.isAssignableFrom(ActiveNode.class)){
            String active = title;
            if (active == null) active = activeTitle;
            search = ACTIVE_SERVICE.findByTitle(active);
            if (search == null) {
                search = new ActiveNode();
                ACTIVE_SERVICE.createOrUpdate((ActiveNode)search);
            }
        }
        else if (cl.isAssignableFrom(ArticleObject.class)){
            search = ARTICLE_SERVICE.findByTitle(title);
            if (search == null) {
                search = new ArticleObject(title);
                ARTICLE_SERVICE.createOrUpdate((ArticleObject)search);
            }
        }
        else if (cl.isAssignableFrom(ExternObject.class)){
            search = EXTERN_SERVICE.findByTitle(title);
            if (search == null) {
                search = new ExternObject(title);
                EXTERN_SERVICE.createOrUpdate((ExternObject)search);
            }
        }
        else if (cl.isAssignableFrom(SubExternObject.class)){
            if (parent == null) search = SUBEXTERN_SERVICE.findByTitle(title); else search = SUBEXTERN_SERVICE.findSubTitleNode(parent, title);
            if (search == null) {
                search = new SubExternObject(title);
                SUBEXTERN_SERVICE.createOrUpdate((SubExternObject)search);
            }
        }
        else if (cl.isAssignableFrom(CategorieObject.class)){
            search = CATEGORIE_SERVICE.findByTitle(title);
            if (search == null) {
                search = new CategorieObject(title);
                CATEGORIE_SERVICE.createOrUpdate((CategorieObject)search);
            }
        }
        else if (cl.isAssignableFrom(SubCategorieObject.class)){
            if (parent == null) search = SUBCATEGORIE_SERVICE.findByTitle(title); else search = SUBCATEGORIE_SERVICE.findSubTitleNode(parent, title);
            if (search == null) {
                search = new SubCategorieObject(title);
                SUBCATEGORIE_SERVICE.createOrUpdate((SubCategorieObject)search);
            }
        }
                
        return search;
    }
    
    private SubArticleObject updateSubarticle(WikiArticle article){
        
        List<WikiArticle> list = new LinkedList();
        
        list.add(article);
        WikiArticle parent = article;
        while(parent.getParent() != null){
            parent = parent.getParent();
            list.add(parent);
        }
        
        ArticleObject article_object = (ArticleObject) searchOrCreateEntity(ArticleObject.class, list.get(list.size()-1).getName());
        
        String sub_article_title = list.get(list.size()-2).getName();
        SubArticleObject sub_article_object = article_object.findHas(sub_article_title);
        if (sub_article_object == null) {
            sub_article_object = new SubArticleObject(sub_article_title);
            SUBARTICLE_SERVICE.createOrUpdate(sub_article_object);
            article_object.addSubArticle(sub_article_object);
            ARTICLE_SERVICE.createOrUpdate(article_object);
        }
                
        if (list.size() == 2) return sub_article_object;
        
        for (int i = list.size()-2; i > 0; i--){
            sub_article_title = list.get(i-1).getName();
            SubArticleObject sub_sub_article_object = sub_article_object.findHas(sub_article_title);
            if (sub_sub_article_object == null) {
                sub_sub_article_object = new SubArticleObject(sub_article_title);
                SUBARTICLE_SERVICE.createOrUpdate(sub_sub_article_object);
                sub_article_object.addSubArticle(sub_sub_article_object);
                SUBARTICLE_SERVICE.createOrUpdate(sub_article_object);
            }
            sub_article_object = sub_sub_article_object;
        }
        
        return sub_article_object;
        
    }
            
}
