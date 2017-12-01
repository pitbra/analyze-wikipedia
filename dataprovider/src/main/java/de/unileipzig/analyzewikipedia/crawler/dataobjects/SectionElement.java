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
    private final List<String> NORMALIZED = new LinkedList();
    private final List<WebFile> REFERENCES = new LinkedList();
    private final List<SectionElement> SECTIONS = new LinkedList();
    
    public SectionElement(String title){
        this.TITLE = title;
    }
    
    public void addText(String text)                    {   this.TEXT.add(text);                    }
    public void addNormalized(String text)              {   this.NORMALIZED.add(text);              }
    public void addReference(URL ref)                   {   this.REFERENCES.add(new WebFile(ref));  }
    public void addSection(SectionElement sec)          {   this.SECTIONS.add(sec);                 }
    
    public String getTitle()                            {   return this.TITLE;                      }
    public List<String> getText()                       {   return this.TEXT;                       }
    public List<String> getNormalized()                       {   return this.NORMALIZED;           }
    public List<WebFile> getReferences()                {   return this.REFERENCES;                 }
    public List<SectionElement> getSections()          {   return this.SECTIONS;                   }
    
    public void setText(List<String> list)              {   this.TEXT.clear();
                                                            this.TEXT.addAll(list);                 }
    public void setNormalized(List<String> list)        {   this.NORMALIZED.clear();
                                                            this.NORMALIZED.addAll(list);           }
    public void setReferences(List<WebFile> list)       {   this.REFERENCES.clear();
                                                            this.REFERENCES.addAll(list);           }
    public void setSections(List<SectionElement> list)  {   this.SECTIONS.clear();
                                                            this.SECTIONS.addAll(list);             }
    
}
