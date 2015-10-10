package org.ys.day0916;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class ShuffleDemo {


	public static class MyWritable implements WritableComparable<Object> {
		
		private IntWritable first;
		private IntWritable second;
		MyWritable(){set(new IntWritable(),new IntWritable());};
		MyWritable(IntWritable first,IntWritable second){
			set(first,second);
		}
		void set(IntWritable first,IntWritable second){
			this.first=first;
			this.second=second;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			// TODO Auto-generated method stub
			first.write(out);
			second.write(out);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			first.readFields(in);
			second.readFields(in);
		}

		@Override
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			if(o instanceof MyWritable){
				MyWritable my = (MyWritable) o;
				if(this.first.compareTo(my.first)!=0){
					return this.first.compareTo(my.first);
				}
				return -this.second.compareTo(my.second);
			}
			return 0;
		}
		public String toString(){
			return this.first.get()+"\t"+this.second.get();
		}
	}

	public static class SMapper extends
			Mapper<Object, Text, MyWritable, NullWritable> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, MyWritable, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			IntWritable first = new IntWritable(Integer.parseInt(data[0]));
			IntWritable second = new IntWritable(Integer.parseInt(data[1]));
			MyWritable mKey = new MyWritable(first,second);
			context.write(mKey, NullWritable.get());
		}
	}

	public static class SReducer extends
			Reducer<MyWritable, NullWritable, MyWritable, NullWritable> {
		@Override
		protected void reduce(
				MyWritable arg0,
				Iterable<NullWritable> arg1,
				Reducer<MyWritable, NullWritable, MyWritable, NullWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			arg2.write(arg0, NullWritable.get());
		}
	}
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("error-----------------");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]), true);
		}
		Job job = new Job(new Configuration(),ShuffleDemo.class.getSimpleName());
		job.setJarByClass(ShuffleDemo.class);
		job.setMapperClass(SMapper.class);
		job.setMapOutputKeyClass(MyWritable.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setReducerClass(SReducer.class);
		job.setOutputKeyClass(MyWritable.class);
		job.setOutputValueClass(NullWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
	
}
