/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.helper.Neo4jSessionFactory;
import java.util.HashMap;
import java.util.Map;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.session.Session;

/**
 *
 * @author Pit.Braunsdorf
 * @param <T>
 */
public abstract class GenericService<T extends Entity> implements Service<T> {

    private static final int DEPTH_LIST = 0;
    private static final int DEPTH_ENTITY = 1;
    protected Session session = Neo4jSessionFactory.getInstance().getNeo4jSession();

    @Override
    public Iterable<T> findAll() {
        return session.loadAll(getEntityType(), DEPTH_LIST);
    }

    @Override
    public T find(long id) {
        return session.load(getEntityType(), id, DEPTH_ENTITY);
    }

    @Override
    public void delete(long id) {
        session.delete(session.load(getEntityType(), id));
    }

    @Override
    public T createOrUpdate(T entity) {
        session.save(entity, DEPTH_ENTITY);
        return find(entity.getId());
    }

    @Override
    public T findByTitle(String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", SeekerThread.replaceText(title));

        String query = QueryHelper.FindByTitle();

        return session.queryForObject(getEntityType(), query, params);
    }

    @Override
    public Iterable<Entity> shortestPathTo(String sourceTitle, String destinationType, String destinationTitle) {
        /*Map<String, Object> params = new HashMap<>();
        params.put("startType", this.getEntityType());
        params.put("startTitle", sourceTitle);
        params.put("destType", destinationType);
        params.put("destTitle", destinationType);
                
        String query = QueryHelper.ShortestPath();
        
        return session.query(Entity.class, query, params*/
        return null;
    }

    @Override
    public Iterable<Entity> getAllLinkedNodes(String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", SeekerThread.replaceText(title));

        String query = QueryHelper.FindLinkedTo();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Integer getNodeCounter() {
        Map<String, Object> params = new HashMap<>();
        //params.put("title", SeekerThread.replaceText(title));

        String query = QueryHelper.countAllNodes();
        org.neo4j.ogm.model.Result result = session.query(query, params);
        java.util.Iterator<Map<String, Object>> erg = result.iterator();
        while (erg.hasNext()){
            Map<String, Object> tmp = erg.next();
            if (tmp.containsKey("COUNT(n)")){
                return Integer.parseInt(tmp.get("COUNT(n)").toString());
            }
        }
        
        return -1;
    }

    @Override
    public Iterable<Entity> getNodesWithLabel(String label) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findAllNodesWithLabel(label);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getAllNodesWithoutConnection() {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findAllNodesWithoutConnection();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getAllNodesWithConnection(String type, String direction) {
        Map<String, Object> params = new HashMap<>();
        
        String query;
        switch (direction){
            case Relationship.INCOMING:
                query = QueryHelper.findAllNodesWithIncomeConnection(type);
                break;
            case Relationship.OUTGOING:
            default:
                query = QueryHelper.findAllNodesWithOutgoingConnection(type);
                break;
        }
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getLabeledNodesWithConnection(String label, String type, String direction) {
        Map<String, Object> params = new HashMap<>();
        
        String query;
        switch (direction){
            case Relationship.INCOMING:
                query = QueryHelper.findLabeledNodesWithIncomeConnection(label, type);
                break;
            case Relationship.OUTGOING:
            default:
                query = QueryHelper.findLabeledAllNodesWithOutgoingConnection(label, type);
                break;
        }
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getAllNodesByLabelAndSequence(String label, String sequence) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findAllNodesByLabelAndTitle(label, sequence);
        
        return session.query(Entity.class, query, params);
    }
    
    abstract Class<T> getEntityType();

    private static class QueryHelper {

        private static String findLabeledNodesWithIncomeConnection(String label, String type){
            return "MATCH (o)<-[:`" + type + "`]-(f) WHERE (o:" + label + ") return o";
        }
        
        private static String findLabeledAllNodesWithOutgoingConnection(String label, String type){
            return "MATCH (f)-[:`" + type + "`]->(d) WHERE (d:" + label + ") return d";
        }
        
        private static String findAllNodesByLabelAndTitle(String label, String sequence){
            return "MATCH (n) WHERE '" + label + "' in LABELS(n) AND n.title =~ '" + sequence + "' RETURN n";
        }
        
        private static String findAllNodesWithIncomeConnection(String type){
            return "MATCH (o)<-[:`" + type + "`]-(f) return o";
        }
        
        private static String findAllNodesWithOutgoingConnection(String type){
            return "MATCH (f)-[:`" + type + "`]->(d) return d";
        }
        
        private static String findAllNodesWithoutConnection(){
            return "MATCH (n) WHERE NOT (n)-[]-() RETURN n";
        }
        
        private static String findAllNodesWithLabel(String label) {
            return "MATCH (a:" + label + ") RETURN a";
        }
        
        private static String countAllNodes() {
            return "START n = node(*) RETURN COUNT(n)";
        }
        
        private static String FindByTitle() {
            return "Match (n) WHERE n.title = {title} RETURN n LIMIT 1";
        }

        private static String ShortestPath() {
            return "MATCH (n), (m)\n"
                    + "WHERE n.title = {startTitle} "
                    + "AND m.title = {destTitle} "
                    + "MATCH path = allShortestPaths( (n)-[*..4]-(m) )\n"
                    + "RETURN path";
        }

        private static String FindLinkedTo() {
            return "MATCH (n)-[rel:LINK_TO]->(b) "
                    + "WHERE n.title = {title} "
                    + "RETURN b";
        }

        public QueryHelper() {
        }

    }
}
