package de.unileipzig.analyzewikipedia.dumpreader.controller;

import de.unileipzig.analyzewikipedia.dumpreader.runner.ControllerRunner;
import de.unileipzig.analyzewikipedia.neo4j.dataobjects.*;
import de.unileipzig.analyzewikipedia.neo4j.service.*;

import java.io.File;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.model.Result;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;

/**
 * @author Danilo Morgado
 */
@RunWith(ControllerRunner.class)
public class Controller_Test {
    
    private static final Boolean DEBUG = false;
    
    private static Long timeUsing;
    private static Long timeGeneral;
    private static String methode = "";
    
    private static ActiveServiceImpl active_service;
    private static ArticleServiceImpl article_service;
    private static SubArticleServiceImpl subarticle_service;
    private static ExternServiceImpl extern_service;
    private static SubExternServiceImpl subextern_service;
    private static CategorieServiceImpl categorie_service;
    private static SubCategorieServiceImpl subcategorie_service;
    
    private static File test_articlenetwork;
    private static File test_articlestructure;
    private static File test_data;
    private static File test_shortestpath;
    private static File test_wikilinks;
    
    private static void deleteDB(){
        
        Iterable<ActiveNode> avtiveobjects = active_service.findAll();
        for (ActiveNode node : avtiveobjects){
            active_service.delete(node.getId());
        }
        
        Iterable<ArticleObject> articleobjects = article_service.findAll();
        for (ArticleObject node : articleobjects){
            article_service.delete(node.getId());
        }
        
        Iterable<SubArticleObject> subarticleobjects = subarticle_service.findAll();
        for (SubArticleObject node : subarticleobjects){
            subarticle_service.delete(node.getId());
        }
        
        Iterable<ExternObject> externobjects = extern_service.findAll();
        for (ExternObject node : externobjects){
            extern_service.delete(node.getId());
        }
        
        Iterable<SubExternObject> subexternobjects = subextern_service.findAll();
        for (SubExternObject node : subexternobjects){
            subextern_service.delete(node.getId());
        }
        
        Iterable<CategorieObject> categorieobjects = categorie_service.findAll();
        for (CategorieObject node : categorieobjects){
            categorie_service.delete(node.getId());
        }
        
        Iterable<SubCategorieObject> subcategorieobjects = subcategorie_service.findAll();
        for (SubCategorieObject node : subcategorieobjects){
            subcategorie_service.delete(node.getId());
        }
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        
        timeGeneral = 0l;
        
        active_service = new ActiveServiceImpl();
        article_service = new ArticleServiceImpl();
        subarticle_service = new SubArticleServiceImpl();
        extern_service = new ExternServiceImpl();
        subextern_service = new SubExternServiceImpl();
        categorie_service = new CategorieServiceImpl();
        subcategorie_service = new SubCategorieServiceImpl();
        
        test_articlenetwork = new File("src/test/java/de/unileipzig/analyzewikipedia/dumpreader/testfiles/test_articlenetwork.xml");
        test_articlestructure = new File("src/test/java/de/unileipzig/analyzewikipedia/dumpreader/testfiles/test_articlestructure.xml");
        test_data = new File("src/test/java/de/unileipzig/analyzewikipedia/dumpreader/testfiles/test_data.xml");
        test_shortestpath = new File("src/test/java/de/unileipzig/analyzewikipedia/dumpreader/testfiles/test_shortestpath.xml");
        test_wikilinks = new File("src/test/java/de/unileipzig/analyzewikipedia/dumpreader/testfiles/test_wikilinks.xml");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.format("=== Controller_Test ===%n");
        System.out.format("Testtime of class: %d ms [%d us]%n", (timeGeneral / 1000000), (timeGeneral / 1000));
    }
    
    @Before
    public void setUp() {
        deleteDB();
        timeUsing = System.nanoTime();
    }
    
    @After
    public void tearDown() {
        long time = System.nanoTime();
        timeGeneral += (time - timeUsing);
        System.out.format("Time for '%45s': % 6d ms [% 8d us].%n", methode, ((time - timeUsing) / 1000000), ((time - timeUsing) / 1000));
        deleteDB();
    }
    
