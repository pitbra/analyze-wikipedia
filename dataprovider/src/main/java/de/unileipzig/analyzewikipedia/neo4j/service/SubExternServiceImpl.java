/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubExternObject;


public class SubExternServiceImpl extends GenericService<SubExternObject> implements SubExternService {

    @Override
    Class<SubExternObject> getEntityType() {
        return SubExternObject.class;
    }
    
}
