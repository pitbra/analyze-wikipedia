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
    
    private List<HasRelationship> hasRelationships;
    
    public void addSubCategorie(SubCategorieObject subExtern, String title) {
        HasRelationship has = new HasRelationship();
        has.setFrom(this);
        has.setTo(subExtern);
        has.setTitle(title);
        hasRelationships.add(has);
    }
    
    public CategorieObject() {
        this("");
    }
    
    public CategorieObject(String title) {
        this.title = title;
        this.hasRelationships = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String getTitle() {
        return title;
    }

    public CategorieObject(String title, List<HasRelationship> hasRelaionships) {
        this.title = title;
        this.hasRelationships = hasRelaionships;
    }
    
    @Override
    public String toString() {
        return "Article{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", subarticles=" + hasRelationships.size() +
                "}";
    }

    public boolean contains(SubCategorieObject sub_cat) {
        for(int i = 0; i < hasRelationships.size(); ++i) {
            if(hasRelationships.get(i).getTo() == sub_cat){
                return true;
            }
        }
        return false;
    }
    
}
