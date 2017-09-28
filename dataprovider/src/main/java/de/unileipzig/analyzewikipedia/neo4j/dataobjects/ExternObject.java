/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Extern")
public class ExternObject extends Entity implements ToLinkedEntities{

    @Property(name = "title")
    private String title;
    
    private List<HasRelationship> hasRelaionships;
    
    public void addSubExtern(SubExternObject subExtern, String title) {
        HasRelationship has = new HasRelationship();
        has.setFrom(this);
        has.setTo(subExtern);
        has.setTitle(title);
        hasRelaionships.add(has);
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExternObject() {
        this("");
    }
    
    public ExternObject(String title) {
        this.title = title;
        this.hasRelaionships = new ArrayList<>();
    }
}
