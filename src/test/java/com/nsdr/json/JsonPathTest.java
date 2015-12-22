/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsdr.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class JsonPathTest {

	public JsonPathTest() {
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
	public void hello() throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getResource("/europeana-oai.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());

		List<JsonBranch> paths = new ArrayList<>();
		paths.add(new JsonBranch("@about", "$.['edm:ProvidedCHO'][0]['@about']",
			JsonBranch.Category.MANDATORY));
		paths.add(new JsonBranch("Proxy/dc:title", 
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:title']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION,
			JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dcterms:alternative",
			"$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:alternative']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:description", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:description']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:creator", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:creator']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:publisher", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:publisher']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:contributor", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:contributor']",
			JsonBranch.Category.SEARCHABILITY));
		paths.add(new JsonBranch("Proxy/dc:type", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:type']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.IDENTIFICATION,
			JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:identifier", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:identifier']",
			JsonBranch.Category.IDENTIFICATION));
		paths.add(new JsonBranch("Proxy/dc:language", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:language']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:coverage", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:coverage']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:temporal", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:temporal']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:spatial", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:spatial']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:subject", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:subject']",
			JsonBranch.Category.MANDATORY, JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION,
			JsonBranch.Category.MULTILINGUALITY));
		paths.add(new JsonBranch("Proxy/dc:date", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:date']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.BROWSING, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:created", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:created']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:issued", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:issued']",
			JsonBranch.Category.IDENTIFICATION, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:extent", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:extent']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:medium", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:medium']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dcterms:provenance", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:provenance']",
			JsonBranch.Category.DESCRIPTIVENESS));
		paths.add(new JsonBranch("Proxy/dcterms:hasPart", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:hasPart']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dcterms:isPartOf", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dcterms:isPartOf']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/dc:format", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:format']",
			JsonBranch.Category.DESCRIPTIVENESS, JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:source", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:source']",
			JsonBranch.Category.DESCRIPTIVENESS));
		paths.add(new JsonBranch("Proxy/dc:rights", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:rights']",
			JsonBranch.Category.REUSABILITY));
		paths.add(new JsonBranch("Proxy/dc:relation", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['dc:relation']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/edm:isNextInSequence", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:isNextInSequence']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.CONTEXTUALIZATION, JsonBranch.Category.BROWSING));
		paths.add(new JsonBranch("Proxy/edm:type", "$.['ore:Proxy'][?(@['edm:europeanaProxy'][0] == 'false')]['edm:type']",
			JsonBranch.Category.SEARCHABILITY, JsonBranch.Category.BROWSING));

		for (int i = 0; i < lines.size(); i++) {
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(lines.get(i));
			double j = 0.0;
			Map<String, double[]> stat = new HashMap<>();
			stat.put("total", new double[]{0.0, 0.0});
			for (JsonBranch.Category category : JsonBranch.Category.values()) {
				stat.put(category.name(), new double[]{0.0, 0.0});
			}
			for (JsonBranch jp : paths) {
				Object value = JsonPath.read(document, jp.getJsonPath());
				increaseTotal(stat, jp.getCategories());
				if (value.getClass() == JSONArray.class) {
					if (!((JSONArray)value).isEmpty()) {
						increaseInstance(stat, jp.getCategories());
					}
				} else if (value.getClass() == String.class) {
					if (StringUtils.isNotBlank((String)value)) {
						increaseInstance(stat, jp.getCategories());
					}
				} else {
					System.err.println(jp.getLabel() + " value.getClass(): " + value.getClass());
					System.err.println(jp.getLabel() + ": " + value);
				}
			}
			for (String key : stat.keySet()) {
				System.err.println(key + ": " + (stat.get(key)[1] == 0.0 ? 0.0 : (stat.get(key)[1] / stat.get(key)[0])));
			}
		}

	}

	private void increaseTotal(Map<String, double[]> stat, List<JsonBranch.Category> categories) {
		stat.get("total")[0]++;
		for (JsonBranch.Category category : categories) {
			stat.get(category.name())[0]++;
		}
	}

	private void increaseInstance(Map<String, double[]> stat, List<JsonBranch.Category> categories) {
		stat.get("total")[1]++;
		for (JsonBranch.Category category : categories) {
			stat.get(category.name())[1]++;
		}
	}
}
