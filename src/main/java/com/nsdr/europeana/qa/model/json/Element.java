package com.nsdr.europeana.qa.model.json;

import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Element {
	String name;
	String namespace;
	List<Child> children;
	boolean isComplex;
}
