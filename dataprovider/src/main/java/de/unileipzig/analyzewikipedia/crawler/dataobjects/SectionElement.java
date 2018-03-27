package de.unileipzig.analyzewikipedia.crawler.dataobjects;

import java.io.Serializable;

import java.net.URL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Danilo Morgado
 */
public class SectionElement implements Serializable {
    
    private final String TITLE;
    private final List<String> REFTEXT = new LinkedList();
    private String text;
    private String normalized;
    private final List<URL> REFERENCES = new LinkedList();
    private final List<SectionElement> SECTIONS = new LinkedList();
    private final Map<URL, List<String>> OVERLAPPHRASES = new HashMap();
    
    public SectionElement(String title){
        this.TITLE = title;
        this.normalized = "";
    }
        
    public void addReftext(String text)                 {   this.REFTEXT.add(text);                     }
    public void addReference(URL ref)                   {   this.REFERENCES.add(ref);                   }
    public void addSection(SectionElement sec)          {   this.SECTIONS.add(sec);                     }
    public void addOverlapphrases(  URL u,  
                                    List<String> l)     {   this.OVERLAPPHRASES.put(u, l);              }
    
    public String getTitle()                            {   return this.TITLE;                      }
    public String getText()                             {   return this.text;                       }
    public List<String> getReftext()                    {   return this.REFTEXT;                    }
    public String getNormalized()                       {   return this.normalized;                 }
    public List<URL> getReferences()                    {   return this.REFERENCES;                 }
    public List<SectionElement> getSections()           {   return this.SECTIONS;                   }
    public Map< URL, List<String>> getOverlapphrases()  {   return this.OVERLAPPHRASES;             }
    
    public void setReftext(List<String> list)           {   this.REFTEXT.clear();
                                                            this.REFTEXT.addAll(list);              }
    public void setText(String text)                    {   this.text = text;                       }
    public void setNormalized(String normalized)        {   this.normalized = normalized;           }
    public void setReferences(List<URL> list)           {   this.REFERENCES.clear();
                                                            this.REFERENCES.addAll(list);           }
    public void setSections(List<SectionElement> list)  {   this.SECTIONS.clear();
                                                            this.SECTIONS.addAll(list);             }
    
}
