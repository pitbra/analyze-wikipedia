package de.unileipzig.analyzewikipedia.crawler.controller;

import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.nio.charset.StandardCharsets;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpStatus;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Danilo Morgado
 */
public class Fetcher {
    
    private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    
    /**
     * METHOD: isnatll trusted certificates
     */
    private static void trustAllCertificates(){
        
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }
        };

        // try installing trust manager
        try {

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                            return true;
                    }

            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (NoSuchAlgorithmException | KeyManagementException ex) {}
        
    }
    
    
    /**
     * METHOD: download HTML site and return it as string value
     * 
     * @param website as string
     * @return html site as string
     */
    protected static Object[] download_html(URL website) {
        
        Integer responsecode = -1;
        StringBuilder response = new StringBuilder();
        BufferedReader in;
        
        try {
            trustAllCertificates();
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestProperty("User-Agent", USERAGENT);
            connection.setConnectTimeout(2000);
            
            try {
                connection.connect();
                
                responsecode = connection.getResponseCode();
            
                if (responsecode >= HttpStatus.SC_BAD_REQUEST) {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                }

                String inputLine;

                while ((inputLine = in.readLine()) != null) response.append(inputLine);

                in.close();

                connection.disconnect();
            
            } catch (SocketTimeoutException | UnknownHostException e) {
            }
             
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
                
        return new Object[]{responsecode, response.toString()};
        
    }
    
    /**
     * METHOD: download HTML site and return it as string value
     * 
     * @param website as string
     * @return html site as string
     */
    protected static CrawledElement downloadAsCrawledElement(URL website) {
        
        StringBuilder response = new StringBuilder();
        BufferedReader in;
        CrawledElement crawl = null;
        
        try {
            trustAllCertificates();
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestProperty("User-Agent", USERAGENT);
            connection.setConnectTimeout(2000);
            
            try {
                connection.connect();

                crawl = checkConnection(website, connection);

                if (connection.getResponseCode() >= HttpStatus.SC_BAD_REQUEST) {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                }

                String inputLine;

                while ((inputLine = in.readLine()) != null) response.append(inputLine);

                in.close();

                crawl.putContent(response.toString());

                connection.disconnect();
                
            } catch (SocketTimeoutException | UnknownHostException e) {
            }
                
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
                
        return crawl;
        
    }
    
    /**
     * METHOD: download HTML site and return it as string value
     * 
     * @param website as string
     * @param search the including xml tag
     * @return urls as list
     */
    protected static List<String> download_xmllist(URL website, String search) {
        
        StringBuilder response = new StringBuilder();
        BufferedReader in;
        String text = "";
        
        try {
            trustAllCertificates();
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestProperty("User-Agent", USERAGENT);
            connection.setConnectTimeout(2000);
            
            try {
                connection.connect();

                if (connection.getResponseCode() >= HttpStatus.SC_BAD_REQUEST) {
                    in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                } else {
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                }

                String inputLine;

                while ((inputLine = in.readLine()) != null) response.append(inputLine);

                in.close();

                text = response.toString();

                connection.disconnect();
                
            } catch (SocketTimeoutException | UnknownHostException e) {
            }
            
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
        
        Document doc = null;

        // try to load the text
        try {
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(text);
            InputSource is = new InputSource(sr);
            doc = builder.parse(is);
            
            doc.getDocumentElement().normalize();

        } catch (IOException | ParserConfigurationException | DOMException | SAXException e) {
            //
        }
        
        List<String> list = new LinkedList();
        
        if (doc != null){
            
            NodeList nList = doc.getElementsByTagName(search);

            // iterate page by page
            for (int i=0; i<nList.getLength(); i++) {

                // select the page
                Node nNode = nList.item(i);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    for (int j = 0; j < eElement.getElementsByTagName("el").getLength(); j++){
                    
                        // select list element
                        String title = eElement.getElementsByTagName("el").item(j).getTextContent();
                        
                        list.add(title);
                        
                    }
                    
                }
                
            }
            
        }
        
        return list;
        
    }
    
    /**
     * METHOD: check connection and generate crawl element
     * 
     * @param article as object
     */
    private static CrawledElement checkConnection(URL website, HttpURLConnection connection) {
        
        CrawledElement crawl = new CrawledElement(website);
        
        try {
            crawl.putRequestCode(connection.getResponseCode());
        } catch (IOException ex) {}
        
        crawl.putLastModified(connection.getLastModified());
        crawl.putContentLength(connection.getContentLengthLong());
        crawl.putDownloadDate(connection.getDate());
        
        Map<String, List<String>> map = connection.getHeaderFields();
        crawl.putRequestStatus(map.get(null).get(0));
        if (map.containsKey("Content-Type")){
            crawl.putContentType(map.get("Content-Type").get(0));
        }
        if (map.containsKey("Content-language")){
            crawl.putContentLanguage(map.get("Content-language").get(0));
        }
        
        return crawl;
        
    }
    
    public static Boolean checkStatus(Integer code){
        return code == HttpStatus.SC_OK;
    }
    
}
