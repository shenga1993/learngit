package org.ys.day0921;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class UniqueUID {
	
	public static class UidMapper extends
	Mapper<Object, Text, Text, IntWritable> {
		IntWritable one = new IntWritable(1);
		Text tkey = new Text();
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, IntWritable>.Context context)
						throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if(data.length==6){
				String keyWord = data[2];
				if(keyWord.contains("仙剑奇侠传")){
					String uid = data[1];
					tkey.set(uid);
					context.write(tkey, one);
				}
			}
		}
		
	}

	public static class UidReducer extends
			Reducer<Text, IntWritable, Text, NullWritable> {
		private static int count ;
		@Override
		protected void reduce(Text arg0, Iterable<IntWritable> arg1,
				Reducer<Text, IntWritable, Text, NullWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			for(IntWritable val:arg1){
				count+=val.get();
			}
			if(count==1){
				arg2.write(arg0, NullWritable.get());
			}
		}
	}

	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("error======================");
			System.exit(0);
		}
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]), true);
		}
		Job job = new Job(conf,UniqueUID.class.getSimpleName());
		job.setJarByClass(UniqueUID.class);
		job.setMapperClass(UidMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(UidReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
	
}
