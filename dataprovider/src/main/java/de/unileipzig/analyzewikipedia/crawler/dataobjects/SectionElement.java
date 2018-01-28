package de.unileipzig.analyzewikipedia.crawler.dataobjects;

import java.net.URL;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Danilo Morgado
 */
public class SectionElement {
    
    private final String TITLE;
    private final List<String> REFTEXT = new LinkedList();
    private String text;
    private String normalized;
    private String highlighted;
    private final List<WebFile> REFERENCES = new LinkedList();
    private final List<SectionElement> SECTIONS = new LinkedList();
    
    public SectionElement(String title){
        this.TITLE = title;
        this.normalized = "";
        this.highlighted = "";
    }
        
    public void addReftext(String text)                 {   this.REFTEXT.add(text);                 }
    public void addReference(URL ref)                   {   this.REFERENCES.add(new WebFile(ref));  }
    public void addSection(SectionElement sec)          {   this.SECTIONS.add(sec);                 }
    
    public String getTitle()                            {   return this.TITLE;                      }
    public String getText()                             {   return this.text;                       }
    public List<String> getReftext()                    {   return this.REFTEXT;                    }
    public String getNormalized()                       {   return this.normalized;                 }
    public String getHighlighted()                      {   return this.highlighted;                }
    public List<WebFile> getReferences()                {   return this.REFERENCES;                 }
    public List<SectionElement> getSections()           {   return this.SECTIONS;                   }
    
    public void setReftext(List<String> list)           {   this.REFTEXT.clear();
                                                            this.REFTEXT.addAll(list);              }
    public void setText(String text)                    {   this.text = text;                       }
    public void setNormalized(String normalized)        {   this.normalized = normalized;           }
    public void setHighlighted(String highlighted)      {   this.highlighted = highlighted;         }
    public void setReferences(List<WebFile> list)       {   this.REFERENCES.clear();
                                                            this.REFERENCES.addAll(list);           }
    public void setSections(List<SectionElement> list)  {   this.SECTIONS.clear();
                                                            this.SECTIONS.addAll(list);             }
    
}
