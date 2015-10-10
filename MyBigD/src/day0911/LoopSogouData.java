package day0911;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LoopSogouData {

	public static class LoopReducer extends
			Reducer<Text, Text, Text, Text> {
		protected void reduce(
				Text arg0,
				java.lang.Iterable<Text> arg1,
				org.apache.hadoop.mapreduce.Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			for(Text t : arg1){
				arg2.write(arg0, t);
			}
		};
	}

	public static class LoopMapper extends Mapper<Text, Text, Text, Text> {
		protected void map(
				Text key,
				Text value,
				org.apache.hadoop.mapreduce.Mapper<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			context.write(key, value);
		};
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		if (null == args || args.length != 2) {
			System.err.println("<Usage>:....");
			System.exit(1);
		}
		// TODO Auto-generated method stub
		Job job = new Job(new Configuration(), "LoopSogouData");
		job.setJarByClass(LoopSogouData.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setMapperClass(LoopMapper.class);
		job.setReducerClass(LoopReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}

}
