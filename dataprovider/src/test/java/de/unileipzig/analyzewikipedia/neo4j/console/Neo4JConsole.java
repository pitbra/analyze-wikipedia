package de.unileipzig.analyzewikipedia.neo4j.console;

import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;

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
        DataProvider prov = new DataProvider("bolt://localhost:7687/", "neo4j", "tgZHyAtvhlWDav5CXD0F");
        prov.FindByTitle("Alan_Smithee");  
    }
    
}
