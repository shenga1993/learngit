package org.ys.day0922;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Test4 {

	public static class Test4Mapper extends Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (null != data && data.length == 6) {
				String name = data[0];
				String province = data[4];
				tkey.set(province);
				tvalue.set(name);
				context.write(tkey, tvalue);
			}
		}
	}

	public static class Test4Reducer extends
			Reducer<Text, Text, Text, IntWritable> {
		HashMap<String, String> hm = new HashMap<>();
		IntWritable value = new IntWritable();
		TreeSet<String> ts = new TreeSet<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				if (Integer.parseInt(o1.split("\t")[0]) != Integer.parseInt(o2
						.split("\t")[0])) {
					return Integer.parseInt(o2.split("\t")[0])
							- Integer.parseInt(o1.split("\t")[0]);
				} else {
					return o2.split("\t")[1].hashCode()
							- o1.split("\t")[1].hashCode();
				}
			}

		});

		@Override
		protected void reduce(Text key, Iterable<Text> val,
				Reducer<Text, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int count = 0;
			String s = "";
			for (Text t : val) {
				s += t.toString() + "\t";
				count++;
			}
			hm.put(key.toString(), s);
			value.set(count);
			ts.add(count + "\t" + key.toString());
			if (ts.size() > 3) {
				ts.remove(ts.last());
			}
			context.write(key, value);
		}

		@Override
		protected void cleanup(
				Reducer<Text, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			ArrayList<String> al = new ArrayList<>();
			Iterator<String> it = ts.iterator();
			while (it.hasNext()) {
				String s = it.next();
				String province = s.split("\t")[1];
				al.add(hm.get(province));
			}
			HashSet<String> hs = new HashSet<>();
			ArrayList<String> al1 = new ArrayList<>();
			ArrayList<String> al2 = new ArrayList<>();
			ArrayList<String> al3 = new ArrayList<>();
			String s1 = al.get(0);
			String s2 = al.get(1);
			String s3 = al.get(2);
			for(String str:s1.split("\t")){
				al1.add(str);
			}
			for(String str:s2.split("\t")){
				al2.add(str);
			}
			for(String str:s3.split("\t")){
				al3.add(str);
			}
			al1.retainAll(al2);
			al1.retainAll(al3);
			for(String s:al1){
				hs.add(s);
			}
			String str = "";
			for(String s:hs){
				str+=s+"\t";
			}
			context.write(new Text("前3的交集"+str), new IntWritable(0));
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Job job = new Job(new Configuration(), Test4.class.getSimpleName());
		job.setJarByClass(Test4.class);
		job.setMapperClass(Test4Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(Test4Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
