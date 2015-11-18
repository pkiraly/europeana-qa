package com.nsdr.europeana.qa.model;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * TODO
 * - multiple instances: fraction numbers
 * - duplicated values
 * - boolean fields: 
 * -- has duplicated values
 * -- has empty value
 * - test
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class CompletenessCounter {

	private int total = 0;
	private int met = 0;
	private final String recordID;

	public CompletenessCounter(String recordID) {
		this.recordID = recordID;
	}

	public void count(Map<String, Object> json, Property property) {
		for (Property child : property.getChildren()) {
			if (child.getType().equals(Property.TYPE.STRING)) {
				countString(json, child);
			} else if (child.getType().equals(Property.TYPE.INTEGER)) {
				countInteger(json, child);
			} else if (child.getType().equals(Property.TYPE.LONG)) {
				countLong(json, child);
			} else if (child.getType().equals(Property.TYPE.FLOAT)) {
				countFloat(json, child);
			} else if (child.getType().equals(Property.TYPE.DOUBLE)) {
				countDouble(json, child);
			} else if (child.getType().equals(Property.TYPE.BOOLEAN)) {
				countBoolean(json, child);
			} else if (child.getType().equals(Property.TYPE.STRINGLIST)) {
				countListOfStrings(json, child);
			} else if (child.getType().equals(Property.TYPE.OBJECT)) {
				total++;
				count((Map<String, Object>) json.get(child.getName()), child);
			} else if (child.getType().equals(Property.TYPE.OBJECTLIST)) {
				countListOfObjects(json, child);
			} else if (child.getType().equals(Property.TYPE.BOOLEANLIST)) {
				countListOfBooleans(json, child);
			} else if (child.getType().equals(Property.TYPE.LANGUAGEMAP)) {
				countLanguageMap(json, child);
			} else if (child.getType().equals(Property.TYPE.LANGUAGEMAPLIST)) {
				countLanguageMapList(json, child);
			} else if (child.getType().equals(Property.TYPE.LANGUAGELIST)) {
				countLanguageList(json, child);
			} else if (child.getType().equals(Property.TYPE.RESOURCELIST)) {
				countResourceList(json, child);
			}
		}
	}

	public float getResult() {
		return (float) met / total;
	}

	private void countString(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			if (!(json.get(property.getName()) instanceof String)) {
				System.err.println(recordID + ") not String: " + property.getName() + ", " + json.get(property.getName()) + ", but " + json.get(property.getName()).getClass().getCanonicalName());
			} else {
				String value = (String) json.get(property.getName());
				if (StringUtils.isNotBlank(value)) {
					met++;
				}
			}
		}
	}

	private void countInteger(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			try {
				Integer value = (Integer) json.get(property.getName());
				if (value != null) {
					met++;
				}
			} catch (Exception e) {
				System.err.println(recordID + ") number: " + property.getName() + ", " + json.get(property.getName()));
				throw e;
			}
		}
	}

	private void countFloat(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			Float value = (Float) json.get(property.getName());
			if (value != null) {
				met++;
			}
		}
	}

	private void countLong(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			Object t = json.get(property.getName());
			Long value;
			if (t instanceof Long) {
				value = (Long) t;
			} else {
				value = Long.valueOf((Integer) t);
			}
			//Long value = (Long) json.get(property.getName());
			if (value != null) {
				met++;
			}
		}
	}

	private void countDouble(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			try {
				Double value = null;
				if (json.get(property.getName()) instanceof Double) {
					value = (Double) json.get(property.getName());
				} else if (json.get(property.getName()) instanceof Integer) {
					value = Double.valueOf((Integer) json.get(property.getName()));
				}
				if (value != null) {
					met++;
				}
			} catch (Exception e) {
				System.err.println(recordID + ") countDouble: " + property.getName() + ", " + json.get(property.getName()));
				throw e;
			}
		}
	}

	private void countBoolean(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			Boolean value = (Boolean) json.get(property.getName());
			if (value != null) {
				met++;
			}
		}
	}

	private void countListOfStrings(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			try {
				List<String> values = (List<String>) json.get(property.getName());
				for (String value : values) {
					total++;
					if (StringUtils.isNotBlank(value)) {
						met++;
					}
				}
			} catch (ClassCastException e) {
				System.err.println(recordID + ") countListOfStrings: " + property.getName() + ", " + json.get(property.getName()));
			}
		}
	}

	private void countListOfObjects(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			try {
				List<Object> values = (List<Object>) json.get(property.getName());
				for (Object value : values) {
					count((Map<String, Object>) value, property);
				}
			} catch (ClassCastException e) {
				System.err.println(recordID + ") countListOfObjects: " + property.getName() + ", " + json.get(property.getName()));
			}
		}
	}

	private void countListOfBooleans(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			try {
				List<Boolean> values = (List<Boolean>) json.get(property.getName());
				for (Object value : values) {
					count((Map<String, Object>) value, property);
				}
			} catch (ClassCastException e) {
				System.err.println(recordID + ") countListOfBooleans: " + property.getName() + ", " + json.get(property.getName()));
			}
		}
	}

	private void countLanguageMap(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			try {
				Map<String, List<String>> values = (Map<String, List<String>>) json.get(property.getName());
				for (String languageCode : values.keySet()) {
					List<String> languageValues = values.get(languageCode);
					for (String value : languageValues) {
						total++;
						if (StringUtils.isNotBlank(value)) {
							met++;
						}
					}
				}
			} catch (ClassCastException e) {
				System.err.println(String.format("%s) %s @countLanguageMap: %s, %s", recordID, e.getLocalizedMessage(), property.getName(), json.get(property.getName())));
			}
		}
	}

	private void countLanguageMapList(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			try {
				List<Map<String, Object>> values = (List<Map<String, Object>>) json.get(property.getName());
				for (Map<String, Object> value : values) {
					countLanguageMap(value, property);
				}
			} catch (ClassCastException e) {
				System.err.println(String.format("%s) %s @countLanguageMapList: %s, %s", recordID, e.getLocalizedMessage(), property.getName(), json.get(property.getName())));
			}
		}
	}

	private void countLanguageList(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			try {
				List<Object> values = (List<Object>) json.get(property.getName());
				for (Object value : values) {
					if (value instanceof String) {
						if (StringUtils.isNotBlank((String)value)) {
							met++;
						}
					} else if (value instanceof Map) {
						countLanguageMap((Map<String, Object>)value, property);
					}
				}
			} catch (ClassCastException e) {
				System.err.println(String.format("%s) %s @countLanguageList: %s, %s", recordID, e.getLocalizedMessage(), property.getName(), json.get(property.getName())));
			}
		}
	}

	private void countResourceList(Map<String, Object> json, Property property) {
		total++;
		if (json.containsKey(property.getName())) {
			total--;
			try {
				List<Object> values = (List<Object>) json.get(property.getName());
				for (Object value : values) {
					if (value instanceof String) {
						if (StringUtils.isNotBlank((String)value)) {
							met++;
						}
					} else if (value instanceof Map) {
						countLanguageMap((Map<String, Object>)value, property);
					} else {
						System.err.println("#countResourceList class: " + value.getClass().getCanonicalName());
					}
				}
			} catch (ClassCastException e) {
				System.err.println(String.format("%s) %s @countResourceList: %s, %s", recordID, e.getLocalizedMessage(), property.getName(), json.get(property.getName())));
			}
		}
	}

}
