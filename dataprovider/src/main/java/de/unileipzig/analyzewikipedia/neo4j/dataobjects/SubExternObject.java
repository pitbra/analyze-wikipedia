package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "SubExtern")
public class SubExternObject extends Entity implements ToLinkedEntities {

    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name = "title")
    private String title;
    // </editor-fold>

    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public SubExternObject() {
        this("");
    }

    public SubExternObject(String title) {
        this.title = title;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> methods" defaultstate="collapsed">
    @Override
    public String toString() {
        return "SubExtern{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                "}";
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
