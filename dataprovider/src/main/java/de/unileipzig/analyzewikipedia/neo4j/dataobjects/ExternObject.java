package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Extern")
public class ExternObject extends Entity implements ToLinkedEntities {

    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name = "title")
    private String title;
    
    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    private List<HasRelationship> hasRelationships;
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public ExternObject() {
        this("");
    }
    
    public ExternObject(String title) {
        this(title, new ArrayList<HasRelationship>());
    }
    
    public ExternObject(String title, List<HasRelationship> hasRelaionships) {
        this.title = title;
        this.hasRelationships = hasRelaionships;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> methods" defaultstate="collapsed">
    @Override
    public String toString() {
        return "Extern{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", subexternsize=" + hasRelationships.size() +
                "}";
    }
    
    private boolean contains(SubExternObject subExtern) {
        for(int i = 0; i < hasRelationships.size(); ++i) {
            if(hasRelationships.get(i).getTo() == subExtern){
                return true;
            }
        }
        return false;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> adders" defaultstate="collapsed">
    public void addSubExtern(SubExternObject subExtern) {
        addSubExtern(subExtern, "");
    }
    
    public void addSubExtern(SubExternObject subExtern, String title) {
        if (contains(subExtern)) return;
        HasRelationship has = new HasRelationship();
        has.setFrom(this);
        has.setTo(subExtern);
        has.setTitle(title);
        hasRelationships.add(has);
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
