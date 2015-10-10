package org.ys.day1002;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class HomeWork2_2 {
	public class HomeWork2_2Reducer extends
			Reducer<Text, Text, Text, Text> {
		@SuppressWarnings("rawtypes")
		MultipleOutputs mos = null;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected void setup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos = new MultipleOutputs(context);
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void reduce(Text key, Iterable<Text> value,
				Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String name = key.toString();
			if("model".equals(name)){
				for(Text tmp:value){
					mos.write(name, tmp, NullWritable.get());
				}
			}else if("type".equals(name)){
				for(Text tmp:value){
					mos.write(name, tmp, NullWritable.get());
				}
			}else if("possession".equals(name)){
				for(Text tmp:value){
					mos.write(name, tmp, NullWritable.get());
				}
			}
		}
		@Override
		protected void cleanup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos.close();
		}
	}

	public static class HomeWork2_2Mapper extends
			Mapper<Object, Text, Text, Text> {
		private static int time = 0;
		private static int len = 0;
		private Text tkey = new Text();
		Text tvalue = new Text();
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(time==0){
				time++;
				String line = value.toString();
				String[] data = line.split("\t");
				len = data.length;
			}else {
				String line = value.toString();
				String [] data = line.split("\t");
				if(null!=data&&data.length==len){
					String model = data[5];
					String type = data[8];
					String possession = data[9];
					tkey.set("type");
					tvalue.set(type);
					context.write(tkey, tvalue);
					tkey.set("model");
					tvalue.set(model);
					context.write(tkey, tvalue);
					tkey.set("possession");
					tvalue.set(possession);
					context.write(tkey, tvalue);
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Job job = new Job(new Configuration(),HomeWork2_2.class.getSimpleName());
		job.setJarByClass(HomeWork2_2.class);
		job.setMapperClass(HomeWork2_2Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(HomeWork2_2Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.waitForCompletion(true);
	}
}
