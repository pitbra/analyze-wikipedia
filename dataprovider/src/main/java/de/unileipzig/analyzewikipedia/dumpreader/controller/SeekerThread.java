package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class SeekerThread implements Runnable {
    
    private static final String SECTION_SEPERATOR = "=";
    
    /**
     * KONSTRUCTOR: default
     */
    public SeekerThread(){  
    }
    
    /**
     * METHOD: execution of thread link seeker
     */
    @Override
    public void run() {
        
        Document doc;
        
        do{
            
            doc = ThreadController.removeDocument();

            if (doc != null){
                
                readArticle(doc);

            }
            
        } while(ThreadController.getReaderIsAlive() || (!ThreadController.getReaderIsAlive() && !ThreadController.docIsEmpty()) );
        
    }
    
    /**
     * METHOD: read the document text to page
     * 
     * @param doc as document
     */
    private void readArticle(Document doc){
        
        // TEST out the root element name
//        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//        System.out.println("-------------------------------------------------");

        // select document by pages
        NodeList nList = doc.getElementsByTagName(Components.getPageTag());

        // iterate page by page
        for (int i=0; i<nList.getLength(); i++) {

            // select the page
            Node nNode = nList.item(i);

            // TEST out the node element name
//            System.out.println("\nCurrent Element :" + nNode.getNodeName());
//            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");

            // check if element of type node element
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                
                // select element by title
                String title = eElement.getElementsByTagName(Components.getTitleTag()).item(0).getTextContent();
                title = replaceText(title);
                title = escapeString(title);

                // select element by text
                String text = eElement.getElementsByTagName(Components.getTextTag()).item(0).getTextContent();
                
                WikiArticle article = generateMainArticle(title, text);
                
                // safe page in store
                ThreadController.addArticle(article);
                
                // TESTout the page title and linkcount
//                System.out.println(page.getName() + " " + page.getIntLinks().size() + ":" + page.getExtLinks().size());
                
            }
        }
        
    }
    
    /**
     * METHOD: split give text in article sections
     * 
     * @param title as string
     * @param text as string
     * @return article with structure
     */
    public static WikiArticle generateSectionArticle(String title, String text){
        return generateMainArticle(title, text, false);
    }
    
    /**
     * METHOD: split give text in article sections
     * 
     * @param title as string
     * @param text as string
     * @return article with structure
     */
    public static WikiArticle generateMainArticle(String title, String text){
        return generateMainArticle(title, text, true);
    }
    
    /**
     * METHOD: split give text in article sections
     * 
     * @param title as string
     * @param text as string
     * @param search as link searcher
     * @return article with structure
     */
    private static WikiArticle generateMainArticle(String title, String text, Boolean search){
        
        WikiArticle article = new WikiArticle(title);
        
        BufferedReader br;
        
        String separator = SECTION_SEPERATOR + SECTION_SEPERATOR;
        
        Queue<String> lines = new LinkedList();
        
        try {
            
            br = new BufferedReader(new StringReader(text));

            String currentLine = "";
            
            while ((currentLine = br.readLine()) != null) {
                
                // remove first spaces
                currentLine = currentLine.trim();
                
                // delete to small sentences for short searching
                if (currentLine.length() < 3) {
                    
                    // start again
                    continue;
                    
                }
                
                if( currentLine.length() > (separator.length()*2+3) &&
                    currentLine.substring(0,separator.length()).equals(separator) && 
                    !currentLine.substring((separator.length()+1),(separator.length()+1)).equals(SECTION_SEPERATOR) &&
                    currentLine.substring(currentLine.length()-separator.length(),currentLine.length()).equals(separator) &&
                    !currentLine.substring(currentLine.length()-(separator.length()+1),currentLine.length()-separator.length()).equals(SECTION_SEPERATOR) ) {
                    
                    if (search){
                        for (String line : lines){

                            // TEST out the search line
    //                        String tabs = "";
    //                        for (int i = 2; i < separator.length()+1; i++) tabs = tabs + "\t";
    //                        System.out.println(tabs + "MAIN: Search> " + line);
    //                        System.out.println();

                            // search wiki links
                            searchIntLinks(article, line);

                            // search external links
                            searchExtLinks(article, line);
                        }
                    }
                    
                    article.setText(new LinkedList(lines));
                    
                    lines.clear();
                    lines.add(currentLine);
                    
                    while ((currentLine = br.readLine()) != null) {
                        
                        // remove first spaces
                        currentLine = currentLine.trim();

                        // delete to small sentences for short searching
                        if (currentLine.length() < 3) {

                            // start again
                            continue;

                        }
                
                        lines.add(currentLine);
                    }
                }
                
                // store line
                if (currentLine != null) {
                    lines.add(currentLine);
                    article.setText(new LinkedList(lines));
                }
                
            }
            
            if (!lines.isEmpty()){
                article = generateSections(article, lines, separator, search);
            }
            
            br.close();
            
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {}
        
        return article;
    }
    
    /**
     * METHOD: split give text in article sections
     * 
     * @param title as string
     * @param text as string
     * @ return sectionlist
     */
    private static WikiArticle generateSections(WikiArticle article, Queue<String> lines, String separator, Boolean search){
        WikiArticle subarticle = null;
        Queue<String> queue = new LinkedList();
        
        String currentLine;
        while ((currentLine = lines.poll()) != null){
            if( currentLine.length() > (separator.length()*2+3) &&
                    currentLine.substring(0,separator.length()).equals(separator) && 
                    !currentLine.substring((separator.length()+1),(separator.length()+1)).equals(SECTION_SEPERATOR) &&
                    currentLine.substring(currentLine.length()-separator.length(),currentLine.length()).equals(separator) &&
                    !currentLine.substring(currentLine.length()-(separator.length()+1),currentLine.length()-separator.length()).equals(SECTION_SEPERATOR) ) {
                
                String title = currentLine.substring((separator.length()+1), currentLine.length()-(separator.length()+1));
                title = replaceText(title);
                title = escapeString(title);
                subarticle = new WikiArticle(title);
                article.addSubArticle(subarticle);
                subarticle.setParent(article);
                
                queue = new LinkedList();
                subarticle.setText(queue);
                
                continue;
            }
            
            queue.add(currentLine);
            
        }
        
        String new_separator = separator + SECTION_SEPERATOR;
        
        if (search) {
        
            if (article.getSubArticles().isEmpty() && !queue.isEmpty()) {
            
                Queue<String> look = new LinkedList(queue);
                
                while (!look.isEmpty()){
                    String line = look.remove();

                    // search wiki links
                    searchIntLinks(article, line);

                    // search external links
                    searchExtLinks(article, line);
                }

            }
        
        }
        
        for (WikiArticle suba : article.getSubArticles()){
            
            int count = 0;
            for (String line : suba.getText()){
                
                if( line.length() > (new_separator.length()*2+3) &&
                    line.substring(0,new_separator.length()).equals(new_separator) && 
                    !line.substring((new_separator.length()+1),(new_separator.length()+1)).equals(SECTION_SEPERATOR) &&
                    line.substring(line.length()-new_separator.length(),line.length()).equals(new_separator) &&
                    !line.substring(line.length()-(new_separator.length()+1),line.length()-new_separator.length()).equals(SECTION_SEPERATOR) ) {
                    
                    break;
                    
                }
                
                count++;
                
                // TEST out the search line
//                String tabs = "";
//                for (int i = 2; i < separator.length()+1; i++) tabs = tabs + "\t";
//                System.out.println(tabs + suba.getName() + ": Search> " + line);
//                System.out.println();
                
                // search wiki links
                searchIntLinks(suba, line);

                // search external links
                searchExtLinks(suba, line);
                
            }
            
            Queue<String> hold = new LinkedList();
            
            for (int i = 0; i < count; i++) hold.add(suba.getText().remove());
            
            Queue<String> keep = new LinkedList(suba.getText());
            
            suba.setText(hold);
            
            generateSections(suba, keep, new_separator, search);
        }
        
        return article;
    }
    
    /**
     * METHOD: search data in dopple square brackets in text
     *
     * @param text as string
     */
    private static void searchIntLinks(WikiArticle article, String text){
                
        // TEST out the article name
//        System.out.println(article.getName());
        
        // TEST out the text
//        System.out.println("Text : " + text);
        
        // select inter wiki link via regular expression
        Pattern patter = Pattern.compile("\\[\\[(.*?)\\]\\]", Pattern.MULTILINE);
        Matcher matcher = patter.matcher(text);
        
        // add link to linklist, if it is link
        while(matcher.find()) {
            
            // select by special matcher format
            String [] temp = matcher.group(1).split("\\|");
            if(temp == null || temp.length == 0) continue;
            
            // add the link if it isn't categorie or other
            String link = temp[0];
            String name = "";
            
            // if link has special name, safe it
            if (temp.length > 1) {
                name = replaceText(temp[1]);
                name = escapeString(name);
            }
            
            if(!link.contains(":") || (link.contains(":") && link.contains("#"))) {
                    
                link = replaceText(link);
                link = escapeString(link);
                                
                // check if article links on a subarticle
                int sub = link.indexOf("#");
                if (sub >= 0){
                    
                    // add link to page
                    if (Components.getTricker(2)){
                        String l = link.substring(0, sub);
                        String s = link.substring(sub+1, link.length());
                        
                        // if no article is given, set this article
                        WikiArticle parent = article;
                        while (parent.getParent() != null) parent = parent.getParent();
                        if (l.length() == 0) l = parent.getName();
                        
                        article.addWikiSubLink(l, s, name);
                    }
                    
                    // TEST out the subcategorie
//                    System.out.println("Sublink:   " + link);
                    
                } else {
                
                    // add link to page
                    if (Components.getTricker(1)){
                        article.addWikiLink(link, name);
                    }

                    // TEST out the link
//                    System.out.println("Link:      " + link);
                
                }

            } else {
                
                if (Components.getTricker(4)){
                    
                    // read the categories
                    String[] categorie_split = link.split(":");
                    
                    String main_cat = replaceText(categorie_split[0]);
                    main_cat = escapeString(main_cat);
                    
                    String sub_cat = replaceText(categorie_split[1]);
                    sub_cat = escapeString(sub_cat);

                    // add categorie to page
                    if (categorie_split.length > 1) article.addCategorieName(main_cat, sub_cat);

                    // TEST out categories
//                    System.out.println("Category:  " + categorie_split[0] + " -> " + categorie_split[1]);

                }
                
            }
            
        }
        
    }
    
    /**
     * METHOD: search data in normal square brackets in text
     *
     * @param text as string
     */
    private static void searchExtLinks(WikiArticle article, String text){
        
        // TEST out the article name
//        System.out.println(article.getName());
        
        // TEST out the text
//        System.out.println("Text : " + text);
          
        // clean inter links by replacing the start and end sequence
        text = text.replace("[[", "");
        text = text.replace("]]", "");
        
        // select external link via regular expression
        Pattern patter = Pattern.compile("\\[(.*?)\\]", Pattern.MULTILINE);
        Matcher matcher = patter.matcher(text);
        
        // add link to linklist, if it is link
        while(matcher.find()) {
            
            // select by special matcher format
            String [] temp = matcher.group(1).split("\\|");
            if(temp == null || temp.length == 0) continue;
            
            // add the link if it isn't categorie or other
            String link = temp[0];
            String filetype = "";
            if (temp.length > 1) {
                filetype = replaceText(temp[1]);
                filetype = escapeString(filetype);
            }
            
            // if link has special description, safe it
//            String description = temp[0];
//            String specialCharakter = " ";
//            while (description.startsWith(specialCharakter)){description = description.substring(1, description.length());}
//            while (description.endsWith(specialCharakter)){description = description.substring(0, description.length()-1);}
//            int pos = description.indexOf(" ");
//            description = description.substring(pos, description.length());
//            description = replaceText(description);
//            description = escapeString(description);
            
            String linkurl = "";
            
            int pos = link.indexOf(" ");
            
            // check if a whitespace is following
            if (pos > 0){
                
                // cut the link url
                linkurl = link.substring(0, pos);
                                
            } else {
                
                // check if link has no description and is link
                if(ThreadController.isUrl(link)){
                    
                    // remember it
                    linkurl = link;
                    
                }else{
                    
                    // TEST out external links without a description
//                    System.out.println("Ext-err : " + link);
                    
                }
                
            }
                
            if (Components.getTricker(3) && ThreadController.isUrl(linkurl)) article.addExternLink(linkurl, filetype);

            // TEST out the external link
//            System.out.println("External: " + linkurl);
                        
        }
        
    }
    
    /**
     * METHOD: replace the special character in a title 
     *
     * @param text as string
     * @return replaced text
     */
    public static String replaceText(String text){
        String replaceCharakter = " ";
        String specialCharakter = "_";
        String replaced = text.replace(replaceCharakter, specialCharakter);
        while (replaced.startsWith(specialCharakter)){replaced = replaced.substring(1, replaced.length());}
        while (replaced.endsWith(specialCharakter)){replaced = replaced.substring(0, replaced.length()-1);}
        return replaced;
    }
    
    /**
     * METHOD: replace the special character in a title 
     *
     * @param text as string
     * @return replaced text
     */
    public static String unreplaceText(String text){
        String replaceCharakter = "_";
        String specialCharakter = " ";
        String replaced = text.replace(replaceCharakter, specialCharakter);
        return replaced;
    }
    
    /**
     * METHOD: replace the special character
     *
     * @param text as string
     * @return replaced text
     */
    public static String escapeString(String text){
        return StringEscapeUtils.escapeJava(text);
    }
    
    /**
     * METHOD: revert the replacement of special characters
     *
     * @param text as string
     * @return replaced text
     */
    public static String unescapeString(String text){
        return StringEscapeUtils.unescapeJava(text);
    }
    
}
