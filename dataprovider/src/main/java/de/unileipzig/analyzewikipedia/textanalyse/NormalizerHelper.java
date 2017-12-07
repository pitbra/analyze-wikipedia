package de.unileipzig.analyzewikipedia.textanalyse;

/**
 * @author Danilo Morgado
 */
public class NormalizerHelper {
    
    public static String normalize(String lang, String sentences){
        
        initLanguage(lang);
        
        String edit = editSentences(sentences);
        
        edit = standard(edit);
                
        return edit;
        
    }
    
    private static void initLanguage(String lang){
        
        AnalyzerHelper.init(lang);
        SnowballStemmerHelper.init(lang);
        
    }
    
    private static String editSentences(String sentences){
        
        if (AnalyzerHelper.isUsable()) sentences = AnalyzerHelper.analyze(sentences);
        if (SnowballStemmerHelper.isUsable()) sentences = SnowballStemmerHelper.analyze(sentences);
        
        return sentences;
        
    }
    
    private static String standard(String sentences){
        
        if (
                !AnalyzerHelper.isUsable() &&
                !SnowballStemmerHelper.isUsable()
            )
        {
            sentences = AnalyzerHelper.standard(sentences);
        }
        
        return sentences;
        
    }
       
}
