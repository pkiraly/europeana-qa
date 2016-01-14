package com.nsdr.europeana.qa.model;

import com.nsdr.json.JsonBranch;
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
public class EdmBranchesTest {

	public EdmBranchesTest() {
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
	public void testPaths() {
		List<JsonBranch> paths = EdmBranches.getPaths();
		assertNotNull(paths);
		assertFalse(paths.isEmpty());
		assertEquals(36, paths.size());
		JsonBranch firstPath = paths.get(0);
		assertNotNull(firstPath);
		assertEquals("edm:ProvidedCHO/@about", firstPath.getLabel());
		assertEquals("$.['edm:ProvidedCHO'][0]['@about']", firstPath.getJsonPath());
		assertFalse(firstPath.getCategories().isEmpty());
		assertEquals(1, firstPath.getCategories().size());
		assertEquals(JsonBranch.Category.MANDATORY, firstPath.getCategories().get(0));
	}
}
