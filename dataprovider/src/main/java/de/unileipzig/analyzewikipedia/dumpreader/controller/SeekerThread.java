package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.WikiPage;
import java.net.MalformedURLException;
import java.net.URL;

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
     * METHODE: execution of thread link seeker
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
            
        } while(ThreadController.getReaderIsAlive() || !ThreadController.docIsEmpty());
                
    }
    
    /**
     * METHODE: read the document text to page
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
                WikiPage page = new WikiPage(eElement.getElementsByTagName(Components.getTitleTag()).item(0).getTextContent());
                
                // select element by text
                String text = eElement.getElementsByTagName(Components.getTextTag()).item(0).getTextContent();
                
                // search wiki links in text
                searchIntLinks(page, text);
                
                // search external links in text
                searchExtLinks(page, text);
                
                // send neo4j DB the page with links
                ThreadController.addPage(page);
                
                // TESTout the page title and linkcount
//                System.out.println(page.getName() + " " + page.getIntLinks().size() + ":" + page.getExtLinks().size());
                
            }
        }
        
    }
    
    /**
     * METHODE: search data in dopple square brackets in text
     *
     * @param text as string
     */
    private void searchIntLinks(WikiPage page, String text){
                
        // TEST out the page name
//        System.out.println(page.getName());
        
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
            if(link.contains(":") == false) {
                    
                link = link.replace(" ", "_");
                
                // check if article links on a subarticle
                int sub = link.indexOf("#");
                if (sub >= 0){
                    
                    // add link to page
                    page.addIntSubLink(link.substring(0, sub), link.substring(sub+1, link.length()));
                    
                    // TEST out the subcategorie
//                    System.out.println("Sublink:   " + link);
                    
                } else {
                
                    // add link to page
                    page.addIntLink(link);

                    // TEST out the link
//                    System.out.println("Link:      " + link);
                
                }

            } else {
                
                // read the categories
                String[] categorie_split = link.split(":");
                
                // add categorie to page
                page.addCategory(categorie_split[0], categorie_split[1]);
                
                // TEST out categories
//                System.out.println("Category:  " + categorie_split[0] + " -> " + categorie_split[1]);
                
            }
            
        }
        
    }
    
    /**
     * METHODE: search data in normal square brackets in text
     *
     * @param text as string
     */
    private void searchExtLinks(WikiPage page, String text){
        
        // TEST out the page name
//        System.out.println(page.getName());
        
        // TEST out the text
//        System.out.println("Text : " + text);
          
        // clean inter links by replacing the start
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
                
                // TEST out external links without a description
//                System.out.println("Ext-err : " + link + " : " + isUrl(link));
                
            }
                        
            // add link to page if it is an url
            if (isUrl(linkurl)) page.addExtLink(linkurl);

            // TEST out the external link
//            System.out.println("External: " + linkurl);
                        
        }
        
    }
    
    /**
     * METHODE: check if given url has correct form of an url
     * 
     * @param url as string
     * @return is url as boolean
     */
    private boolean isUrl(String url){
        
        try {
            URL tmp = new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }

    }
    
}
