package com.nsdr.europeana.qa.model;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CompletenessCounter {
	int total = 0;
	int met = 0;

	public CompletenessCounter() {}

	public void count(Map<String, Object> json, Property property) {
		for (Property child : property.getChildren()) {
			if (child.getType().equals(Property.TYPE.STRING)) {
				countString(json, child);
			} else if (child.getType().equals(Property.TYPE.NUMBER)) {
				countString(json, child);
			} else if (child.getType().equals(Property.TYPE.BOOLEAN)) {
				countString(json, child);
			} else if (child.getType().equals(Property.TYPE.STRINGLIST)) {
				countListOfStrings(json, child);
			} else if (child.getType().equals(Property.TYPE.OBJECT)) {
				// count((Map<String, Object>)json.get(child.getName()), child);
			} else if (child.getType().equals(Property.TYPE.OBJECTLIST)) {
				countListOfObjects(json, child);
			} else if (child.getType().equals(Property.TYPE.LANGUAGEMAP)) {
				countLanguageMap(json, child);
			}
		}
	}

	public float getResult() {
		return (float) met / total;
	}

	private void countString(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			String value = (String) json.get(property.getName());
			if (StringUtils.isNotBlank(value))
					met++;
		}
	}

	private void countListOfStrings(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			List<String> values = (List<String>) json.get(property.getName());
			for (String value : values) {
				total++;
				if (StringUtils.isNotBlank(value))
					met++;
			}
		}
	}

	private void countListOfObjects(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			List<Object> values = (List<Object>) json.get(property.getName());
			for (Object value : values) {
				count((Map<String, Object>)value, property);
			}
		}
	}

	private void countLanguageMap(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			Map<String, List<String>> values = (Map<String, List<String>>) json.get(property.getName());
			for (String languageCode : values.keySet()) {
				List<String> languageValues = values.get(languageCode);
				for (String value : languageValues) {
					total++;
					if (StringUtils.isNotBlank(value))
						met++;
				}
			}
		}
	}

}
