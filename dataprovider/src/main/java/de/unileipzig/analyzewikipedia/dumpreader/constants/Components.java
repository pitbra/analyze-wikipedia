package de.unileipzig.analyzewikipedia.dumpreader.constants;

import de.unileipzig.analyzewikipedia.dumpreader.controller.Starter;
import java.io.File;

/**
 * @author Danilo Morgado
 * 
 * components of this application
 * 
 */
public class Components {
    
    private final static String TEST_FILE = new File(new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent()).getParent() + "/src/test/java/de/unileipzig/analyzewikipedia/dumpreader/testfiles/test_3pages.xml"; //"/Desktop/test.xml";
    
    private final static String PAGE_TAG = "page";
    private final static String TITLE_TAG = "title";
    private final static String TEXT_TAG = "text";
    
    private final static String NEO4J_LINK = "bolt://localhost:7687/";
    private final static String NEO4J_USER = "neo4j";
    private final static String NEO4J_PASS = "tgZHyAtvhlWDav5CXD0F";
    
    private final static String URL_WIKI = "https://de.wikipedia.org/wiki/";
    
    private final static int CORES = 1;//Runtime.getRuntime().availableProcessors();
    
    private final static Integer THREAD_SLEEPTIME = 100;
    
    private final static Integer TIMER_SLEEPTIME = 5000;
        
    /**
     * GETTER: return path of test file (WINDOWS)
     * 
     * @return path as string
     */
    public static final String getTestFile(){
        
        return TEST_FILE;
        
    }
    
    /**
     * GETTER: return page tag
     * 
     * @return tag as string
     */
    public static final String getPageTag(){
        
        return PAGE_TAG;
        
    }
    
    /**
     * GETTER: return title tag
     * 
     * @return tag as string
     */
    public static final String getTitleTag(){
        
        return TITLE_TAG;
        
    }
    
    /**
     * GETTER: return text tag
     * 
     * @return tag as string
     */
    public static final String getTextTag(){
        
        return TEXT_TAG;
        
    }
    
    /**
     * GETTER: return neo4j address
     * 
     * @return link as string
     */
    public static final String getNeo4jLink(){
        
        return NEO4J_LINK;
        
    }
    
    /**
     * GETTER: return neo4j user
     * 
     * @return user as string
     */
    public static final String getNeo4jUser(){
        
        return NEO4J_USER;
        
    }
    
    /**
     * GETTER: return neo4j password
     * 
     * @return pass as string
     */
    public static final String getNeo4jPass(){
        
        return NEO4J_PASS;
        
    }
        
    /**
     * GETTER: get url of wiki site
     * 
     * @return url as string
     */
    public static final String getWikiURL(){
        
        return URL_WIKI;
        
    }
    
    /**
     * GETTER: get core number
     * 
     * @return cores as integer
     */
    public static final Integer getCores(){
        
        return CORES;
        
    }
    
    /**
     * GETTER: get sleep time for thread
     * 
     * @return sleeptime as integer
     */
    public static final Integer getThreadSleepTime(){
        
        return THREAD_SLEEPTIME;
        
    }
    
    /**
     * GETTER: get sleep time for timer
     * 
     * @return sleeptime as integer
     */
    public static final Integer getTimerSleepTime(){
        
        return TIMER_SLEEPTIME;
        
    }
           
}
