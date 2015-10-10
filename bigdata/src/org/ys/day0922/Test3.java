package org.ys.day0922;

import java.io.IOException;
import java.util.HashSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class Test3 {

	
	public static class Comparator implements RawComparator<Text> {

		@Override
		public int compare(Text o1, Text o2) {
			// TODO Auto-generated method stub
			if(o1.toString().contains(o2.toString())||o2.toString().contains(o1.toString())){
				return 0;
			}else
				return o1.toString().hashCode()-o2.toString().hashCode();
		}

		@Override
		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			// TODO Auto-generated method stub
			int compareBytes = WritableComparator.compareBytes(b1, s1, 8, b2, s2,8);
			return compareBytes;
		}

	}

	public static class Test3Mapper2 extends
			Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text("");
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String province = value.toString();
			tkey.set(province);
			context.write(tkey, tvalue);
		}
	}

	public static class Test3Mapper extends
	Mapper<Object, Text, Text, Text> {
		Text tkey = new Text();
		Text tvalue = new Text("");
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String []data = line.split(",");
			if(null!=data&&data.length==6){
				String province = data[4];
				String market = data[3];
				tkey.set(province);
				if(market.startsWith(province)){
					tvalue.set(market);
					context.write(tkey, tvalue);
					tvalue.set("");
				}else{
					context.write(tkey,tvalue);
				}
			}
		}
	}

	public static class Test3Reducer extends
	Reducer<Text, Text, Text, IntWritable> {
		IntWritable intw = new IntWritable();
		@Override
		protected void reduce(Text key, Iterable<Text> val,
				Reducer<Text, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			HashSet<String> hs = new HashSet<>();
			for(Text value:val){
				if(!"".equals(value))
					hs.add(value.toString());
			}
			int count = hs.size();
			intw.set(count);
			context.write(key, intw);
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=3){
			System.exit(0);
		}
		Job job = new Job(new Configuration(),Test3.class.getSimpleName());
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, Test3Mapper2.class);
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, Test3Mapper.class);
		job.setJarByClass(Test3.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(Test3Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		job.waitForCompletion(true);
	}
}
