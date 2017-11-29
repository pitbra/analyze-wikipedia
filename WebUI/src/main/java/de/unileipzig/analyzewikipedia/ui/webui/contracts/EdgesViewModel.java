/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.ui.webui.contracts;

/**
 *
 * @author Pit.Braunsdorf
 */
public class EdgesViewModel {
    private EntityViewModel from;
    private EntityViewModel to;
    private RelationshipType type;

    public EntityViewModel getFrom() {
        return from;
    }

    public void setFrom(EntityViewModel from) {
        this.from = from;
    }

    public EntityViewModel getTo() {
        return to;
    }

    public void setTo(EntityViewModel to) {
        this.to = to;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
    
    
}