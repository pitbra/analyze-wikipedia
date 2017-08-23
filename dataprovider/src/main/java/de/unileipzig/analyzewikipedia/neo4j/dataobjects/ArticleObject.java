package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.List;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label="Article")
public class ArticleObject extends Entity{
        
    @Property(name="title")
    private String title;

    @Relationship(type="HAS")
    List<SubArticleObject> subArticles;
    
    public ArticleObject() {
        this.title="";
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }

    public ArticleObject(String title, List<SubArticleObject> subArticles) {
        this.title = title;
        this.subArticles = subArticles;
    }
    
    @Override
    public String toString() {
        return "Article{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", subarticles=" + subArticles.size() +
                "}";
    }
}
