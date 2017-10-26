package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubCategory")
public class SubCategorieObject extends Entity implements FromContainsEntity {
    
    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name = "title")
    private String title;
    
    @Relationship(type = "CONTAINS", direction = Relationship.OUTGOING)
    List<ContainsRelationship> contained;
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public SubCategorieObject() {
        this("", new ArrayList<ContainsRelationship>() );
    }
   
     public SubCategorieObject(String title) {
        this(title, new ArrayList<ContainsRelationship>());
    }
    
    public SubCategorieObject(String title, List<ContainsRelationship> contained) {
        this.title = title;
        this.contained = contained;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> methods" defaultstate="collapsed">
    @Override
    public String toString() {
        return "SubCategory{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", containedsize=" + contained.size() +
                "}";
    }
    // </editor-fold>
    
    // <editor-fold desc=">> adders" defaultstate="collapsed">
    public void addContained(ToContainsEntity entity) {
        addContained(entity, "");
    }
    
    public void addContained(ToContainsEntity entity, String title) {
        ContainsRelationship contain = new ContainsRelationship();
        contain.setFrom(this);
        contain.setTo(entity);
        contain.setTitle(title);
        contained.add(contain);
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
