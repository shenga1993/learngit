package day0914;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class SimpleData {

	public static class MyMapper extends
			Mapper<Object, Text, Text, NullWritable> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			context.write(value, NullWritable.get());
		}
	}

	public static class SDMapper extends
			Mapper<Object, Text, Text, NullWritable> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			context.write(value, NullWritable.get());
		}
	}

	public static class SDReducer extends
			Reducer<Text, NullWritable, Text, NullWritable> {
		@Override
		protected void setup(
				Reducer<Text, NullWritable, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			super.setup(context);
		}
		@Override
		protected void reduce(Text arg0, Iterable<NullWritable> arg1,
				Reducer<Text, NullWritable, Text, NullWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			arg2.write(arg0, NullWritable.get());
		}
		@Override
		protected void cleanup(
				Reducer<Text, NullWritable, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			super.cleanup(context);
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=3){
			System.err.println("error--------------");
			System.exit(-1);
		}
		Job job = new Job(new Configuration(),SimpleData.class.getSimpleName());
		job.setJarByClass(SimpleData.class);
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class,SDMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class,MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setReducerClass(SDReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		job.waitForCompletion(true);
	}
	
}
