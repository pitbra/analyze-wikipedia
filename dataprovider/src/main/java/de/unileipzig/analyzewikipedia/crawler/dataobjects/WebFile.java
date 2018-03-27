package de.unileipzig.analyzewikipedia.crawler.dataobjects;

import java.io.Serializable;

import java.net.URL;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Danilo Morgado
 */
public class WebFile implements Serializable {
    
    private final URL URL;
    private Integer status;
    private String language;
    private String origin;
    private String cleaned;
    private String normalized;
    private final Map<SectionElement, Measurement> MEASUREMENT_TEXT = new HashMap();
    private final Map<SectionElement, Measurement> MEASUREMENT_NORM = new HashMap();
    
    public WebFile(URL url){
        this.URL = url;
        this.status = 900;
        this.language = "##";
    }
    
    public void addMeasurmantText(SectionElement s,  Measurement m) { this.MEASUREMENT_TEXT.put(s, m);  }
    public void addMeasurmantNorm(SectionElement s,  Measurement m) { this.MEASUREMENT_NORM.put(s, m);  }
    
    public URL getUrl()                                             { return this.URL;                  }
    public Integer getStatus()                                      { return this.status;               }
    public String getLanguage()                                     { return this.language;             }
    public String getOrigin()                                       { return this.origin;               }
    public String getCleaned()                                      { return this.cleaned;              }
    public String getNormalized()                                   { return this.normalized;           }
    public Map<SectionElement,  Measurement> getMeasurementText()   { return this.MEASUREMENT_TEXT;     }
    public Map<SectionElement,  Measurement> getMeasurementNorm()   { return this.MEASUREMENT_NORM;     }
    
    public void setStatus(Integer status)                           { this.status = status;             }
    public void setLanguage(String language)                        { this.language = language;         }
    public void setOrigin(String text)                              { this.origin = text;               }
    public void setCleaned(String text)                             { this.cleaned = text;              }
    public void setNormalized(String text)                          { this.normalized = text;           }
    
}
