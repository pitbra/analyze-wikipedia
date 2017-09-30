/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 *
 * @author Pit.Braunsdorf
 */
@RelationshipEntity(type="CONTAINS")
public class ContainsRelationship {
    @GraphId   private Long relationshipId;
    @Property  private String title;
    @StartNode private FromContainsEntity from;
    @EndNode   private ToContainsEntity to;

    public Long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FromContainsEntity getFrom() {
        return from;
    }

    public void setFrom(FromContainsEntity from) {
        this.from = from;
    }

    public ToContainsEntity getTo() {
        return to;
    }

    public void setTo(ToContainsEntity to) {
        this.to = to;
    }
    
    
}
