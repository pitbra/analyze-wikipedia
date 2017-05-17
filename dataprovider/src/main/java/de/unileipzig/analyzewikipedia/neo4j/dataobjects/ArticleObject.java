package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo4j.driver.v1.types.Node;

public class ArticleObject implements INodeObject {

    private final NodeType _type = NodeType.Article;
    private final Map<String, Object> _annotations;
    private boolean status;
    private String _title;

    private String _uid;

    private ArticleObject() {
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

    public static ArticleObject CreateArticleObject() {
        ArticleObject art = new ArticleObject();

        art._uid = UUID.randomUUID().toString();
        art.AddAnnotation(AnnotationKeys.UID, art._uid);

        return art;
    }

    public static ArticleObject FromNode(Node node) {
        ArticleObject art = new ArticleObject();

        art._uid = node.get(AnnotationKeys.UID).asString();
        for (String key : node.keys()) {
            if (key == null ? AnnotationKeys.UID == null : key.equals(AnnotationKeys.UID)) {
                continue;
            }

            art.AddAnnotation(key, node.get(key));
        }

        return art;
    }

    public void setStatus() {
        status = true;
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public String GetUUID() {
        return _uid;
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
