package de.unileipzig.analyzewikipedia.crawler.dataobjects;

import java.net.URL;

/**
 * @author Danilo Morgado
 */
public class WebFile {
    
    private final URL URL;
    private Integer status;
    private String language;
    private String origin;
    private String finale;
    
    public WebFile(URL url){
        this.URL = url;
    }
    
    public URL getUrl()                          {   return this.URL;                    }
    public Integer getStatus()                   {   return this.status;                 }
    public String getLanguage()                  {   return this.language;               }
    public String getOriginText()                {   return this.origin;                 }
    public String getFinalText()                 {   return this.finale;                 }
    
    public void setStatus(Integer status)        {   this.status = status;               }
    public void setLanguage(String language)     {   this.language = language;           }
    public void setOriginText(String text)       {   this.origin = text;                 }
    public void setFinalText(String text)        {   this.finale = text;                 }
    
}
