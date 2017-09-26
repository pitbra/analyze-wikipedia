/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.console;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ActiveNode;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubCategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubExternObject;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.ExternServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubExternService;
import de.unileipzig.analyzewikipedia.neo4j.service.SubExternServiceImpl;

/**
 *
 * @author Pit.Braunsdorf
 */
public class Neo4JConsole {

    public static void main(String[] args) {
        ArticleServiceImpl service = new ArticleServiceImpl();
        
        Iterable<Entity> result = service.getAllLinkedNodes("Terrorismus");
        
        int t = 1;
    }
}
