package de.unileipzig.analyzewikipedia.crawler.dataobjects;

import java.net.URL;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Danilo Morgado
 */
public class CrawledElement {
    
    private final URL url;
    
    private Long neo4j_id;
    
    private Integer downloadcount;
    
    private final Map<Integer, Integer> REQUESTCODE = new HashMap();
    private final Map<Integer, String> REQUESTSTATUS = new HashMap();
    private final Map<Integer, Long> LASTMODIFIED = new HashMap();
    private final Map<Integer, Long> DOWNLOADDATE = new HashMap();
    
    private final Map<Integer, Long> CONTENTLENGTH = new HashMap();
    private final Map<Integer, String> CONTENTTYPE = new HashMap();
    private final Map<Integer, String> CONTENTLANGUAGE = new HashMap();
    
    private final Map<Integer, String> CONTENT = new HashMap();;
    
    public CrawledElement(URL url){
        this.url = url;
        this.downloadcount = 1;
    }
    
    public void putRequestCode(Integer code){
        if (code == null) return;
        if (REQUESTCODE.containsKey(downloadcount)) downloadcount++;
        REQUESTCODE.put(downloadcount, code);
    }
    
    public void putRequestStatus(String status){
        if (status == null) return;
        if (REQUESTSTATUS.containsKey(downloadcount)) downloadcount++;
        REQUESTSTATUS.put(downloadcount, status);
    }
    
    public void putLastModified(Long time){
        if (time == null) return;
        if (LASTMODIFIED.containsKey(downloadcount)) downloadcount++;
        LASTMODIFIED.put(downloadcount, time);
    }
    
    public void putDownloadDate(Long time){
        if (time == null) return;
        if (DOWNLOADDATE.containsKey(downloadcount)) downloadcount++;
        DOWNLOADDATE.put(downloadcount, time);
    }
    
    public void putContentLength(Long length){
        if (length == null) return;
        if (CONTENTLENGTH.containsKey(downloadcount)) downloadcount++;
        CONTENTLENGTH.put(downloadcount, length);
    }
    
    public void putContentType(String type){
        if (type == null) return;
        if (CONTENTTYPE.containsKey(downloadcount)) downloadcount++;
        CONTENTTYPE.put(downloadcount, type);
    }
    
    public void putContentLanguage(String lang){
        if (lang == null) return;
        if (CONTENTLANGUAGE.containsKey(downloadcount)) downloadcount++;
        CONTENTLANGUAGE.put(downloadcount, lang);
    }
    
    public void putContent(String content){
        if (content == null) return;
        if (CONTENT.containsKey(downloadcount)) downloadcount++;
        CONTENT.put(downloadcount, content);
    }
    
    public Long getID()                              { return this.neo4j_id;                                 }
    public URL getUrl()                              { return this.url;                                      }
    public Integer getDownloadCount()                { return this.downloadcount;                            }
    public Integer getRequestCode()                  { return this.REQUESTCODE.get(downloadcount);           }
    public String getRequestStatus()                 { return this.REQUESTSTATUS.get(downloadcount);         }
    public Long getLastModified()                    { return this.LASTMODIFIED.get(downloadcount);          }
    public Long getDownloadDate()                    { return this.DOWNLOADDATE.get(downloadcount);          }
    public Long getContentLength()                   { return this.CONTENTLENGTH.get(downloadcount);         }
    public String getContentType()                   { return this.CONTENTTYPE.get(downloadcount);           }
    public String getContentLanguage()               { return this.CONTENTLANGUAGE.get(downloadcount);       }
    public String getContent()                       { return this.CONTENT.get(downloadcount);               }
    
    public Integer getRequestCode(Integer key)       { return this.REQUESTCODE.get(key);                     }
    public String getRequestStatus(Integer key)      { return this.REQUESTSTATUS.get(key);                   }
    public Long getLastModified(Integer key)         { return this.LASTMODIFIED.get(key);                    }
    public Long getDownloadDate(Integer key)         { return this.DOWNLOADDATE.get(key);                    }
    public Long getContentLength(Integer key)        { return this.CONTENTLENGTH.get(key);                   }
    public String getContentType(Integer key)        { return this.CONTENTTYPE.get(key);                     }
    public String getContentLanguage(Integer key)    { return this.CONTENTLANGUAGE.get(key);                 }
    public String getContent(Integer key)            { return this.CONTENT.get(key);                         }
    
    public void setID(Long id)                       { this.neo4j_id = id;                                   }
}
