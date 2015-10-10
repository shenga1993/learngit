package org.ys.day0916;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CPDemo {

	public static class CPMapper extends Mapper<Object, Text, Text, Text> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Text tParent = new Text();
			Text tChild = new Text();
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 2) {
				String child = data[0];
				String parent = data[1];
				tParent.set(parent);
				tChild.set(child + 1);
				context.write(tParent, tChild);
				tParent.set(parent + 2);
				tChild.set(child);
				context.write(tChild, tParent);
			}
		}
	}

	public static class CPReducer extends Reducer<Text, Text, Text, Text> {
		@Override
		protected void reduce(Text arg0, Iterable<Text> arg1,
				Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			ArrayList<String> achild = new ArrayList<>();
			ArrayList<String> aparent = new ArrayList<>();
			Text tchild = new Text();
			Text tparent = new Text();
			String child = null;
			String parent = null;
			for (Text val : arg1) {
				if (val.toString().endsWith("1")) {
					child = val.toString().substring(0,
							val.toString().length() - 1);
					achild.add(child);
				} else if (val.toString().endsWith("2")) {
					parent = val.toString().substring(0,
							val.toString().length() - 1);
					aparent.add(parent);
				}
			}
			if (!achild.isEmpty() && !aparent.isEmpty()) {
				for (String str : achild) {
					for (String s : aparent) {
						tchild.set(str);
						tparent.set(s);
						arg2.write(tchild, tparent);
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("error----------------------");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if (fs.exists(new Path(args[1]))) {
			fs.delete(new Path(args[1]), true);
		}
		Job job = new Job(new Configuration(), CPDemo.class.getSimpleName());
		job.setJarByClass(CPDemo.class);
		job.setMapperClass(CPMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(CPReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
