/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects.relationships;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 *
 * @author Pit.Braunsdorf
 */
@RelationshipEntity(type="LINK_TO")
public class SubArticleLinkToArticle extends LinkToRelationship<SubArticleObject, ArticleObject>{
}
