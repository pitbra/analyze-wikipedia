package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

public enum RelationshipType {
	HAS("has"), LINK("link");

	String _type;
	RelationshipType(String type){
		_type = type;
	}
	
	public String GetType() {
		return _type;
	}

}