    @Test
    public void check_articlestructure(){
        methode = "check_articlestructure";
        
        ThreadController.initThreads(new String[]{test_articlestructure.getPath()});
        
        Integer count = article_service.getNodeCounter();
        assertThat(count, is(23));
        if (DEBUG){
            System.out.println("=== Console 0   ===" + "   Count all nodes");
            System.out.println(count);
        }
        
        Entity result1 = article_service.findByTitle("Main Article");
        String expected1 = "Main_Article";
        assertThat(result1.getTitle(), is(expected1));
        if (DEBUG){
            System.out.println("=== Console 1   ===" + "   Is 'Main_Article'");
            System.out.println(result1.getTitle());
        }

        Entity result2 = article_service.findByTitle("Article B");
        String expected2 = "Article_B";
        assertThat(result2.getTitle(), is(expected2));
        if (DEBUG){
            System.out.println("=== Console 2   ===" + "   Is 'Article_B'");
            System.out.println(result2.getTitle());
        }

        String randomValue = RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(1,5) + 5);
        ((ArticleObject) result2).setTitle(randomValue);
        article_service.createOrUpdate((ArticleObject) result2);

        Entity result3 = article_service.find(result2.getId());
        assertThat(result3.getTitle(), is(randomValue));
        assertThat(result2, is(result3));
        if (DEBUG){
            System.out.println("=== Console 3   ===" + "   Find 'Article_B' by ID");
            System.out.println(result3);
        }
        
        article_service.delete(result3.getId());
        Entity result4 = article_service.findByTitle("Article_B");
        assertThat(result4, is(nullValue()));
        if (DEBUG){
            System.out.println("=== Console 4   ===" + "   Deletetd 'Article_B'");
            System.out.println(result4);
        }
        
        count = article_service.getNodeCounter();
        assertThat(count, is(22));
        if (DEBUG){
            System.out.println("=== Console 5   ===" + "   Count all nodes");
            System.out.println(count);
        }

