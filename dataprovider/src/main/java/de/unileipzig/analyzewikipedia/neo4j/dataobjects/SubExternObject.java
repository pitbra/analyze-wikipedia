package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "SubExtern")
public class SubExternObject extends Entity implements ToLinkedEntities{

    @Property(name = "title")
    private String title;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SubExternObject() {
        this("");
    }

    public SubExternObject(String title) {
        this.title = title;
    }
    
}
