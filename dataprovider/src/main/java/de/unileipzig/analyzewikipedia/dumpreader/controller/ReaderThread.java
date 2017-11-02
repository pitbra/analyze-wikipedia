package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.nio.charset.StandardCharsets;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class ReaderThread implements Runnable {
    
    private static String file_name = "";
    private static long file_length = 0;
    private static long read_length = 0;
    
    private static long start_time;
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public ReaderThread(){
        
    }
            
    /**
     * METHOD: execution of thread file reader
     * 
     */
    @Override
    public void run() {
        
        getFiles(ThreadController.getArguments());
        
        // convert each file to documents
        while (!ThreadController.fileIsEmpty()){
            
            readFile(ThreadController.removeFile());
            
        }
        
    }
    
    /**
     * METHOD: get files from input arguments
     * 
     * @param pathes as string array
     */
    private void getFiles(String[] pathes){
        
        // check each input argument
        for (String path : pathes){
            
            File test_file = new File(path);
            
            // if file hasn't correct extension, next file
            if (!hasCorrectFileExtension(test_file)) continue;
            
            // test if file exist
            if (test_file.exists()){
            
                // check if file is file (add it) or directory (add elements in it)
                if (test_file.isFile()){

                    // save file in store
                    ThreadController.addFile(test_file);

                } else if (test_file.isDirectory()){

                    File[] directory_files = test_file.listFiles();
                    
                    for (File directory_file : directory_files) {
                        
                        if (directory_file.exists()) {
                            
                            // save file in store
                            ThreadController.addFile(directory_file);
                                    
                        }
                    
                    }
                    
                }
            
            }
            
        }
        
        // set fix test path if arguments are empty
        if (pathes.length == 0){
            
            ThreadController.addFile(new File(Components.getTestFile()));
            
        }
        
    }
    
    /**
     * METHOD: check file of correct extension
     * 
     * @param file as object
     * @return status as boolean
     */
    private boolean hasCorrectFileExtension(File file) {
        
        // get filepath
        String name = file.getName();
        
        try {
            
            // try to seperate file extension
            name = name.substring(name.lastIndexOf(".") + 1);
                        
        } catch (Exception e) {
            
            return false;
            
        }
        
        // check extension with extension list
        for (String ext : Components.getFileExtension()){
            
                if(name.equals(ext)) return true;
                
        }
        
        return false;
        
    }
    
    /**
     * METHOD: read a file line by line and store the page textes
     * 
     * @param file as object
     */
    private void readFile(File file){
        
        BufferedReader br;
        
        try {
            
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            
            String page_text = "";
            String currentLine = br.readLine();
            
            file_name = file.getName();
            file_length = file.length();
            
            start_time = System.currentTimeMillis();
            
            // check first line of mediawiki tag
            if(currentLine.contains("<" + Components.getMediaTag())) {
                
                while (currentLine != null){
                    
                    // remember text length
                    read_length += currentLine.length();
                    
                    // check start of page
                    if(currentLine.contains("<" + Components.getPageTag() + ">")) {

                        // clear page text
                        page_text = "";

                    }

                    // concat line to pagetext
                    page_text = page_text + currentLine + "\n";

                    // check end of page
                    if(currentLine.contains("</" + Components.getPageTag() + ">")) {

                        Document doc = stringToDocument(page_text);

                        ThreadController.addDocument(doc);

                        // TEST out the page text
    //                    System.out.println(page_text);

                    }

                    // read next line
                    currentLine = br.readLine();

                }
                
            }
            
            br.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReaderThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * METHOD: load xml text from string
     * 
     * @param xmltext as file
     * @return document
     */
    private Document stringToDocument(String xmltext){
        
        Document doc = null;

        // try to load the text
        try {
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(xmltext);
            InputSource is = new InputSource(sr);
            doc = builder.parse(is);
            
            doc.getDocumentElement().normalize();

        } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
            //
        }
        
        return doc;
        
    }
        
}
