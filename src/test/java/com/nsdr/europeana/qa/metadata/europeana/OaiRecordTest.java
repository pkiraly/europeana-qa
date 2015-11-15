package com.nsdr.europeana.qa.metadata.europeana;

import com.nsdr.europeana.qa.metadata.Metadata;
import com.nsdr.europeana.qa.metadata.europeana.OaiRecordMetadata;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class OaiRecordTest {

	private ObjectMapper mapper;

	public OaiRecordTest() {
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
	public void hello() throws URISyntaxException, IOException {
		Path path = Paths.get(getClass().getResource("/europeana-oai.json").toURI());
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		String expectedProvider = "Erbario - Museo di Botanica...";
		List<String> ids = Arrays.asList(new String[]{
			"http://data.europeana.eu/item/2023829/0003BD0E39A5AD674A1682C144699B279C7EF163",
			"http://data.europeana.eu/item/2023829/00F94128742D204F5A6838D7C74BF360C92FC104",
			"http://data.europeana.eu/item/2023829/014BED30EA2346C8B9FA25BBC05DB9A4945EDDCA",
			"http://data.europeana.eu/item/2023829/01BCF76F123532352C03C7D73BCE56E20B6206EA",
			"http://data.europeana.eu/item/2023829/01DC82F2F4E0BFE41106CB6B9B56E4489E61BF3D",
			"http://data.europeana.eu/item/2023829/03BFEAE54F1D17D4BA5139581DBAE3A2900DB366",
			"http://data.europeana.eu/item/2023829/0439FED3AB920A87A13301474416A530D8956B2A",
			"http://data.europeana.eu/item/2023829/07398BCABC5FB1EDD8AE6F050BED6DB4FA12B348",
			"http://data.europeana.eu/item/2023829/085465009BAFA02227280DA94B7EE946E0D5599D",
			"http://data.europeana.eu/item/2023829/091EC0CCB8AC469F5508A5825F70AAAC2E2ED4EB"
		});

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String expectedId = ids.get(i);

			Map<String, Object> record = mapper.readValue(line,
				new TypeReference<HashMap<String, Object>>(){});
			Metadata oaiRecord = new OaiRecordMetadata(record);
			assertEquals(expectedId, oaiRecord.getId());
			assertEquals(expectedProvider, oaiRecord.getDataProvider());
		}
	}
}
