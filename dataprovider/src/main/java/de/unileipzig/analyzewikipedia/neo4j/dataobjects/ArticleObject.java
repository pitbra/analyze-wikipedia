package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Article")
public class ArticleObject extends Entity {

    @Property(name = "title")
    private String title;

    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    List<SubArticleObject> subArticles;

    @Relationship(type = "ACTIVE", direction = Relationship.OUTGOING)
    ActiveNode active;

    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<ExternObject> externs;
    
    @Relationship(type = "LINK_TO", direction = Relationship.OUTGOING)
    List<SubExternObject> subExterns;
    
    @Relationship(type = "LINK_TO")
    List<ArticleObject> articles;
    
    @Relationship(type = "CONTAINS", direction = Relationship.INCOMING)
    List<CategorieObject> categories;


    public ArticleObject() {
        this.title = "";
        this.subArticles = new ArrayList<>();
        this.active = null;
        this.subExterns = new ArrayList<>();
        this.externs = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.articles = new ArrayList<>();
    }
    
    public void addLinkToArticle(ArticleObject article) {
        articles.add(article);
    }
    
    public void addCategorie(CategorieObject category)  {
        categories.add(category);
    }
    
    public List<CategorieObject> getCategorie() {
        return categories;
    }

    public List<SubArticleObject> getSubArticles() {
        return subArticles;
    }

    public List<SubExternObject> getSubExterns() {
        return subExterns;
    }

    public void addLinkToExtern(ExternObject extern) {
        externs.add(extern);
    }

    public void addLinkToSubExtern(SubExternObject subExtern) {
        subExterns.add(subExtern);
    }

    public List<ExternObject> getExterns() {
        return externs;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public ArticleObject(String title, List<SubArticleObject> subArticles, ActiveNode active, List<ExternObject> externs, List<SubExternObject> subExterns) {
        this.title = title;
        this.subArticles = subArticles;
        this.active = active;
        this.externs = externs;
        this.subExterns = subExterns;
    }

    public void addSubArticle(SubArticleObject subArticle) {
        subArticles.add(subArticle);
    }

    @Override
    public String toString() {
        return "Article{"
                + "id=" + getId()
                + ", title='" + title + '\''
                + ", subarticles=" + subArticles.size()
                + "}";
    }

    public boolean isActive() {
        return this.active != null;
    }

    public void SetActive(ActiveNode active) {
        this.active = active;
    }
}
