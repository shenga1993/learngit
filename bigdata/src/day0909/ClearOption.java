package day0909;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ClearOption {

	public static class UidMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		Text uid = new Text();
		public static final IntWritable one = new IntWritable(1);

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] arr = line.split("\t");
			// if(arr!=null){
			uid.set(arr[1]);
			context.write(uid, one);
			// }
		}
	}

	public static class UidReduce extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		@Override
		protected void reduce(Text key, Iterable<IntWritable> value,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub

			int sum = 0;
			for (IntWritable val : value) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);

		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {

		String input = "hdfs://master:9000/day0909/1w";
		String output = "hdfs://master:9000/day0909/1wCount";
		@SuppressWarnings("deprecation")
		Job job = new Job(new Configuration(), "uid count");
		job.setJarByClass(ClearOption.class);
		job.setMapperClass(UidMapper.class);
		job.setReducerClass(UidReduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		// }
		//
	}

}
