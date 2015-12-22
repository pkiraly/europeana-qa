package com.nsdr.europeana.qa.model;

import com.jayway.jsonpath.Configuration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
	public void hello() throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getResource("/europeana-oai.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		JsonPathBasedCompletenessCounter counter = new JsonPathBasedCompletenessCounter();
		for (int i = 0; i < lines.size(); i++) {
			counter.count(lines.get(i));
			counter.getCounters().printResults();
		}
	}
}
