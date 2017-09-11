/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;


/**
 *
 * @author Pit.Braunsdorf
 */
public abstract class Entity {
    @GraphId private Long _id;
    
    public Entity() {
    }
        
    public Long getId() {
        return _id;
    }
    
    public abstract String getTitle();
    
    public abstract void setPage(PageObject page);
    public abstract PageObject getPage();
}
