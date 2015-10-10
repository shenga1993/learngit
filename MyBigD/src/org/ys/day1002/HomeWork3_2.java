package org.ys.day1002;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class HomeWork3_2 {

	public static class HomeWork3_2Mapper extends
			Mapper<Object, Text, Text, Text > {
		private static int time = 0;
		private Text tkey = new Text();
		private Text val = new Text();
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(time==0){
				time++;
			}else{
				String[] data = value.toString().split("\t");
				if(data!=null&&data.length==39){
					String carType = data[8];
					String brand = data[7];
					int count = Integer.parseInt(data[11]);
					String fdj = data[12];
					String rl = data[15];
					tkey.set(carType+","+brand);
					val.set(count+"\t"+fdj+"\t"+rl);
					context.write(tkey, val);
				}
			}
		}
	}

	public static class HomeWork3_2Reducer extends
			Reducer<Text, Text, Text, Text> {
		Text value = new Text();
		@Override
		protected void reduce(Text key, Iterable<Text> val,
				Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String [] data = null;
			int count = 0;
			for(Text tmp:val){
				String line = tmp.toString();
				data = line.split("\t");
				count+=Integer.parseInt(data[0]);
			}
			value.set(data[1]+"\t"+data[2]+"\t"+count);
			context.write(key, value);
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
		Job job = new Job(new Configuration(),HomeWork3_2.class.getSimpleName());
		job.setJarByClass(HomeWork3_2.class);
		job.setMapperClass(HomeWork3_2Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(HomeWork3_2Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
	
}
