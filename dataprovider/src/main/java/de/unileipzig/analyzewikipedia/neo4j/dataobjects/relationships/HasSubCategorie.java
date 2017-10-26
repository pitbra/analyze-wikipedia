/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects.relationships;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubCategorieObject;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 *
 * @author Pit.Braunsdorf
 */
@RelationshipEntity(type = "HAS")
public class HasSubCategorie {
    Long id;
    
    @StartNode
    CategorieObject categorie;
    
    @EndNode
    SubCategorieObject subCategorie;

    public HasSubCategorie() {
    }
}
