/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.neo4j.helper;

import de.unileipzig.analyzewikipedia.neo4j.dataprovider.ConnectionOptions;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;

/**
 *
 * @author Pit.Braunsdorf
 */
public class DatabaseHelper {

    private final Driver _driver;

    /**
     *
     * @throws Exception
     */
    public DatabaseHelper() throws Exception {
        _driver = CreateDriver();
        
        if(_driver ==  null) {
            throw new Exception("Could not create driver");
        }
    }
    
    /**
     *
     * @param driver
     */
    public DatabaseHelper(Driver driver) {
        _driver = driver;
    }
    
    private Driver CreateDriver() {
        try {
            ConnectionOptions opts = ConnectionOptions.GetInstance();
            return GraphDatabase.driver(opts.GetConnectionString(), AuthTokens.basic(opts.GetUsername(), opts.GetPassword()));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     *
     * @param stmt
     * @return
     * @throws Exception
     */
    public StatementResult ExecuteStatement(String stmt) throws Exception{
        Session session = _driver.session();
        Transaction tx = session.beginTransaction();
        StatementResult result = tx.run(stmt);
        tx.success();

        return result;
    }

    /**
     *
     */
    public void close() {
        _driver.close();
    }
}
