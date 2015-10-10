package day0911;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class SogouDataOption {

	public static class RankOrderReducer extends
			Reducer<IntWritable, NullWritable, IntWritable, NullWritable> {
		@SuppressWarnings("unused")
		@Override
		protected void reduce(
				IntWritable key,
				Iterable<NullWritable> value,
				Reducer<IntWritable, NullWritable, IntWritable, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int count = 0;
			IntWritable outputInt = new IntWritable();
			for(NullWritable val:value){
				count++;
			}
			outputInt.set(count);
			context.write(outputInt,NullWritable.get());
		}
	}


	public static class RankOrderMapper extends
			Mapper<Object, Text, IntWritable, NullWritable> {
		public static final IntWritable one = new IntWritable(1);
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, IntWritable, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if(data.length==6){
				int rank = Integer.parseInt(data[3]);
				int order = Integer.parseInt(data[4]);
				if(rank<3&&order>2){
					context.write(one,NullWritable.get());
				}
			}
		}
	}
	

	public static class URLMapper extends
			Mapper<Object,Text, Text, NullWritable> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			Text t = new Text();
			if(data.length==6){
				String time = data[0];
				String keyWord = data[2];
				String url = data[5];
				String uid = data[1];
				int hour = Integer.parseInt(time.substring(8,10));
				if(hour>7&&hour<9&&keyWord.contains("赶集网")&&url.contains("www.ganji.com")){
					t.set(uid);
					context.write(t, NullWritable.get());
				}
			}
		}
	}

	public static class SDReducer extends
			Reducer<Text, NullWritable, Text, NullWritable> {
		@Override
		protected void reduce(Text arg0, Iterable<NullWritable> arg1,
				Reducer<Text, NullWritable, Text, NullWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			arg2.write(arg0, NullWritable.get());
		}
	}

	public static class SDMapper extends
			Mapper<Object,Text,Text,NullWritable> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String [] data = line.split("\t");
			Text t = new Text();
			if(data.length==6){
				String keyWord = data[2];
				int order = Integer.parseInt(data[4]);
				if(keyWord.length()>256&&order<3){
					t.set(data[1]);
					context.write(t,NullWritable.get());
				}
			}
		}
	}
	
	static Configuration conf  = new Configuration();
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if(args.length!=3){
			System.err.println("Usage: pls check ur input");
			System.exit(-1);
		}
		Job job1 = new Job(conf,"UID");
		Job job2 = new Job(conf,"Count");
		job1.setJarByClass(SogouDataOption.class);
		MultipleInputs.addInputPath(job1, new Path(args[0]),TextInputFormat.class, SDMapper.class);
		MultipleInputs.addInputPath(job1, new Path(args[0]), TextInputFormat.class, URLMapper.class);
		job1.setOutputFormatClass(TextOutputFormat.class);
		job1.setMapOutputKeyClass(Text.class);
		job1.setMapOutputValueClass(NullWritable.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(NullWritable.class);
		job1.setReducerClass(SDReducer.class);
		job2.setMapperClass(RankOrderMapper.class);
		job2.setReducerClass(RankOrderReducer.class);
		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(TextOutputFormat.class);
		job2.setMapOutputKeyClass(IntWritable.class);
		job2.setMapOutputValueClass(NullWritable.class);
		job2.setJarByClass(SogouDataOption.class);
		job2.setOutputKeyClass(IntWritable.class);
		job2.setOutputValueClass(NullWritable.class);
		FileInputFormat.addInputPath(job2,new Path(args[0]));
		FileOutputFormat.setOutputPath(job1, new Path(args[1]));
		FileOutputFormat.setOutputPath(job2, new Path(args[2]));
		job1.waitForCompletion(true);
		job2.waitForCompletion(true);
	}
}
