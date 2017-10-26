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

    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name="title")
    private String title;
    
    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    List<HasRelationship> hasRelationships;
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public CategorieObject() {
        this("");
    }
    
    public CategorieObject(String title) {
        this(title, new ArrayList<HasRelationship>());
    }

    public CategorieObject(String title, List<HasRelationship> hasRelaionships) {
        this.title = title;
        this.hasRelationships = hasRelaionships;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> methods" defaultstate="collapsed">
    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", subcategoriesize=" + hasRelationships.size() +
                "}";
    }

    private boolean contains(SubCategorieObject sub_cat) {
        for(int i = 0; i < hasRelationships.size(); ++i) {
            if(hasRelationships.get(i).getTo() == sub_cat){
                return true;
            }
        }
        return false;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> adders" defaultstate="collapsed">
    public void addSubCategorie(SubCategorieObject subExtern) {
        addSubCategorie(subExtern, "");
    }
    
    public void addSubCategorie(SubCategorieObject subCategorie, String title) {
        if (contains(subCategorie)) return;
        HasRelationship has = new HasRelationship();
        has.setFrom(this);
        has.setTo(subCategorie);
        has.setTitle(title);
        hasRelationships.add(has);
    }
    // </editor-fold>
    
    // <editor-fold desc=">> getters" defaultstate="collapsed">
    @Override
    public String getTitle() {
        return title;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> setters" defaultstate="collapsed">
    public void setTitle(String title) {
        this.title = title;
    }
    // </editor-fold>
        
}
