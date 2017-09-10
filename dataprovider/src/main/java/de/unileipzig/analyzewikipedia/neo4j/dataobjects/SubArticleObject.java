package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import java.util.List;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubArticle")
public class SubArticleObject extends Entity{    
    @Property(name = "title")
    String title;
    
    @Relationship(type = "has", direction = "INCOMING")
    ArticleObject parentArticle;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<ExternObject> externs;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<SubExternObject> subExterns;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<ArticleObject> articles;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<SubArticleObject> subArticles;
        
    public SubArticleObject() {
        this.title = "";
        this.parentArticle = null;
    }

    public SubArticleObject(String title, ArticleObject parentArticle) {
        this.title = title;
        this.parentArticle = parentArticle;
    }
    
    @Override
    public String toString() {
        return "SubArticle{" +
                "title='" + title+ '\'' +
                ", parentArticle='" + parentArticle.getTitle() + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void addLinkToExtern(ExternObject extern) {
        externs.add(extern);
    }    
    
    public void addLinkToSubExtern(SubExternObject subExtern) {
        subExterns.add(subExtern);
    }    
    
    public void addLinkToArticle(ArticleObject article) {
        articles.add(article);
    }    
    
    public void addLinkToSubArticle(SubArticleObject subArticle) {
        subArticles.add(subArticle);
    }    

    @Override
    public String getTitle() {
        return title;
    }
    
}
