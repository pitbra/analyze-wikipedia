/*
 * 
 */
package dataprovider;

import java.util.Dictionary;

public interface INodeObject {
    public NodeType GetType();

    public Dictionary<String, String> GetAnnotations();

    public void AddAnnotation(String key, String value);
}
