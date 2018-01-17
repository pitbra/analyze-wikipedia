/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unileipzig.analyzewikipedia.ui.webui.helper;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.Entity;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ExternObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubExternObject;
import de.unileipzig.analyzewikipedia.ui.webui.contracts.EntityViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pit.Braunsdorf
 */
public class MappingHelper {

    private static final int STRING_LENGTH = 20;

    public static List<EntityViewModel> MapArticles(Iterable<ArticleObject> artObjects) {
        List<EntityViewModel> entities = new ArrayList<>();

        for (ArticleObject cur : artObjects) {
            entities.add(MapArticle(cur));
        }

        return entities;
    }

    public static List<EntityViewModel> MapEntities(Iterable<Entity> subs) {
        return MapEntities(subs, false);
    }
    
        public static List<EntityViewModel> MapEntities(Iterable<Entity> subs, boolean ellipsis) {
        List<EntityViewModel> entities = new ArrayList<>();
        for (Entity cur : subs) {
            EntityViewModel ent = new EntityViewModel();

            String name = StringHelper.prettyPrintString(cur.getTitle());
            
            if(ellipsis) {
                name = StringHelper.ellipsiereString(name, STRING_LENGTH);
            }
            
            ent.setFullName(StringHelper.prettyPrintString(cur.getTitle()));
            ent.setName(name);
            ent.setId(cur.getId());
            ent.setType(getCssClass(cur));

            entities.add(ent);
        }

        return entities;
    }

    private static String getCssClass(Object obj) {
        if (obj instanceof ArticleObject) {
            return "article";
        }

        if (obj instanceof SubArticleObject) {
            return "subarticle";
        }

        if (obj instanceof ExternObject) {
            return "extern";
        }

        if (obj instanceof SubExternObject) {
            return "subextern";
        }

        if (obj instanceof Entity) {
            return "entity";
        }

        return "node";
    }

    public static EntityViewModel MapArticle(ArticleObject cur) {
        return MapArticle(cur, false);
    }

    public static EntityViewModel MapArticle(ArticleObject cur, boolean ellipsis) {
        EntityViewModel ent = new EntityViewModel();
        
        String name = StringHelper.prettyPrintString(cur.getTitle());

        if (ellipsis) {
            name = StringHelper.ellipsiereString(name, STRING_LENGTH);
        }

        ent.setFullName(StringHelper.prettyPrintString(cur.getTitle()));
        ent.setName(name);
        ent.setType("Article");
        ent.setId(cur.getId());

        return ent;
    }

    public static EntityViewModel MapEntity(Entity extern) {
        EntityViewModel ent = new EntityViewModel();

        ent.setName(StringHelper.prettyPrintString(extern.getTitle()));
        ent.setId(extern.getId());
        ent.setType(getCssClass(extern));

        return ent;
    }
}
