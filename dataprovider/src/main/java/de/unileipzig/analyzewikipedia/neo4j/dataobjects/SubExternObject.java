package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubExtern")
public class SubExternObject extends Entity {

    @Property(name = "title")
    private String title;
    
    @Relationship(type = "HAS", direction = Relationship.INCOMING)
    ExternObject parentExtern;
    
    @Relationship(type = "LINK_TO", direction = Relationship.INCOMING)
    List<ArticleObject> articles;
    
    @Relationship(type = "LINK_TO", direction = Relationship.INCOMING)
    List<SubArticleObject> subArticles;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExternObject getParentExtern() {
        return parentExtern;
    }

    public void setParentExtern(ExternObject parentExtern) {
        this.parentExtern = parentExtern;
    }
    
    public SubExternObject() {
    }

    public SubExternObject(String title, ExternObject parentExtern) {
        this.title = title;
        this.parentExtern = parentExtern;
    }
    
}
