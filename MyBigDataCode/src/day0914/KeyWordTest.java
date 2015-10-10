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
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class KeyWordTest {

	public static class KeyWordMapper extends Mapper<Object, Text, Text, Text> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String name = value.toString();
			String[] data = name.split("\t");
			Text tkey = new Text();
			if (data.length == 6) {
				String keyWord = data[2];
				if (keyWord.contains("爱奇艺")) {
					tkey.set(keyWord);
					context.write(tkey, value);
				}else if(keyWord.contains("赶集网")){
					tkey.set(keyWord);
					context.write(tkey, value);
				}
			}
		}
	}

	public static class KeyWordReducer extends Reducer<Text, Text, Text, Text> {
		static MultipleOutputs<Text, Text> mos;

		@Override
		protected void setup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos = new MultipleOutputs<Text, Text>(context);
		}

		@Override
		protected void reduce(Text arg0, Iterable<Text> arg1,
				Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			for(Text val:arg1){
				if(arg0.toString().contains("爱奇艺")){
					mos.write(arg0,val,"aiqiyi");
				}else if(arg0.toString().contains("赶集网")){
					mos.write(arg0,val,"ganjiwang");
				}
			}
		}

		@Override
		protected void cleanup(Reducer<Text, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			mos.close();
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("error");
			System.exit(-1);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]),true);
		}
		Job job = new Job(new Configuration(),KeyWordTest.class.getSimpleName());
		job.setJarByClass(KeyWordTest.class);
		job.setMapperClass(KeyWordMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(KeyWordReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
