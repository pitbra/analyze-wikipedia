package dataproviderConsole;

import java.util.Random;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.RelationshipType;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.SubArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;

public class Programm {

	public static void main(String[] args) {
		DataProvider prov = new DataProvider("bolt://localhost:7687/", "neo4j", "tgZHyAtvhlWDav5CXD0F");
		
		ArticleObject article = ArticleObject.CreateArticleObject();		
		article.AddAnnotation("title", "Title");
		prov.CreateArticle(article);
		
		ArticleObject article2 = ArticleObject.CreateArticleObject();
		article2.AddAnnotation("title", "Napoleon");
		prov.CreateArticle(article2);
		
		SubArticleObject subArticle = new SubArticleObject();
		subArticle.AddAnnotation("title", "Subtitle");
		prov.CreateSubArticle(subArticle);
		
		prov.CreateRelationship(RelationshipType.LINK, article, article2);
		prov.CreateRelationship(RelationshipType.HAS, article, subArticle);
		prov.CreateRelationship(RelationshipType.HAS, article2, subArticle);
	}
}
