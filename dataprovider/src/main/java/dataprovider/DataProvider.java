/*
 * 
 */
package dataprovider;

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
    public DataProvider(String connectionString) {
	// Create ConnectionOptions
	ConnectionOptions.GetInstance(connectionString);
	_databaseWrapper = new DatabaseWrapper();
    }

    /**
     * Creates the database object.
     *
     * @return true, if successful
     */
    public boolean CreateArticle(ArticleObject articleObject) {
	_databaseWrapper.CreateNode(articleObject);
	return false;
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
