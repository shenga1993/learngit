package org.ys.day0922;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Test5 {

	public static class Test5Reducer extends Reducer<Text, Text, Text, Text> {
		Text tvalue = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> val,
				Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			ArrayList<Float> al1 = new ArrayList<>();
			ArrayList<Float> al2 = new ArrayList<>();
			ArrayList<Float> al3 = new ArrayList<>();
			ArrayList<Float> al4 = new ArrayList<>();
			ArrayList<Float> al5 = new ArrayList<>();
			for (Text tmp : val) {
				String s = tmp.toString();
				if (s.endsWith("D1")) {
					al1.add(Float.parseFloat(s.substring(0, s.length() - 2)));
				} else if (s.endsWith("D2")) {
					al2.add(Float.parseFloat(s.substring(0, s.length() - 2)));
				} else if (s.endsWith("D3")) {
					al3.add(Float.parseFloat(s.substring(0, s.length() - 2)));
				} else if (s.endsWith("D4")) {
					al4.add(Float.parseFloat(s.substring(0, s.length() - 2)));
				} else if (s.endsWith("D5")) {
					al5.add(Float.parseFloat(s.substring(0, s.length() - 2)));
				}
			}
			Collections.sort(al1);
			Collections.sort(al2);
			Collections.sort(al3);
			Collections.sort(al4);
			Collections.sort(al5);
			if (al1.size() > 2) {
				al1.remove(0);
				al1.remove(al1.size() - 1);
			}
			float f1 = 0f;
			for (float s : al1) {
				f1 += s;
			}
			float d1 = f1 / al1.size();
			if(al2.size()>2){
				al2.remove(0);
				al2.remove(al2.size() - 1);
			}
			
			float f2 = 0f;
			for (float s : al2) {
				f2 += s;
			}
			float d2 = f2 / al2.size();
			if(al3.size()>2){
				al3.remove(0);
				al3.remove(al3.size() - 1);
			}
			
			float f3 = 0f;
			for (float s : al3) {
				f3 += s;
			}
			float d3 = f3 / al3.size();
			if(al4.size()>2){
				al4.remove(0);
				al4.remove(al4.size() - 1);
			}
			
			float f4 = 0f;
			for (float s : al4) {
				f4 += s;
			}
			float d4 = f4 / al4.size();
			if(al5.size()>2){
				al5.remove(0);
				al5.remove(al5.size() - 1);
			}
		
			float f5 = 0f;
			for (float s : al5) {
				f5 += s;
			}
			float d5 = f5 / al5.size();
			tvalue.set(d1 + "," + d2 + "," + d3 + "," + d4 + "," + d5);
			context.write(key, tvalue);
		}
	}

	public static class Test5Mapper1 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5) {
				String province = data[4];
				if (province.equals("北京")) {
					String name = data[0];
					String price = data[1];
					tkey.set(name);
					tvalue.set(price + "D1");
					context.write(tkey, tvalue);
				}
			}
		}
	}

	public static class Test5Mapper2 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5) {
				String province = data[4];
				if (province.equals("北京")) {
					String name = data[0];
					String price = data[1];
					tkey.set(name);
					tvalue.set(price + "D2");
					context.write(tkey, tvalue);
				}
			}

		}
	}

	public static class Test5Mapper3 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5) {
				String province = data[4];
				if (province.equals("北京")) {
					String name = data[0];
					String price = data[1];
					tkey.set(name);
					tvalue.set(price + "D3");
					context.write(tkey, tvalue);
				}
			}

		}
	}

	public static class Test5Mapper4 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5) {
				String province = data[4];
				if (province.equals("北京")) {
					String name = data[0];
					String price = data[1];
					tkey.set(name);
					tvalue.set(price + "D4");
					context.write(tkey, tvalue);
				}
			}

		}
	}

	public static class Test5Mapper5 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5) {
				String province = data[4];
				if (province.equals("北京")) {
					String name = data[0];
					String price = data[1];
					tkey.set(name);
					tvalue.set(price + "D5");
					context.write(tkey, tvalue);
				}
			}

		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Job job = new Job(new Configuration(), Test5.class.getSimpleName());
		job.setJarByClass(Test5.class);
		MultipleInputs.addInputPath(job, new Path(args[0]),
				TextInputFormat.class, Test5Mapper1.class);
		MultipleInputs.addInputPath(job, new Path(args[1]),
				TextInputFormat.class, Test5Mapper2.class);
		MultipleInputs.addInputPath(job, new Path(args[2]),
				TextInputFormat.class, Test5Mapper3.class);
		MultipleInputs.addInputPath(job, new Path(args[3]),
				TextInputFormat.class, Test5Mapper4.class);
		MultipleInputs.addInputPath(job, new Path(args[4]),
				TextInputFormat.class, Test5Mapper5.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(Test5Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[5]));
		job.waitForCompletion(true);
	}
}
