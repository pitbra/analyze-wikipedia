/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import org.neo4j.driver.v1.types.Node;

public class ExternObject implements INodeObject {

    private final NodeType _type = NodeType.ExternSource;
    private final Map<String, Object> _annotations;

    private String _uid;
    private String _title;

    private ExternObject() {
        this._annotations = new HashMap<>();
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

    public static ExternObject CreateExternObject() {
        ExternObject ext = new ExternObject();

        ext._uid = UUID.randomUUID().toString();
        ext.AddAnnotation(AnnotationKeys.UID, ext._uid);

        return ext;
    }

    @Override
    public String GetUUID() {
        return _uid;
    }

    static INodeObject FromNode(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public INodeObject FindSubNode(String subNode) throws Exception {
        return INodeObject.FindSubNode(this, subNode);
    }

    @Override
    public void SetIsActive(boolean isActive) throws Exception {
        INodeObject.SetIsActive(this, isActive);
    }
}
