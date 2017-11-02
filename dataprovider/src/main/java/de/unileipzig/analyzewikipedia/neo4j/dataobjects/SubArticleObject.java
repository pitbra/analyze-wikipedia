package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import de.unileipzig.analyzewikipedia.neo4j.service.SubArticleServiceImpl;

@NodeEntity(label = "SubArticle")
public class SubArticleObject extends Entity implements FromLinkedEntities, ToLinkedEntities {    
    
    // <editor-fold desc=">> classvariables" defaultstate="collapsed">
    @Property(name = "title")
    String title;
    
    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    List<HasRelationship> hasRelationships;
    
    List<LinkToReleationship> links;
    // </editor-fold>
    
    // <editor-fold desc=">> constructors" defaultstate="collapsed">
    public SubArticleObject() {
        this("");
    }
    
    public SubArticleObject(String title) {
        this(title, new ArrayList<HasRelationship>(), new ArrayList<LinkToReleationship>());
    }
    
    public SubArticleObject(String title, List<HasRelationship> hasRelaionships, List<LinkToReleationship> links) {
        this.title = title;
        this.hasRelationships = hasRelaionships;
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
    
    private boolean contains(SubArticleObject sub_art) {
        for(int i = 0; i < hasRelationships.size(); ++i) {
            if(hasRelationships.get(i).getTo() == sub_art){
                return true;
            }
        }
        return false;
    }
    
    public SubArticleObject findSub(String title){
        
        if (this.title.equals(title)) return this;
        
        SubArticleServiceImpl service = new SubArticleServiceImpl();
        for(HasRelationship has : hasRelationships) {
            
            if (has.getTo().getTitle().equals(title)){
                return (SubArticleObject) has.getTo();
            }
            
            SubArticleObject found = service.find(has.getTo().getId()).findSub(title);
            if (found != null) return found;
                        
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
    // </editor-fold>
    
}
