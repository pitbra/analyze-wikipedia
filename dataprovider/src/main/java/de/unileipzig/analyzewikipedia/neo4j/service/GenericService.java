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

    private String replaceString(String text){
        String out = SeekerThread.replaceText(text);
        out = SeekerThread.escapeString(out);
        return out;
    }
    
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
        params.put("title", replaceString(title));

        String query = QueryHelper.findByTitle();

        return session.queryForObject(getEntityType(), query, params);
    }

    @Override
    public T findSubTitleNode(String title, String subtitle) {
        Map<String, Object> params = new HashMap<>();
        String query = QueryHelper.findSubArtcileByTitle(replaceString(title), replaceString(subtitle));
        
        return session.queryForObject(getEntityType(), query, params);
    }

    @Override
    public Iterable<Entity> getAllLinkedNodes(String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", replaceString(title));

        String query = QueryHelper.findLinkedTo();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Integer getNodeCounter() {
        Map<String, Object> params = new HashMap<>();

        String query = QueryHelper.countNodes();
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
    public Iterable<Entity> getNodesWithoutConnection() {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findAllNodesWithoutConnection();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesWithConnection(String type, String direction) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findNodesWithConnection(type, direction);
                
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getLabeledNodesWithConnection(String label, String type, String direction) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findLabeledNodesWithConnection(type, direction, label);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesByLabelAndTitlesequence(String label, String sequence) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findNodesByLabelAndTitlesequence(label, sequence);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesWithOnlyActiveConnection() {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findNodesWithOnlyActiveConnection();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getActiveNodes() {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findActiveNodes();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getShortestPath(String start, String end){
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findShortestPath(replaceString(start), replaceString(end));
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getShortestPath(String start, String end, String type){
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findShortestPath(replaceString(start), replaceString(end), type);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getSubNodesWithConnection() {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findSubNodesWithConnection();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesWithTitledConnection(String title) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findNodesWithTitledConnection(title);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getWeblinks(String title) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findWeblinks(replaceString(title));
        
        return session.query(Entity.class, query, params);
    }
    
    abstract Class<T> getEntityType();

    private static class QueryHelper {
        private static String findWeblinks(String title){
            return "MATCH (n)-[r:HAS]->(s)-[rs:LINKS]->(f) WHERE n.title = \"" + title + "\" RETURN f "
                   + "UNION MATCH (n)-[r:LINKS]->(f:SubExtern) WHERE n.title = \"" + title + "\" RETURN f";
        }
        
        private static String findNodesWithTitledConnection(String title){
            return "MATCH (f)-[r {title: '" + title + "'}]->(d) RETURN f";
        }
        
        private static String findShortestPath(String start, String end){
            return "MATCH (s { title: '" + start + "' }),(d { title: '" + end + "' }), p = shortestPath((s)-[*]-(d)) WITH p WHERE length(p) > 1 RETURN p";
        }
        
        private static String findShortestPath(String start, String end, String type){
            return "MATCH (s { title: '"+ start + "' }), (d { title: '" + end + "' }) , p = shortestPath((s)-[r:" + type + "*]-(d)) WITH p WHERE length(p) > 1 RETURN p";
        }
        
        private static String findNodesWithConnection(String type, String direction){
            switch (direction){
                case Relationship.INCOMING:
                    return "MATCH (f)-[:" + type + "]->(d) return d";
                case Relationship.OUTGOING:
                default:
                    return "MATCH (s)-[:" + type + "]->(f) return s";
                    
            }
        }
        
        private static String findLabeledNodesWithConnection(String type, String direction, String label){
            switch (direction){
                case Relationship.INCOMING:
                    return "MATCH (n:" + label + ")<-[:" + type + "]-(f) return n";
                case Relationship.OUTGOING:
                default:
                    return "MATCH (n:" + label + ")-[:" + type + "]->(f) return n";
            }
        }
                
        private static String findNodesByLabelAndTitlesequence(String label, String sequence){
            return "MATCH (n) WHERE '" + label + "' in LABELS(n) AND n.title =~ '" + sequence + "' RETURN n";
        }
        
        private static String findActiveNodes(){
            return "MATCH (n)-[:ACTIVE]->(f) RETURN n";
        }
        
        private static String findNodesWithOnlyActiveConnection(){
            return "MATCH (n)-[:ACTIVE]->(a) WITH n, SIZE( (n)-[]->() ) as likes WHERE likes = 1 RETURN n";
        }
        
        private static String findSubNodesWithConnection(){
            return "MATCH (n)-[r1:HAS]->(s)-[r2:LINKS]->(d) RETURN s";
        }
        
        private static String findAllNodesWithoutConnection(){
            return "MATCH (n) WHERE NOT (n)-[]-() RETURN n";
        }
        
        private static String findAllNodesWithLabel(String label) {
            return "MATCH (a:" + label + ") RETURN a";
        }
        
        private static String countNodes() {
            return "START n = node(*) RETURN COUNT(n)";
        }
        
        private static String findByTitle() {
            return "Match (n) WHERE n.title = {title} RETURN n LIMIT 1";
        }

        private static String findSubArtcileByTitle(String title, String subtitle) {
            return "MATCH (n { title: '" + title + "'})-[r:HAS]->(f { title: '" + subtitle + "'}) RETURN f";
        }

        private static String findLinkedTo() {
            return "MATCH (n)-[rel:LINKS    ]->(b) "
                    + "WHERE n.title = {title} "
                    + "RETURN b";
        }

        public QueryHelper() {
        }

    }
}
