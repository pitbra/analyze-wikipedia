package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 */
@NodeEntity(label="Active")
public final class ActiveNode extends Entity {
    
    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name = "title")
    private String title = "Active";
    
    @Relationship(type = "ACTIVE", direction = Relationship.OUTGOING)
    List<ActiveRelationship> activeRelationship;
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public ActiveNode() {
        this.title = "Active";
        this.activeRelationship = new ArrayList<>();
    }
    // </editor-fold>
    
    // <editor-fold desc=">> methode" defaultstate="collapsed">
    @Override
    public String toString() {
        return "Active{"
                + "id=" + getId()
                + ", title='" + title + '\''
                + ", activeRelationSize=" + activeRelationship.size()
                + "}";
    }
    
    public boolean contains(ArticleObject art) {
        for(int i = 0; i < activeRelationship.size(); ++i) {
            if(activeRelationship.get(i).getTo() == art){
                return true;
            }
        }
        return false;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> adders" defaultstate="collapsed">
    public void addArticle(ArticleObject article) {
        if (contains(article)) return;
        ActiveRelationship active = new ActiveRelationship();
        active.setFrom(this);
        active.setTo(article);
        activeRelationship.add(active);
    }
    // </editor-fold>
    
    // <editor-fold desc=">> getters" defaultstate="collapsed">
    @Override
    public String getTitle() {
        return title;
    }
    // </editor-fold>
    
}

