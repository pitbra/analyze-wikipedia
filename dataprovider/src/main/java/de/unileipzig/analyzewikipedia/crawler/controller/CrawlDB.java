package de.unileipzig.analyzewikipedia.crawler.controller;

import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Danilo Morgado
 */
public class CrawlDB {
    
    private static final Integer MAXIMUM_QUEUE_ELEMENTS = 100;
    
    private final Queue<CrawledElement> ELEMENTS = new ConcurrentLinkedQueue<>();
    
    // save serialized object in root folder
    private final static String SAVEPATH = "crawlDB.dat";
    
    /**
     * METHOD: save the database
     */
    protected void saveDB(){
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(SAVEPATH);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ELEMENTS);
        } catch (IOException e) {
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {}
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {}
            }
        }
    }
    
    /**
     * METHOD: load the database
     */
    protected void loadDB(){
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(SAVEPATH);
            try {
                ois = new ObjectInputStream(fis);
                try {
                    Queue<CrawledElement> tmp_elements = (Queue<CrawledElement>) ois.readObject();
                    ELEMENTS.clear();
                    ELEMENTS.addAll(tmp_elements);
                } catch (ClassNotFoundException e) {}
            } catch (StreamCorruptedException e) {
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {}
                }
            }
        } catch (IOException e) {
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {}
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {}
            }
        }
    }
    
    /**
     * METHOD: add element to database queue
     * 
     * @param element as object
     */
    public void add(CrawledElement element){
        
        ELEMENTS.add(element);
        
        if (ELEMENTS.size() > MAXIMUM_QUEUE_ELEMENTS) ELEMENTS.remove();
                
    }
    
    /**
     * METHOD: get an existing crawled element
     * 
     * @param title of article
     * @return element as object
     */
    public CrawledElement get(String title){
        
        for(CrawledElement element : ELEMENTS) {
            if (element.getTitle().equals(title)) return element;
        }
        
        return null;
        
    }
    
}
