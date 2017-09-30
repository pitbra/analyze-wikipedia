package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.relationships.LinkToRelationship;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Article")
public class ArticleObject extends Entity implements FromLinkedEntities, ToLinkedEntities, ToContainsEntity{

    @Property(name = "title")
    private String title;

    @Relationship(type = "ACTIVE", direction = Relationship.OUTGOING)
    ActiveNode active;

    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    List<SubArticleObject> ownSubArticles;
    
    List<HasRelationship> hasRelationships;
    
    List<LinkToReleationship> links;
        
    public ArticleObject() {
        this("");
    }
    
    public ArticleObject(String title) {
        this.title = title;
        this.active = null;
        this.ownSubArticles = new ArrayList<>();
        this.hasRelationships = new ArrayList<>();
        this.links = new ArrayList<>();
    }
    
    public void addLinkToArticle(ArticleObject article, String title) {
        addLink(article, title);
    }
    
      
    public List<SubArticleObject> getOwnSubArticles() {
        return ownSubArticles;
    }

    public void addLinkToExtern(ExternObject extern, String title) {
        addLink(extern, title);
    }

    public void addLinkToSubExtern(SubExternObject subExtern, String title) {
        addLink(subExtern, title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public ArticleObject(String title, List<LinkToReleationship> links, ActiveNode active, List<SubArticleObject> ownSubArticles) {
        this.title = title;
        this.active = active;
        this.ownSubArticles = ownSubArticles;
        this.links = links;
    }

    public void addLinkToSubArticle(SubArticleObject subArticle, String title) {
        addLink(subArticle, title);
    }
    
    public void addSubArticle(SubArticleObject subArticle) {
        HasRelationship has = new HasRelationship();
        has.setTitle("");
        has.setFrom(this);
        has.setTo(subArticle);
        hasRelationships.add(has);
    }
   
    @Override
    public String toString() {
        return "Article{"
                + "id=" + getId()
                + ", title='" + title + '\''
                + ", active=" + (active==null)
                + ", ownsubarticles=" + ownSubArticles.size()
                + "}";
    }

    public boolean isActive() {
        return this.active != null;
    }

    public void SetActive(ActiveNode active) {
        this.active = active;
    }    

    private void addLink(ToLinkedEntities entity, String title) {
        LinkToReleationship link = new LinkToReleationship();
        link.setTitle(title);
        link.setFrom(this);
        link.setTo(entity);
        links.add(link);
    }
    
}
