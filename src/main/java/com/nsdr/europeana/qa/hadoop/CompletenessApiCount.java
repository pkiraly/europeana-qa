package com.nsdr.europeana.qa.hadoop;

import com.nsdr.europeana.qa.model.CompletenessCounter;
import com.nsdr.europeana.qa.model.Schema;
import com.nsdr.europeana.qa.model.Property;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class CompletenessApiCount {

	public static class CompletenessMapper
		extends Mapper<LongWritable, Text, Text, FloatWritable> {

		private final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
		private static Schema schema;
		static {
			ClassLoader classLoader = CompletenessMapper.class.getClassLoader();
			try {
				schema = new Schema(
					Paths.get(
						CompletenessMapper.class.getClassLoader()
							.getResource("edm-schema-api.txt").toURI()));
			} catch (URISyntaxException ex) {
				Logger.getLogger(CompletenessApiCount.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		/*
		private final static List<String> properties = Arrays.asList(new String[]{
			"timestamp_created_epoch", "europeanaAggregation",
			"europeanaCollectionName:AS", "timespans", "about", "aggregations",
			"places", "type", "concepts", "timestamp_created", "timestamp_update_epoch",
			"providedCHOs", "title:AS", "year", "timestamp_update", "edmDatasetName:AS",
			"europeanaCompleteness", "proxies", "europeanaAggregation/about"});
		*/

		@Override
		public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

			Property root = schema.getRoot();
			Map<String, Object> record = mapper.readValue(value.toString(),
				new TypeReference<HashMap<String, Object>>() {
				});

			if (record != null) {
				String id = (String) record.get("id");
				String collectionId = ((List<String>) record.get("europeanaCollectionName")).get(0);
				String dataProvider = record.containsKey("dataProvider") 
					? StringUtils.abbreviate(((List<String>) record.get("dataProvider")).get(0), 30)
					: collectionId;

				CompletenessCounter counter = new CompletenessCounter(id);
				counter.count(record, root);
				// context.write(new Text(String.format("\"%s\",%s,%s", dataProvider, collectionId, id)), new FloatWritable(counter.getResult()));
				context.write(new Text(String.format("\"%s\",%s", dataProvider, key)), new FloatWritable(counter.getResult()));
			} else {
				System.err.println("record length: " + value.toString().length());
				System.err.println("record: " + value.toString());
			}
		}
	}

	public static class FloatSumReducer
		extends Reducer<Text, FloatWritable, Text, FloatWritable> {

		private final FloatWritable result = new FloatWritable();

		@Override
		public void reduce(Text key, Iterable<FloatWritable> values,
			Context context
		) throws IOException, InterruptedException {
			float sum = 0.0f;
			for (FloatWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: CompletenessCount <in> <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "completeness count");
		job.setJarByClass(CompletenessApiCount.class);
		job.setMapperClass(CompletenessMapper.class);

		/**
		 * ** Uncomment the following line to enable the Combiner ***
		 */
		//job.setCombinerClass(FloatSumReducer.class);
		job.setReducerClass(FloatSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
