package com.nsdr.json;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
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
public class JsonJUnitTest {

	public JsonJUnitTest() {
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
	public void simpleRead() throws IOException, URISyntaxException {

		ClassLoader classLoader = getClass().getClassLoader();
		Path path = Paths.get(classLoader.getResource("json-examples/facebook.json").toURI());
		FileInputStream fileStream;
		try {
			fileStream = new FileInputStream(path.toFile());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try (JsonReader rdr = Json.createReader(fileStream)) {
			JsonObject obj = rdr.readObject();
			JsonArray results = obj.getJsonArray("data");
			int i = 0;
			for (JsonObject result : results.getValuesAs(JsonObject.class)) {
				switch (i) {
					case 0:
						assertEquals("xxx", result.getJsonObject("from").getString("name"));
						assertEquals("yyy", result.getString("message", ""));
						break;
					case 1:
						assertEquals("ppp", result.getJsonObject("from").getString("name"));
						assertEquals("qqq", result.getString("message", ""));
						break;
					default: break;
				}
				i++;
			}
		}
	}
	
	
	@Test
	public void edmSchemaRead() throws IOException, URISyntaxException {

		ClassLoader classLoader = getClass().getClassLoader();
		Path path = Paths.get(classLoader.getResource("json-examples/edm-schema-oai.json").toURI());
		FileInputStream fileStream;
		try {
			fileStream = new FileInputStream(path.toFile());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try (JsonReader rdr = Json.createReader(fileStream)) {
			JsonObject json = rdr.readObject();
			JsonValue root = json.get("root");
			assertNotNull(root);
			assertEquals(JsonValue.ValueType.OBJECT, root.getValueType());
			assertEquals("OBJECT", ((JsonObject)root).getString("type"));
			JsonArray children = ((JsonObject)root).getJsonArray("elements");
			for (JsonObject child : children.getValuesAs(JsonObject.class)) {
				assertNotNull(child.getString("name"));
				assertNotNull(child.getString("type"));
				assertNotNull(json.getJsonObject(child.getString("name")));
				
			}
			
			// System.err.println("type: "  + root.getValueType());
			/*
			JsonArray results = obj.getJsonArray("data");
			int i = 0;
			for (JsonObject result : results.getValuesAs(JsonObject.class)) {
				switch (i) {
					case 0:
						assertEquals("xxx", result.getJsonObject("from").getString("name"));
						assertEquals("yyy", result.getString("message", ""));
						break;
					case 1:
						assertEquals("ppp", result.getJsonObject("from").getString("name"));
						assertEquals("qqq", result.getString("message", ""));
						break;
					default: break;
				}
				i++;
			}*/
		}
	}

}
