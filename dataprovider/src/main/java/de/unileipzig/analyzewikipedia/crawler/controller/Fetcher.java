package de.unileipzig.analyzewikipedia.crawler.controller;

import de.unileipzig.analyzewikipedia.crawler.dataobjects.CrawledElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author Danilo Morgado
 */
public class Fetcher {
    
    /**
     * download HTML site and return it as string value
     * 
     * @param website as string
     * @return html site as string
     */
    protected static CrawledElement download_html(URL website) {
        
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

        StringBuilder response = new StringBuilder();
        BufferedReader in;
        CrawledElement crawl = null;
        
        try {
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.setConnectTimeout(5 * 1000);
            connection.connect();
            
            crawl = checkConnection(website, connection);
            
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) response.append(inputLine);
            
            in.close();
            
            crawl.putContent(response.toString());
            
            connection.disconnect();
            
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
                
        return crawl;
        
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
    
}
