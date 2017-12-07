package de.unileipzig.analyzewikipedia.textanalyse;

import java.io.IOException;
import java.io.StringReader;

import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.BasqueStemmer;
import org.tartarus.snowball.ext.CatalanStemmer;
import org.tartarus.snowball.ext.DanishStemmer;
import org.tartarus.snowball.ext.DutchStemmer;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.FinnishStemmer;
import org.tartarus.snowball.ext.FrenchStemmer;
import org.tartarus.snowball.ext.German2Stemmer;
import org.tartarus.snowball.ext.GermanStemmer;
import org.tartarus.snowball.ext.HungarianStemmer;
import org.tartarus.snowball.ext.IrishStemmer;
import org.tartarus.snowball.ext.ItalianStemmer;
import org.tartarus.snowball.ext.KpStemmer;
import org.tartarus.snowball.ext.LithuanianStemmer;
import org.tartarus.snowball.ext.LovinsStemmer;
import org.tartarus.snowball.ext.NorwegianStemmer;
import org.tartarus.snowball.ext.PorterStemmer;
import org.tartarus.snowball.ext.PortugueseStemmer;
import org.tartarus.snowball.ext.RomanianStemmer;
import org.tartarus.snowball.ext.RussianStemmer;
import org.tartarus.snowball.ext.SpanishStemmer;
import org.tartarus.snowball.ext.SwedishStemmer;
import org.tartarus.snowball.ext.TurkishStemmer;

/**
 * @author Danilo Morgado
 */
public class SnowballStemmerHelper {
    
    private static final List<SnowballProgram> STEMMERS = new LinkedList();
    
    private static final BasqueStemmer SNOWBALL_BASQUE = new BasqueStemmer();
    private static final CatalanStemmer SNOWBALL_CATALAN = new CatalanStemmer();
    private static final DanishStemmer SNOWBALL_DANISH = new DanishStemmer();
    private static final DutchStemmer SNOWBALL_DUTCH = new DutchStemmer();
    private static final EnglishStemmer SNOWBALL_ENGLISH = new EnglishStemmer();
    private static final FinnishStemmer SNOWBALL_FINNISH = new FinnishStemmer();
    private static final FrenchStemmer SNOWBALL_FRENCH = new FrenchStemmer();
    private static final German2Stemmer SNOWBALL_GERMAN2 = new German2Stemmer();
    private static final GermanStemmer SNOWBALL_GERMAN = new GermanStemmer();
    private static final HungarianStemmer SNOWBALL_HUNGARIAN = new HungarianStemmer();
    private static final IrishStemmer SNOWBALL_IRISH = new IrishStemmer();
    private static final ItalianStemmer SNOWBALL_ITALIAN = new ItalianStemmer();
    private static final KpStemmer SNOWBALL_KP = new KpStemmer();
    private static final LithuanianStemmer SNOWBALL_LITHUANIAN = new LithuanianStemmer();
    private static final LovinsStemmer SNOWBALL_LOVINS = new LovinsStemmer();
    private static final NorwegianStemmer SNOWBALL_NORWEGIAN = new NorwegianStemmer();
    private static final PorterStemmer SNOWBALL_PORTER = new PorterStemmer();
    private static final PortugueseStemmer SNOWBALL_PORTUGUESE = new PortugueseStemmer();
    private static final RomanianStemmer SNOWBALL_ROMANIAN = new RomanianStemmer();
    private static final RussianStemmer SNOWBALL_RUSSIAN = new RussianStemmer();
    private static final SpanishStemmer SNOWBALL_SPANISH = new SpanishStemmer();
    private static final SwedishStemmer SNOWBALL_SWEDISH = new SwedishStemmer();
    private static final TurkishStemmer SNOWBALL_TURKISH = new TurkishStemmer();
    
