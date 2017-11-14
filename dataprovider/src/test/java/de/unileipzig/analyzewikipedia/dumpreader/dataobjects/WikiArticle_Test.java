jop
        package de.unileipzig.analyzewikipedia.dumpreader.dataobjects;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * @author Danilo Morgado
 */
public class WikiArticle_Test {
    
    private static long timeUsing;
    private static long timeGeneral;
    
    // fix data
    private static String parentTitle;
    private static WikiArticle parentArticle;
    private static String articleTitle;
    // random data
    private static String randomTitle;
    private static WikiArticle randomArticle;
    // special data
    private final static String EMPTYSTRING = "";
    private final static String NULLSTRING = null;
    
    @BeforeClass
    public static void setUpClass() {

        parentTitle = "parent";
        parentArticle = new WikiArticle(parentTitle);
        
        articleTitle = "title";
        do {
            randomTitle = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(1,10) + 4);
        } while (articleTitle.equals(randomTitle));
        
        randomArticle = new WikiArticle(randomTitle);
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("\n=== WikiArticle_Test ===\nTesttime of class: " + (timeGeneral / 1000000) + " ms [" + (timeGeneral / 1000) + " us]\n");
    }
    
    @Before
    public void setUp() {
        timeUsing = System.nanoTime();
    }
    
    @After
    public void tearDown() {
        long time = System.nanoTime();
        timeGeneral += (time - timeUsing);
        System.out.println("Testtime of methode: " + ((time - timeUsing) / 1000000) + " ms [" + ((time - timeUsing) / 1000) + " us]\n");
    }
    
    @Test
    public void checkCreateEmptyArticle(){
        
        WikiArticle article = new WikiArticle();
        
        assertThat(article.getParent(), is(nullValue()));
        assertEquals(article.getName(), EMPTYSTRING);
        assertThat(article.getText().size(), is(0));
        assertThat(article.getWikiSubLinks().size(), is(0));
        assertThat(article.getWikiLinks().size(), is(0));
        assertThat(article.getSubArticles().size(), is(0));
        assertThat(article.getWikiUnknownSubLinks().size(), is(0));
        assertThat(article.getExternLinks().size(), is(0));
        assertThat(article.getCategories().size(), is(0));
        
    }
    
    @Test
    public void checkCreateNullStringArticle(){
        
        WikiArticle article = new WikiArticle(NULLSTRING);
        
        assertThat(article.getName(), is(nullValue()));
        
    }
    
    @Test
    public void checkCreateRandomArticle(){
        
        WikiArticle parent = new WikiArticle();
        WikiArticle article = new WikiArticle(articleTitle);
        article.setParent(parent);
        
        int count_subart = RandomUtils.nextInt(1,10);
        for (int i = 0; i < count_subart; i++){
            WikiArticle subarticle = new WikiArticle();
            article.addSubArticle(subarticle);
        }
        
        String randomValue = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(1,5) + 5);
        
        int randomLines = RandomUtils.nextInt(5,10);
        Queue<String> list = new LinkedList();
        for (int i = 0; i < randomLines; i++){
            String randomText = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10,50));
            list.add(randomText);
        }
        article.setText(list);
        
        int count_wikilinks = RandomUtils.nextInt(1,10);
        for (int i = 0; i < count_wikilinks; i++){
            article.addWikiLink(randomValue, randomValue);
        }
        
        int count_wikisublinks = RandomUtils.nextInt(1,10);
        for (int i = 0; i < count_wikisublinks; i++){
            article.addWikiSubLink(randomValue, randomValue, randomValue);
        }
        
        int count_wikiunknownsublinks = RandomUtils.nextInt(1,10);
        for (int i = 0; i < count_wikiunknownsublinks; i++){
            article.addWikiUnknownSubLink(randomValue, randomValue, randomValue);
        }
        
        int count_extlinks = RandomUtils.nextInt(1,10);
        for (int i = 0; i < count_extlinks; i++){
            article.addExternLink(randomValue, randomValue);
        }
        
        int count_catlinks = RandomUtils.nextInt(1,10);
        for (int i = 0; i < count_catlinks; i++){
            article.addCategorieName(randomValue, randomValue);
        }
        
        assertThat(article.getParent(), is(parent));
        assertEquals(article.getName(), articleTitle);
        assertThat(article.getText().size(), is(randomLines));
        assertThat(article.getSubArticles().size(), is(count_subart));
        assertThat(article.getWikiLinks().size(), is(count_wikilinks));
        assertThat(article.getWikiSubLinks().size(), is(count_wikisublinks));        
        assertThat(article.getWikiUnknownSubLinks().size(), is(count_wikiunknownsublinks));
        assertThat(article.getExternLinks().size(), is(count_extlinks));
        assertThat(article.getCategories().size(), is(count_catlinks));
        
    }
    
}
