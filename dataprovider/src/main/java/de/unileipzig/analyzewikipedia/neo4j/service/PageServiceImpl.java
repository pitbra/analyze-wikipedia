/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.PageObject;


public class PageServiceImpl extends GenericService<PageObject> implements PageService {

    @Override
    Class<PageObject> getEntityType() {
        return PageObject.class;
    }
       
}
