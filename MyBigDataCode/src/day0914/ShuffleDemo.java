package day0914;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class ShuffleDemo {
	private static TreeSet<Integer> ts = new TreeSet<>();
	public static class ShuffleMapper extends
			Mapper<Object, Text, IntWritable, IntWritable> {
		IntWritable val = new IntWritable();
		IntWritable id = new IntWritable();
		int count = 0;
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, IntWritable, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int i = Integer.parseInt(value.toString());
			ts.add(i);
		}
		@Override
		protected void cleanup(
				Mapper<Object, Text, IntWritable, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Iterator<Integer> it = ts.iterator();
			while(it.hasNext()){
				count++;
				val.set(it.next());
				id.set(count);
				context.write(id, val);
			}
		}
	}
	public static class ShuffleReducer extends
			Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
		@Override
		protected void reduce(
				IntWritable arg0,
				Iterable<IntWritable> arg1,
				Reducer<IntWritable, IntWritable, IntWritable, IntWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			for(IntWritable val:arg1){
				arg2.write(arg0, val);
			}
			
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("error----------------------");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]),true);
		}
		Job job = new Job(new Configuration(),ShuffleDemo.class.getSimpleName());
		job.setJarByClass(ShuffleDemo.class);
		job.setMapperClass(ShuffleMapper.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(ShuffleReducer.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
