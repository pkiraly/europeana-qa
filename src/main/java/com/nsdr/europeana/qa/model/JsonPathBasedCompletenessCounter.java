package com.nsdr.europeana.qa.model;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.nsdr.json.JsonBranch;
import net.minidev.json.JSONArray;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class JsonPathBasedCompletenessCounter {

	private final String recordID;
	private Counters counters;

	public JsonPathBasedCompletenessCounter() {
		this.recordID = null;
	}

	public JsonPathBasedCompletenessCounter(String recordID) {
		this.recordID = recordID;
	}

	public void count(String jsonString) {
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
		counters = new Counters();
		for (JsonBranch jp : EdmBranches.getPaths()) {
			Object value = null;
			try {
				value = JsonPath.read(document, jp.getJsonPath());
			} catch (PathNotFoundException e) {

			}
			counters.increaseTotal(jp.getCategories());
			if (value != null) {
				if (value.getClass() == JSONArray.class) {
					if (!((JSONArray) value).isEmpty()) {
						counters.increaseInstance(jp.getCategories());
					}
				} else if (value.getClass() == String.class) {
					if (StringUtils.isNotBlank((String) value)) {
						counters.increaseInstance(jp.getCategories());
					}
				} else {
					System.err.println(jp.getLabel() + " value.getClass(): " + value.getClass());
					System.err.println(jp.getLabel() + ": " + value);
				}
			}
		}
	}

	public Counters getCounters() {
		return counters;
	}

}