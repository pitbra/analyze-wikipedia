/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.ui.webui.contracts;

/**
 *
 * @author Pit.Braunsdorf
 */
public class EntityViewModel {
    private String _fullName;
    private String _name;
    private String _type;
    private String _mainArticle;

    public String getMainArticle() {
        return _mainArticle;
    }

    public void setMainArticle(String _mainArticle) {
        this._mainArticle = _mainArticle;
    }
    private long _id;

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }


    public String getFullName() {
        return _fullName;
    }

    public void setFullName(String fullName) {
        _fullName = fullName;
    }    
}
