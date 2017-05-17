package de.unileipzig.analyzewikipedia.neo4j.helper;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.INodeObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import java.util.HashMap;

public class StatementHelper {

    public static String CreateStatementForNode(INodeObject nodeObject) {
        String stmt = String.format("CREATE (a:%s {", nodeObject.GetType().GetType());

        for (String key : nodeObject.GetAnnotations().keySet()) {
            stmt += String.format("%s: {%s},", key, key);
        }

        stmt = stmt.replaceFirst(".$", "");
        stmt += "})";

        return stmt;
    }

    public static String CreateStatementForRelationship(RelationshipType type, INodeObject from, INodeObject to) {
        String stmt = String.format("MATCH (a:%s),(b:%s) "
                + "WHERE a.%s = '%s' AND b.%s = '%s' "
                + "CREATE (a)-[r:%s { name: a.name + '<->' + b.name }]->(b)"
                + "RETURN r",
                from.GetType().GetType(),
                to.GetType().GetType(),
                AnnotationKeys.UID,
                from.GetUUID().toString(),
                AnnotationKeys.UID,
                to.GetUUID().toString(),
                type.GetType());
        return stmt;
    }

    public static String MatchStatementForAnnotations(HashMap<String, String> searchParams) {
        String stmt = "MATCH (a) WHERE ";

        for (String key : searchParams.keySet()) {
            stmt += String.format("a.%s = '%s' ", key, searchParams.get(key));

            if (searchParams.keySet().toArray()[searchParams.size() - 1] != key) {
                stmt += "AND ";
            }
        }

        stmt += "RETURN (a)";

        return stmt;
    }

    public static String MatchStatementForNodeHasSubnode(INodeObject node, String subnode) {
        String stmt = String.format("MATCH (a:%s)-[rel:has]->(b) "
                + "WHERE a.title='%s' "
                + "AND b.title='%s' "
                + "RETURN b", node.GetType().GetType(), node.GetTitle(), subnode);

        return stmt;
    }

}
