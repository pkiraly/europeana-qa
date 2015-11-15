package com.nsdr.europeana.qa.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Property {

	enum TYPE {
		STRING, STRINGLIST, OBJECT, OBJECTLIST, BOOLEAN, BOOLEANLIST, 
		LANGUAGEMAP, LANGUAGEMAPLIST, INTEGER, FLOAT, DOUBLE, LONG
	};

	private String name;
	private String path;
	private Property parent = null;
	private List<Property> children = new ArrayList<Property>();
	private TYPE type = TYPE.STRING;

	public Property(String name) {
		this.name = name;
	}

	public Property(String name, Property parent) {
		this(name);
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Property getParent() {
		return parent;
	}

	public void setParent(Property parent) {
		this.parent = parent;
	}

	public List<Property> getChildren() {
		return children;
	}

	public void setChildren(List<Property> children) {
		this.children = children;
	}

	public void addChild(Property child) {
		this.children.add(child);
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Property))
			return false;
		Property other = (Property)o;
		return (name.equals(other.getName())
			&& type.equals(other.getType())
			&& ((parent == null && other.getParent() == null) 
			  || ((parent != null && other.getParent() != null) 
					&& parent.equals(other.getParent())))
			&& children.equals(other.getChildren()));
	}

	@Override
	public String toString() {
		return String.format("name: %s, path: %s, type: %s", name, path, type);
	}
}
