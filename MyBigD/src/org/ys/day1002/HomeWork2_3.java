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

public class HomeWork2_3 {

	public static class HomeWork2_3Mapper extends
			Mapper<Object, Text, Text, IntWritable> {
		private static int time = 0;
		private static int len = 0;
		private static final IntWritable one = new IntWritable(1);
		private Text tkey = new Text();
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			if(time==0){
				String line = value.toString();
				String[] data = line.split("\t");
				len = data.length;
			}else{
				if(value.toString().split("\t")!=null&&value.toString().split("\t").length==len){
					int age = Integer.parseInt(value.toString().split("\t")[37]);
					String sex = value.toString().split("\t")[38];
					if(age<=10&&age>=0){
						tkey.set("0-10");
						context.write(tkey, one);
					}else if(age>10&&age<=20){
						tkey.set("11-20");
						context.write(tkey, one);
					}else if(age>20&&age<=30){
						tkey.set("21-30");
						context.write(tkey, one);
					}else if(age>30&&age<=40){
						tkey.set("31-40");
						context.write(tkey, one);
					}else if(age>40&&age<=50){
						tkey.set("41-50");
						context.write(tkey, one);
					}else if(age>50&&age<=60){
						tkey.set("51-60");
						context.write(tkey, one);
					}else if(age>60&&age<=70){
						tkey.set("61-70");
						context.write(tkey, one);
					}else if(age>70&&age<=80){
						tkey.set("71-80");
						context.write(tkey, one);
					}else if(age>80&&age<=90){
						tkey.set("81-90");
						context.write(tkey, one);
					}
					if("男性".equals(sex)){
						tkey.set("男");
						context.write(tkey, one);
					}else if("女性".equals(sex)){
						tkey.set("女");
						context.write(tkey, one);
					}
				}
			}
		}
	}

	public static class HomeWork2_3Reducer extends
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
		Job job = new Job(new Configuration(),HomeWork2_3.class.getSimpleName());
		job.setJarByClass(HomeWork2_3.class);
		job.setMapperClass(HomeWork2_3Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(HomeWork2_3Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
