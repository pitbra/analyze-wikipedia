package de.unileipzig.analyzewikipedia.crawler.dataobjects;

import java.io.Serializable;

import java.net.URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Danilo Morgado
 */
public class CrawledElement implements Serializable {
    
    private final URL url;
    
    private String title;
    private String web_title;
    
    private Long neo4j_id;
    
    private String language;
    
    private Integer downloadcount;
    
    private final Map<Integer, Integer> REQUESTCODE = new HashMap();
    private final Map<Integer, String> REQUESTSTATUS = new HashMap();
    private final Map<Integer, Long> LASTMODIFIED = new HashMap();
    private final Map<Integer, Long> DOWNLOADDATE = new HashMap();
    
    private final Map<Integer, Long> CONTENTLENGTH = new HashMap();
    private final Map<Integer, String> CONTENTTYPE = new HashMap();
    private final Map<Integer, String> CONTENTLANGUAGE = new HashMap();
    
    private final Map<Integer, String> CONTENT = new HashMap();
    
    private final Map<Integer, List<String>> REFLISTS = new HashMap();
    
    private final Map<Integer, SectionElement> SECTIONS = new HashMap();
    
    private final Map<Integer, Map<URL, WebFile>> WEBFILES = new HashMap();
    
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
    
    public void putSection(SectionElement section){
        if (section == null) return;
        if (SECTIONS.containsKey(downloadcount)) downloadcount++;
        SECTIONS.put(downloadcount, section);
    }
    
    public void putReflist(List<String> list){
        if (list == null) return;
        if (REFLISTS.containsKey(downloadcount)) downloadcount++;
        REFLISTS.put(downloadcount, list);
    }
    
    public void putWebfiles(Map<URL, WebFile> webfiles){
        if (webfiles == null) return;
        if (WEBFILES.containsKey(downloadcount)) downloadcount++;
        WEBFILES.put(downloadcount, webfiles);
    }
        
    public Long getID()                                     { return this.neo4j_id;                                 }
    public URL getUrl()                                     { return this.url;                                      }
    public String getTitle()                                { return this.title;                                    }
    public String getWebtitle()                             { return this.web_title;                                }
    public String getLanguage()                             { return this.language;                                 }
    public Integer getDownloadCount()                       { return this.downloadcount;                            }
    public Integer getRequestCode()                         { return this.REQUESTCODE.get(downloadcount);           }
    public String getRequestStatus()                        { return this.REQUESTSTATUS.get(downloadcount);         }
    public Long getLastModified()                           { return this.LASTMODIFIED.get(downloadcount);          }
    public Long getDownloadDate()                           { return this.DOWNLOADDATE.get(downloadcount);          }
    public Long getContentLength()                          { return this.CONTENTLENGTH.get(downloadcount);         }
    public String getContentType()                          { return this.CONTENTTYPE.get(downloadcount);           }
    public String getContentLanguage()                      { return this.CONTENTLANGUAGE.get(downloadcount);       }
    public String getContent()                              { return this.CONTENT.get(downloadcount);               }
    public SectionElement getSections()                     { return this.SECTIONS.get(downloadcount);              }
    public List<String> getReflist()                        { return this.REFLISTS.get(downloadcount);              }
    public Map<URL, WebFile> getWebfiles()                  { return this.WEBFILES.get(downloadcount);              }
    
    public Integer getRequestCode(Integer key)              { return this.REQUESTCODE.get(key);                     }
    public String getRequestStatus(Integer key)             { return this.REQUESTSTATUS.get(key);                   }
    public Long getLastModified(Integer key)                { return this.LASTMODIFIED.get(key);                    }
    public Long getDownloadDate(Integer key)                { return this.DOWNLOADDATE.get(key);                    }
    public Long getContentLength(Integer key)               { return this.CONTENTLENGTH.get(key);                   }
    public String getContentType(Integer key)               { return this.CONTENTTYPE.get(key);                     }
    public String getContentLanguage(Integer key)           { return this.CONTENTLANGUAGE.get(key);                 }
    public String getContent(Integer key)                   { return this.CONTENT.get(key);                         }
    public SectionElement getSections(Integer key)          { return this.SECTIONS.get(key);                        }
    public List<String> getReflist(Integer key)             { return this.REFLISTS.get(key);                        }
    public Map<URL, WebFile> getWebfiles(Integer key)       { return this.WEBFILES.get(key);                        }
    
    public void setID(Long id)                              { this.neo4j_id = id;                                   }
    public void setTitle(String title)                      { this.title = title;                                   }
    public void setWebtitle(String title)                   { this.web_title = title;                               }
    public void setLanguage(String language)                { this.language = language;                             }
}
