package com.nsdr.europeana.qa.metadata.europeana;

import com.nsdr.europeana.qa.metadata.Metadata;
import com.nsdr.europeana.qa.hadoop.CompletenessCountForOaiRecord;
import com.nsdr.europeana.qa.model.CompletenessCounter;
import com.nsdr.europeana.qa.model.Property;
import com.nsdr.europeana.qa.model.Schema;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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
public class OaiCounterTest {

	private ObjectMapper mapper;
	private static Schema schema;

	static {
		ClassLoader classLoader = CompletenessCountForOaiRecord.CompletenessMapper.class.getClassLoader();
		try {
			schema = new Schema(
				Paths.get(
					classLoader.getResource("edm-schema-oai.txt").toURI()));
		} catch (URISyntaxException ex) {
			Logger.getLogger(CompletenessCountForOaiRecord.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public OaiCounterTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		mapper = new ObjectMapper(new JsonFactory());
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCounter() throws URISyntaxException, IOException {
		Property root = schema.getRoot();
		Path path = Paths.get(getClass().getResource("/europeana-oai.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		int i = 0;
		for (String line : lines) {
			Map<String, Object> record = mapper.readValue(line,
				new TypeReference<HashMap<String, Object>>() {
				});
			Metadata oaiRecord = new OaiRecordMetadata(record);
			CompletenessCounter counter = new CompletenessCounter(oaiRecord.getId());
			counter.count(record, root);
			assertNotNull(counter.getResult());
			if (++i == 100)
				break;
		}
	}

	@Test
	public void testCounter2() throws URISyntaxException, IOException {
		Property root = schema.getRoot();
		Path path = Paths.get("/home/kiru/git/europeana-oai-pmh-client/europeana.json");
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		for (String line : lines) {
			Map<String, Object> record = mapper.readValue(line,
				new TypeReference<HashMap<String, Object>>() {
				});
			Metadata oaiRecord = new OaiRecordMetadata(record);
			CompletenessCounter counter = new CompletenessCounter(oaiRecord.getId());
			counter.count(record, root);
			assertNotNull(counter.getResult());
		}
	}
}
