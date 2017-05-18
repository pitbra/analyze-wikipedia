/*
 * 
 */
package de.unileipzig.analyzewikipedia.neo4j.dataprovider;

import de.unileipzig.analyzewikipedia.neo4j.constants.AnnotationKeys;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.CustomNodeObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.INodeObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubCategorieObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubExternObject;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class DataProvider.
 */
public class DataProvider {

    DatabaseWrapper _databaseWrapper;
    private CustomNodeObject statusNode;

    /**
     * Instantiates a new data provider.
     *
     * @param connectionString the connection string
     * @param username
     * @param password
     * @param useStatusNode
     * @throws java.lang.Exception
     */
    public DataProvider(String connectionString, String username, String password, boolean useStatusNode) throws Exception {
        // Create ConnectionOptions
        ConnectionOptions.GetInstance(connectionString, username, password);
        _databaseWrapper = new DatabaseWrapper();

        if (useStatusNode) {
            CreateStatusNode();
        }
    }

    public DataProvider() throws Exception {
        try {
            if (ConnectionOptions.GetInstance().GetConnectionString() == null) {
                throw new Exception();
            }

            if (_databaseWrapper == null) {
                _databaseWrapper = new DatabaseWrapper();
            }
        } catch (Exception ex) {
            // that should not happen
            throw ex;
        }
    }

    /**
     * Creates the database object.
     *
     * @param articleObject
     * @return true, if successful
     */
    public boolean CreateArticle(ArticleObject articleObject) {
        boolean success = _databaseWrapper.CreateNode(articleObject);

        return success;
    }

    /**
     *
     * @param subArticleObject
     * @return
     */
    public boolean CreateSubArticle(SubArticleObject subArticleObject) {
        boolean success = _databaseWrapper.CreateNode(subArticleObject);

        return success;
    }

    /**
     *
     * @param externObject
     * @return
     */
    public boolean CreateExtern(ExternObject externObject) {
        boolean success = _databaseWrapper.CreateNode(externObject);

        return success;
    }

    /**
     *
     * @param subExternObject
     * @return
     */
    public boolean CreateSubExtern(SubExternObject subExternObject) {
        boolean success = _databaseWrapper.CreateNode(subExternObject);

        return success;
    }

    /**
     *
     * @param categorieObject
     * @return
     */
    public boolean CreateCategorie(CategorieObject categorieObject) {
        boolean success = _databaseWrapper.CreateNode(categorieObject);

        return success;
    }

    /**
     *
     * @param subCategorieObject
     * @return
     */
    public boolean CreateSubCategorie(SubCategorieObject subCategorieObject) {
        boolean success = _databaseWrapper.CreateNode(subCategorieObject);

        return success;
    }

    /**
     *
     * @param title
     * @param returnRelationships
     * @return
     */
    public List<INodeObject> FindByTitle(String title, boolean returnRelationships) {
        return _databaseWrapper.FindByAnnotation(AnnotationKeys.TITLE, title, returnRelationships);
    }

    /**
     *
     * @param title
     * @return
     */
    public List<INodeObject> FindByTitle(String title) {
        return _databaseWrapper.FindByAnnotation(AnnotationKeys.TITLE, title, false);
    }

    /**
     *
     * @param has
     * @param from
     * @param to
     */
    public void CreateRelationship(RelationshipType has, INodeObject from, INodeObject to) {
        _databaseWrapper.CreateRelationsship(has, from, to);
    }

    public INodeObject FindSubNode(INodeObject node, String subNode) {
        return _databaseWrapper.FindSubNode(node, subNode);
    }

    private void CreateStatusNode() {
        if (statusNode == null) {
            statusNode = new CustomNodeObject();
           _databaseWrapper.CreateNode(statusNode);
        }
    }

    public void SetNodeIsActive(INodeObject node, boolean active) {
        _databaseWrapper.CreateRelationsship(RelationshipType.IS_ACTIVE, node, statusNode);
    }
}
