/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;
import jdk.nashorn.internal.objects.DataPropertyDescriptor;
import org.neo4j.driver.v1.types.Node;

/**
 *
 * @author Pit.Braunsdorf
 */
public class CategorieObject implements INodeObject {

    private final NodeType _type = NodeType.Categorie;
    private final Map<String, Object> _annotations;

    private String _uid;
    private String _title;

    private CategorieObject() {
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

    public static CategorieObject CreateCategorieObject() {
        CategorieObject cat = new CategorieObject();

        cat._uid = UUID.randomUUID().toString();
        cat.AddAnnotation(AnnotationKeys.UID, cat._uid);

        return cat;
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
}
