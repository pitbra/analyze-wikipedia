package de.unileipzig.analyzewikipedia.textanalyse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Danilo Morgado
 * 
 * class for supporting by building diffrent ngram methods
 */
public class NGramHelper {
    
    private static void addMapElement(Map<String, List<String>> map, String ngram, String norm){
        
        if (map.get(norm) == null){
            List<String> list = new LinkedList();
            list.add(ngram);
            map.put(norm, list);
        } else {
            if (!map.get(norm).contains(ngram)) map.get(norm).add(ngram);
        }
        
    }
    
    
    public static List<String> getOverlapedPassages(String origintext, String overlaptext, int minphrasecount){
        
        List<String> passages = new LinkedList();
        
        String[] words = overlaptext.split("\\s+");
        
        String tmp = "";
        int count = 0;
        for (String word : words){
            if (word.equals(StringSimiliarityHelper.REPLACE_STRING)){
                if (tmp.trim().length() > 0 && count >= minphrasecount) passages.addAll(checkPhraseInText(origintext, tmp.trim(), minphrasecount));
                tmp = "";
                count = 0;
            } else {
                tmp = tmp + " " + word;
                count++;
            }
        }
        if (tmp.trim().length() > 0) passages.add(tmp.trim());
        
        return passages;
        
    }
    
    private static List<String> checkPhraseInText(String text, String phrase, int phrasecount){
        
        List<String> found = new LinkedList();
        
        String[] words = phrase.split("\\s+");
        
        int start = 0;
        int end = phrasecount - 1;
        String tmp;
        String rem = "";
        
        while(true){
            tmp = "";
            for (int i = start; i <= end; i++) tmp = tmp + " " + words[i];
            tmp = tmp.trim();
            
            if(text.contains(tmp)) {
                rem = tmp;
                end++;
                if (end >= words.length) break;
            } else {
                if (rem.length() > 0) {
                    if (!found.contains(rem)) found.add(rem);
                    rem = "";
                }
                if (end - start > phrasecount){
                    start = end;
                    end = start + phrasecount;
                    if (end >= words.length) break;
                } else {
                    start++;
                    end++;
                    if (end >= words.length) break;
                }
            }        
        }
        
        if (rem.length() > 0) {
            if (!found.contains(rem)) found.add(rem);
            rem = "";
        }
        
        return found;
        
    }
    
    protected static String getWortoverlapText(String origin, String comp){
        
        Map<String, List<String>> wordsOfOriginGramm = new ConcurrentHashMap<>();
        Map<String, List<String>> wordsOfComparGramm = new ConcurrentHashMap<>();
        
        for (String ngram : ngrams(1, origin)) {
            String norm = ngram.toLowerCase().replaceAll("\\p{Punct}", "");
            addMapElement(wordsOfOriginGramm, ngram, norm);
        }
        
        for (String ngram : ngrams(1, comp)) {
            String norm = ngram.toLowerCase().replaceAll("\\p{Punct}", "");
            addMapElement(wordsOfComparGramm, ngram, norm);
        }
        
        for (Map.Entry<String, List<String>> entry : wordsOfComparGramm.entrySet()) {
            if(!wordsOfOriginGramm.containsKey(entry.getKey())) {
                wordsOfComparGramm.remove(entry.getKey());
            }
        }
        
        for (Map.Entry<String, List<String>> entry : wordsOfOriginGramm.entrySet()) {
            if(wordsOfComparGramm.containsKey(entry.getKey())) {
                wordsOfOriginGramm.remove(entry.getKey());
            }
        }
        
        origin = " " + origin + " ";
        for (Map.Entry<String, List<String>> entry : wordsOfOriginGramm.entrySet()) {
            for (String word : entry.getValue()){
                origin = origin.replace(" " + word + " ", " " + StringSimiliarityHelper.REPLACE_STRING + " ");
            }
        }
        origin = origin.trim();
        
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
    
    public static List<String> ngrams(int n, String str) {
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
