package de.unileipzig.analyzewikipedia.textanalyse;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.analysis.bg.BulgarianAnalyzer;
//import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.bn.BengaliAnalyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.fi.FinnishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.gl.GalicianAnalyzer;
import org.apache.lucene.analysis.hi.HindiAnalyzer;
import org.apache.lucene.analysis.hu.HungarianAnalyzer;
import org.apache.lucene.analysis.id.IndonesianAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.lv.LatvianAnalyzer;
import org.apache.lucene.analysis.no.NorwegianAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.sv.SwedishAnalyzer;

/**
 * @author Danilo Morgado
 * 
 * class helps to analyse the crawled webdocuments
 */
public class AnalyzerHelper {
    
    private static String language;
    
    private static StopwordAnalyzerBase ANALYSER;
    
    /**
     * METHOD: initialize the analyzer helper for special language
     * 
     * @param lang
     */
    protected static void init(String lang){
        
        language = lang.toLowerCase().trim();
        
        setAnalyser();
        
    }
    
    /**
     * METHOD: set the analyzer helper for special language
     */
    private static void setAnalyser(){
        
        switch(language){
            case "af": /* Afrikaans */                                          break;
            case "an": /* Aragonese */                                          break;
            case "ar": ANALYSER = new ArabicAnalyzer();                         break;
            case "be": /* Belarusian */                                         break;
            case "bg": ANALYSER = new BulgarianAnalyzer();                      break;
            case "bn": ANALYSER = new BengaliAnalyzer();                        break;
            case "br": /* Breton */                                             break;
            case "ca": /* Catalan */                                            break;
            case "cs": ANALYSER = new CzechAnalyzer();                          break;
            case "cy": /* Welsh */                                              break;
            case "da": /* Danish */                                             break;
            case "de": ANALYSER = new GermanAnalyzer();                         break;
            case "el": ANALYSER = new GreekAnalyzer();                          break;
            case "en": ANALYSER = new EnglishAnalyzer();                        break;
            case "es": ANALYSER = new SpanishAnalyzer();                        break;
            case "et": /* Estonian */                                           break;
            case "eu": /* Basque */                                             break;
            case "fa": /* Persian */                                            break;
            case "fi": ANALYSER = new FinnishAnalyzer();                        break;
            case "fr": ANALYSER = new FrenchAnalyzer();                         break;
            case "ga": /* Irish */                                              break;
            case "gl": ANALYSER = new GalicianAnalyzer();                       break;
            case "gu": /* Gujarati */                                           break;
            case "he": /* Hebrew */                                             break;
            case "hi": ANALYSER = new HindiAnalyzer();                          break;
            case "hr": /* Croatian */                                           break;
            case "ht": /* Haitian */                                            break;
            case "hu": ANALYSER = new HungarianAnalyzer();                      break;
            case "hy": /* Armenian */                                           break;
            case "id": ANALYSER = new IndonesianAnalyzer();                     break;
            case "is": /* Icelandic */                                          break;
            case "it": ANALYSER = new ItalianAnalyzer();                        break;
            case "ja": /* Japanese */                                           break;
            case "km": /* CentralKhmer */                                       break;
            case "kn": /* Kannada */                                            break;
            case "ko": /* Korean */                                             break;
            case "lt": /* Lithuanian */                                         break;
            case "lv": ANALYSER = new LatvianAnalyzer();                        break;
            case "mk": /* Macedonian */                                         break;
            case "ml": /* Malayalam */                                          break;
            case "mr": /* Marathi */                                            break;
            case "ms": /* Malay */                                              break;
            case "mt": /* Maltese */                                            break;
            case "ne": /* Nepali */                                             break;
            case "nl": /* Dutch */                                              break;
            case "no":
            case "nb":
            case "nn": ANALYSER = new NorwegianAnalyzer();                      break;
            case "oc": /* Occitan */                                            break;
            case "pa": /* Panjabi */                                            break;
            case "pl": /* Polish */                                             break;
            case "pt": ANALYSER = new PortugueseAnalyzer();                     break;
            case "ro": /* Romanian */                                           break;
            case "ru": ANALYSER = new RussianAnalyzer();                        break;
            case "sk": /* Slovak */                                             break;
            case "sl": /* Slovenian */                                          break;
            case "so": /* Somali */                                             break;
            case "sq": /* Albanian */                                           break;
            case "sr": /* Serbian */                                            break;
            case "sv": ANALYSER = new SwedishAnalyzer();                        break;
            case "sw": /* Swahili */                                            break;
            case "ta": /* Tamil */                                              break;
            case "te": /* Telugu */                                             break;
            case "th": /* Thai */                                               break;
            case "tl": /* Tagalog */                                            break;
            case "tr": /* Turkish */                                            break;
            case "uk": /* Ukrainian */                                          break;
            case "ur": /* Urdu */                                               break;
            case "vi": /* Vietnamese */                                         break;
            case "yi": /* Yiddish */                                            break;
            default: /* NULL */                                                 break;
        }
        
    }
    
    /**
     * METHOD: check if language is usable
     * @return language exist
     */
    protected static Boolean isUsable(){
        
        return ANALYSER != null;
        
    }
    
    /**
     * METHOD: analyse the given text
     * 
     * @param sentence as string
     * @return text
     */
    protected static String analyze(String sentence){
          
        String analysed = "";
        
        TokenStream stream;
        
        try {
            setAnalyser();
            stream = ANALYSER.tokenStream("", new StringReader(sentence));
            
            CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            
            while (stream.incrementToken()) {
                analysed = analysed + cattr.toString() + " ";
            }
            
            stream.end();
            stream.close();
            
        } catch (IOException ex) {}
        
        return analysed;
    }
    
    /**
     * METHOD: standard method for analysing
     * 
     * @param sentence as string
     * @return text
     */
    protected static String standard(String sentence){
        
        String standard = "";
        
        Analyzer analyzer;
        TokenStream stream;
        
        try {
            analyzer = new StandardAnalyzer();
            stream = analyzer.tokenStream("", new StringReader(sentence));
            
            CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
            
            TokenStream result = new StandardFilter(stream);
            result = new LowerCaseFilter(result);
            
            result.reset();
            while (result.incrementToken()) {
                standard = standard + cattr.toString() + " ";
            }
            
            stream.end();
            stream.close();
            
            analyzer.close();
            
        } catch (IOException ex) {}    
        
        return standard;
        
    }
    
}
