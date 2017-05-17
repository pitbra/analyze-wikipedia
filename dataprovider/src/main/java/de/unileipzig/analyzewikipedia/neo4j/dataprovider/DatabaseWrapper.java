/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataprovider;

import org.neo4j.driver.v1.*;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.INodeObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.helper.DatabaseHelper;
import de.unileipzig.analyzewikipedia.neo4j.helper.StatementHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseWrapper {

    DatabaseHelper _dbHelper;

    public DatabaseWrapper() throws Exception {
        try {
            _dbHelper = new DatabaseHelper();
        } catch (Exception ex) {
            throw new Exception("Could not create DatabaseHelper", ex);
        }
    }

    public void close() {
        _dbHelper.close();
    }

    public boolean CreateNode(INodeObject nodeObject) {
        String stmt = StatementHelper.CreateStatementForNode(nodeObject);

        try {
            _dbHelper.ExecuteStatement(stmt);

            return true;
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public boolean CreateRelationsship(RelationshipType type, INodeObject from, INodeObject to) {
        String stmt = StatementHelper.CreateStatementForRelationship(type, from, to);

        try {
            _dbHelper.ExecuteStatement(stmt);
            return true;
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;

    }

    public List<INodeObject> FindByAnnotation(String annotation, String value, boolean withRelationships) {
        HashMap<String, String> map = new HashMap<String, String>() {
            {
                put(annotation, value);
            }
        };

        if (!withRelationships) {
            return FindByAnnotations(map);
        } else {
            return FindByAnnotationsWithRelationships(map);
        }

    }

    private List<INodeObject> FindByAnnotations(HashMap<String, String> hashMap) {
        List<INodeObject> res = new ArrayList<>();
        String stmt = StatementHelper.MatchStatementForAnnotations(hashMap);
        
        try {
                StatementResult result = _dbHelper.ExecuteStatement(stmt);

                while (result.hasNext()) {
                    Record rec = result.next();

                    res.add(INodeObject.FromRecord(rec));
                }

        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    private List<INodeObject> FindByAnnotationsWithRelationships(HashMap<String, String> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public INodeObject FindSubNode(INodeObject node, String subnode)
    {
        try {
            String stmt = StatementHelper.MatchStatementForNodeHasSubnode(node, subnode);            
            StatementResult result = _dbHelper.ExecuteStatement(stmt);
            while(result.hasNext()) {
                return INodeObject.FromRecord(result.next());
            }
        } catch (Exception ex) {
            Logger.getLogger(DatabaseWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}
