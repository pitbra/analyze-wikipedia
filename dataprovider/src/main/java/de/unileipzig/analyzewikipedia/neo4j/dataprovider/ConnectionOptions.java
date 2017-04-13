/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataprovider;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectionOptions.
 */
public class ConnectionOptions {

    /** The connection string. */
    private String _connectionString = "bolt://localhost:7687";

    private String _userName = "neo4j";

    private String _password = "tgZHyAtvhlWDav5CXD0F";

    /** The self. */
    private static ConnectionOptions _self;

    /**
     * Instantiates a new connection options.
     *
     * @param connectionString
     *            the connection string
     * @param password 
     */
    private ConnectionOptions(String connectionString, String username, String password) {
    	_connectionString = connectionString;
    	_userName = username;
    	_password = password;
    }

    /**
     * Gets the instance.
     *
     * @return the connection options
     * @throws Exception
     *             the exception
     */
    public static ConnectionOptions GetInstance() throws Exception {
	if (_self == null) {
	    throw new Exception();
	}

	return _self;
    }

    /**
     * Gets the instance.
     *
     * @param connectionString
     *            the connection string
     * @return the connection options
     */
    public static ConnectionOptions GetInstance(String connectionString, String username, String password) {
	if (_self == null) {
	    _self = new ConnectionOptions(connectionString, username, password);
	}

	return _self;
    }

    /**
     * Gets the connection string.
     *
     * @return the string
     */
    public String GetConnectionString() {
	return _connectionString;
    }

    /**
     * Sets the connection string.
     *
     * @param connectionString
     *            the connection string
     */
    public void SetConnectionString(String connectionString) {
	_connectionString = connectionString;
    }

    /**
     * Gets the username.
     *
     * @return the string
     */
    public String GetUsername() {
	return _userName;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the username
     */
    public void SetUsername(String username) {
	_userName = username;
    }

    /**
     * Gets the password.
     *
     * @return the string
     */
    public String GetPassword() {
	return _password;
    }

    public void SetPassword(String password) {
	_password = password;
    }
}
