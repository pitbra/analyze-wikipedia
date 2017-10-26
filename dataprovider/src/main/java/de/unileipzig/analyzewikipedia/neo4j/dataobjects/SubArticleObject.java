package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "SubArticle")
public class SubArticleObject extends Entity implements FromLinkedEntities, ToLinkedEntities {    
    
    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name = "title")
    String title;
    
    List<LinkToReleationship> links;
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public SubArticleObject() {
        this("");
    }
    
    public SubArticleObject(String title) {
        this(title, new ArrayList<LinkToReleationship>());
    }
    
    public SubArticleObject(String title, List<LinkToReleationship> links) {
        this.title = title;
        this.links = links;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> methods" defaultstate="collapsed">
    @Override
    public String toString() {
        return "SubArticle{"
                + "id=" + getId()
                + ", title='" + title + '\''
                + ", linksize=" + links.size()
                + "}";
    }
    // </editor-fold>
    
    // <editor-fold desc=">> adders" defaultstate="collapsed">
    public void addLinkToArticle(ArticleObject article) {
        addLink(article, "");
    }
    
    public void addLinkToArticle(ArticleObject article, String title) {
        addLink(article, title);
    }
    
    public void addLinkToSubArticle(SubArticleObject subArticle) {
        addLink(subArticle, "");
    } 
    
    public void addLinkToSubArticle(SubArticleObject subArticle, String title) {
        addLink(subArticle, title);
    }
    
    public void addLinkToExtern(ExternObject extern) {
        addLink(extern, "");
    }
    
    public void addLinkToExtern(ExternObject extern, String title) {
        addLink(extern, title);
    }    
    
    public void addLinkToSubExtern(SubExternObject subExtern) {
        addLink(subExtern, "");
    }
    
    public void addLinkToSubExtern(SubExternObject subExtern, String title) {
        addLink(subExtern, title);
    }
    
    private void addLink(ToLinkedEntities entity, String title) {
        LinkToReleationship link = new LinkToReleationship();
        link.setTitle(title);
        link.setFrom(this);
        link.setTo(entity);
        links.add(link);
    }
    // </editor-fold>
    
    // <editor-fold desc=">> getters" defaultstate="collapsed">
    @Override
    public String getTitle() {
        return title;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> setters" defaultstate="collapsed">
    public void setTitle(String title) {
        this.title = title;
    }
    // </editor-fold>
    
}
