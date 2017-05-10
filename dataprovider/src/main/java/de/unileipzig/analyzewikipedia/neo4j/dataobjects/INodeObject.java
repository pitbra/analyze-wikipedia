/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import de.unileipzig.analyzewikipedia.neo4j.constants.NodeTypeConstants;

/**
 * @author Pit.Braunsdorf
 *
 */
public interface INodeObject {

    /**
     * Gets the type.
     *
     * @return the node type
     */
    public NodeType GetType();

    /**
     * Gets the annotations.
     *
     * @return the map
     */
    public Map<String, Object> GetAnnotations();

    /**
     * Adds the annotation.
     *
     * @param key the key
     * @param value the value
     */
    public void AddAnnotation(String key, Object value);

    /**
     * Gets the UUID.
     *
     * @return the uuid
     */
    public String GetUUID();

    public static INodeObject FromRecord(Record record) {
        List<Value> values = record.values();

        for (Value val : values) {
            Node node = val.asNode();
            for (String label : node.labels()) {
                switch (label) {
                    case NodeTypeConstants.ARTICLE: {
                        return ArticleObject.FromNode(node);
                    }
                    case NodeTypeConstants.SUB_ARTICLE: {
                        return SubArticleObject.FromNode(node);
                    }
                    case NodeTypeConstants.CATEGORY: {
                        return CategorieObject.FromNode(node);
                    }
                    case NodeTypeConstants.SUB_CATEGORY: {
                        return SubCategorieObject.FromNode(node);
                    }
                    case NodeTypeConstants.EXTERN: {
                        return ExternObject.FromNode(node);
                    }
                    case NodeTypeConstants.SUB_EXTERN: {
                        return SubExternObject.FromNode(node);
                    }
                    default:
                        throw new IndexOutOfBoundsException(label);
                }
            }
        }

        return null;
    }
}
