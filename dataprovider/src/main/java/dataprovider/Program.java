/*
 * 
 */
package dataprovider;

public class Program {

    public static void main(String[] args) {
	final DataProvider prov = new DataProvider("localhost:7474");

	prov.CreateArticle(new ArticleObject());
    }
}
