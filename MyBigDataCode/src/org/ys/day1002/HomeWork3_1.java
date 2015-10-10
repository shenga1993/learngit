package org.ys.day1002;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class HomeWork3_1 {

	public static class HomeWork3_1Mapper extends
			Mapper<Object, Text, Text, IntWritable> {
		private static int time = 0;
		private Text tkey = new Text();
		private IntWritable val = new IntWritable();
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(time==0){
				time++;
			}else{
				String line = value.toString();
				String[] data = line.split("\t");
				if(data!=null&&data.length==39){
					if(data[4].equals("2013")){
						String carType = data[8];
						int count = Integer.parseInt(data[11]);
						tkey.set(carType);
						val.set(count);
					}
				}
			}
			
		}
	}

	public static class HomeWork3_1Reducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		IntWritable val = new IntWritable();
		@Override
		protected void reduce(Text key, Iterable<IntWritable> value,
				Reducer<Text, IntWritable, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int count = 0;
			for(IntWritable tmp:value){
				count+=tmp.get();
			}
			val.set(count);
			context.write(key, val);
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("Check your input");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]), true);
		}
		Job job = new Job(new Configuration(),HomeWork3_1.class.getSimpleName());
		job.setJarByClass(HomeWork3_1.class);
		job.setMapperClass(HomeWork3_1Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(HomeWork3_1Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
