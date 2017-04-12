package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;

public class SubArticleObject implements INodeObject {

	private NodeType _type = NodeType.SubArticle;
	private Map<String, Object> _annotations;
	private UUID _uid;

	public SubArticleObject() {
		_uid = UUID.randomUUID();
		
		AddAnnotation(AnnotationKeys.UID, _uid);
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

	@Override
	public UUID GetUUID() {
		return _uid;
	}

}
