package de.unileipzig.analyzewikipedia.textanalyse;

/**
 * @author Danilo Morgado
 */
public class MediaWikiLanguageHelper {
    
    public enum Language {
        DE          (0),
        EN          (1),
        ES          (2);

        private final int state;

        Language(int code) {
            this.state = code;
        }

        public int getLanguage() {
            return this.state;
        }
    }
    
    private static final String DE_WEBLINK = "Weblinks";
    private static final String EN_WEBLINK = "External links";
    private static final String ES_WEBLINK = "Enlaces externos";
    
    private static final String[] WEBLINKS = new String[]{DE_WEBLINK, EN_WEBLINK, ES_WEBLINK};
    
    private static final String[] DE_TITLE = new String[]{"Siehe auch",     "Literatur",                                "Anmerkungen",  "Einzelnachweise",                      DE_WEBLINK};
    private static final String[] EN_TITLE = new String[]{"See also",       "Bibliography", "Notes",    "Citations",    "References",   "Further reading",  "Internet sources", EN_WEBLINK};
    private static final String[] ES_TITLE = new String[]{"Véase también",  "Bibliografía", "Notas",                    "Referencias",                                          ES_WEBLINK};
    
    private static final String[][] LANGUAGE = new String[][]{DE_TITLE, EN_TITLE, ES_TITLE};
    
    public static Boolean isNotMediawikiBaseTitle(String title){
        
        for(String[] lang : LANGUAGE){
            
            for(String compare : lang){
                
                if (title.equals(compare)) return true;
                
            }
            
        }
        
        return false;
        
    }
    
    public static Boolean isMediawikiWeblink(String title){
        
        for(String name : WEBLINKS){
            
            if (title.equals(name)) return true;
            
        }
        
        return false;
        
    }
    
}
