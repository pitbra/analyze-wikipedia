package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;

public class SubExternObject implements INodeObject {

	private NodeType _type = NodeType.SubExternSource;
	private Map<String, Object> _annotations = new HashMap<>();
	private UUID _uid;

	public SubExternObject() {
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

        public static SubExternObject CreateSubExternObject() {
		SubExternObject ext = new SubExternObject();
		
		ext._uid = UUID.randomUUID();
		ext.AddAnnotation(AnnotationKeys.UID, ext._uid.toString());
		
		return ext;
	}
        
	@Override
	public UUID GetUUID() {
		return _uid;
	}

}