    protected static void init(String lang){
        
        STEMMERS.clear();
        
        switch(lang.toLowerCase().trim()){
            case "af": /* Afrikaans */                                          break;
            case "an": /* Aragonese */                                          break;
            case "ar": /* Arabic */                                             break;
            case "be": /* Belarusian */                                         break;
            case "bg": /* Bulgarian */                                          break;
            case "bn": /* Bengali */                                            break;
            case "br": /* Breton */                                             break;
            case "ca": STEMMERS.add(SNOWBALL_CATALAN); break;
            case "cs": /* Czech */                                              break;
            case "cy": /* Welsh */                                              break;
            case "da": STEMMERS.add(SNOWBALL_DANISH); break;
            case "de": STEMMERS.add(SNOWBALL_GERMAN2);
                       STEMMERS.add(SNOWBALL_GERMAN); break;
            case "el": /* Greek */                                              break;
            case "en": STEMMERS.add(SNOWBALL_ENGLISH);
                       STEMMERS.add(SNOWBALL_PORTER);
                       STEMMERS.add(SNOWBALL_LOVINS); break;
            case "es": STEMMERS.add(SNOWBALL_SPANISH); break;
            case "et": /* Estonian */                                           break;
            case "eu": STEMMERS.add(SNOWBALL_BASQUE); break;
            case "fa": /* Persian */                                            break;
            case "fi": STEMMERS.add(SNOWBALL_FINNISH); break;
            case "fr": STEMMERS.add(SNOWBALL_FRENCH); break;
            case "ga": STEMMERS.add(SNOWBALL_IRISH); break;
            case "gl": /* Galician */                                           break;
            case "gu": /* Gujarati */                                           break;
            case "he": /* Hebrew */                                             break;
            case "hi": /* Hindi */                                              break;
            case "hr": /* Croatian */                                           break;
            case "ht": /* Haitian */                                            break;
            case "hu": STEMMERS.add(SNOWBALL_HUNGARIAN); break;
            case "hy": /* Armenian */                                           break;
            case "id": /* Indonesian */                                         break;
            case "is": /* Icelandic */                                          break;
            case "it": STEMMERS.add(SNOWBALL_ITALIAN); break;
            case "ja": /* Japanese */                                           break;
            case "km": /* CentralKhmer */                                       break;
            case "kn": /* Kannada */                                            break;
            case "ko": /* Korean */                                             break;
            case "lt": STEMMERS.add(SNOWBALL_LITHUANIAN); break;
            case "lv": /* Latvian */                                            break;
            case "mk": /* Macedonian */                                         break;
            case "ml": /* Malayalam */                                          break;
            case "mr": /* Marathi */                                            break;
            case "ms": /* Malay */                                              break;
            case "mt": /* Maltese */                                            break;
            case "ne": /* Nepali */                                             break;
            case "nl": STEMMERS.add(SNOWBALL_DUTCH);
                       STEMMERS.add(SNOWBALL_KP); break;
            case "no":
            case "nb":
            case "nn": STEMMERS.add(SNOWBALL_NORWEGIAN); break;
            case "oc": /* Occitan */                                            break;
            case "pa": /* Panjabi */                                            break;
            case "pl": /* Polish */                                             break;
            case "pt": STEMMERS.add(SNOWBALL_PORTUGUESE); break;
            case "ro": STEMMERS.add(SNOWBALL_ROMANIAN); break;
            case "ru": STEMMERS.add(SNOWBALL_RUSSIAN); break;
            case "sk": /* Slovak */                                             break;
            case "sl": /* Slovenian */                                          break;
            case "so": /* Somali */                                             break;
            case "sq": /* Albanian */                                           break;
            case "sr": /* Serbian */                                            break;
            case "sv": STEMMERS.add(SNOWBALL_SWEDISH); break;
            case "sw": /* Swahili */                                            break;
            case "ta": /* Tamil */                                              break;
            case "te": /* Telugu */                                             break;
            case "th": /* Thai */                                               break;
            case "tl": /* Tagalog */                                            break;
            case "tr": STEMMERS.add(SNOWBALL_TURKISH); break;
            case "uk": /* Ukrainian */                                          break;
            case "ur": /* Urdu */                                               break;
            case "vi": /* Vietnamese */                                         break;
            case "yi": /* Yiddish */                                            break;
            default: /* NULL */                                                 break;
        }
        
    }
    
    protected static Boolean isUsable(){
        
        return !STEMMERS.isEmpty();
        
    }
    
    private static String stem(String word){
        
        for (SnowballProgram st : STEMMERS){
            st.setCurrent(word);
            st.stem();
            word = st.getCurrent();
        }
        
        return word;
        
    }
    
    protected static String analyze(String sentence){
        
        String analysed = "";
        
        StandardAnalyzer analyser;
        TokenStream stream;
                    
        try {
            analyser = new StandardAnalyzer();
            stream = analyser.tokenStream("", new StringReader(sentence));
            
            CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            
            while (stream.incrementToken()) {
                analysed = analysed + stem(cattr.toString()) + " ";
            }
            
            stream.end();
            stream.close();
            
        } catch (IOException ex) {}
        
        return analysed;
        
    }
    
}
