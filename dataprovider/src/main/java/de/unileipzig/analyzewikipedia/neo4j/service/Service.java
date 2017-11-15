package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;

import java.util.List;

import org.neo4j.ogm.model.Result;

/**
 * @author Pit.Braunsdorf
 * @param <T>
 */
public interface Service<T extends Entity> {
    
    /**
     * Send statement in cypher
     * 
     * @param statement as string
     * @return 
     */
    Result sendStatement(String statement);
    
    /**
     * Find all nodes
     * 
     * @return 
     */
    Iterable<T> findAll();
    
    /**
     * Find node by id
     * 
     * @param id as long
     * @return 
     */
    T find(long id);
    
    /**
     * Delete node by id
     * 
     * @param title node title 
     */
    void delete(long id);
    
    /**
     * Update given node
     * 
     * @param object node
     * @return 
     */
    T createOrUpdate(T object);
    
    /**
     * Get node by title
     * 
     * @param title node title
     * @return 
     */
    T findByTitle(String title);
    
    /**
     * Get subnode of given node
     * 
     * @param title node title
     * @param subtitle subnode title
     * @return 
     */
    T findSubNode(String title, String subtitle);
    
    /**
     * List all subarticles of article
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getAllSubArticlesOfArticle(String title);
    
    /**
     * List article and all subarticles of this article
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getAllLinksOfNodeAndAllSubArticles(String title);
    
    /**
     * List nodes with title and relationtype
     * 
     * @param title node title
     * @param relationtype of relation
     * @return 
     */
    Iterable<Entity> getTitledNodesWithTypedRelation(String title, String relationtype);
    
    /**
     * List nodes with typed relation to typed nodes
     * 
     * @param type node type
     * @param relationtype of relation
     * @return 
     */
    Iterable<Entity> getNodesWithTypedRelationToTypedNodes(String type, String relationtype);
    
    /**
     * List all typed nodes with typed relation
     * 
     * @param starttype startnode type
     * @param endtype endnode type
     * @param relationtype relation type
     * @return 
     */
    Iterable<Entity> getTypedNodesWithTypedRelationToTypedNodes(String starttype, String endtype, String relationtype);
    
    /**
     * List the articles to given subarticle title
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getArticleFromSubarticleTitle(String title);
    
    /**
     * List all articles and subarticles from given article
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getArticleAndAllSubArticles(String title);
    
    /**
     * List all nodes of type with directed relation
     * 
     * @param nodetype node type
     * @param relationtype relation type
     * @param direction direction of relation
     * @return 
     */
    Iterable<Entity> getTypedNodesWithTypedRelationInWay(String nodetype, String relationtype, String direction);
    
    /**
     * List all articles and subarticles from given article
     * 
     * @param startnodetype startnode type
     * @param starttitle startnode title
     * @param endnodetype endnode type
     * @param endtitle endnode type
     * @param relationtype relation type
     * @param reationtitle relation title
     * @param direction direction of relation
     * @return 
     */
    Iterable<Entity> getSpecificNode(String startnodetype, String starttitle, String endnodetype, String endtitle, String relationtype, String reationtitle, String direction);
    
    /**
     * List all nodes that linked from given title
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getLinkedNodes(String title);
    
    /**
     * List all nodes that linked to given title
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getRelatedNodes(String title);
    
    /**
     * List all nodes that linked to given title
     * 
     * @param title node title
     * @param type relatednode type
     * @return 
     */
    Iterable<Entity> getRelatedNodes(String title, String type);
    
    /**
     * Count all nodes
     * 
     * @return 
     */
    Integer getNodeCounter();
    
    /**
     * Count all relations
     * 
     * @return 
     */
    Integer getRelationCounter();
    
    /**
     * Get nodes by type and title
     * 
     * @param type node type
     * @param title node title
     * @return 
     */
    Iterable<Entity> getNodes(String type, String title);
    
    /**
     * Get notes by type
     * 
     * @param type node type
     * @return 
     */
    Iterable<Entity> getTypedNodes(String type);
    
    /**
     * Get nodes by title
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getTitledNodes(String title);
    
    /**
     * List all nodes without relations
     * 
     * @return 
     */
    Iterable<Entity> getNodesWithoutConnection();
    
    /**
     * List active nodes without connections
     * 
     * @return 
     */
    Iterable<Entity> getEmptyActiveNodes();
    
    /**
     * List active nodes
     * 
     * @return 
     */
    Iterable<Entity> getActiveNodes();
    
    /**
     * List all nodes with given type and the given title stringsequenze
     * 
     * @param type nodetype
     * @param sequence searchstring
     * @return 
     */
    Iterable<Entity> getNodesByTypeAndTitlesequence(String type, String sequence);
    
    /**
     * List path of shortest way between given nodes of specifiv relation type
     * 
     * @param start startnode title
     * @param end endnode title
     * @return 
     */
    Iterable<Entity> getShortestPath(String start, String end);
    
    /**
     * List path of shortest way between given nodes of specifiv relation type
     * 
     * @param start startnode title
     * @param end endnode title
     * @param type relationstype
     * @return 
     */
    Iterable<Entity> getShortestPath(String start, String end, String type);
    
    /**
     * List path of shortest way between given nodes of specifiv relation type
     * 
     * @param start startnode title
     * @param end endnode title
     * @param types relationstype as array
     * @return 
     */
    Iterable<Entity> getShortestPath(String start, String end, String[] types);
    
    /**
     * List all nodes with given title
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getNodesWithTitledConnection(String title);
    
    /**
     * List all weblinks of the node
     * 
     * @param title node title
     * @return 
     */
    List<String> getWeblinks(String title);
    
    /**
     * List all extern links of the node
     * 
     * @param title node title
     * @return 
     */
    Iterable<Entity> getExternFiles(String title);
    
    /**
     * Returns the domaen of given subextern
     * 
     * @param id of the node
     * @return 
     */
    Iterable<Entity> getDomain(long id);
    
    /**
     * Returns the domaen of given subextern
     * 
     * @param title subextern title
     * @return 
     */
    Iterable<Entity> getDomain(String title);
    
}
