/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;

public class ExternObject implements INodeObject {

	private final NodeType _type = NodeType.Article;
	private Map<String, Object> _annotations = new HashMap<>();
	
	private UUID _uid;
	
	private ExternObject() {
	}
	
	public NodeType GetType() {
		return _type;
	}

	public Map<String, Object> GetAnnotations() {
		return _annotations;
	}

	public void AddAnnotation(String key, Object value) {
		_annotations.put(key, value);
	}
	
	public static ExternObject CreateExternObject() {
		ExternObject art = new ExternObject();
		
		art._uid = UUID.randomUUID();
		art.AddAnnotation(AnnotationKeys.UID, art._uid.toString());
		
		return art;
	}
	
	@Override
	public UUID GetUUID() {
		return _uid;
	}
}