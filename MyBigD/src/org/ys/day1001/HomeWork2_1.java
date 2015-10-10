package org.ys.day1001;

import java.io.IOException;
import java.util.HashMap;

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


public class HomeWork2_1 {

	public static class HomeWork2_1Mapper extends
			Mapper<Object, Text, IntWritable, IntWritable> {
		private static int time = 0;
		private static int len = 0;
		private IntWritable intSex = new IntWritable();
		private static final IntWritable one = new IntWritable(1);
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, IntWritable, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(time==0){
				time++;
				len = value.toString().split("\t").length;
			}else{
				String line = value.toString();
				String [] data = line.split("\t");
				if(null!=data&&data.length==len){
					String sex = data[len-1];
					if("女性".equals(sex)){
						intSex.set(0);
						context.write(intSex,one);
					}else if("男性".equals(sex)){
						intSex.set(1);
						context.write(intSex, one);
					}
				}
			}
		}
	}

	public static class HomeWork2_1Reducer extends
			Reducer<IntWritable, IntWritable, Text, Text> {
		private static int sum = 0;
		private static HashMap<Integer,Integer> hm = new HashMap<>();
		@Override
		protected void reduce(IntWritable key, Iterable<IntWritable> value,
				Reducer<IntWritable, IntWritable, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int count = 0;
			for(IntWritable tmp:value){
				sum+=tmp.get();
				count+=tmp.get();
			}
			hm.put(key.get(),count);
		}
		@Override
		protected void cleanup(
				Reducer<IntWritable, IntWritable, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			double percent = (double)hm.get(0)/(double)sum;
			context.write(new Text("女性"),new Text(percent*100+"%"));
			percent = (double)hm.get(1)/(double)sum;
			context.write(new Text("男性"),new Text(percent*100+"%"));
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
		Job job = new Job(new Configuration(),HomeWork2_1.class.getSimpleName());
		job.setJarByClass(HomeWork2_1.class);
		job.setMapperClass(HomeWork2_1Mapper.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(HomeWork2_1Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
	
}
