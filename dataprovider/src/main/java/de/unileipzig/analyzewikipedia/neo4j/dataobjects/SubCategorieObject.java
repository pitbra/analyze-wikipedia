package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "SubCategory")
public class SubCategorieObject extends Entity {
    @Property(name = "title")
    private String title;
    
    public SubCategorieObject() {
        this("");
    }
    
    public SubCategorieObject(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
