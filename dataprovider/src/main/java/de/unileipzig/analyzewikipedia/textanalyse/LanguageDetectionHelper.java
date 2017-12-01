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
        Code        Language        Stemmer
    
        af          Afrikaans       -
        an          Aragonese       -
        ar          Armenian        ok
        ast         -               -
        be          Belarusian      -
        bg          Bulgarian       -
        bn          Bengali         -
        br          Breton          -
        ca          Catalan         ok
        cs          Czech           -
        cy          Welsh           -
        da          Danish          ok
        de          German          ok
        el          Greek           -
        en          English         ok
        es          Spanish         ok
        et          Estonian        -
        eu          Basque          ok
        fa          Persian         -
        fi          Finnish         ok             
        fr          French          ok
        ga          Irish           ok
        gl          Galician        -
        gu          Gujarati        -
        he          Hebrew          -
        hi          Hindi           -
        hr          Croatian        -
        ht          Haitian         -
        hu          Hungarian       ok
        id          Indonesian      -
        is          Icelandic       -
        it          Italian         ok
        ja          Japanese        -
        km          Central Khmer   -
        kn          Kannada         -
        ko          Korean          -
        lt          Lithuanian      ok             
        lv          Latvian         -
        mk          Macedonian      -
        ml          Malayalam       -
        mr          Marathi         -
        ms          Malay           -
        mt          Maltese         -
        ne          Nepali          -
        nl          Dutch           ok             
        no          Norwegian       -
        oc          Occitan         -
        pa          Panjabi         -
        pl          Polish          -
        pt          Portuguese      ok
        ro          Romanian        ok
        ru          Russian         ok
        sk          Slovak          -
        sl          Slovenian       -
        so          Somali          -
        sq          Albanian        -
        sr          Serbian         -
        sv          Swedish         ok
        sw          Swahili         -
        ta          Tamil           -
        te          Telugu          -
        th          Thai            -
        tl          Tagalog         -
        tr          Turkish         ok
        uk          Ukrainian       -
        ur          Urdu            -
        vi          Vietnamese      -
        yi          Yiddish         -
        zh-CN       
        zh-TW       
    
    */
    
    private static LanguageDetector detector;
    private static LanguageResult result;
        
    public static String getLanguage(String text) {
        
        try {
            detector = new OptimaizeLangDetector().loadModels();
        } catch (IOException ex) {
            return "error";
        }
        
        result = detector.detect(text);
        
        return result.getLanguage();
        
    }
    
}
