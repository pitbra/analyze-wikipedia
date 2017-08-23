/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Extern")
public class ExternObject extends Entity {

    @Property(name = "title")
    private String title;

    @Relationship(type = "HAS", direction = Relationship.OUTGOING)
    private List<SubExternObject> subExterns;
    
    @Relationship(type = "LINK_TO", direction = Relationship.INCOMING)
    List<ArticleObject> articles;
    
    public void addLinkFromArticle(ArticleObject article) {
        articles.add(article);
    }

    public List<ArticleObject> getArticle() {
        return articles;
    }

    public void addSubExtern(SubExternObject subExtern) {
        subExterns.add(subExtern);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ExternObject() {
        this.title = "";
        this.subExterns = new ArrayList<>();
    }

    public ExternObject(String title, List<SubExternObject> subExterns) {
        this.title = title;
        this.subExterns = subExterns;
    }

}
