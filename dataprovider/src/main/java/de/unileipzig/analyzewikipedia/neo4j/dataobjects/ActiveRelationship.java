package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 *
 * @author Danilo Morgado
 */
@RelationshipEntity(type="ACTIVE")
public class ActiveRelationship {
    @GraphId   private Long relationshipId;
    @Property  private String title;
    @StartNode private Entity from;
    @EndNode   private Entity to;

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

    public Entity getFrom() {
        return from;
    }

    public void setFrom(Entity from) {
        this.from = from;
    }

    public Entity getTo() {
        return to;
    }

    public void setTo(Entity to) {
        this.to = to;
    }
    
    
}
