package day0909;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DataClear {
	
	public static class WordFilterMapper extends Mapper<Object,Text,Text,IntWritable>{
		public final static IntWritable one = new IntWritable(1);
		public Text word = new Text();
		//setup
		/*
		 *key offset
		 *value line 
		 */
		public void map(Object key,Text val,Context context) throws IOException, InterruptedException{
			String line = val.toString();
			String[] arr = line.split(" ");
			for (String str:arr) {
				word.set(str);
				context.write(word,one);
			}
		}
		//cleanup
	}
	public class SumReduce extends Reducer<Text,IntWritable,Text,IntWritable> {
		public IntWritable result = new IntWritable();
		public void reduce(Text key,Iterable<IntWritable> val,Context context) throws IOException, InterruptedException{
			int sum = 0;
			for(IntWritable value:val){
				sum+=value.get();
			}
			result.set(sum);
			context.write(key,result);
		}
	}
	@SuppressWarnings("deprecation")
	public void main(String[] args) throws IOException{
		String input = null;
		String output = null;
		if(null!=args&&args.length==2){
			input = args[0];
			output = args[1];
			Job job = new Job(new Configuration(),"Data Clear");
			job.setJarByClass(DataClear.class);
			job.setMapperClass(WordFilterMapper.class);
			job.setReducerClass(SumReduce.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			FileInputFormat.addInputPath(job, new Path(input));
			FileOutputFormat.setOutputPath(job, new Path(output));
		}
	}
	
}
