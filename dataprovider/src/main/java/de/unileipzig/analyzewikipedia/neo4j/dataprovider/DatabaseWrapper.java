/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataprovider;

import org.neo4j.driver.v1.*;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.INodeObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.helper.StatementHelper;

public class DatabaseWrapper {

	Driver _driver;

	public DatabaseWrapper() {
		_driver = GetDriver();
	}

	public void close() {
		_driver.close();
	}

	public boolean CreateNode(INodeObject nodeObject) {
		try {
			Session session = _driver.session();
			String stmt = StatementHelper.CreateStatementForNode(nodeObject);
			Transaction tx = session.beginTransaction();
			tx.run(stmt, nodeObject.GetAnnotations());
			tx.success();

			session.close();

			return true;
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	private Driver GetDriver() {
		
		try{
		ConnectionOptions opts = ConnectionOptions.GetInstance();
		return GraphDatabase.driver(opts.GetConnectionString(), AuthTokens.basic(opts.GetUsername(), opts.GetPassword()));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public boolean CreateRelationsship(RelationshipType type, INodeObject from, INodeObject to) {
		try (Session session = _driver.session()) {
			String stmt = StatementHelper.CreateStatementForRelationship(type, from, to);
			try (Transaction tx = session.beginTransaction()) {
				tx.run(stmt);
				tx.success();

				return true;
			}

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}
}
