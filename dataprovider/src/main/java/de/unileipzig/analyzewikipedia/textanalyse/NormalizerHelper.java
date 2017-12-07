package de.unileipzig.analyzewikipedia.textanalyse;

/**
 * @author Danilo Morgado
 */
public class NormalizerHelper {
    
    public static String normalize(String lang, String sentences){
        
        AnalyzerHelper.init(lang);
        SnowballStemmerHelper.init(lang);
        
        String edit = sentences;
        
        if (AnalyzerHelper.isUsable()) {
            edit = AnalyzerHelper.analyze(edit);
        }
        if (SnowballStemmerHelper.isUsable()) {
            edit = SnowballStemmerHelper.analyze(edit);
        }
        
        if (    !AnalyzerHelper.isUsable() &&
                !SnowballStemmerHelper.isUsable()    ) {
            edit = AnalyzerHelper.standard(sentences);
        }
                
        return edit;
        
    }
       
}
