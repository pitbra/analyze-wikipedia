package de.unileipzig.analyzewikipedia.textanalyse;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

/**
 * @author Danilo Morgado
 */
public class TextConverterHelper {
    
    public static String parseText(String text) {
        
        BodyContentHandler handler = new BodyContentHandler();
        AutoDetectParser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        
        InputStream is = IOUtils.toInputStream(text);

        try {
            parser.parse(is, handler, metadata);
        } catch (IOException | SAXException | TikaException ex) {
            return "";
        }

        return handler.toString();
        
    }
    
    public static Object[] normaliseText(String language, String text){

        // clean text via apache tika
        String cleaned = TextConverterHelper.parseText(text);

        // find language via apache tika
        if (language == null) language = LanguageDetectionHelper.getLanguage(cleaned);

        // lowercase for good stemming
        cleaned = cleaned.toLowerCase();

        // stem via tartarus snowball
        String stem = StemmerHelper.stem(language, cleaned);

        return new Object[]{stem, language};

    }
    
    public static String cutByTag(String text, String[] cut){

        String tag_start = cut[0];
        String tag_end = cut[1];
        
        while (text.contains(tag_start) && text.contains(tag_end)){
            
            Integer end = text.indexOf(tag_end);

            String to_end = text.substring(0, end);

            Integer to_start = to_end.lastIndexOf(tag_start);
            
            text = text.substring(0, to_start) + text.substring(end + tag_end.length());
        
        }
        
        return text;
        
    }
    
    public static String cleanTag(String text, String[] tags){
        
        for (String tag : tags){
            
            text = text.replaceAll(tag, "");
            
        }
        
        return text;
        
    }
    
    public static String insertDuringTag(String text, String[] inserts){
        
        String tag_start = inserts[0];
        String tag_end = inserts[1];
        String special = inserts[2];
        
        while(text.contains(tag_start) && text.contains(tag_end)){

            Integer end = text.indexOf(tag_end);
            
            String to_end = text.substring(0, end);
            
            Integer to_start = to_end.lastIndexOf(tag_start);
            
            String insert = text.substring(to_start + tag_start.length(), end);
            
            if (insert.contains(special)) insert = insert.substring(insert.indexOf(special) + special.length());
            
            text = text.substring(0, to_start) + insert + text.substring(end + tag_end.length());
            
        }
        
        return text;
        
    }
    
}
