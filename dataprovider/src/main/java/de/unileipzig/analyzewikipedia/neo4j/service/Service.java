/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 * @param <T>
 */
public interface Service<T extends Entity> {
    Iterable<T> findAll();
    
    T find(long id);
    //T find(String title);
    
    void delete(long id);
    //void delete(String title);
    
    T createOrUpdate(T object);
    
    T findByTitle(String title);
    
    Iterable<Entity> shortestPathTo(String sourceTitle, String destinationType, String destinationTitle);
    
    /**
     * Gibt alle Knoten zurück, auf die dieser verlinkt.
     * 
     * @param title Titel des Knotens
     * @return 
     */
    Iterable<Entity> getAllLinkedNodes(String title);
    
    /**
     * Gibt Anzahl der Knoten zurück
     * 
     * @return 
     */
    Integer getNodeCounter();
    
    /**
     * Gibt Anzahl alle Knoten des bestimmten Types zurück
     * 
     * @param label Label der Knoten
     * @return 
     */
    Iterable<Entity> getNodesWithLabel(String label);
    
    /**
     * Gibt alle Knoten ohne Verbindung zu anderen Knoten zurück
     * 
     * @return 
     */
    Iterable<Entity> getAllNodesWithoutConnection();
    
    /**
     * Gibt alle Knoten ohne Verbindung zu anderen Knoten zurück, ausser dem Activen Knoten
     * 
     * @return 
     */
    Iterable<Entity> getAllNodesWithOnlyActiveConnection();
    
    /**
     * Gibt alle aktiven Knoten zurück
     * 
     * @return 
     */
    Iterable<Entity> getAllActiveNodes();
    
    /**
     * Gibt alle Knoten mit angegebenner Verbindung zurück
     * 
     * @param type Typ der Relation
     * @param direction to or from node
     * @return 
     */
    Iterable<Entity> getAllNodesWithConnection(String type, String direction);
    
    /**
     * Gibt alle Knoten mit angegebenner Typ und suchvariable für den Title zurück
     * 
     * @param label Label der Knoten
     * @param sequence Suchstring für den Title
     * @return 
     */
    Iterable<Entity> getAllNodesByLabelAndSequence(String label, String sequence);
    
    /**
     * Gibt alle Knoten mit angegebenner Verbindung zurück
     * 
     * @param label Label of node
     * @param type Typ der Relation
     * @param direction to or from node
     * @return 
     */
    Iterable<Entity> getLabeledNodesWithConnection(String label, String type, String direction);
    
    /**
     * Gibt alle Knoten zurück die von einem Subknoten verlinkt sind
     * 
     * @return 
     */
    Iterable<Entity> getSubNodesWithConnection();
}
