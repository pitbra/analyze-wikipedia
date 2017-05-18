/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Pit.Braunsdorf
 */
public final class CustomNodeObject implements INodeObject{

    private final NodeType _type;
    private Map<String, Object> _annotation;
    private String _title;
    private final String _uid;

    public CustomNodeObject() {
        _uid = UUID.randomUUID().toString();
        _type = NodeType.Custom;
        
        AddAnnotation(AnnotationKeys.UID, _uid);
    }
    
    @Override
    public NodeType GetType() {
        return _type;
    }

    @Override
    public Map<String, Object> GetAnnotations() {
        return _annotation;
    }

    @Override
    public String GetTitle() {
        return _title;
    }

    @Override
    public void SetTitle(String title) {
        _title = title;
        AddAnnotation(AnnotationKeys.TITLE, title);
    }

    @Override
    public void AddAnnotation(String key, Object value) {
        _annotation.put(key, value);
    }

    @Override
    public String GetUUID() {
        return _uid;
    }

    @Override
    public INodeObject FindSubNode(String subNode) throws Exception {
        //TODO
        return null;
    }

    @Override
    public void SetIsActive(boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
