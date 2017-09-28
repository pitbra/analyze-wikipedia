package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "SubCategory")
public class SubCategorieObject extends Entity implements FromContainsEntity{
    @Property(name = "title")
    private String title;
    
    List<ContainsRelationship> contained;
    
    public SubCategorieObject() {
        this("", new ArrayList<ContainsRelationship>() );
    }
   
     public SubCategorieObject(String title) {
        this(title, new ArrayList<ContainsRelationship>() );
    }
    
    public SubCategorieObject(String title, List<ContainsRelationship> contained) {
        this.title = title;
        this.contained = contained;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void AddContained(ToContainsEntity entity, String title) {
        ContainsRelationship contain = new ContainsRelationship();
        contain.setFrom(this);
        contain.setTo(entity);
        contain.setTitle(title);
    }
    
}
