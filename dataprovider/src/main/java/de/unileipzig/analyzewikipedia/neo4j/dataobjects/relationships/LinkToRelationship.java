/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects.relationships;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.StartNode;

/**
 *
 * @author Pit.Braunsdorf
 * @param <TStart>
 * @param <TEnd>
 */
public abstract class LinkToRelationship<TStart extends Entity, TEnd extends Entity> {
    Long id;    
    
    @StartNode
    TStart startNode;
    
    @EndNode
    TEnd endNode;

    public LinkToRelationship() {
    }
            
    public TStart getStartNode() {
        return startNode;
    }

    public TEnd getEndNode() {
        return endNode;
    }    
}
