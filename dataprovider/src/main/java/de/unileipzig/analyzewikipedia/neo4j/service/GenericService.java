package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.dumpreader.controller.SeekerThread;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.helper.Neo4jSessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.model.Result;
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

    private static String[] getDircetion(String direction){
        if (direction == null) direction = Relationship.UNDIRECTED;
        switch (direction){
                case Relationship.INCOMING:
                    return new String[]{"<-","-"};
                case Relationship.OUTGOING:
                    return new String[]{"-","->"};
                case Relationship.UNDIRECTED:
                default:
                    return new String[]{"-","-"};
            }
        }
    
    private String replaceString(String text){
        String out = SeekerThread.replaceText(text);
        out = SeekerThread.escapeString(out);
        return out;
    }
    
    @Override
    public Result sendStatement(String statement) {
        
        return session.query(statement, new HashMap<>());
        
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
    public Iterable<Entity> getAllSubArticlesOfArticle(String title){
        Map<String, Object> params = new HashMap<>();
        params.put("title", replaceString(title));

        String query = QueryHelper.findAllSubArticlesOfArticle();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getArticleAndAllSubArticles(String title){
        Map<String, Object> params = new HashMap<>();
        params.put("title", replaceString(title));

        String query = QueryHelper.findArticleAndAllSubArticles();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getAllLinksOfNodeAndAllSubArticles(String title){
        List<Entity> list = new ArrayList();
        
        Map<String, Object> params = new HashMap<>();
        params.put("title", replaceString(title));
        String query = QueryHelper.findArticleAndAllSubArticles();
        Iterable<Entity> result = session.query(Entity.class, query, params);
        for (Entity ent : result) list.add(ent);
        
        List<Entity> links = new ArrayList();
        for (Entity ent : list){
            query = QueryHelper.findLinkedTo(ent.getId());
            Iterable<Entity> l = session.query(Entity.class, query, params);
            for (Entity e : l){
                if (!links.contains(e)) links.add(e);
            }
        }
        
        return links;
    }
    
    @Override
    public T findSubNode(String title, String subtitle) {
        Map<String, Object> params = new HashMap<>();
        String query = QueryHelper.findSubArticleByTitle(replaceString(title), replaceString(subtitle));
        
        return session.queryForObject(getEntityType(), query, params);
    }
    
    @Override
    public Iterable<Entity> getLinkedNodes(String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", replaceString(title));

        String query = QueryHelper.findLinkedTo();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getRelatedNodes(String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", replaceString(title));

        String query = QueryHelper.findLinkedFrom();
        
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
    public Integer getRelationCounter() {
        Map<String, Object> params = new HashMap<>();

        String query = QueryHelper.countRelations();
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
    public Iterable<Entity> getTypedNodes(String type) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findSpecificNode(replaceString(type), null);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getTitledNodes(String title) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findSpecificNode(null, replaceString(title));
        
        return session.query(Entity.class, query, params);
    }
        
    @Override
    public Iterable<Entity> getNodes(String type, String title) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findSpecificNode(replaceString(type), replaceString(title));
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesWithoutConnection() {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findAllNodesWithoutConnection();
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesByTypeAndTitlesequence(String type, String sequence) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findNodesByTypeAndTitlesequence(type, sequence);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getEmptyActiveNodes() {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findEmptyActiveNodes();
        
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
    public Iterable<Entity> getTitledNodesWithTypedRelation(String title, String relationtype) {
        Map<String, Object> params = new HashMap<>();

        String query = QueryHelper.findSpecificNode(null, title, null, null, relationtype, null, null);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesWithTypedRelationToTypedNodes(String type, String relationtype) {
        Map<String, Object> params = new HashMap<>();

        String query = QueryHelper.findSpecificNode(null, null, type, null, relationtype, null, null);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getTypedNodesWithTypedRelationToTypedNodes(String starttype, String endtype, String relationtype) {
        Map<String, Object> params = new HashMap<>();

        String query = QueryHelper.findSpecificNode(starttype, null, endtype, null, relationtype, null, null);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getTypedNodesWithTypedRelationInWay(String nodetype, String relationtype, String direction) {
        Map<String, Object> params = new HashMap<>();

        String query = QueryHelper.findSpecificNode(nodetype, null, null, null, relationtype, null, direction);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getNodesWithTitledConnection(String title) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findSpecificNode(null, null, null, null, null, replaceString(title), null);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getSpecificNode(String startnodetype, String starttitle, String endnodetype, String endtitle, String relationtype, String reationtitle, String direction) {
        Map<String, Object> params = new HashMap<>();

        String query = QueryHelper.findSpecificNode(startnodetype, starttitle, endnodetype, endtitle, relationtype, reationtitle, direction);
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getWeblinks(String title) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findWeblinks(replaceString(title));
        
        return session.query(Entity.class, query, params);
    }
    
    @Override
    public Iterable<Entity> getDomain(long id) {
        Map<String, Object> params = new HashMap<>();
        
        String query = QueryHelper.findDomain(id);
        
        return session.query(Entity.class, query, params);
    }
    
    abstract Class<T> getEntityType();

    private static class QueryHelper {
        
        private static String findDomain(long id){
            return "MATCH (n:Extern)-[r:HAS]->(f) WHERE ID(f) = " + id + " RETURN n";
        }
        
        private static String findWeblinks(String title){
            return "MATCH (n)-[r:HAS]->(s)-[rs:LINKS]->(f) WHERE n.title = \"" + title + "\" RETURN f "
                   + "UNION MATCH (n)-[r:LINKS]->(f:SubExtern) WHERE n.title = \"" + title + "\" RETURN f";
        }
        
        private static String findShortestPath(String start, String end){
            return "MATCH (s { title: '" + start + "' }),(d { title: '" + end + "' }), p = shortestPath((s)-[*]-(d)) WITH p WHERE length(p) > 1 RETURN p";
        }
        
        private static String findShortestPath(String start, String end, String type){
            return "MATCH (s { title: '"+ start + "' }), (d { title: '" + end + "' }) , p = shortestPath((s)-[r:" + type + "*]-(d)) WITH p WHERE length(p) > 1 RETURN p";
        }
                
        private static String findNodesByTypeAndTitlesequence(String type, String sequence){
            return "MATCH (n) WHERE '" + type + "' in LABELS(n) AND n.title =~ '" + sequence + "' RETURN n";
        }
        
        private static String findActiveNodes(){
            return "MATCH (n)-[:ACTIVE]->(f) RETURN n";
        }
        
        private static String findEmptyActiveNodes(){
            return "MATCH (n)-[:ACTIVE]->(a) WITH n, SIZE( (n)-[]->() ) as likes WHERE likes = 1 RETURN n";
        }
        
        private static String findSpecificNode(String nodetype, String title){
            if (nodetype != null && nodetype.length() > 0) nodetype = ":"  + nodetype; else nodetype = "";
            if (title != null && title.length() > 0) title = " { title: '" + title + "' } "; else title = "";
            return "MATCH (s" + nodetype + title + ") RETURN s";
        }
              
        private static String findSpecificNode(String startnodetype, String starttitle, String endnodetype, String endtitle, String relationtype, String reationtitle, String direction){
            if (startnodetype != null && startnodetype.length() > 0) startnodetype = ":"  + startnodetype; else startnodetype = "";
            if (starttitle != null && starttitle.length() > 0) starttitle = " { title: '" + starttitle + "' } "; else starttitle = "";
            if (endnodetype != null && endnodetype.length() > 0) endnodetype = ":"  + endnodetype; else endnodetype = "";
            if (endtitle != null && endtitle.length() > 0) endtitle = " { title: '" + endtitle + "' } "; else endtitle = "";
            if (relationtype != null && relationtype.length() > 0) relationtype = ":"  + relationtype; else relationtype = "";
            if (reationtitle != null && reationtitle.length() > 0) reationtitle = " { title: '" + reationtitle + "' } "; else reationtitle = "";
            return "MATCH (s" + startnodetype + starttitle + ")" + getDircetion(direction)[0] + "[r" + relationtype + reationtitle + "]" + getDircetion(direction)[1] + "(e" + endnodetype + endtitle + ") RETURN s";
        }
        
        private static String findAllNodesWithoutConnection(){
            return "MATCH (n) WHERE NOT (n)-[]-() RETURN n";
        }
        
        private static String countNodes() {
            return "START n = node(*) RETURN COUNT(n)";
        }
        
        private static String countRelations() {
            return "START n = relationship(*) RETURN COUNT(n)";
        }
        
        private static String findByTitle() {
            return "Match (n) WHERE n.title =~ (?i){title} RETURN n LIMIT 1";
        }

        private static String findSubArticleByTitle(String title, String subtitle) {
            return "MATCH (n { title: '" + title + "'})-[r:HAS]->(f { title: '" + subtitle + "'}) RETURN f";
        }

        private static String findLinkedTo(Long id) {
            return "MATCH (n)-[r:LINKS]->(f) WHERE ID(n) = " + id + " RETURN f";
        }
        
        private static String findLinkedFrom() {
            return "MATCH (n)-[r:LINKS]->(f) WHERE f.title = {title} RETURN n";
        }
        
        private static String findLinkedTo() {
            return "MATCH (n)-[r:LINKS]->(f) WHERE n.title = {title} RETURN f";
        }
        
        private static String findArticleAndAllSubArticles(){
            return  "MATCH (a:Article)-[r*1..]->(s:SubArticle) " +
                    "WHERE ALL(rel in r WHERE type(rel)= 'HAS') " +
                    "AND a.title = {title} " +
                    "RETURN a,s";
        }
        
        private static String findAllSubArticlesOfArticle(){
            return "MATCH (a:Article)-[r*1..]->(s:SubArticle) " +
                    "WHERE ALL(rel in r WHERE type(rel)= 'HAS') " +
                    "AND a.title = {title} " +
                    "RETURN s";
        }
        
        public QueryHelper() {
        }

    }
}
