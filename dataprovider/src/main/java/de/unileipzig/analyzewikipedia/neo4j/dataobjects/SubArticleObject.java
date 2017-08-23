package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubArticle")
public class SubArticleObject extends Entity{    
    @Property(name = "title")
    String title;
    
    @Relationship(type = "has", direction = "INCOMING")
    ArticleObject parentArticle;

    public SubArticleObject() {
        this.title = "";
        this.parentArticle = null;
    }

    public SubArticleObject(String title, ArticleObject parentArticle) {
        this.title = title;
        this.parentArticle = parentArticle;
    }
    
    @Override
    public String toString() {
        return "SubArticle{" +
                "id=" + getId() +
                ", title='" + title+ '\'' +
                ", parentArticle='" + parentArticle.getTitle() + '\'' +
                '}';
    }
    
    
}
