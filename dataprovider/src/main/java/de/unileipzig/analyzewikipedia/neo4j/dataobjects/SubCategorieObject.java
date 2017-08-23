package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubCategory")
public class SubCategorieObject extends Entity {
    @Property(name = "title")
    private String title;
    
    @Relationship(type = "HAS", direction = Relationship.INCOMING)
    CategorieObject parentCategorie;

    public SubCategorieObject() {
        this.title = "";
        this.parentCategorie = null;
    }

    public SubCategorieObject(String title, CategorieObject parentCategorie) {
        this.title = title;
        this.parentCategorie = parentCategorie;
    }

    public CategorieObject getParentCategorie() {
        return parentCategorie;
    }

    public void setParentCategorie(CategorieObject parentCategorie) {
        this.parentCategorie = parentCategorie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
