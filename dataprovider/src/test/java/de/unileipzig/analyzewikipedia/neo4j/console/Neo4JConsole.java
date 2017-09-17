/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.console;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ActiveNode;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubCategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubExternObject;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.ExternServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubExternService;
import de.unileipzig.analyzewikipedia.neo4j.service.SubExternServiceImpl;

/**
 *
 * @author Pit.Braunsdorf
 */
public class Neo4JConsole {

    public static void main(String[] args) {

        ActiveNode active = new ActiveNode();

        CategorieObject cat = new CategorieObject();
        cat.setTitle("Category");
        
        SubCategorieObject subcat = new SubCategorieObject();
        subcat.setTitle("SubCategory");
        
        subcat.setParentCategorie(cat);
        
        ArticleObject article = new ArticleObject();
        article.setTitle("Article");
        article.SetActive(active);
        
        SubArticleObject subArticle = new SubArticleObject();
        subArticle.setTitle("SubArticle");

        article.addLinkToSubArticle(subArticle);
        
        ArticleServiceImpl artService = new ArticleServiceImpl();
        artService.createOrUpdate(article);
        ExternObject extern = new ExternObject();
        extern.setTitle("Extern");

        SubExternObject subExtern = new SubExternObject("SubExtern", extern);

        extern.addSubExtern(subExtern);


        article.addLinkToExtern(extern);
        article.addLinkToSubExtern(subExtern);
        article.addLinkToCategorie(subcat);
        artService.createOrUpdate(article);

        ExternServiceImpl extService = new ExternServiceImpl();
        extService.createOrUpdate(extern);
        
        ExternObject extern2 = new ExternObject();
        extern2.setTitle("Extern 2");
        subExtern.setTitle("SubExtern 2");
        subExtern.setParentExtern(extern2);

        SubExternServiceImpl subExtService = new SubExternServiceImpl();
        subExtService.createOrUpdate(subExtern);
    }
}
