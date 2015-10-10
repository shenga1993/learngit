package org.ys.day1001;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class HomeWork1_2 {

	public static class MyMapper extends
			Mapper<Object, Text, Text, IntWritable> {
		private static int time = 0;
		private static int dataLen = 0;
		private static int yearindex = 0;
		private static int monthindex = 0;
		private static int saleNumindex = 0;
		ArrayList<String> al = new ArrayList<>();
		private Text tKey = new Text();
		private IntWritable intValue = new IntWritable();
		private static int count = 0;

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if (time == 0) {
				String line = value.toString();
				String[] data = line.split("\t");
				dataLen = data.length;
				for (String str : data) {
					al.add(str);
				}
				saleNumindex = al.indexOf("数量");
				yearindex = al.indexOf("年");
				monthindex = al.indexOf("月");
				time++;
			} else {
				String line = value.toString();
				String[] data = line.split("\t");
				if (data.length == dataLen) {
					int year = Integer.parseInt(data[yearindex]);
					if (year == 2013) {
						String month = data[monthindex];
						int saleNum = Integer.parseInt(data[saleNumindex]);
						tKey.set(month);
						intValue.set(saleNum);
						count += saleNum;
						context.write(tKey, intValue);
					}
				}
			}

		}

		@Override
		protected void cleanup(
				Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			context.write(new Text("2013总销售量"), new IntWritable(count));
		}
	}

	public static class MyReducer extends
			Reducer<Text, IntWritable, Text, Text> {
		private Text tkey = new Text();
		private Text tvalue = new Text();
		private TreeMap<Integer, Integer> tmap = new TreeMap<>();
		private static int count = 0;
		@Override
		protected void reduce(Text key, Iterable<IntWritable> value,
				Reducer<Text, IntWritable, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if("2013总销售量".equals(key.toString())){
				for(IntWritable tmp:value){
					count+=tmp.get();
				}
			}else{
				int monthCount = 0;
				for(IntWritable tmp:value){
					monthCount+=tmp.get();
				}
				tmap.put(Integer.parseInt(key.toString()),monthCount);
			}
		}
		@Override
		protected void cleanup(
				Reducer<Text, IntWritable, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Set<Entry<Integer,Integer>> set = tmap.entrySet();
			for(Entry<Integer,Integer> tmp:set){
				double percent = (double)tmp.getValue()/(double)count;
				tkey.set(tmp.getKey()+"");
				tvalue.set(percent*100+"%");
				context.write(tkey, tvalue);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("Check your input");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]), true);
		}
		Job job = new Job(new Configuration(),HomeWork1_2.class.getSimpleName());
		job.setJarByClass(HomeWork1_2.class);
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
