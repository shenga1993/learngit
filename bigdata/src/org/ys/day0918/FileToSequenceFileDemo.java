package org.ys.day0918;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class FileToSequenceFileDemo {

	public static class MyMapper extends
			Mapper<Object, Text, Text, NullWritable> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
//			String line = value.toString();
//			BytesWritable val = new BytesWritable();
//			byte [] buf = line.getBytes();
//			val.set(buf, 0, buf.length);
			context.write(value,NullWritable.get());
		}
	}

	public static class MyReducer extends
			Reducer<Text, NullWritable, Text, Text> {
		Text tmp = new Text("");
		@Override
		protected void reduce(
				Text arg0,
				Iterable<NullWritable> arg1,
				Reducer<Text, NullWritable, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			arg2.write(arg0,tmp);
		}
	}	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("error-------------------");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]),true);
		}
		Job job = new Job(new Configuration(),FileToSequenceFileDemo.class.getSimpleName());
		job.setJarByClass(FileToSequenceFileDemo.class);
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
