package de.unileipzig.analyzewikipedia.neo4j.helper;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.INodeObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;

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
	
	public static String CreateStatementForRelationship(RelationshipType type, INodeObject from, INodeObject to)
	{
		String stmt  = String.format("MATCH (a:%s),(b:%s) "
				+ "WHERE a.%s = '%s' AND b.%s = '%s' "
				+ "CREATE (a)-[r:%s { name: a.name + '<->' + b.name }]->(b)"
				+ "RETURN r"
				, from.GetType().GetType()
				, to.GetType().GetType()
				, AnnotationKeys.UID
				, from.GetUUID().toString()
				, AnnotationKeys.UID
				, to.GetUUID().toString()
				, type.GetType()) ;
		return stmt;	
	}

	public static String MatchStatementForTitle(String title)
	{
		return String.format("MATCH (n) "
				+ "WHERE n.title = '%s'", title);
	}
}
