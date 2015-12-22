package com.nsdr.europeana.qa.metadata.europeana;

import com.nsdr.europeana.qa.metadata.Metadata;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class OaiRecordMetadata implements Metadata {

	private final Map<String, Object> json;
	private String id;
	private String dataProvider;

	public OaiRecordMetadata(Map<String, Object> json) {
		this.json = json;
	}

	@Override
	public String getId() {
		if (id == null) {
			id = (String) json.get("identifier");
		}
		return id;
	}

	@Override
	public String getDataProvider() {
		if (dataProvider == null) {
			String collectionId = ((List<String>) json.get("sets")).get(0);
			dataProvider = collectionId;
			if (json.containsKey("ore:Aggregation")) {
				Map<String, Object> aggregation = (Map<String, Object>) ((List<Object>) json.get("ore:Aggregation")).get(0);
				if (aggregation.containsKey("edm:dataProvider")) {
					List<String> dataProviders = ((List<String>) aggregation.get("edm:dataProvider"));
					if (!dataProviders.isEmpty()) {
						if (dataProviders.get(0) instanceof String) {
							if (StringUtils.isNotBlank(dataProviders.get(0))) {
								dataProvider = dataProviders.get(0);
								if (dataProvider.contains(" / ")) {
									dataProvider = dataProvider.substring(0, dataProvider.indexOf(" / "));
								}
								dataProvider = StringUtils.abbreviate(dataProvider, 30)
									.replace("\"", "");
							}
						} else {
							try {
								System.err.println("getDataProvider: dataProviders is not a String, but "
									+ dataProviders.get(0).getClass().getCanonicalName() 
									+ " value: " + dataProviders.get(0));
							} catch (ClassCastException e) {
								System.err.println(e.getLocalizedMessage());
								e.printStackTrace(System.err);
							}
						}
					}
				}
			}
		}
		return dataProvider;
	}
}
