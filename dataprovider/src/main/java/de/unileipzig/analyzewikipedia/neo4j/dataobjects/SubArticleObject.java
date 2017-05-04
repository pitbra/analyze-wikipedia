package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;

public class SubArticleObject implements INodeObject {

	private NodeType _type = NodeType.SubArticle;
	private Map<String, Object> _annotations = new HashMap<>();
	private UUID _uid;

	public SubArticleObject() {
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

        public static SubArticleObject CreateSubArticleObject() {
		SubArticleObject art = new SubArticleObject();
		
		art._uid = UUID.randomUUID();
		art.AddAnnotation(AnnotationKeys.UID, art._uid.toString());
		
		return art;
	}
        
	@Override
	public UUID GetUUID() {
		return _uid;
	}

}
