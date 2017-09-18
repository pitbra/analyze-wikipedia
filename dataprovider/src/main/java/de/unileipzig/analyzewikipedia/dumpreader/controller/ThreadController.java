package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;
import de.unileipzig.analyzewikipedia.dumpreader.windows.FileExplorer;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;

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
    
    /**
     * METHOD: initial the application
     * 
     * @param args as stringarray
     * @throws java.lang.Exception
     */
    protected static void initApplication(String[] args)throws Exception{
        
        arguments = checkArguments(args);
        
        if (arguments.length == 0){
            FileExplorer file_explorer = new FileExplorer();
            file_explorer.setVisible(true);
        } else {
            initThreads();
        }
        
    }
    
    /**
     * METHOD: initial the threads
     * 
     * @throws java.lang.Exception
     */
    public static void initThreads() throws Exception{
        
        // initial seeker and transmitor thread
        for (int i = 0; i < Components.getCores(); i++){
            
            SEEKERS[i] = new Thread(new SeekerThread());
            TRANSMS[i] = new Thread(new TransmitorThread());

        }
                
        // start reader
        READER.start();
        
        // try to wait a while
        waitMillis(Components.getReaderLeadtime());
                
        // start seeker thread
        for (int i = 0; i < Components.getCores(); i++){

            SEEKERS[i].start();

        }
        
        // try to wait a while
        waitMillis(Components.getReaderLeadtime());
        
        // start transmitor thread
        for (int i = 0; i < Components.getCores(); i++){
            
            TRANSMS[i].start();

        }
        
        // block main until reader is finish
        try {
            READER.join();
        } catch (InterruptedException e) {}
        
        // block main programm during thread execution
        for (int i = 0; i < Components.getCores(); i++){

            try {
                SEEKERS[i].join();
                TRANSMS[i].join();
            } catch (InterruptedException e) {}

        }
        
        System.exit(0);
    }
    
    /**
     * METHOD: wait e specific time
     * 
     * @param millis as integer
     */
    protected static void waitMillis(Integer millis){
        
        // try to wait a specific time
        try {
            java.util.concurrent.TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * METHOD: check if given url has correct form of an url
     * 
     * @param url as string
     * @return is url as boolean
     */
    protected static boolean isUrl(String url){
        
        try {
            URL tmp = new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }

    }
    
    /**
     * METHOD: check arguments of correct use
     * 
     * @param args argarray
     * return path list
     */
    private static String[] checkArguments(String[] args){
        
        List<String> newArg = new LinkedList();
        
        // iterate over all arguments
        for (String tmpArg : args) {

            boolean brk = false;
            
            // HELP-MODE
            if( Arrays.asList(Components.getArgument(0)).contains(tmpArg) )
            {
                Components.helpText();
                System.exit(0);
            }
            
            // check all arguments (not help) and set the trickers
            for (int i = 1; i < Components.getTrickers(); i++){
                if( !Components.getTricker(i) && Arrays.asList(Components.getArgument(i)).contains(tmpArg))
                {
                    Components.setTricker(0, true); //remember if minimum one tricker is set
                    Components.setTricker(i, true);
                    brk = true;
                }
            }
            
            if (!brk) newArg.add(tmpArg);

        }
        
        // when no trickers are set, set all, because the user send only a file list and want to get all
        if (!Components.getTricker(0)){
            for (int i = 0; i<Components.getTrickers(); i++){
                Components.setTricker(i, true);
            }
        }
        
        return newArg.toArray(new String[newArg.size()]);
        
    }
    
    /**
     * METHOD: add a file to queue
     * 
     * @param file as file
     */
    protected static void addFile(File file){
        
        FILES.add(file);
        
    }
    
    /**
     * METHOD: add a document to queue
     * 
     * @param doc as document
     */
    protected static void addDocument(Document doc){
        
        // MEMORY OPTIMISATION, wait until other threads finished there jobs
        while(DOCS.size() > Components.getDocStackLimit()) {
            
            waitMillis(Components.getStackLimitTime());
            
        }
        
        DOCS.add(doc);
        
    }
    
    /**
     * METHOD: add a page to queue
     * 
     * @param page as object
     */
    protected static void addPage(WikiPage page){
        
        // MEMORY OPTIMISATION, wait until other threads finished there jobs
        while(PAGES.size() > Components.getPageStackLimit()) {
            
            waitMillis(Components.getStackLimitTime());
            
        }
        
        PAGES.add(page);
        
    }
    
    /**
     * METHOD: get the top file of the queue
     * 
     * @return file as object
     */
    protected static File removeFile(){
        
        return FILES.poll();
        
    }
    
    /**
     * METHOD: get the top document of the queue
     * 
     * @return doc as string
     */
    protected static Document removeDocument(){
        
        return DOCS.poll();
        
    }
    
    /**
     * METHOD: get the top page of the queue
     * 
     * @return page as object
     */
    protected static WikiPage removePage(){
        
        return PAGES.poll();
        
    }
    
    /**
     * METHOD: check if file queue is empty
     * 
     * @return empty as boolean
     */
    protected static boolean fileIsEmpty(){
    
        return FILES.isEmpty();
        
    }
    
    /**
     * METHOD: check if document queue is empty
     * 
     * @return empty as boolean
     */
    protected static boolean docIsEmpty(){
    
        return DOCS.isEmpty();
        
    }
    
    /**
     * METHOD: check if page queue is empty
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
    
    /**
     * GETTER: get size of file stack
     * 
     * @return size as integer
     */
    protected static Integer getFileStackSize(){
        
        return FILES.size();
        
    }
    
    /**
     * GETTER: get size of doc stack
     * 
     * @return size as integer
     */
    protected static Integer getDocStackSize(){
        
        return DOCS.size();
        
    }
    
    /**
     * GETTER: get size of page stack
     * 
     * @return size as integer
     */
    protected static Integer getPageStackSize(){
        
        return PAGES.size();
        
    }
    
    /**
     * SETTER: set arguments
     * 
     * @param argus as string array
     */
    public static void setArguments(String[] argus){
    
        arguments = argus;
        
    }
    
}
