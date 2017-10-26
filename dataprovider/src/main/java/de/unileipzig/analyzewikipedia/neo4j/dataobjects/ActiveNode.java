/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 *
 * @author Pit.Braunsdorf
 */
@NodeEntity(label="Active")
public final class ActiveNode extends Entity{
    
    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name = "title")
    private String title = "Active";
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public ActiveNode() {
        this.title = "Active";
    }
    // </editor-fold>
    
    // <editor-fold desc=">> getters" defaultstate="collapsed">
    @Override
    public String getTitle() {
        return title;
    }
    // </editor-fold>
    
}

