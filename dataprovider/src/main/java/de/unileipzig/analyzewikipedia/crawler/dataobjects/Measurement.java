package de.unileipzig.analyzewikipedia.crawler.dataobjects;

/**
 * @author Danilo Morgado
 */
public class Measurement {
    
    private double ngramDistance;
    private double ngramFrequenze;
    private double jarowinklerDistance;
    private double levensteinDistance;
    private String wordOverlapText;
    private int longestWordSequenze;
    
    public Measurement(){
    }
    
    public double getNgramDistance()                                { return this.ngramDistance;                        }
    public double getNgramFrequenze()                               { return this.ngramFrequenze;                       }
    public double getJarowinklerDistance()                          { return this.jarowinklerDistance;                  }
    public double getLevensteinDistance()                           { return this.levensteinDistance;                   }
    public String getWordoverlaptext()                              { return this.wordOverlapText;                      }
    public int getLongestWordSequenze()                             { return this.longestWordSequenze;                  }
    
    public void setNgramDistance(double ngramDistance)              { this.ngramDistance = ngramDistance;               }
    public void setNgramFrequenze(double ngramFrequenze)            { this.ngramFrequenze = ngramFrequenze;             }
    public void setJarowinklerDistance(double jarowinklerDistance)  { this.jarowinklerDistance = jarowinklerDistance;   }
    public void setLevensteinDistance(double levensteinDistance)    { this.levensteinDistance = levensteinDistance;     }
    public void setWordoverlaptext(String text)                     { this.wordOverlapText = text;                      }
    public void setLongestWordSequenze(int num)                     { this.longestWordSequenze = num;                   }
    
}
