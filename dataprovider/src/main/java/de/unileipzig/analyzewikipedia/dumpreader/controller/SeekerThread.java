package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiArticle;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
        
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Danilo Morgado
 * 
 * class to use as thread to do diffrent jobs
 */
public class SeekerThread implements Runnable {
       
    /**
     * KONSTRUCTOR: default
     * 
     */
    public SeekerThread(){
        
    }
    
    /**
     * METHOD: execution of thread link seeker
     * 
     */
    @Override
    public void run() {
        
        Document doc;
        
        do{
            
            doc = ThreadController.removeDocument();

            if (doc != null){
                
                readPage(doc);

            }
            
        } while(ThreadController.getReaderIsAlive() || (!ThreadController.getReaderIsAlive() && !ThreadController.docIsEmpty()) );
        
    }
    
    /**
     * METHOD: read the document text to page
     * 
     * @param doc as document
     */
    private void readPage(Document doc){
        
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
                WikiPage page = new WikiPage(replaceText(title));
                
                // select element by text
                String text = eElement.getElementsByTagName(Components.getTextTag()).item(0).getTextContent();
                
                // seperate text in articlesections
                generateSectionlist(page.getName(), text).forEach((section) -> {
                    // create new article and add it to page
                    WikiArticle article = new WikiArticle(section[0]);
                    page.addArticle(article);
                    
                    // search wiki links in text
                    searchIntLinks(article, section[1]);

                    // search external links in text
                    searchExtLinks(article, section[1]);
                });
                
                // safe page in store
                ThreadController.addPage(page);
                
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
     * @ return sectionlist
     */
    private List<String[]> generateSectionlist(String title, String text){
        
        Queue<String[]> sectionlist = new LinkedList();
        
        BufferedReader br;
        
        try {
        
            br = new BufferedReader(new StringReader(text));

            String currentLine;
            String lineStore = "";
            String currentArticlename = title;

            while ((currentLine = br.readLine()) != null) {
                
                // delete to small sentences for short searching
                if (currentLine.length() < 3) {
                    
                    // start again
                    continue;
                    
                }
                
                // check start of article
                if( currentLine.length() > 5 &&
                    currentLine.substring(0,2).contains("==") && 
                    !currentLine.substring(3,3).equals("=") &&
                    currentLine.substring(currentLine.length()-2,currentLine.length()).contains("==") &&
                    !currentLine.substring(currentLine.length()-3,currentLine.length()-2).equals("=") ) {
                    
                    // add new article section and clear linestore
                    sectionlist.add(new String[]{currentArticlename, lineStore});
                    lineStore = "";
                    
                    // save new artcile name
                    currentArticlename = currentLine.substring(3, currentLine.length()-3);
                    continue;
                            
                }
                
                // store line
                lineStore = lineStore + currentLine + "\n";

            }
            
            // add last lines to last article
            sectionlist.add(new String[]{currentArticlename, lineStore});
            
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {}
        
        return new LinkedList(sectionlist);
    }
    
    /**
     * METHOD: search data in dopple square brackets in text
     *
     * @param text as string
     */
    private void searchIntLinks(WikiArticle article, String text){
                
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
            String name = null;
            
            // if link has special name, safe it
            if (temp.length > 1) name = temp[1];
            
            if(link.contains(":") == false) {
                    
                link = replaceText(link);
                
                // check if article links on a subarticle
                int sub = link.indexOf("#");
                if (sub >= 0){
                    
                    // add link to page
                    if (Components.getTricker(2)){
                        String l = link.substring(0, sub);
                        String n = link.substring(sub+1, link.length());
                        try { l = URLEncoder.encode(l, "UTF-8"); } catch (UnsupportedEncodingException ex){}
                        try { n = URLEncoder.encode(n, "UTF-8"); } catch (UnsupportedEncodingException ex){}
                        article.addWikiSubLink(l, n);
                    }
                    
                    // TEST out the subcategorie
//                    System.out.println("Sublink:   " + link);
                    
                } else {
                
                    // add link to page
                    if (Components.getTricker(1)){
                        try { link = URLEncoder.encode(link, "UTF-8"); } catch (UnsupportedEncodingException ex){}
                        article.addWikiLink(link, name);
                    }

                    // TEST out the link
//                    System.out.println("Link:      " + link);
                
                }

            } else {
                
                if (Components.getTricker(4)){
                    
                    // read the categories
                    String[] categorie_split = link.split(":");

                    // add categorie to page
                    if (categorie_split.length > 1) article.addCategorieName(replaceText(categorie_split[0]), replaceText(categorie_split[1]));

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
    private void searchExtLinks(WikiArticle article, String text){
        
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
            
            int pos = link.indexOf(" ");
            String linkurl = "";
            
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
                    
            // add link to page if it is an url
            if (Components.getTricker(3) && ThreadController.isUrl(linkurl)) article.addExternLink(linkurl);

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
        String replaced = text.replace(" ", "_");
        return replaced;
    }
    
}
