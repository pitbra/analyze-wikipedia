/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;

public class CategorieObject implements INodeObject {

	private final NodeType _type = NodeType.Categorie;
	private Map<String, Object> _annotations = new HashMap<>();
	
	private UUID _uid;
	
	private CategorieObject() {
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
	
	public static CategorieObject CreateCategorieObject() {
		CategorieObject cat = new CategorieObject();
		
		cat._uid = UUID.randomUUID();
		cat.AddAnnotation(AnnotationKeys.UID, cat._uid.toString());
		
		return cat;
	}
	
	@Override
	public UUID GetUUID() {
		return _uid;
	}
}