        Iterable<Entity> result6 = article_service.getNodesByTypeAndTitlesequence("Article", "^M.*");
        String expected6 = "Main_Article";
        for (Entity ent : result6){
            assertTrue(ent.getTitle().equals(expected6));
        }
        if (DEBUG){
            System.out.println("=== Console 6   ===" + "   List articles with starting letter 'M'");
            for (Entity ent : result6){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result7 = article_service.getLinkedNodes("Main Article");
        String[] expected7 = new String[]{"/test1", "Article_A"};
        for (Entity ent : result7){
            Boolean b = false;
            for (String str : expected7){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 7   ===" + "   List links of article 'Main_Article'");
            for (Entity ent : result7){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result8 = active_service.getActiveNodes();
        String expected8 = "Active";
        for (Entity ent : result8){
            assertThat(ent.getTitle(), is(expected8));
        }
        if (DEBUG){
            System.out.println("=== Console 8   ===" + "   List active nodes.");
            for (Entity ent : result8){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result9 = subextern_service.getRelatedNodes("/test1");
        String[] expected9 = new String[]{"Sub_3", "Main_Article"};
        for (Entity ent : result9){
            Boolean b = false;
            for (String str : expected9){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 9   ===" + "   List links of article '/test1'");
            for (Entity ent : result9){
                System.out.println(ent.getTitle());
            }
        }
                
    }
    
    @Test
    public void check_shortestpath(){
        methode = "check_shortestpath";
        
        ThreadController.initThreads(new String[]{test_shortestpath.getPath()});
        
        Integer count = article_service.getNodeCounter();
        assertThat(count, is(9));
        if (DEBUG){
            System.out.println("=== Console 0   ===" + "   Count all nodes");
            System.out.println(count);
        }
        
        Iterable<Entity> result1 = active_service.getShortestPath("Start", "End");
        String[] expected1 = new String[]{"Start", "Active", "End"};
        List<Entity> list1 = new LinkedList();
        for (Entity ent : result1) list1.add(ent);
        for (int i = 0; i < list1.size(); i++){
            assertThat(list1.get(i).getTitle(), is(expected1[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 1   ===" + "   List shortest path.");
            for (Entity ent : result1){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result2 = active_service.getShortestPath("Start", "End", "LINKS");
        String[] expected2 = new String[]{"Start", "Path_A", "Path_B", "End"};
        List<Entity> list2 = new LinkedList();
        for (Entity ent : result2) list2.add(ent);
        for (int i = 0; i < list2.size(); i++){
            assertThat(list2.get(i).getTitle(), is(expected2[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 2   ===" + "   List shortest path.");
            for (Entity ent : result2){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result3 = active_service.getNodesWithTitledConnection("Short cut");
        String[] expected3 = new String[]{"Start", "Path_A"};
        List<Entity> list3 = new LinkedList();
        for (Entity ent : result3) list3.add(ent);
        for (int i = 0; i < list3.size(); i++){
            assertThat(list3.get(i).getTitle(), is(expected3[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 3   ===" + "   Nodes of titled concetion 'Short cut'.");
            for (Entity ent : result3){
                System.out.println(ent.getTitle());
            }
        }

    }
    
    @Test
    public void check_wikilinks(){
        methode = "check_wikilinks";
        
        ThreadController.initThreads(new String[]{test_wikilinks.getPath()});
        
        Integer count = article_service.getNodeCounter();
        assertThat(count, is(13));
        if (DEBUG){
            System.out.println("=== Console 0a  ===" + "   Count all nodes");
            System.out.println(count);
        }
        
        count = article_service.getRelationCounter();
        assertThat(count, is(12));
        if (DEBUG){
            System.out.println("=== Console 0b  ===" + "   Count all relations");
            System.out.println(count);
        }

        Iterable<Entity> result1 = article_service.getWeblinks("Alan Smithee");
        String[] expected1 = new String[]{"/test/test.html", "/beitrag/alan-smithee-die-film-legende-lebt", "/static/topicalbumbackground/13641/der_mann_der_niemals_lebte.html", "/rn/arts/atoday/stories/s353584.htm"};
        List<Entity> list1 = new LinkedList();
        for (Entity ent : result1) list1.add(ent);
        for (int i = 0; i < list1.size(); i++){
            assertThat(list1.get(i).getTitle(), is(expected1[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 1   ===" + "   Get weblinks of 'Alan Smithee'");
            for (Entity ent : result1){
                System.out.println(ent.getTitle());
            }
        }
        
        String[] expected2 = new String[]{"http://www.test.org", "http://dradiowissen.de", "http://einestages.spiegel.de", "http://www.abc.net.au"};
        List<String> list2 = new LinkedList();
        List<String> list3 = new LinkedList();
        for (Entity ent : result1){
            Iterable<Entity> doms = subextern_service.getDomain(ent.getId());
            for (Entity dom : doms){
                list2.add(dom.getTitle());
                list3.add((dom.getTitle()) + ent.getTitle());
            }
        }
        for (int i = 0; i < list2.size(); i++){
            assertThat(list2.get(i), is(expected2[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 2   ===" + "   Get domain of weblinks for 'Alan Smithee'");
            for (String str : list2){
                System.out.println(str);
            }
        }
        
        String[] expected3 = new String[]{"http://www.test.org/test/test.html", "http://dradiowissen.de/beitrag/alan-smithee-die-film-legende-lebt", "http://einestages.spiegel.de/static/topicalbumbackground/13641/der_mann_der_niemals_lebte.html", "http://www.abc.net.au/rn/arts/atoday/stories/s353584.htm"};
        for (int i = 0; i < list3.size(); i++){
            assertThat(list3.get(i), is(expected3[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 3   ===" + "   List full weblinks for 'Alan Smithee'");
            for (String str : list3){
                System.out.println(str);
            }
        }

        Entity result4 = subextern_service.findSubNode("http://dradiowissen.de", "/beitrag/alan-smithee-die-film-legende-lebt");
        String expected4 = "/beitrag/alan-smithee-die-film-legende-lebt";
        assertTrue(result4.getTitle().equals(expected4));
        if (DEBUG){
            System.out.println("=== Console 4   ===" + "   List the searched subextern.");
            System.out.println(result4.getTitle());
        }
        
        String new_article_title = SeekerThread.escapeString(SeekerThread.replaceText("Not Connected"));
        Entity result5 = article_service.findByTitle(new_article_title);
        assertThat(result5, is(nullValue()));
        if (DEBUG){
            System.out.println("=== Console 5   ===" + "   Exist 'Not Connected'");
            System.out.println(result5);
        }

        Result result6 = article_service.sendStatement("CREATE (n:Article { title: '" + new_article_title + "' }) RETURN n");
        if (DEBUG) System.out.println("=== Console 6   ===" + "   Create 'Not Connected'");
        for (Map<String, Object> map : result6.queryResults()){
            if (map.containsKey("n")) {
                assertTrue(((ArticleObject) map.get("n")).getTitle().equals(new_article_title));
                if (DEBUG) System.out.println(((ArticleObject) map.get("n")).getTitle());
            }
        }
        
        count = article_service.getNodeCounter();
        assertThat(count, is(14));
        if (DEBUG){
            System.out.println("=== Console 7a  ===" + "   Count all nodes");
            System.out.println(count);
        }

        count = article_service.getRelationCounter();
        assertThat(count, is(12));
        if (DEBUG){
            System.out.println("=== Console 7b  ===" + "   Count all relations");
            System.out.println(count);
        }
        
        Iterable<Entity> result8 = article_service.getNodesWithoutConnection();
        String expected8 = new_article_title;
        List<Entity> list8 = new LinkedList();
        for (Entity ent : result8) list8.add(ent);
        for (int i = 0; i < list8.size(); i++){
            assertThat(list8.get(i).getTitle(), is(expected8));
        }
        if (DEBUG){
            System.out.println("=== Console 8   ===" + "   List nodes without connections.");
            for (Entity ent : result8){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result9 = article_service.getTitledNodesWithTypedRelation("Weblinks", "LINKS");
        List<Entity> list9 = new LinkedList();
        for (Entity ent : result9) list9.add(ent);
        assertThat(list9.size(), is(1));
        if (DEBUG){
            System.out.println("=== Console 9   ===" + "   List 'Weblinks' nodes with links.");
            for (Entity ent : result9){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result10 = article_service.getTitledNodes("Weblinks");
        List<Entity> list10 = new LinkedList();
        for (Entity ent : result10) list10.add(ent);
        assertThat(list10.size(), is(2));
        if (DEBUG){
            System.out.println("=== Console 10  ===" + "   List all 'Weblinks' nodes.");
            for (Entity ent : result10){
                System.out.println(ent.getTitle());
            }
        }

    }
    
    @Test
    public void check_data(){
        methode = "check_data";
        
        ThreadController.initThreads(new String[]{test_data.getPath()});
        
        Integer count = article_service.getNodeCounter();
        assertThat(count, is(1660));
        if (DEBUG){
            System.out.println("=== Console 0   ===" + "   Count all nodes");
            System.out.println(count);
        }

        count = article_service.getRelationCounter();
        assertThat(count, is(1927));
        if (DEBUG){
            System.out.println("=== Console 1   ===" + "   Count all relations");
            System.out.println(count);
        }

        Iterable<Entity> result2 = article_service.getSpecificNode("Article", "Aristoteles", null, null, null, null, null);
        String expected2 = "Aristoteles";
        for (Entity ent : result2){
            assertTrue(ent.getTitle().equals(expected2));
        }
        if (DEBUG){
            System.out.println("=== Console 2   ===" + "   Get specific node article 'Aristoteles'");
            for (Entity ent : result2){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result3 = article_service.getSpecificNode("SubArticle", null, "Article", null, "LINKS", null, Relationship.OUTGOING);
        List<Entity> list3 = new LinkedList();
        for (Entity ent : result3) list3.add(ent);
        assertThat(list3.size(), is(114));
        if (DEBUG){
            System.out.println("=== Console 3a  ===" + "   Count specific nodes");
            System.out.println(list3.size());
        }
        String[] expected3 = new String[]{"Leben", "Werk", "Nachwirkung", "\\u00DCberlieferung_und_Charakter_der_Schriften", "Einteilung_der_Wissenschaften_und_Grundlegendes", "Sprache,_Logik_und_Wissen", "Bedeutungstheorie", "Pr\\u00E4dikate_und_Eigenschaften", "Deduktion_und_Induktion:_Argumenttypen_und_Erkenntnismittel", "Deduktion", "Induktion", "Dialektik:_Theorie_der_Argumentation_{{Anker|Dialektik}}", "Rhetorik:_Theorie_der_\\u00DCberzeugung", "Syllogistische_Logik", "Kanonische_S\\u00E4tze", "Wissen_und_Wissenschaft", "Naturphilosophie", "Metaphysik", "Ontologie", "Theologie", "Biologie", "Methodologie_der_Biologie:_Trennung_von_Fakten_und_Ursachen", "Inhalte_der_Zoologie", "Seelenlehre:_Theorie_des_Lebendigseins", "Ethik", "Gl\\u00FCck_als_das_Ziel_des_guten_Lebens", "Tugenden", "Lebensformen_und_Lust", "Politische_Philosophie", "Entstehung,_Bestandteile_und_Zweck_des_Staates", "B\\u00FCrger_und_Verfassung_eines_Staates", "Theorie_der_Dichtung", "Hymnos", "Antike", "Mittelalter", "Neuzeit", "Siehe_auch", "Textausgaben_und_\\u00DCbersetzungen", "Der_historische_Aristoteles", "Rezeption", "Weblinks", "Anmerkungen", "Leben", "Kindheit_und_Jugend", "Lincolns_Aufstieg", "Parlamentarier_und_Anwalt_in_Illinois", "Familiengr\\u00FCndung", "Abgeordneter_im_Repr\\u00E4sentantenhaus", "Zuspitzung_der_Sklavenfrage", "Lincoln_als_gem\\u00E4\\u00DFigter_Gegner_der_Sklaverei", "Pr\\u00E4sidentschaftswahl_von_1860", "Lincoln_als_Pr\\u00E4sident", "Amtsantritt_und_Kriegsbeginn", "Lincolns_Politik_im_Krieg", "Kriegsziele_und_Kriegsgr\\u00FCnde", "Sklavenbefreiung", "Indianerpolitik", "Wiederwahl_1864", "Sieg_und_Tod", "Nachleben", "Literatur", "Belletristik", "Verfilmungen", "Weblinks", "Einzelnachweise", "Name", "Merkmale", "Verbreitung", "Vermehrung,_Pflege_und_Ernte", "Inhaltsstoffe", "Verwendung_als_Duftpflanze", "Verwendung_in_der_K\\u00FCche", "Pharmaziegeschichte", "Verwendung_in_der_Heilkunde", "\\u00C4therisches_Rosmarin\\u00F6l", "Anwendung_des_\\u00D6ls", "Symbolik_des_Rosmarins", "Siehe_auch", "Leben", "Filmografie_(Auswahl)", "Nominierungen", "Weblinks", "Wortbedeutung", "Musikalische_Charakteristik", "Spielweise", "Form", "Geschichte", "Ragtime_im_fr\\u00FChen_Jazz", "Bedeutende_Komponisten_und_Interpreten", "H\\u00F6rbeispiele", "Literatur", "Bedeutung", "Das_Relativit\\u00E4tsprinzip", "Relativit\\u00E4t_von_Raum_und_Zeit", "Lichtgeschwindigkeit_als_Grenze", "Vereinigung_von_Raum_und_Zeit_zur_Raumzeit", "\\u00C4quivalenz_von_Masse_und_Energie", "Magnetfelder_in_der_Relativit\\u00E4tstheorie", "Gravitation_und_die_Kr\\u00FCmmung_der_Raumzeit", "Die_mathematische_Struktur_der_allgemeinen_Relativit\\u00E4tstheorie", "Uhren_im_Gravitationsfeld", "Kosmologie", "Schwarze_L\\u00F6cher", "Gravitationswellen", "Spezielle_Relativit\\u00E4tstheorie", "Allgemeine_Relativit\\u00E4tstheorie", "Weitere_geometrische_Theorien", "Experimentelle_Best\\u00E4tigungen", "Wahrnehmung_in_der_\\u00D6ffentlichkeit", "Wissenschaftliche_Anerkennung", "Physikalische_Einf\\u00FChrungen_und_Diskussion", "Popul\\u00E4re_Literatur", "Philosophische_Einf\\u00FChrungen_und_Diskussion", "Weblinks"};
        for (Entity ent : result3){
            Boolean b = false;
            for (String str : expected3){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 3b  ===" + "   Get specific node subarticle links article.");
            for (Entity ent : result3){
                System.out.println(ent.getTitle());
            }
        }
        
    }
    
    @Test
    public void check_articlenetwork(){
        methode = "check_articlenetwork";
        
        ThreadController.initThreads(new String[]{test_articlenetwork.getPath()});
        
        Integer count = article_service.getNodeCounter();
        assertThat(count, is(21));
        if (DEBUG){
            System.out.println("=== Console 0   ===" + "   Count all nodes");
            System.out.println(count);
        }
        
        Iterable<Entity> result1 = article_service.getAllSubArticlesOfArticle("Article 5");
        String[] expected1 = new String[]{"Sub_5a", "Sub_5a1", "Sub_5a2", "Sub_5a2a", "Sub_5a2a1", "Sub_5b"};
        for (Entity ent : result1){
            Boolean b = false;
            for (String str : expected1){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 1   ===" + "   List all subarticles of 'Article 5'");
            for (Entity ent : result1){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result2 = article_service.getArticleAndAllSubArticles("Article 5");
        String[] expected2 = new String[]{"Article_5", "Sub_5a", "Sub_5a1", "Sub_5a2", "Sub_5a2a", "Sub_5a2a1", "Sub_5b"};
        for (Entity ent : result2){
            Boolean b = false;
            for (String str : expected2){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 2   ===" + "   List article and all subarticles of 'Article 5'");
            for (Entity ent : result2){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result3 = article_service.getAllLinksOfNodeAndAllSubArticles("Article 5");
        String[] expected3 = new String[]{"Article_1", "Article_4"};
        for (Entity ent : result3){
            Boolean b = false;
            for (String str : expected3){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 3   ===" + "   List all links of 'Article 5' and all SubArticles");
            for (Entity ent : result3){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result4 = article_service.getTypedNodesWithTypedRelationInWay("Article", "LINKS", Relationship.OUTGOING);
        String[] expected4 = new String[]{"Article_2", "Article_3"};
        for (Entity ent : result4){
            Boolean b = false;
            for (String str : expected4){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 4   ===" + "   List all articles with outgoing links.");
            for (Entity ent : result4){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result5 = article_service.getTypedNodesWithTypedRelationInWay("SubArticle", "LINKS", Relationship.OUTGOING);
        String[] expected5 = new String[]{"Sub_1a", "Sub_4b", "Sub_5a1", "Sub_5a2a1", "Sub_5b"};
        for (Entity ent : result5){
            Boolean b = false;
            for (String str : expected5){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 5   ===" + "   List all subarticles with outgoing links.");
            for (Entity ent : result5){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result6 = article_service.getTypedNodes("Article");
        String[] expected6 = new String[]{"Article_1", "Article_2", "Article_3", "Article_4", "Article_5", "Article_6"};
        for (Entity ent : result6){
            Boolean b = false;
            for (String str : expected6){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 6   ===" + "   List all articles.");
            for (Entity ent : result6){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result7 = article_service.getTitledNodes("Article 3");
        String expected7 = "Article_3";
        for (Entity ent : result7){
            assertTrue(ent.getTitle().equals(expected7));
        }
        if (DEBUG){
            System.out.println("=== Console 7   ===" + "   List all nodes with title 'Article_3'.");
            for (Entity ent : result7){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<SubArticleObject> result8 = subarticle_service.findAll();
        String[] expected8 = new String[]{"Sub_1a", "Sub_1b", "Sub_2a", "Sub_2b", "Sub_3a", "Sub_3b", "Sub_4a", "Sub_4b", "Sub_5a", "Sub_5a1", "Sub_5a2", "Sub_5a2a", "Sub_5a2a1", "Sub_5b"};
        for (Entity ent : result8){
            Boolean b = false;
            for (String str : expected8){
                if (ent.getTitle().equals(str)) {
                    b = true;
                    break;
                }
            }
            assertTrue(b);
        }
        if (DEBUG){
            System.out.println("=== Console 8   ===" + "   List all subarticlenode.");
            for (Entity ent : result8){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result9 = subarticle_service.getEmptyActiveNodes();
        String expected9 = "Article_6";
        for (Entity ent : result9){
            assertTrue(ent.getTitle().equals(expected9));
        }
        if (DEBUG){
            System.out.println("=== Console 9   ===" + "   Get active notes without links or has relations.");
            for (Entity ent : result9){
                System.out.println(ent.getTitle());
            }
        }
        
        Iterable<Entity> result10 = subextern_service.getNodesWithTypedRelationToTypedNodes("SubArticle", "HAS");
        String[] expected10 = new String[]{"Article_1", "Article_2", "Article_3", "Article_4", "Sub_5a2", "Sub_5a1", "Article_5", "Sub_5a", "Sub_5a2a", "Sub_5a2a1"};
        List<Entity> list10 = new LinkedList();
        for (Entity ent : result10) list10.add(ent);
        for (int i = 0; i < list10.size(); i++){
            assertThat(list10.get(i).getTitle(), is(expected10[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 10  ===" + "   List subarticle with has relation.");
            for (Entity ent : result10){
                System.out.println(ent.getTitle());
            }
        }

        Iterable<Entity> result11 = subextern_service.getTypedNodesWithTypedRelationToTypedNodes("SubArticle", "SubArticle", "HAS");
        String[] expected11 = new String[]{"Sub_5a", "Sub_5a1", "Sub_5a2", "Sub_5a2a", "Sub_5a2a1"};
        List<Entity> list11 = new LinkedList();
        for (Entity ent : result11) list11.add(ent);
        for (int i = 0; i < list11.size(); i++){
            assertThat(list11.get(i).getTitle(), is(expected11[i]));
        }
        if (DEBUG){
            System.out.println("=== Console 11  ===" + "   List subarticle with has relation from subarticles.");
            for (Entity ent : result11){
                System.out.println(ent.getTitle());
            }
        }

    }
        
}
