package de.unileipzig.analyzewikipedia.neo4j.dataobjects;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "SubCategory")
public class SubCategorieObject extends Entity {
    @Property(name = "title")
    private String _title;
    
    @Relationship(type = "HAS", direction = Relationship.INCOMING)
    CategorieObject parentCategorie;

    @Relationship(type = "PAGE_OF", direction = Relationship.INCOMING)
    PageObject page;
    
    public SubCategorieObject() {
        this._title = "";
        this.parentCategorie = null;
    }

    public SubCategorieObject(String title, CategorieObject parentCategorie) {
        this._title = title;
        this.parentCategorie = parentCategorie;
    }

    public CategorieObject getParentCategorie() {
        return parentCategorie;
    }

    public void setParentCategorie(CategorieObject parentCategorie) {
        this.parentCategorie = parentCategorie;
    }

    @Override
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
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
