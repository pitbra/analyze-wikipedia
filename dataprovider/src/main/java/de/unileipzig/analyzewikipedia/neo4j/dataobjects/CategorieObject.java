/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 */
@NodeEntity(label="Category")
public class CategorieObject extends Entity {

    @Property(name="title")
    private String title;

    @Relationship(type="HAS")
    List<SubCategorieObject> subArticles;
    
    public CategorieObject() {
        this.title="";
        this.subArticles = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    public CategorieObject(String title, List<SubCategorieObject> subArticles) {
        this.title = title;
        this.subArticles = subArticles;
    }
    
    public void addSubArticle(SubCategorieObject subArticle){
        subArticles.add(subArticle);
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
