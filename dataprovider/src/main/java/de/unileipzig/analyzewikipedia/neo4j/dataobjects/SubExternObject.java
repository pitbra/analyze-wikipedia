package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubExtern")
public class SubExternObject extends Entity {

    @Property(name = "title")
    private String title;
    
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

    public SubExternObject() {
        this("");
    }

    public SubExternObject(String title) {
        this.title = title;
        this.subArticles = new ArrayList<>();
    }
    
}
