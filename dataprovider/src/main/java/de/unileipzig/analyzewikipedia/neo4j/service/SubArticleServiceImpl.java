/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;


public class SubArticleServiceImpl extends GenericService<SubArticleObject> implements SubArticleService {

    @Override
    Class<SubArticleObject> getEntityType() {
        return SubArticleObject.class;
    }
 
}
