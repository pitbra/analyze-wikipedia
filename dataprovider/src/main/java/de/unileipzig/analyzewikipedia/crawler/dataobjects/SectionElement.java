package de.unileipzig.analyzewikipedia.crawler.dataobjects;

import java.net.URL;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Danilo Morgado
 */
public class SectionElement {
    
    private final String TITLE;
    private final List<String> TEXT = new LinkedList();
    private String normalized;
    private String highlighted;
    private final List<WebFile> REFERENCES = new LinkedList();
    private final List<SectionElement> SECTIONS = new LinkedList();
    
    public SectionElement(String title){
        this.TITLE = title;
        this.normalized = "";
        this.highlighted = "";
    }
    
    public String outText() {
        String text = "";
        for (String str : TEXT){
            text = text + " " + str;
        }
        return text;
    }
    
    public void addText(String text)                    {   this.TEXT.add(text);                    }
    public void addReference(URL ref)                   {   this.REFERENCES.add(new WebFile(ref));  }
    public void addSection(SectionElement sec)          {   this.SECTIONS.add(sec);                 }
    
    public String getTitle()                            {   return this.TITLE;                      }
    public List<String> getText()                       {   return this.TEXT;                       }
    public String getNormalized()                       {   return this.normalized;                 }
    public String getHighlighted()                      {   return this.highlighted;               }
    public List<WebFile> getReferences()                {   return this.REFERENCES;                 }
    public List<SectionElement> getSections()           {   return this.SECTIONS;                   }
    
    public void setText(List<String> list)              {   this.TEXT.clear();
                                                            this.TEXT.addAll(list);                 }
    public void setNormalized(String normalized)        {   this.normalized = normalized;           }
    public void setHighlighted(String highlighted)      {   this.highlighted = highlighted;         }
    public void setReferences(List<WebFile> list)       {   this.REFERENCES.clear();
                                                            this.REFERENCES.addAll(list);           }
    public void setSections(List<SectionElement> list)  {   this.SECTIONS.clear();
                                                            this.SECTIONS.addAll(list);             }
    
}
