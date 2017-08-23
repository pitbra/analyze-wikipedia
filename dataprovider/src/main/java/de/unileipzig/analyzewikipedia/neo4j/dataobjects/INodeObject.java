/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.List;
import java.util.Map;
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
     *
     * @return
     */
    public String GetTitle();
    
    /**
     *
     * @param title
     */
    public void SetTitle(String title);
    
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

    /**
     *
     * @param record
     * @return
     */
    public static INodeObject FromRecord(Record record) {
        List<Value> values = record.values();

        for (Value val : values) {
            Node node = val.asNode();
            for (String label : node.labels()) {
                switch (label) {
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
    
    /**
     *
     * @param subNode
     * @return
     * @throws Exception
     */
    public INodeObject FindSubNode(String subNode ) throws Exception;
    
    /**
     *
     * @param node
     * @param subNode
     * @return
     * @throws Exception
     */
    public static INodeObject FindSubNode(INodeObject node, String subNode) throws Exception
    {      
        return null;
    }
    
    /**
     *
     * @param isActive
     * @throws java.lang.Exception
     */
    public void SetIsActive(boolean isActive) throws Exception;

    /**
     *
     * @param node
     * @param isActive
     * @throws Exception
     */
    public static void SetIsActive(INodeObject node, boolean isActive) throws Exception{
        
    }
}
