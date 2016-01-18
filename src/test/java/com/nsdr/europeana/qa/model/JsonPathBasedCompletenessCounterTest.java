package com.nsdr.europeana.qa.model;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.minidev.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class JsonPathBasedCompletenessCounterTest {

	public JsonPathBasedCompletenessCounterTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCounter() throws URISyntaxException, IOException {

		List<String> existing = Arrays.asList(new String[]{"edm:ProvidedCHO/@about", "Proxy/dc:title",
			"Proxy/dc:description", "Proxy/dc:identifier", "Proxy/dc:source",
			"Proxy/dc:rights", "Proxy/edm:type", "Proxy/edm:rights", "Aggregation/edm:rights",
			"Aggregation/edm:provider", "Aggregation/edm:dataProvider",
			"Aggregation/edm:isShownAt", "Aggregation/edm:isShownBy",
			"Aggregation/edm:object"});
		List<String> missing = Arrays.asList(new String[]{"Aggregation/edm:hasView",
			"Proxy/dcterms:alternative", "Proxy/dc:creator", "Proxy/dc:publisher",
			"Proxy/dc:contributor", "Proxy/dc:type", "Proxy/dc:language", "Proxy/dc:coverage",
			"Proxy/dcterms:temporal", "Proxy/dcterms:spatial", "Proxy/dc:subject", "Proxy/dc:date",
			"Proxy/dcterms:created", "Proxy/dcterms:issued", "Proxy/dcterms:extent",
			"Proxy/dcterms:medium", "Proxy/dcterms:provenance", "Proxy/dcterms:hasPart",
			"Proxy/dcterms:isPartOf", "Proxy/dc:format", "Proxy/dc:relation",
			"Proxy/edm:isNextInSequence"});
		List<String> empty = Arrays.asList(new String[]{});

		Path path = Paths.get(getClass().getResource("/europeana-oai.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		JsonPathBasedCompletenessCounter counter = new JsonPathBasedCompletenessCounter();
		counter.setVerbose(true);
		for (String line : lines) {
			counter.count(line);
			assertEquals(new HashSet<>(existing), new HashSet<>(counter.getExistingFields()));
			assertEquals(new HashSet<>(missing), new HashSet<>(counter.getMissingFields()));
			assertEquals(new HashSet<>(empty), new HashSet<>(counter.getEmptyFields()));
		}
	}

	@Test
	public void showConfigurations() {
		Configuration conf = Configuration.builder().options(Option.AS_PATH_LIST).build();
		assertEquals(1, conf.getOptions().size());
		assertEquals(Option.AS_PATH_LIST, (Option) conf.getOptions().toArray()[0]);
	}

	@Test
	public void jsonPath() throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getResource("/json-examples/store.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		String json = StringUtils.join(lines, "");

		int MAX = 100000;

		long start = System.currentTimeMillis();
		for (int i = 0; i < MAX; i++) {
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
			Object firstBook = JsonPath.read(document, "$.store.book[0]");
			JsonPath.read(firstBook, "$.author");
			JsonPath.read(firstBook, "$.title");
		}
		long cacheTook = System.currentTimeMillis() - start;

		start = System.currentTimeMillis();
		for (int i = 0; i < MAX; i++) {
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
			JsonPath.read(document, "$.store.book[0].author");
			JsonPath.read(document, "$.store.book[0].title");
		}
		long noCacheTook = System.currentTimeMillis() - start;
		assertTrue(cacheTook > noCacheTook);
	}

	@Test
	public void jsonPathCaching() throws URISyntaxException, IOException {
		List<String> fullPaths = Arrays.asList(new String[]{
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:alternative']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:creator']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:publisher']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:contributor']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:type']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:identifier']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:language']",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:coverage']"
		});

		String proxyCachePath = "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]";
		List<String> cachedPaths = Arrays.asList(new String[]{
			"$.['dc:title']",
			"$.['dcterms:alternative']",
			"$.['dc:description']",
			"$.['dc:creator']",
			"$.['dc:publisher']",
			"$.['dc:contributor']",
			"$.['dc:type']",
			"$.['dc:identifier']",
			"$.['dc:language']",
			"$.['dc:coverage']"
		});

		Path path = Paths.get(getClass().getResource("/europeana-oai.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());

		int MAX = 1000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < MAX; i++) {
			for (String line : lines) {
				Object document = Configuration.defaultConfiguration().jsonProvider().parse(line);
				Object proxy = ((JSONArray) JsonPath.read(document, proxyCachePath)).get(0);
				for (String jsPath : cachedPaths) {
					try {
						JsonPath.read(proxy, jsPath);
					} catch (PathNotFoundException e) {
					}
				}
			}
		}
		long cacheTook = System.currentTimeMillis() - start;

		start = System.currentTimeMillis();
		for (int i = 0; i < MAX; i++) {
			for (String line : lines) {
				Object document = Configuration.defaultConfiguration().jsonProvider().parse(line);
				for (String jsonPath : fullPaths) {
					try {
						JsonPath.read(document, jsonPath);
					} catch (PathNotFoundException e) {
					}
				}
			}
		}
		long noCacheTook = System.currentTimeMillis() - start;
		System.out.println(String.format("%d %s %d", cacheTook, (cacheTook > noCacheTook ? ">" : (cacheTook < noCacheTook ? "<" : "==")), noCacheTook));
		assertTrue(cacheTook > noCacheTook);
	}

	@Test
	public void aboutness() throws URISyntaxException, IOException {
		String jsonPath = "$.['edm:ProvidedCHO'][0]['@about']";
		Path path = Paths.get(getClass().getResource("/europeana-oai.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		for (String line : lines) {
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(line);
			try {
				String about = JsonPath.read(document, jsonPath);
				assertNotNull(about);
				assertTrue(StringUtils.isNotBlank(about));
				assertTrue(about.startsWith("http://data.europeana.eu/item/"));
			} catch (PathNotFoundException e) {
			}
		}
	}
}
