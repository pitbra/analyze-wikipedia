package de.unileipzig.analyzewikipedia.neo4j.console;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class starts and inits the java application
 */
public class Neo4JConsole {

    /**
     * MAIN: start the java file
     * 
     * @param args as string array
     */
    public static void main(String[] args) {
        ArticleObject article = new ArticleObject();
        article.setTitle("Titel");
        
        SubArticleObject subArticle = new SubArticleObject();
        subArticle.setTitle("SubArticle");
        
        article.addSubArticle(subArticle);
        
        ArticleServiceImpl service = new ArticleServiceImpl();
        service.createOrUpdate(article);
    }
    
}
