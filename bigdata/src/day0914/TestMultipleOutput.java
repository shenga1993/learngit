package day0914;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class TestMultipleOutput {
	static String name;
	@SuppressWarnings("rawtypes")
	static MultipleOutputs mos;

	public static class MyReduce extends
			Reducer<Text, Text, Text, Text> {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected void setup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos = new MultipleOutputs(context);
		}
		@SuppressWarnings("unchecked")
		@Override
		protected void reduce(Text arg0, Iterable<Text> arg1,
				Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			
			for(Text t:arg1){
				mos.write(arg0, t, arg0.toString());
			}
		}
		@Override
		protected void cleanup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos.close();
		}
	}

	public static class MyMapper extends
			Mapper<Object, Text, Text, Text> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String [] data = line.split("\t");
			Text tkey = new Text();
			Text tvalue=new Text();
			if(data.length==6){
				String time = data[0];
				tkey.set(time);
				tvalue.set(line.replace(data[0],""));
				context.write(tkey, tvalue);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if(args.length!=2){
			System.err.println("some trouble with your input");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]), true);
		}
		Job job = new Job(new Configuration(),TestMultipleOutput.class.getSimpleName());
		job.setJarByClass(TestMultipleOutput.class);
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReduce.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}

}
