package com.nsdr.europeana.qa.model;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.nsdr.json.JsonBranch;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONArray;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class JsonPathBasedCompletenessCounter {

	private final String recordID;
	private Counters counters;
	private List<String> missingFields;
	private List<String> emptyFields;
	private List<String> existingFields;
	private boolean verbose = false;

	public JsonPathBasedCompletenessCounter() {
		this.recordID = null;
	}

	public JsonPathBasedCompletenessCounter(String recordID) {
		this.recordID = recordID;
	}

	public void count(String jsonString) {
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
		if (verbose) {
			missingFields = new ArrayList<>();
			emptyFields = new ArrayList<>();
			existingFields = new ArrayList<>();
		}
		counters = new Counters();
		for (JsonBranch jp : EdmBranches.getPaths()) {
			Object value = null;
			try {
				value = JsonPath.read(document, jp.getJsonPath());
			} catch (PathNotFoundException e) {
				// System.err.println("PathNotFoundException: " + e.getLocalizedMessage());
			}
			counters.increaseTotal(jp.getCategories());
			if (value != null) {
				if (value.getClass() == JSONArray.class) {
					if (!((JSONArray) value).isEmpty()) {
						counters.increaseInstance(jp.getCategories());
						if (verbose)
							existingFields.add(jp.getLabel());
					} else if (verbose) {
						missingFields.add(jp.getLabel());
					}
				} else if (value.getClass() == String.class) {
					if (StringUtils.isNotBlank((String) value)) {
						counters.increaseInstance(jp.getCategories());
						if (verbose)
							existingFields.add(jp.getLabel());
					} else if (verbose) {
						emptyFields.add(jp.getLabel());
					}
				} else {
					System.err.println(jp.getLabel() + " value.getClass(): " + value.getClass());
					System.err.println(jp.getLabel() + ": " + value);
				}
			} else if (verbose) {
				missingFields.add(jp.getLabel());
			}
		}
	}

	public Counters getCounters() {
		return counters;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public List<String> getMissingFields() {
		return missingFields;
	}

	public List<String> getEmptyFields() {
		return emptyFields;
	}

	public List<String> getExistingFields() {
		return existingFields;
	}

}