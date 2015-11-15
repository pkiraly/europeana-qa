package com.nsdr.europeana.qa.metadata.europeana;

import com.nsdr.europeana.qa.metadata.Metadata;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class SearchRecordMetadata implements Metadata {

	private final Map<String, Object> json;
	private String id;
	private String dataProvider;

	public SearchRecordMetadata(Map<String, Object> json) {
		this.json = json;
	}

	@Override
	public String getId() {
		if (id == null) {
			id = (String) json.get("id");
		}
		return id;
	}

	@Override
	public String getDataProvider() {
		if (dataProvider == null) {
			String collectionId = ((List<String>) json.get("europeanaCollectionName")).get(0);
			dataProvider = json.containsKey("dataProvider")
				? StringUtils.abbreviate(((List<String>) json.get("dataProvider")).get(0), 30)
				: collectionId;
		}
		return dataProvider;
	}
}
