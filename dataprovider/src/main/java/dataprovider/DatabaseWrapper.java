/*
 * 
 */
package dataprovider;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class DatabaseWrapper {

    public DatabaseWrapper() {
    }

    public void CreateNode(INodeObject nodeObject) {
	Driver driver;
	try {
	    driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "tgZHyAtvhlWDav5CXD0F"));
	    final Session session = driver.session();
	    session.run(String.format("CREATE (a:%s {name: {name}, title: {title}})", nodeObject.GetType().GetType()),
		    parameters("name", "Arthur", "title", "King"));

	    session.close();
	    driver.close();
	} catch (final Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
