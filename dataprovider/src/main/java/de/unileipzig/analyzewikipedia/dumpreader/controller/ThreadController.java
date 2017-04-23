package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import java.io.File;

import org.w3c.dom.Document;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Danilo Morgado
 * 
 * class controls the threads
 */
public class ThreadController {
    
    private static String[] arguments;
    
    private static final Thread READER = new Thread(new ReaderThread());
    private static final Thread[] SEEKERS = new Thread[Components.getCores()];
    private static final Thread[] TRANSMS = new Thread[Components.getCores()];
    
    private static final Queue<File> FILES = new ConcurrentLinkedQueue();
    private static final Queue<Document> DOCS = new ConcurrentLinkedQueue();
    private static final Queue<WikiPage> PAGES = new ConcurrentLinkedQueue();
    
    protected static void initThreads(String[] args){
        
        arguments = args;
                
        // initial seeker and transmitor thread
        for (int i = 0; i < Components.getCores(); i++){

            SEEKERS[i] = new Thread(new SeekerThread());
            TRANSMS[i] = new Thread(new TransmitorThread());

        }
                
        // start reader
        READER.start();
        
        // try to wait a second
        try {
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // start seeker and transmitor thread
        for (int i = 0; i < Components.getCores(); i++){

            SEEKERS[i].start();
            TRANSMS[i].start();

        }
                
        // block reader
        try {
            READER.join();
        }catch (InterruptedException e) {}
        
        // block main programm during thread execution
        for (int i = 0; i < Components.getCores(); i++){

            try {
                SEEKERS[i].join();
                TRANSMS[i].join();
            }catch (InterruptedException e) {}

        }
                
    }
    
    /**
     * METHODE: add a file to queue
     * 
     * @param file as file
     */
    protected static void addFile(File file){
        
        FILES.add(file);
        
    }
    
    /**
     * METHODE: add a document to queue
     * 
     * @param doc as document
     */
    protected static void addDocument(Document doc){
        
        DOCS.add(doc);
        
    }
    
    /**
     * METHODE: add a page to queue
     * 
     * @param page as object
     */
    protected static void addPage(WikiPage page){
        
        PAGES.add(page);
        
    }
    
    /**
     * METHODE: get the top file of the queue
     * 
     * @return file as object
     */
    protected static File removeFile(){
        
        return FILES.poll();
        
    }
    
    /**
     * METHODE: get the top document of the queue
     * 
     * @return doc as string
     */
    protected static Document removeDocument(){
        
        return DOCS.poll();
        
    }
    
    /**
     * METHODE: get the top page of the queue
     * 
     * @return page as object
     */
    protected static WikiPage removePage(){
        
        return PAGES.poll();
        
    }
    
    /**
     * METHODE: check if file queue is empty
     * 
     * @return empty as boolean
     */
    protected static boolean fileIsEmpty(){
    
        return FILES.isEmpty();
        
    }
    
    /**
     * METHODE: check if document queue is empty
     * 
     * @return empty as boolean
     */
    protected static boolean docIsEmpty(){
    
        return DOCS.isEmpty();
        
    }
    
    /**
     * METHODE: check if page queue is empty
     * 
     * @return empty as boolean
     */
    protected static boolean pageIsEmpty(){
    
        return PAGES.isEmpty();
        
    }
    
    /**
     * GETTER: get arguments
     * 
     * @return arguments as string array
     */
    protected static String[] getArguments(){
    
        return arguments;
        
    }
    
    /**
     * GETTER: get reader thread is alive
     * 
     * @return status as boolean
     */
    protected static boolean getReaderIsAlive(){
    
        return READER.isAlive();
        
    }
    
    /**
     * GETTER: get seeker thread group is alive
     * 
     * @return status as boolean
     */
    protected static boolean getSeekersAreAlive(){
    
        for(Thread thread : SEEKERS){
                
            if (thread.isAlive()) return true;

        }
        
        return false;
        
    }
    
    /**
     * GETTER: get transmitors thread group is alive
     * 
     * @return status as boolean
     */
    protected static boolean getTransmitorsAreAlive(){
    
        for(Thread thread : TRANSMS){
                
            if (thread.isAlive()) return true;

        }
                
        return false;
                
    }
    
}
