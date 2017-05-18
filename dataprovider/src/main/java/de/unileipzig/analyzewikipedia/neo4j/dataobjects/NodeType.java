/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import de.unileipzig.analyzewikipedia.neo4j.constants.NodeTypeConstants;

public enum NodeType {    
    Article(NodeTypeConstants.ARTICLE), 
    SubArticle(NodeTypeConstants.SUB_ARTICLE), 
    ExternSource(NodeTypeConstants.EXTERN), 
    SubExternSource(NodeTypeConstants.SUB_EXTERN), 
    Categorie(NodeTypeConstants.CATEGORY), 
    SubCategorie(NodeTypeConstants.SUB_CATEGORY),
    Custom(NodeTypeConstants.CUSTOM);
    
    private final String _type;

    NodeType(String type) {
	_type = type;
    }

    public String GetType() {
	return _type;
    }
}
