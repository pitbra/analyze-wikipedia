/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;

public class ExternSourceObject implements INodeObject {

	private final NodeType _type = NodeType.ExternSource;
	private Map<String, Object> _annotations = new HashMap<>();
	
	private UUID _uid;
	
	private ExternSourceObject() {
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
	
	public static ExternSourceObject CreateExternObject() {
		ExternSourceObject ext = new ExternSourceObject();
		
		ext._uid = UUID.randomUUID();
		ext.AddAnnotation(AnnotationKeys.UID, ext._uid.toString());
		
		return ext;
	}
	
	@Override
	public UUID GetUUID() {
		return _uid;
	}
}
