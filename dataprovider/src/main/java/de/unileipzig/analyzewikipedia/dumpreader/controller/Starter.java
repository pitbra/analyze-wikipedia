package de.unileipzig.analyzewikipedia.dumpreader.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Danilo
 * 
 * class starts and inits the java application
 */
public class Starter {

    /**
     * MAIN: start the java file
     * 
     * @param args as string array
     */
    public static void main(String[] args) {
        
        try {
            // load the threads and start work
            ThreadController.initThreads(args);
        } catch (Exception ex) {
            Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
        }
                     
    }
    
}
