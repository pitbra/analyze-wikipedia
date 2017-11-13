package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;


public class CategorieServiceImpl extends GenericService<CategorieObject> implements CategorieService {

    @Override
    Class<CategorieObject> getEntityType() {
        return CategorieObject.class;
    }    
}
