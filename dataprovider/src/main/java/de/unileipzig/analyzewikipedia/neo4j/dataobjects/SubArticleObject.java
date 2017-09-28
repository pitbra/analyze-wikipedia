package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubArticle")
public class SubArticleObject extends Entity implements FromLinkedEntities, ToLinkedEntities{    
    @Property(name = "title")
    String title;
    
    List<LinkToReleationship> links;
    
    public SubArticleObject() {
        this("");
    }
    
    public SubArticleObject(String title) {
        this.title = title;
        this.links = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return "SubArticle{" +
                "title='" + title+ '\'' +
                '}';
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void addLinkToExtern(ExternObject extern, String title) {
        addLink(extern, title);
    }    
    
    public void addLinkToSubExtern(SubExternObject subExtern, String title) {
        addLink(subExtern, title);
    }    
    
    public void addLinkToArticle(ArticleObject article, String title) {
        addLink(article, title);
    }    
    
    public void addLinkToSubArticle(SubArticleObject subArticle, String title) {
        addLink(subArticle, title);
    }    

    @Override
    public String getTitle() {
        return title;
    }

    private void addLink(ToLinkedEntities entity, String title) {
        LinkToReleationship link = new LinkToReleationship();
        link.setTitle("");
        link.setFrom(this);
        link.setTo(entity);
        links.add(link);
    }
    
}
