package com.nsdr.europeana.qa.cli;

import com.nsdr.europeana.qa.model.JsonPathBasedCompletenessCounter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class Counter {

	public static final Logger log = Logger.getLogger(Counter.class.getCanonicalName());

	private List<String> existingFields;
	private List<String> missingFields;
	private List<String> emptyFields;
	private Map<String, Double> results;

	public Counter() {
	}

	public List<String> getExistingFields() {
		return existingFields;
	}

	public void setExistingFields(List<String> existingFields) {
		this.existingFields = existingFields;
	}

	public List<String> getMissingFields() {
		return missingFields;
	}

	public void setMissingFields(List<String> missingFields) {
		this.missingFields = missingFields;
	}

	public List<String> getEmptyFields() {
		return emptyFields;
	}

	public void setEmptyFields(List<String> emptyFields) {
		this.emptyFields = emptyFields;
	}

	public Map<String, Double> getResults() {
		return results;
	}

	public void setResults(Map<String, Double> results) {
		this.results = results;
	}

	public static void main(String[] args) {
		String id = System.getProperty("id");
		System.err.println("id: " + id);
		String record = args[0];
		if (StringUtils.isNotBlank(id)) {
			record = fetch(String.format("http://www.europeana.eu/api/v2/record/%s.json?wskey=api2demo", id));
		}
		System.err.println("record: " + record);
		JsonPathBasedCompletenessCounter completenessCounter = new JsonPathBasedCompletenessCounter();
		completenessCounter.setVerbose(true);
		completenessCounter.count(record);

		Counter counter = new Counter();
		counter.results = completenessCounter.getCounters().getResults();
		counter.existingFields = completenessCounter.getExistingFields();
		counter.missingFields = completenessCounter.getMissingFields();
		counter.emptyFields = completenessCounter.getEmptyFields();

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(counter);
		} catch (IOException ex) {
			log.severe(ex.getLocalizedMessage());
		}
		System.out.println(jsonInString);
	}

	private static String fetch(String url) {
		System.err.println("url: " + url);
		String content = "";
		try {
			URL address = new URL(url);
			try (BufferedReader in = new BufferedReader(
				new InputStreamReader(address.openStream()))) {
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					content += inputLine;
				}
			}
		} catch (MalformedURLException ex) {
			log.severe(ex.getLocalizedMessage());
		} catch (IOException ex) {
			log.severe(ex.getLocalizedMessage());
		}
		System.err.println("content: " + content);
		return content;
	}
}
