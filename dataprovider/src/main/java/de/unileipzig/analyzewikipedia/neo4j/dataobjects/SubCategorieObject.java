package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;

public class SubCategorieObject implements INodeObject {

	private NodeType _type = NodeType.SubCategorie;
	private Map<String, Object> _annotations = new HashMap<>();
	private UUID _uid;

	public SubCategorieObject() {
		_uid = UUID.randomUUID();
		
		AddAnnotation(AnnotationKeys.UID, _uid.toString());
	}
	
	@Override
	public NodeType GetType() {
		return _type;
	}

	@Override
	public Map<String, Object> GetAnnotations() {
		return _annotations;
	}

	@Override
	public void AddAnnotation(String key, Object value) {
		_annotations.put(key, value);
	}

        public static SubCategorieObject CreateSubCategorieObject() {
		SubCategorieObject sub = new SubCategorieObject();
		
		sub._uid = UUID.randomUUID();
		sub.AddAnnotation(AnnotationKeys.UID, sub._uid.toString());
		
		return sub;
	}
        
	@Override
	public UUID GetUUID() {
		return _uid;
	}

}