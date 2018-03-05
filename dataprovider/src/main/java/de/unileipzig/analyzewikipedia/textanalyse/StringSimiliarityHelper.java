package de.unileipzig.analyzewikipedia.textanalyse;

import org.apache.lucene.search.spell.NGramDistance;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.apache.lucene.search.spell.LevensteinDistance;

/**
 * @author Danilo Morgado
 */
public class StringSimiliarityHelper {
    
    public static final String REPLACE_STRING = "...";
    
    public static int getLongestWordSequenze(String replacedtext){
        
        String[] words = replacedtext.split("\\s+");
        
        int max = 0;
        int count = 0;
        for (String word : words){
            if (word.equals(REPLACE_STRING)){
                if (count > max) max = count;
                count = 0;
            } else {
                count++;
            }
        }
        
        return Math.max(count, max);
        
    }
    
    public static String getWordOverlapText(String origin, String comp){
        return NGramHelper.getWortoverlapText(origin, comp);
    }
    
    // shows a logarithmic near field ngram model in percent, threshold over 0.3    [0.0 -> 1.0]
    public static double getNgramFrequenze(String origin, String comp){
        
        return NGramHelper.getNgramFrequenze(origin, comp, 2);
        
    }
    
    // shows a length of same ngrams in percent, threshold over 0.6 [0.0 -> 1.0]
    public static double getNgramDistance(String origin, String comp){
        
        int min = Math.min(origin.length(), comp.length());
        double sum = 0.0;
        
        for (int i = 2; i < min; i++){
            NGramDistance ngd = new NGramDistance(i);
            double res = ngd.getDistance(origin, comp);
            sum += res;
        }
        
        return sum/min;
        
    }
    
    // threshold over 0.7    [0.0 -> 1.0]
    public static double getJarowinklerDistance(String origin, String comp){
        
        JaroWinklerDistance jwd = new JaroWinklerDistance();
    
        return jwd.getDistance(origin, comp);
        
    }
    
    // threshold over 0.3    [0.0 -> 1.0]
    public static double getLevensteinDistance(String origin, String comp){
    
        LevensteinDistance lsd = new LevensteinDistance();
        
        return lsd.getDistance(origin, comp);
    
    }
}
