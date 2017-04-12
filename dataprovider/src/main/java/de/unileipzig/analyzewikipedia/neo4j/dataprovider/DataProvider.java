/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataprovider;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.IDatabaseObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;

// TODO: Auto-generated Javadoc
/**
 * The Class DataProvider.
 */
public class DataProvider {

	DatabaseWrapper _databaseWrapper;

	/**
	 * Instantiates a new data provider.
	 *
	 * @param connectionString
	 *            the connection string
	 */
	public DataProvider(String connectionString, String username, String password) {
		// Create ConnectionOptions
		ConnectionOptions.GetInstance(connectionString,username, password);
		_databaseWrapper = new DatabaseWrapper();
	}

	/**
	 * Creates the database object.
	 *
	 * @return true, if successful
	 */
	public boolean CreateArticle(ArticleObject articleObject) {
		boolean success = _databaseWrapper.CreateNode(articleObject);
		
		return success;
	}
	
	public boolean CreateSubArticle(SubArticleObject subArticleObject) {
		boolean success = _databaseWrapper.CreateNode(subArticleObject);
		
		return success;
	}

	/**
	 * Insert into database.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public boolean InsertIntoDatabase(IDatabaseObject object) {

		return false;
	}

	/**
	 * Update database.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public boolean UpdateDatabase(IDatabaseObject object) {
		return false;
	}

	/**
	 * Delete database object.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public boolean DeleteDatabaseObject(IDatabaseObject object) {
		return false;
	}
}
