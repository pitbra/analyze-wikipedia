package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Article")
public class ArticleObject extends Entity implements FromLinkedEntities, ToLinkedEntities, ToContainsEntity{
    
    // <editor-fold desc=">> classvariable" defaultstate="collapsed">
    @Property(name = "title")
    private String title;

    @Relationship(type = "ACTIVE", direction = Relationship.OUTGOING)
    ActiveNode active;

    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    List<HasRelationship> hasRelationships;
    
    List<LinkToReleationship> links;
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public ArticleObject() {
        this("");
    }
    
    public ArticleObject(String title) {
        this(title, new ArrayList<HasRelationship>(), new ArrayList<LinkToReleationship>());
    }
    
    public ArticleObject(String title, List<HasRelationship> hasRelaionships, List<LinkToReleationship> links) {
        this.title = title;
        this.active = null;
        this.hasRelationships = hasRelaionships;
        this.links = links;
    }
    // </editor-fold>
    
    // <editor-fold desc=">> methods" defaultstate="collapsed">
    @Override
    public String toString() {
        return "Article{"
                + "id=" + getId()
                + ", title='" + title + '\''
                + ", active=" + isActive()
                + ", subarticlesize=" + hasRelationships.size()
                + ", linksize=" + links.size()
                + "}";
    }
    
    public boolean isActive() {
        return this.active != null;
    }
    
    private boolean contains(SubArticleObject sub_art) {
        for(int i = 0; i < hasRelationships.size(); ++i) {
            if(hasRelationships.get(i).getTo() == sub_art){
                return true;
            }
        }
        return false;
    }
    
    public SubArticleObject findSub(String title){
        
        for(HasRelationship has : hasRelationships) {
            
            return ((SubArticleObject) has.getTo()).findSub(title);
            
        }
        
        return null;
    }
    
    public SubArticleObject findHas(String title){
        
        for(HasRelationship has : hasRelationships) {
            
            if (((SubArticleObject) has.getTo()).getTitle().equals(title)) return (SubArticleObject) has.getTo();
            
        }
        
        return null;
        
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
    
    public void addSubArticle(SubArticleObject subArtcile) {
        addSubArticle(subArtcile, "");
    }
    
    public void addSubArticle(SubArticleObject subArticle, String title) {
        if (contains(subArticle)) return;
        HasRelationship has = new HasRelationship();
        has.setTitle(title);
        has.setFrom(this);
        has.setTo(subArticle);
        hasRelationships.add(has);
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
    
    public void setActive(ActiveNode active) {
        this.active = active;
    }
    // </editor-fold>
    
}
