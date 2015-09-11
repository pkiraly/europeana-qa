package com.nsdr.europeana.qa.model;

import junit.framework.TestCase;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class PropertyFactoryTest extends TestCase {

	public PropertyFactoryTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHello() {
		System.err.println("Hello");
		assertEquals(new Property("title"), PropertyFactory.create("title"));
	}
}
