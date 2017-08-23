/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.helper;

import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.Session;


/**
 *
 * @author Pit.Braunsdorf
 */
public class Neo4jSessionFactory {
    private static SessionFactory sessionFactory = new SessionFactory("de.unileipzig.analyzewikipedia.neo4j.dataobjects");
    private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

    private Neo4jSessionFactory() {
    }
    
    public static Neo4jSessionFactory getInstance() {
        return factory;
    }
    
    public Session getNeo4jSession() {
        return sessionFactory.openSession();
    }
}
