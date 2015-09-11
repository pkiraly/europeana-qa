package com.nsdr.europeana.qa.model;

import com.nsdr.europeana.qa.model.Property;
import java.util.List;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
class PropertyFactory {

	static Property create(String name) {
		String parent = null;
		String type = "STRING";
		if (name.contains("/")) {
			String[] parts = name.split("/");
			if (parts.length == 2) {
				parent = parts[0];
				name = parts[1];
			} else {
				name = parts[parts.length-1];
				for (int i = 0; i < parts.length-1; i++) {
					if (i > 0) {
						parent += "/";
					}
					parent += parts[i];
				}
			}
		}
		if (name.contains(":")) {
			String[] parts;
			parts = name.split(":");
			name = parts[0];
			type = resolveTypeAbbreviation(parts[1]);
		}
		Property prop = new Property(name);
		prop.setType(Property.TYPE.valueOf(type));
		prop.setPath(parent);
		return prop;
	}

	private static String resolveTypeAbbreviation(String abbreviation) {
		String type;
		switch (abbreviation) {
			case "B": type = "BOOLEAN"; break;
			case "N": type = "NUMBER"; break;
			case "SL": type = "STRINGLIST"; break;
			case "O": type = "OBJECT"; break;
			case "OL": type = "OBJECTLIST"; break;
			case "LM": type = "LANGUAGEMAP"; break;
			case "S":
			default:
				type = "STRING"; break;
		}
		return type;
	}
	
}
