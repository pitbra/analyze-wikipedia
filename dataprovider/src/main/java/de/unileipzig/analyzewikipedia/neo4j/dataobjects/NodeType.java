/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

public enum NodeType {
    Article("Article"), SubArticle("SubArticle");

    private final String _type;

    NodeType(String type) {
	_type = type;
    }

    public String GetType() {
	return _type;
    }
}
