package com.nsdr.europeana.qa.model.json;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTypeDefinition;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class XsdReaderTest {

	private SchemaReader reader;

	public XsdReaderTest() {
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

	// @Test
	public void read() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.setProperty(DOMImplementationRegistry.PROPERTY,
			"org.apache.xerces.dom.DOMXSImplementationSourceImpl");
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");
		XSLoader schemaLoader = impl.createXSLoader(null);
		XSModel model = schemaLoader.loadURI("src/test/resources/note.xsd");
		XSElementDeclaration el = model.getElementDeclaration("note", "http://www.w3schools.com");
		System.err.println("el: " + el);
		assertEquals("http://www.w3schools.com", el.getNamespace());
		assertEquals("note", el.getName());
		assertEquals(XSTypeDefinition.COMPLEX_TYPE, el.getTypeDefinition().getTypeCategory());
		if (XSTypeDefinition.COMPLEX_TYPE == el.getTypeDefinition().getTypeCategory()) {
			XSComplexTypeDefinition elDef = (XSComplexTypeDefinition) el.getTypeDefinition();
			XSParticle particle = elDef.getParticle();
			XSModelGroup chDef = (XSModelGroup) particle.getTerm();
			XSObjectList list = chDef.getParticles();

			assertNotNull(list);
			assertEquals(4, list.getLength());
			System.err.println("list length: " + list.getLength());
			for (int i = 0; i < list.getLength(); i++) {
				XSObject child = list.item(i);
				System.err.println("child: " + child);
			}
		}

		XSNamedMap map = model.getComponents(XSTypeDefinition.COMPLEX_TYPE);
		System.err.println("map: " + map);
		for (int i = 0; i < map.getLength(); i++) {
			XSObject item = map.item(i);
			XSComplexTypeDefinition type = (XSComplexTypeDefinition) item;
			System.err.println("item " + item);
		}
	}

	// @Test
	public void reader() {
		try {
			SchemaReader reader2 = new SchemaReader("src/test/resources/note.xsd");
			XSElementDeclaration el = reader2.getElement("note", "http://www.w3schools.com");
			List<XSParticle> children = reader2.getChildren(el);
			if (children != null) {
				for (XSParticle child : children) {
					System.err.println("min: " + child.getMinOccurs());
					System.err.println("max: " + child.getMaxOccurs());
					System.err.println("child: " + child);
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(XsdReaderTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassCastException ex) {
			Logger.getLogger(XsdReaderTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(XsdReaderTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(XsdReaderTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}

	@Test
	public void testTree() {
		try {
			reader = new SchemaReader("src/test/resources/note.xsd");
			tree("note", "http://www.w3schools.com");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(XsdReaderTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(XsdReaderTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(XsdReaderTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void tree(String name, String namespace) {
		System.err.println(String.format("tree(%s:%s)", namespace, name));
		XSElementDeclaration el = reader.getElement(name, namespace);
		System.err.println("tree el: " + el);
		List<XSParticle> children = reader.getChildren(el);
		System.err.println("children[");
		if (children != null) {
			for (XSParticle child : children) {
				XSElementDeclaration decl = (XSElementDeclaration)child.getTerm();
				System.err.println(String.format("%s:%s", decl.getNamespace(), decl.getName()));
				System.err.println(String.format("decl type: %s", (decl.getTypeDefinition().getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) ? "complex" : "simple"));
				System.err.println(String.format("decl: %s", decl.getTypeDefinition()));
				System.err.println(String.format("decl: %s", decl.getTypeDefinition().getName()));
				System.err.println(String.format("decl: %s", decl.getTypeDefinition().getNamespace()));
				if (decl.getTypeDefinition().getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
					tree(child.getTerm().getName(), child.getTerm().getNamespace());
				}
			}
		}
		System.err.println("]");
	}
}
