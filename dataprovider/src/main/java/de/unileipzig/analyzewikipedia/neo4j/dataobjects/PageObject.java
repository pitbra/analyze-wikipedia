/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 */
@NodeEntity(label="Page")
public final class PageObject extends Entity{
    
    @Property(name = "title")
    private String title;
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Relationship(type = "PAGE_OF", direction = Relationship.OUTGOING)
    List<Entity> entities;

    public PageObject() {
        this.title = "Active";
    }
    
    @Override
    public void setPage(PageObject page) { }
    
    @Override
    public PageObject getPage() { return null; }
}

