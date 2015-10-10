package org.ys.day0917;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TwoTableConnectDemo {

	public static class AdMapper extends Mapper<Object, Text, Text, Text> {
		static int time = 0;

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			if (time != 0) {
				// TODO Auto-generated method stub
				String line = value.toString();
				String[] data = line.split("\t");
				Text tkey = new Text();
				Text tvalue = new Text();
				if (data.length == 2) {
					String addID = data[0];
					String addname = data[1];
					tkey.set(addID);
					tvalue.set(addname + 2);
					context.write(tkey, tvalue);
				}
			}
			time++;
		}
	}

	public static class FMapper extends Mapper<Object, Text, Text, Text> {
		static int time = 0;

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if (time != 0) {
				String line = value.toString();
				String[] data = line.split("\t");
				Text tkey = new Text();
				Text tvalue = new Text();
				if (data.length == 2) {
					String fname = data[0];
					String addID = data[1];
					tkey.set(fname + 1);
					tvalue.set(addID);
					context.write(tvalue, tkey);
				}
			}
			time++;
		}
	}

	public static class FReducer extends Reducer<Text, Text, Text, Text> {
		static int time = 0;
		@Override
		protected void reduce(Text arg0, Iterable<Text> arg1,
				Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(time==0){
				arg2.write(new Text("factoryname"), new Text("addressname"));
				time++;
			}
			ArrayList<String> alfname = new ArrayList<>();
			ArrayList<String> aladdname = new ArrayList<>();
			Text tkey = new Text();
			Text tvalue = new Text();
			for (Text val : arg1) {
				if (val.toString().endsWith("1")) {
					alfname.add(val.toString().substring(0,
							val.toString().length() - 1));
				} else if (val.toString().endsWith("2")) {
					aladdname.add(val.toString().substring(0,
							val.toString().length() - 1));
				}
			}
			if (!alfname.isEmpty() && !aladdname.isEmpty()) {
				for (String fname : alfname) {
					for (String addname : aladdname) {
						tkey.set(fname);
						tvalue.set(addname);
						arg2.write(tkey, tvalue);
					}
				}
			}

		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("error---------------------------------");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if (fs.exists(new Path(args[2]))) {
			fs.delete(new Path(args[2]), true);
		}
		Job job = new Job(new Configuration(),
				TwoTableConnectDemo.class.getSimpleName());
		job.setJarByClass(TwoTableConnectDemo.class);
		MultipleInputs.addInputPath(job, new Path(args[0]),
				TextInputFormat.class, AdMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]),
				TextInputFormat.class, FMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(FReducer.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		job.waitForCompletion(true);
	}
}
