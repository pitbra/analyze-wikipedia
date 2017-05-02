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
    
    private static final boolean [] tricker = {false, false, false, false, false};       //0=null   //1=TITLE-LINK   //2=ARTICLE-LINK    //3=EXTERN-LINK   //4=CATEGORIE
    
    private static final String[][] ARGUMENT = {    /*0*/   {"-h","/h"},    //HELP
                                                    /*1*/   {"-a","/a"},    //TITLE-LINK
                                                    /*2*/   {"-s","/s"},    //ARTICLE-LINK
                                                    /*3*/   {"-e","/e"},    //EXTERN-LINK
                                                    /*4*/   {"-c","/c"}};   //CATEGORIE
    
    private final static String TEST_FILE = new File(new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent()).getParent() + "/src/test/java/de/unileipzig/analyzewikipedia/dumpreader/testfiles/test_3pages.xml"; //"/Desktop/test.xml";
    
    private final static String PAGE_TAG = "page";
    private final static String TITLE_TAG = "title";
    private final static String TEXT_TAG = "text";
    
    private final static String NEO4J_LINK = "bolt://localhost:7687/";
    private final static String NEO4J_USER = "neo4j";
    private final static String NEO4J_PASS = "tgZHyAtvhlWDav5CXD0F";
    
    private final static String URL_WIKI = "https://de.wikipedia.org/wiki/";
    
    private final static int CORES = Runtime.getRuntime().availableProcessors();
    
    private final static String[] FILE_EXTENSION = {    /* 0*/  "xml"};
    
    // TIME in MILLISECONDS
    private final static Integer THREAD_SLEEPTIME = 100;
    private final static Integer TIMER_SLEEPTIME = 5000;
    private final static Integer READER_LEADTIME = 2000;
    
    static String[] TEXT = {/* 0*/  "WikiDumpReader",
                            /* 1*/  "Information",
                            /* 2*/  "file"};
    
    static String[] INFORMATION = { /*0*/   "Show this information.",
                                    /*1*/   "Search for wiki internal title links.",
                                    /*2*/   "Search for wiki internal article links.",
                                    /*3*/   "Search for external links.",
                                    /*4*/   "Search for categories."};
    
    /**
     * METHOD: generate the helpdialog
     * 
     */
    public static final void helpText(){
        
        // out app name
        System.out.println(TEXT[1] + "\t" + TEXT[0]);
                    
        // out all possible arguments in one line
        String out = "\t" + TEXT[0].toUpperCase() + " " + TEXT[2] + " [";
        for (String[] argu : ARGUMENT) {
            out = out + argu[1] + "] [";
        }
        System.out.println(out.substring(0, out.length()-2));

        // out the possible arguments per line with description
        for (int i = 0; i < ARGUMENT.length; i++){
            System.out.println(ARGUMENT[i][0] + ", " + ARGUMENT[i][1] + "\t\t" + INFORMATION[i]);
        }
    }
    
    /**
     * Getter: return array of fileextensions
     * 
     * @return extensions
     */
    public static final String[] getFileExtension(){
        
        return FILE_EXTENSION;
        
    }
    
    /**
     * GETTER: get status of tricker
     * 
     * @param num as integer
     * @return tricker as boolean
     */
    public static final boolean getTricker(Integer num){
        
        return tricker[num];
        
    }
    
    /**
     * GETTER: get number of trickers
     * 
     * @return tricker as boolean
     */
    public static final Integer getTrickers(){
        
        return tricker.length;
        
    }
    
    /**
     * GETTER: return possible argument as reference
     * 
     * @param num as integer
     * @return argument
     */
    public static final String[] getArgument(Integer num){
        
        return ARGUMENT[num];
        
    }
    
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
    
    /**
     * GETTER: get sleep time for timer
     * 
     * @return sleeptime as integer
     */
    public static final Integer getReaderLeadtime(){
        
        return READER_LEADTIME;
        
    }
    
    /**
     * SETTER: set status of tricker
     * 
     * @param num as integer
     * @param bol as boolean
     * @return tricker as boolean
     */
    public static final boolean setTricker(Integer num, boolean bol){
        
        return tricker[num] = bol;
        
    }
    
}
