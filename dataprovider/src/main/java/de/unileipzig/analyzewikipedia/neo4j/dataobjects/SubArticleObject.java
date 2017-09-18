package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubArticle")
public class SubArticleObject extends Entity{    
    @Property(name = "title")
    String title;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<ExternObject> externs;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<SubExternObject> subExterns;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<ArticleObject> articles;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<SubArticleObject> subArticles;
    
    public SubArticleObject() {
        this("");
    }
    
    public SubArticleObject(String title) {
        this.title = title;
        this.subArticles = new ArrayList<>();
        this.subExterns = new ArrayList<>();
        this.externs = new ArrayList<>();
        this.articles = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return "SubArticle{" +
                "title='" + title+ '\'' +
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
