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
@RelationshipEntity(type="LINKED_TO")
public class LinkToReleationship {
    @GraphId   private Long relationshipId;
    @Property  private String title;
    @StartNode private FromLinkedEntities from;
    @EndNode   private ToLinkedEntities to;

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

    public FromLinkedEntities getFrom() {
        return from;
    }

    public void setFrom(FromLinkedEntities from) {
        this.from = from;
    }

    public ToLinkedEntities getTo() {
        return to;
    }

    public void setTo(ToLinkedEntities to) {
        this.to = to;
    }   
}
