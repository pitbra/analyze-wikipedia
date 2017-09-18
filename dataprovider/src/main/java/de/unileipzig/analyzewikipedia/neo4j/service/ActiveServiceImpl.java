/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ActiveNode;


public class ActiveServiceImpl extends GenericService<ActiveNode> implements ActiveService {

    @Override
    Class<ActiveNode> getEntityType() {
        return ActiveNode.class;
    }
       
}
