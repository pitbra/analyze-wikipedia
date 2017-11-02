package de.unileipzig.analyzewikipedia.neo4j.console;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.service.ArticleServiceImpl;
import de.unileipzig.analyzewikipedia.neo4j.service.SubArticleServiceImpl;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 */
public class Neo4JConsole {

    public static void main(String[] args) {
        ArticleServiceImpl service_art = new ArticleServiceImpl();
        SubArticleServiceImpl service_subart = new SubArticleServiceImpl();
        
        Iterable<Entity> result = service_art.getAllLinkedNodes("Alan Smithee");
        System.out.println("=== Console 1   ===" + "   Linked nodes to 'Alan Smithee'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        Integer count = service_art.getNodeCounter();
        System.out.println("=== Console 2   ===" + "   Count all nodes");
        System.out.println(count);
        
        result = service_art.getNodesWithLabel("Article");
        System.out.println("=== Console 3   ===" + "   Nodes with label 'Article'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getNodesWithoutConnection();
        // in oure graph, all nodes are connected, so it ist just an test of failure
        System.out.println("=== Console 4   ===" + "   Nodes without connection");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getNodesWithConnection("HAS", Relationship.INCOMING);
        System.out.println("=== Console 5a  ===" + "   Nodes with incoming relationship 'HAS'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getNodesWithConnection("LINKS", Relationship.OUTGOING);
        System.out.println("=== Console 5b  ===" + "   Nodes with outgoing relationship 'LINKS'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getNodesByLabelAndTitlesequence("Article", "^A.*");
        System.out.println("=== Console 6   ===" + "   Nodes with label 'Article' and starting letter 'A'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getNodesByLabelAndTitlesequence("Article", "(?i)^a.*");
        System.out.println("=== Console 7   ===" + "   Nodes with label 'Article' and starting letter 'a' case sensitive");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getLabeledNodesWithConnection("SubArticle", "HAS", Relationship.INCOMING);
        System.out.println("=== Console 8a  ===" + "   SubArticle with incoming relationship 'HAS'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getLabeledNodesWithConnection("Article", "LINK_TO", Relationship.OUTGOING);
        System.out.println("=== Console 8b  ===" + "   Article with outgoing relationship 'LINKS'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getNodesWithOnlyActiveConnection();
        System.out.println("=== Console 9   ===" + "   Nodes with only the active connection");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getActiveNodes();
        System.out.println("=== Console 10  ===" + "   All active nodes");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getSubNodesWithConnection();
        System.out.println("=== Console 11  ===" + "   All linked nodes from subnodes");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getShortestPath("Test", "Regisseur");
        System.out.println("=== Console 12a ===" + "   Shortest path from 'Test' to 'Regisseur'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getShortestPath("Test", "Regisseur");
        System.out.println("=== Console 12a ===" + "   Shortest path from 'Test' to 'Regisseur'");
        int i = 0;
        for (Entity ent : result){
            System.out.println("Node " + (++i) + ". " + ent.getTitle());
        }
        
        result = service_art.getShortestPath("Asien", "Geschichte");
        System.out.println("=== Console 12b ===" + "   Shortest path from 'Asien' to 'Geschichte'");
        i = 0;
        for (Entity ent : result){
            System.out.println("Node " + (++i) + ". " + ent.getTitle());
        }
        
        // should not by possible, because ther is a 'HAS' between
        result = service_art.getShortestPath("Test", "No", "LINKS");
        System.out.println("=== Console 12c ===" + "   Shortest path from 'Test' to 'No' by 'LINKS'");
        i = 0;
        for (Entity ent : result){
            System.out.println("Node " + (++i) + ". " + ent.getTitle());
        }
        
        result = service_art.getShortestPath("Asien", "Geschichte", "LINKS");
        System.out.println("=== Console 12d ===" + "   Shortest path from 'Asien' to 'Geschichte' by 'LINKS'");
        i = 0;
        for (Entity ent : result){
            System.out.println("Node " + (++i) + ". " + ent.getTitle());
        }
        
        result = service_art.getNodesWithTitledConnection("Regi");
        System.out.println("=== Console 13  ===" + "   Nodes with 'Regi' as relation name");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        result = service_art.getWeblinks("Aristoteles");
        System.out.println("=== Console 14  ===" + "   Weblinks from 'Aristoteles'");
        for (Entity ent : result){
            System.out.println(ent.getTitle());
        }
        
        Entity art = service_subart.findSubTitleNode("Ang Lee", "Sortierverfahren");
        System.out.println("=== Console 15a ===" + "   Is 'Sortierverfahren' in 'Ang Lee'");
        if (art != null) System.out.println(art.getTitle()); else System.out.println("Sortierverfahren is not a subarticle of Ang Lee.");
        
        art = service_subart.findSubTitleNode("Sortierverfahren", "Weblinks");
        System.out.println("=== Console 15b ===" + "   Is 'Weblinks' in 'Sortierverfahren'");
        if (art != null) System.out.println(art.getTitle()); else System.out.println("Weblinks is not a subarticle of Sortierverfahren.");
        
    }
}
