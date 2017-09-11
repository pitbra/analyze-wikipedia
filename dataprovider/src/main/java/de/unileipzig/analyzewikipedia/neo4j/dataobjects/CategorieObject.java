/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;
import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.objects.DataPropertyDescriptor;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

/**
 *
 * @author Pit.Braunsdorf
 */
@NodeEntity(label="Category")
public class CategorieObject extends Entity {

    @Property(name="title")
    private String title;

    @Relationship(type="HAS")
    List<SubCategorieObject> subArticles;
    
    public CategorieObject() {
        this.title="";
        this.subArticles = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Relationship(type = "PAGE_OF", direction = Relationship.INCOMING)
    PageObject page;
    
    @Override
    public String getTitle() {
        return title;
    }

    public CategorieObject(String title, List<SubCategorieObject> subArticles) {
        this.title = title;
        this.subArticles = subArticles;
    }
    
    public void addSubArticle(SubCategorieObject subArticle){
        subArticles.add(subArticle);
    }
    
    @Override
    public String toString() {
        return "Article{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", subarticles=" + subArticles.size() +
                "}";
    }
    
    @Override
    public void setPage(PageObject page) {
        this.page = page;
    }
    
    @Override
    public PageObject getPage() {
        return this.page;
    }
}
