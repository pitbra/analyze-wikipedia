/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.Map;
import java.util.UUID;

public interface INodeObject {
	public NodeType GetType();

    public Map<String, Object> GetAnnotations();

    public void AddAnnotation(String key, Object value);
    
    public UUID GetUUID();
}
