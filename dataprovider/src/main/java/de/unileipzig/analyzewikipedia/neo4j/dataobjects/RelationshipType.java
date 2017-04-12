package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

public enum RelationshipType {
	;

	String _type;
	RelationshipType(String type){
		_type = type;
	}
	
	public String GetType() {
		// TODO Auto-generated method stub
		return "type";
	}

}
