package de.unileipzig.analyzewikipedia.neo4j.console;

import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class starts and inits the java application
 */
public class Neo4JConsole {

    /**
     * MAIN: start the java file
     * 
     * @param args as string array
     */
    public static void main(String[] args) {
        
        try {
            DataProvider prov = new DataProvider("bolt://localhost:7687/", "neo4j", "tgZHyAtvhlWDav5CXD0F", true);  
            prov.FindByTitle("Alan_Smithee");
        } catch (Exception ex) {
            Logger.getLogger(Neo4JConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
