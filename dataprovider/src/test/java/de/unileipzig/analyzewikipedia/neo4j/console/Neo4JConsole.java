/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.console;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 */
public class Neo4JConsole {

    public static void main(String[] args) {
        ArticleServiceImpl service = new ArticleServiceImpl();
        
        Iterable<Entity> result = service.getAllLinkedNodes("Alan Smithee");
        System.out.println("=== Console 1  ===" + "   Linked nodes to 'Alan Smithee'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        Integer count = service.getNodeCounter();
        System.out.println("=== Console 2  ===" + "   Count all nodes");
        System.out.println(count);
        
        result = service.getNodesWithLabel("Article");
        System.out.println("=== Console 3  ===" + "   Nodes with label 'Article'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getAllNodesWithoutConnection();
        // in oure graph, all nodes are connected, so it ist just an test of failure
        System.out.println("=== Console 4  ===" + "   Nodes without connection");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getAllNodesWithConnection("HAS", Relationship.INCOMING);
        System.out.println("=== Console 5a ===" + "   Nodes with incoming relationship 'HAS'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getAllNodesWithConnection("LINK_TO", Relationship.OUTGOING);
        System.out.println("=== Console 5b ===" + "   Nodes with outgoing relationship 'LINK_TO'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getAllNodesByLabelAndSequence("Article", "^A.*");
        System.out.println("=== Console 6  ===" + "   Nodes with label 'Article' and starting letter 'A'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getAllNodesByLabelAndSequence("Article", "(?i)^a.*");
        System.out.println("=== Console 7  ===" + "   Nodes with label 'Article' and starting letter 'a' case sensitive");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getLabeledNodesWithConnection("SubArticle", "HAS", Relationship.INCOMING);
        System.out.println("=== Console 8a ===" + "   SubArticle with incoming relationship 'HAS'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getLabeledNodesWithConnection("Article", "LINK_TO", Relationship.OUTGOING);
        System.out.println("=== Console 8b ===" + "   Article with outgoing relationship 'LINK_TO'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getAllNodesWithOnlyActiveConnection();
        System.out.println("=== Console 9  ===" + "   Nodes with only the active connection");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getAllActiveNodes();
        System.out.println("=== Console 10 ===" + "   All active nodes");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service.getSubNodesWithConnection();
        System.out.println("=== Console 11 ===" + "   All linked nodes from subnodes");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
    }
}
