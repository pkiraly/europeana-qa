package com.nsdr.europeana.qa.hadoop;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class JsonPathBasedCompletenessCountForOaiRecord {

	public static class EchoingReducer extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
			for (Text value : values)
				context.write(key, value);
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
		job.setJarByClass(JsonPathBasedCompletenessCountForOaiRecord.class);
		job.setMapperClass(JsonPathBasedCompletenessMapper.class);
		// job.addFileToClassPath(Path file);

		/**
		 * ** Uncomment the following line to enable the Combiner ***
		 */
		job.setCombinerClass(EchoingReducer.class);
		job.setReducerClass(EchoingReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
