/*
 * 
 */
package dataprovider;

import java.util.Dictionary;

public class ArticleObject implements INodeObject {

    private final NodeType _type = NodeType.Article;
    private Dictionary<String, String> _annotations;

    public NodeType GetType() {
	return _type;
    }

    public Dictionary<String, String> GetAnnotations() {
	return _annotations;
    }

    public void AddAnnotation(String key, String value) {
	_annotations.put(key, value);
    }
}
