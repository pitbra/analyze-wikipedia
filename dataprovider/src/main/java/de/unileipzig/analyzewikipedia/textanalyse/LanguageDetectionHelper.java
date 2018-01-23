package de.unileipzig.analyzewikipedia.textanalyse;

import java.io.IOException;

import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

/**
 * @author Danilo Morgado
 */
public class LanguageDetectionHelper {
    
    /* supportet languiages
    
        Code        Language
    
        af          Afrikaans
        an          Aragonese
        ar          Arabic
        be          Belarusian
        bg          Bulgarian
        bn          Bengali
        br          Breton
        ca          Catalan
        cs          Czech
        cy          Welsh
        da          Danish
        de          German
        el          Greek
        en          English
        es          Spanish
        et          Estonian
        eu          Basque
        fa          Persian
        fi          Finnish 
        fr          French
        ga          Irish
        gl          Galician
        gu          Gujarati
        he          Hebrew
        hi          Hindi   
        hr          Croatian
        ht          Haitian 
        hu          Hungarian
        hy          Armenian
        id          Indonesian
        is          Icelandic
        it          Italian
        ja          Japanese
        km          Central Khmer
        kn          Kannada
        ko          Korean
        lt          Lithuanian  
        lv          Latvian
        mk          Macedonian
        ml          Malayalam
        mr          Marathi
        ms          Malay
        mt          Maltese
        ne          Nepali
        nl          Dutch  
        no          Norwegian
        oc          Occitan
        pa          Panjabi
        pl          Polish
        pt          Portuguese
        ro          Romanian
        ru          Russian
        sk          Slovak
        sl          Slovenian
        so          Somali
        sq          Albanian
        sr          Serbian
        sv          Swedish
        sw          Swahili
        ta          Tamil
        te          Telugu
        th          Thai
        tl          Tagalog
        tr          Turkish
        uk          Ukrainian
        ur          Urdu
        vi          Vietnamese
        yi          Yiddish
    
    */
    
    private static LanguageDetector detector = null;
    private static LanguageResult result;
        
    public static String getLanguage(String text) {
        
        if (detector == null) {
            try {
                detector = new OptimaizeLangDetector().loadModels();
            } catch (IOException ex) {
                return "Detector error";
            }
        }
        
        result = detector.detect(text);
        
        return result.getLanguage().toLowerCase();
        
    }
    
}
