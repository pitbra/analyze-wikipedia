/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.Map;
import java.util.UUID;

/**
 * @author Pit.Braunsdorf
 *
 */
public interface INodeObject {
	
	/**
	 * Gets the type.
	 *
	 * @return the node type
	 */
	public NodeType GetType();

    /**
     * Gets the annotations.
     *
     * @return the map
     */
    public Map<String, Object> GetAnnotations();

    /**
     * Adds the annotation.
     *
     * @param key the key
     * @param value the value
     */
    public void AddAnnotation(String key, Object value);
    
    /**
     * Gets the UUID.
     *
     * @return the uuid
     */
    public UUID GetUUID();
}
