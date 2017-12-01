package de.unileipzig.analyzewikipedia.textanalyse;

import java.util.LinkedList;
import java.util.List;

import opennlp.tools.tokenize.SimpleTokenizer;

import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.*;

/**
 * @author Danilo Morgado
 */
public class StemmerHelper {
    
    private static final List<SnowballProgram> STEMMER = new LinkedList();
    
    private static void initStemmer(String lang){
        
        STEMMER.clear();
        
        switch(lang){
            case "hy": STEMMER.add(new ArmenianStemmer()); break;
            case "eu": STEMMER.add(new BasqueStemmer()); break;
            case "ca": STEMMER.add(new CatalanStemmer()); break;
            case "da": STEMMER.add(new DanishStemmer()); break;
            case "nl": STEMMER.add(new DutchStemmer());
                       STEMMER.add(new KpStemmer()); break;
            case "en": STEMMER.add(new EnglishStemmer());
                       STEMMER.add(new LovinsStemmer()); break;
            case "fi": STEMMER.add(new FinnishStemmer()); break;
            case "fr": STEMMER.add(new FrenchStemmer()); break;
            case "de": STEMMER.add(new GermanStemmer());
                       STEMMER.add(new German2Stemmer()); break;
            case "hu": STEMMER.add(new HungarianStemmer()); break;
            case "ga": STEMMER.add(new IrishStemmer()); break;
            case "it": STEMMER.add(new ItalianStemmer()); break;
            case "lt": STEMMER.add(new LithuanianStemmer()); break;
//            case "nb": STEMMER.add(new NorwegianStemmer()); break;    /*no language detection*/
            case "pt": STEMMER.add(new PortugueseStemmer()); break;
            case "ro": STEMMER.add(new RomanianStemmer()); break;
            case "ru": STEMMER.add(new RussianStemmer()); break;
            case "es": STEMMER.add(new SpanishStemmer()); break;
            case "sv": STEMMER.add(new SwedishStemmer()); break;
            case "tr": STEMMER.add(new TurkishStemmer()); break;
            default: STEMMER.add(new PorterStemmer()); break;
        }
    }
    
    public static String stem(String lang, String sentences){
        
        initStemmer(lang);
        
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String tokens[] = tokenizer.tokenize(sentences);
        
        String stem = "";
        
        for (int i = 0; i < tokens.length; i++){
            tokens[i] = stem(tokens[i]);
        }
                
        for (String token : tokens) stem = stem + token + " ";
        
        return stem;
        
    }
    
    private static String stem(String word){
        
        String edit = word;
        for (SnowballProgram st : STEMMER){
            st.setCurrent(edit);
            st.stem();
            edit = st.getCurrent();
        }
        
        return edit;
        
    }
    
}
