package org.ys.day1001;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class HomeWork1_3 {

	public static class HomeWork1_3Mapper extends
			Mapper<Object, Text, Text, Text> {
		private static int time = 0;
		private static int len = 0;
		private Text tkey = new Text();
		private Text tValue = new Text();
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(time==0){
				time++;
				String line = value.toString();
				String [] data = line.split("\t");
				len = data.length;
			}else{
				String line = value.toString();
				String[] data = line.split("\t");
				if(null!=data&&data.length==len){
					String year = data[4];
					if("2014".equals(year)){
						String city = data[2];
						String town = data[3];
						int num = Integer.parseInt(data[11]);
						tkey.set(city);
						tValue.set(num+"\t"+town);
						context.write(tkey,tValue);
					}
				}
			}
		}
	}

	public static class HomeWork1_3Reducer extends
			Reducer<Text, Text, Text, Text> {
		static HashMap<String,Integer> hm = new HashMap<>();
		private Text tkey = new Text();
		private Text tvalue = new Text();
		private static int count = 0;
		@Override
		protected void reduce(Text key, Iterable<Text> value,
				Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String city = key.toString();
			for(Text tmp:value){
				String line = tmp.toString();
				int num = Integer.parseInt(line.split("\t")[0]);
				String town  = line.split("\t")[1];
				if(hm.containsKey(city+","+town)){
					hm.put(city+","+town,hm.get(city+","+town)+num);
				}else{
					hm.put(city+","+town,num);
				}
				count+=num;
			}
		}
		@Override
		protected void cleanup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Set<String> set = hm.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()){
				String s = it.next();
				double percent = (double)hm.get(s)/(double)count;
				tkey.set(s);
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
		Job job = new Job(new Configuration(),HomeWork1_3.class.getSimpleName());
		job.setJarByClass(HomeWork1_3.class);
		job.setMapperClass(HomeWork1_3Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(HomeWork1_3Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
	
}
