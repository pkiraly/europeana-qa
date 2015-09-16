package com.nsdr.europeana.qa.model;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class SchemaTest {

	public SchemaTest() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testList() {
		List<String> properties = Arrays.asList(new String[]{
			"timestamp_created_epoch", "europeanaAggregation",
			"europeanaCollectionName:SL", "timespans", "about", "aggregations",
			"places", "type", "concepts", "timestamp_created", "timestamp_update_epoch",
			"providedCHOs", "title:SL", "year", "timestamp_update", "edmDatasetName:SL",
			"europeanaCompleteness", "proxies", "europeanaAggregation/about"});
		Schema schema = new Schema(properties);
		List<Property> topProperties = schema.getRoot().getChildren();
		for (Property p : topProperties) {
			System.err.println(String.format("p: %s/%s::%s (%d)", p.getPath(), p.getName(), p.getType().toString(), p.getChildren().size()));
		}
	}

	@Test
	public void testResource() throws IOException, URISyntaxException {
		ClassLoader classLoader = getClass().getClassLoader();
		Path path = Paths.get(classLoader.getResource("edm-schema.txt").toURI());
		Schema schema = new Schema(path);
		List<Property> topProperties = schema.getRoot().getChildren();
		for (Property p : topProperties) {
			System.err.println(String.format("p: %s/%s::%s (%d)", p.getPath(), p.getName(), p.getType().toString(), p.getChildren().size()));
		}
	}

	@Test
	public void testFile() {
		Path path = Paths.get("/home/kiru/git/europeana-qa/target/classes/edm-schema.txt");
		Schema schema = new Schema(path);
		List<Property> topProperties = schema.getRoot().getChildren();
		for (Property p : topProperties) {
			System.err.println(String.format("p: %s/%s::%s (%d)", p.getPath(), p.getName(), p.getType().toString(), p.getChildren().size()));
		}
	}

}