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
    List<SubCategorieObject> ownSubCategories;
    
    public CategorieObject() {
        this("");
    }
    
    public CategorieObject(String title) {
        this.title = title;
        this.ownSubCategories = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    public CategorieObject(String title, List<SubCategorieObject> subCategories) {
        this.title = title;
        this.ownSubCategories = subCategories;
    }
    
    public void addSubCategorie(SubCategorieObject subCategories){
        this.ownSubCategories.add(subCategories);
    }
    
    public List<SubCategorieObject> getSubCategorie(){
        return this.ownSubCategories;
    }
    
    @Override
    public String toString() {
        return "Article{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", subarticles=" + ownSubCategories.size() +
                "}";
    }
    
}
