/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.service;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.helper.Neo4jSessionFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public T find(long id){
        return session.load(getEntityType(), id, DEPTH_ENTITY);
    }
    
    @Override
    public void delete(long id) {
        session.delete(session.load(getEntityType(), id));
    }
    
    @Override
    public T createOrUpdate(T entity){
        session.save(entity, DEPTH_ENTITY);
        return find(entity.getId());
    }
    
    @Override
    public T findByTitle(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", id);
        
        String query = QueryHelper.FindByTitle();
        
        return session.queryForObject(getEntityType(), query, params);
    }
    
    abstract Class<T> getEntityType();

    private static class QueryHelper {

        private static String FindByTitle() {
            return "Match (n) WHERE n.title = {title} RETURN n LIMIT 1"; 
        }

        public QueryHelper() {
        }
        
    }
}
