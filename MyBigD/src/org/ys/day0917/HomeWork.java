package org.ys.day0917;

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileSystem;
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

public class HomeWork {
	static int num =0;
	public static class MyMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		static int count = 0;
		static final IntWritable one = new IntWritable(1);
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			num++;
			count++;
			Text tkey = new Text();
			String line = value.toString();
			String[] data = line.split("\t");
			if(data.length==6){
				String uid = data[1];
				tkey.set(uid);
				context.write(tkey,one);
			}
		}
		@Override
		protected void cleanup(
				Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			System.out.println(count);
			System.out.println("----------");
			System.out.println();
			System.out.println(num);
		}
	}

	public static class MyReducer extends
			Reducer<Text, IntWritable, Text, Text> {
		TreeSet<String> ts = new TreeSet<>(new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					// TODO Auto-generated method stub
					String count1 = o1.split("\t")[0];
					String count2 = o2.split("\t")[0];
					if(Integer.parseInt(count1)!=Integer.parseInt(count2))
						return Integer.parseInt(count2)-Integer.parseInt(count1);
					return o1.split("\t")[1].hashCode()-o2.split("\t")[1].hashCode();
				}
				
			});
		@SuppressWarnings("unused")
		@Override
		protected void reduce(Text arg0, Iterable<IntWritable> arg1,
				Reducer<Text, IntWritable, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int count = 0;
			for(IntWritable val:arg1){
				count++;
			}
			ts.add(count+"\t"+arg0.toString());
			if(ts.size()>20){
				ts.remove(ts.last());
			}
		}
		@Override
		protected void cleanup(
				Reducer<Text, IntWritable, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Iterator<String> it = ts.iterator();
			Text key = new Text();
			Text value = new Text();
			for(int i=0;i<20&&it.hasNext();i++){
				String line = it.next();
				String [] data = line.split("\t");
				if(data.length==2){
					String count = data[0];
					String uid = data[1];
					key.set(uid);
					value.set(count);
					context.write(key, value);
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
//		if(args.length!=2){
//			System.err.println("error---------------");
//			System.exit(0);
//		}
//		FileSystem fs = FileSystem.get(new Configuration());
//		if(fs.exists(new Path(args[1]))){
//			fs.delete(new Path(args[1]),true);
//		}
		Job job = new Job(new Configuration(),HomeWork.class.getSimpleName());
		job.setJarByClass(HomeWork.class);
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path("hdfs://localhost:9000/sogou.500w.utf8"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:9000/testtt"));
		job.waitForCompletion(true);
	}
}
