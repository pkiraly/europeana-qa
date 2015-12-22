package com.nsdr.europeana.qa.hadoop;

import com.nsdr.europeana.qa.metadata.Metadata;
import com.nsdr.europeana.qa.metadata.europeana.OaiRecordMetadata;
import com.nsdr.europeana.qa.model.JsonPathBasedCompletenessCounter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 *
 * @author Péter Király <peter.kiraly at gwdg.de>
 */
public class JsonPathBasedCompletenessMapper extends Mapper<LongWritable, Text, Text, Text> {

	private final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
	private final boolean withLabel;

	public JsonPathBasedCompletenessMapper() {
		this(false);
	}

	public JsonPathBasedCompletenessMapper(boolean withLabel) {
		this.withLabel = withLabel;
	}

	@Override
	public void map(LongWritable key, Text value, Mapper.Context context)
		throws IOException, InterruptedException {

		Map<String, Object> json = mapper.readValue(value.toString(),
			new TypeReference<HashMap<String, Object>>() {
			});

		if (json != null) {
			Metadata metadata = new OaiRecordMetadata(json);
			JsonPathBasedCompletenessCounter counter = new JsonPathBasedCompletenessCounter();
			counter.count(value.toString());
			context.write(new Text(String.format("\"%s\",%s", metadata.getDataProvider(),
				metadata.getId().replace("http://data.europeana.eu/item/", ""))),
				new Text(counter.getCounters().getResultsAsCSV(withLabel)));
		} else {
			System.err.println("record length: " + value.toString().length());
			System.err.println("record: " + value.toString());
		}
	}
}
