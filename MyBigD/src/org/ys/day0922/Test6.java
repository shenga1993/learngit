package org.ys.day0922;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Test6 {

	public static class Test6Mapper1 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5 && data[3].equals("山西")
					&& data[0].equals("黄瓜")) {
				String price = data[1];
				tkey.set(data[0]+"D1");
				tvalue.set(price);
				context.write(tkey, tvalue);
			}
		}
	}

	public static class Test6Mapper2 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5 && data[3].equals("山西")
					&& data[0].equals("黄瓜")) {
				String price = data[1];
				tkey.set(data[0]+"D2");
				tvalue.set(price );
				context.write(tkey, tvalue);
			}
		}

	}

	public static class Test6Mapper3 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5 && data[3].equals("山西")
					&& data[0].equals("黄瓜")) {
				String price = data[1];
				tkey.set(data[0]+"D3");
				tvalue.set(price );
				context.write(tkey, tvalue);
			}
		}
	}

	public static class Test6Mapper4 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5 && data[3].equals("山西")
					&& data[0].equals("黄瓜")) {
				String price = data[1];
				tkey.set(data[0]+"D4");
				tvalue.set(price);
				context.write(tkey, tvalue);
			}
		}
	}

	public static class Test6Mapper5 extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 5 && data[3].equals("山西")
					&& data[0].equals("黄瓜")) {
				String price = data[1];
				tkey.set(data[0]+"D5");
				tvalue.set(price);
				context.write(tkey, tvalue);
			}
		}
	}

	public static class Test6Reducer extends
			Reducer<Text, Text, Text,Text> {
		HashMap<String,Float> hm = new HashMap<>();
		Text tvalue = new Text();
		@Override
		protected void reduce(Text arg0, Iterable<Text> arg1,
				Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			ArrayList<Float> al = new ArrayList<>();
			float f = 0f;
			for(Text tmp:arg1){
				al.add(Float.parseFloat(tmp.toString()));
			}
			Collections.sort(al);
			if(al.size()>2){
				al.remove(0);
				al.remove(al.size()-1);
			}
			for(float tmp:al){
				f+=tmp;
			}
			float avg = f/al.size();
			tvalue.set(avg+"");
			if(arg0.toString().endsWith("D1")){
				hm.put("D1",avg);
			}else if(arg0.toString().endsWith("D2")){
				hm.put("D2",avg);
			}else if(arg0.toString().endsWith("D3")){
				hm.put("D3",avg);
			}else if(arg0.toString().endsWith("D4")){
				hm.put("D4",avg);
			}else if(arg0.toString().endsWith("D5")){
				hm.put("D5",avg);
			}
			arg2.write(arg0, tvalue);
		}
		@Override
		protected void cleanup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			float f1 = (hm.get("D1")+hm.get("D2")+hm.get("D3"))/3;
			context.write(new Text("SuspectD4"),new Text(f1+""));
			float f2 = (hm.get("D2")+hm.get("D3")+hm.get("D4"))/3;
			context.write(new Text("SuspectD5"), new Text(f2+""));
			f1 = (f1-hm.get("D4"))*(f1-hm.get("D4"));
			f2 = (f2-hm.get("D5"))*(f2-hm.get("D5"));
			float f = f1+f2;
			context.write(new Text("平方误差和"), new Text(f+""));
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Job job = new Job(new Configuration(),Test6.class.getSimpleName());
		job.setJarByClass(Test6.class);
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, Test6Mapper1.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, Test6Mapper2.class);
		MultipleInputs.addInputPath(job, new Path(args[2]), TextInputFormat.class, Test6Mapper3.class);
		MultipleInputs.addInputPath(job, new Path(args[3]), TextInputFormat.class, Test6Mapper4.class);
		MultipleInputs.addInputPath(job, new Path(args[4]), TextInputFormat.class, Test6Mapper5.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(Test6Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[5]));
		job.waitForCompletion(true);
	}
}
