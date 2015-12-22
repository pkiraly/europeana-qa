package com.nsdr.europeana.qa.model.json;

import java.util.ArrayList;
import java.util.List;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class SchemaReader {

	XSModel schema;

	public SchemaReader(String uri) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		System.setProperty(DOMImplementationRegistry.PROPERTY,
			"org.apache.xerces.dom.DOMXSImplementationSourceImpl");
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");
		XSLoader schemaLoader = impl.createXSLoader(null);
		schema = schemaLoader.loadURI(uri);
	}

	public List<XSParticle> getChildren(XSElementDeclaration el) {
		if (el == null) {
			return null;
		}
		if (el.getTypeDefinition() == null) {
			return null;
		}
		if (el.getTypeDefinition().getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
			XSParticle particle = ((XSComplexTypeDefinition) el.getTypeDefinition()).getParticle();
			List<XSParticle> children = new ArrayList<>();
			XSModelGroup term = (XSModelGroup) particle.getTerm();
			XSObjectList particles = term.getParticles();
			if (particles != null) {
				for (int i = 0; i < particles.getLength(); i++) {
					children.add((XSParticle)particles.item(i));
				}
			}
			return children;
		} else {
			System.err.println("el: " + el);
			System.err.println("el type def: " + (XSSimpleTypeDefinition) el.getTypeDefinition());
			// (XSSimpleTypeDefinition) el.getTypeDefinition()
			return null;
		}
	}
	
	public XSElementDeclaration getElement(String name, String namespace) {
		return schema.getElementDeclaration(name, namespace);
	}

}
