package de.unileipzig.analyzewikipedia.textanalyse;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Danilo Morgado
 */
public class NGramHelper {
    
    protected static String getWortoverlapText(String origin, String comp, String replace){
        
        Queue<String> wordsOfOriginGramm = new ConcurrentLinkedQueue();
        Queue<String> wordsOfComparGramm = new ConcurrentLinkedQueue();
        
        for (String ngram : ngrams(1, origin)) wordsOfOriginGramm.add(ngram);
        for (String ngram : ngrams(1, comp)) wordsOfComparGramm.add(ngram);
        
        for (String word : wordsOfComparGramm){
            if(!wordsOfOriginGramm.contains(word)) {
                wordsOfComparGramm.remove(word);
            }
        }
        
        for (String word : wordsOfOriginGramm){
            if(!wordsOfComparGramm.contains(word)) {
                origin = origin.replace(word, replace);
                wordsOfOriginGramm.remove(word);
            }
        }
        
        return origin;
        
    }
    
    protected static double getNgramFrequenzeLOG(String origin, String comp){
        return getNgramFrequenze(origin, comp, 10);
    }
    
    protected static double getNgramFrequenzeLN(String origin, String comp){
        return getNgramFrequenze(origin, comp, null);
    }
    
    protected static double getNgramFrequenze(String origin, String comp, Integer base){
        
        int max = Math.max(origin.split("\\s+").length, comp.split("\\s+").length);
        int val = 0;
        for(int i = max; i > 0; i--){
            val += (max - (i-1)) * i;
        }
        
        int val_compare = getNgramValue(origin, comp);
        
        double division;
        if (base == null || base <= 1) {
            division = 1;
        } else {
            division = Math.log(base);
        }
        
        double log_val = Math.log(val)/division;
        double log_compare = Math.log(val_compare)/division;
        
        return log_compare/log_val;
        
    }
    
    private static int getNgramValue(String s1, String s2){
        
        List<String> result = new ArrayList();
        List<String> wordsOfFirstGramm = new ArrayList();
        List<String> wordsOfSecondGramm = new ArrayList();
        int val = 0;
        
        for (int i = 1; i <= Math.max(s1.length(), s2.length()); i++){
            
            wordsOfFirstGramm.clear();
            wordsOfSecondGramm.clear();
            
            for (String ngram : ngrams(i, s1)) wordsOfFirstGramm.add(ngram);
            for (String ngram : ngrams(i, s2)) wordsOfSecondGramm.add(ngram);
            
            for(String ngram : wordsOfFirstGramm) if(wordsOfSecondGramm.contains(ngram)) result.add(ngram);
            if (result.isEmpty()) break;
                        
            val += i * result.size();
            result.clear();
            
        }
        
        return val;
    }
    
    private static List<String> ngrams(int n, String str) {
        List<String> ngrams = new ArrayList<>();
        String[] words = str.split("\\s+");
        for (int i = 0; i < words.length - n + 1; i++)
            ngrams.add(concat(words, i, i+n));
        return ngrams;
    }

    private static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append(i > start ? " " : "").append(words[i]);
        return sb.toString();
    }
    
}
