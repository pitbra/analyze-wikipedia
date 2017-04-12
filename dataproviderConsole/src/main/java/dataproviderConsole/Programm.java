package dataproviderConsole;

import de.unileipzig.analyzewikipedia.neo4j.dataobjects.ArticleObject;
import de.unileipzig.analyzewikipedia.neo4j.dataprovider.DataProvider;

public class Programm {

	public static void main(String[] args) {
		DataProvider prov = new DataProvider("bolt://localhost:7474", "neo4j", "tgZHyAtvhlWDav5CXD0F");
		
		ArticleObject object = new ArticleObject();
		
		object.AddAnnotation("title", "Title");
		prov.CreateArticle(object);
		
		System.out.println(object.GetUUID());
	}
}
