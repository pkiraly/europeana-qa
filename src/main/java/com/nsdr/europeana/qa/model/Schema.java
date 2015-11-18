package com.nsdr.europeana.qa.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Schema {
	private static final String ROOT = "root";
	Map<String, Property> properties = new HashMap<>();

	public Schema(Path path) {
		build(path);
	}

	public Schema(List<String> props) {
		build(props);
	}

	private void build(Path path) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(path, Charset.defaultCharset());
		} catch (IOException ex) {
			Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (lines != null) {
			List<String> props = new ArrayList<>();
			for (String line : lines) {
				if (StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, "#")) {
					props.add(line);
				}
			}
			build(props);
		}
	}

	private void build(List<String> props) {
		properties.put(ROOT, new Property(ROOT));
		for (String prop : props) {
			// System.err.println("prop: " + prop);
			Property p = PropertyFactory.create(prop);
			Property parent = properties.get(ROOT);
			if (p.getPath() != null) {
				parent = properties.get(p.getPath());
			}

			if (parent == null) {
				System.err.println("\tpath: " + p.getPath());
			}

			parent.addChild(p);
			if (properties.get(p.getName()) != null &&
					!properties.get(p.getName()).getType().equals(p.getType())) {
				Property x = properties.get(p.getName());
				System.err.println(String.format("key exists: %s/%s | %s vs %s", 
					p.getPath(), p.getName(), p.getType(), x.getType()));
			}
			properties.put(p.getName(), p);
		}
	}

	public Property getRoot() {
		return properties.get("root");
	}

	public Property getProperty(String name) {
		return properties.get(name);
	}
}
