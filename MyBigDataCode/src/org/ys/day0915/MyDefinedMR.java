package org.ys.day0915;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MyDefinedMR {

	public static class MyReducer extends
			Reducer<Text, MyWritable, Text, MyWritable> {
		MyWritable value = new MyWritable();

		@Override
		protected void reduce(Text arg0, Iterable<MyWritable> arg1,
				Reducer<Text, MyWritable, Text, MyWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			int a = 0;
			int b = 0;
			int c = 0;
			for (MyWritable val : arg1) {
				a += val.testMessage;
				b += val.traffic;
				c += val.callTime;
			}
			value.set(a, b, c);
			arg2.write(arg0, value);
		}
	}

	public static class MyWritable implements Writable {
		private int testMessage;
		private int traffic;
		private int callTime;

		private MyWritable() {
		}

		private MyWritable(int testMessage, int traffic, int callTime) {
			super();
			set(testMessage, traffic, callTime);
		}

		void set(int testMessage, int traffic, int callTime) {
			this.testMessage = testMessage;
			this.traffic = traffic;
			this.callTime = callTime;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			// TODO Auto-generated method stub
			out.writeInt(traffic);
			out.writeInt(testMessage);
			out.writeInt(callTime);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			this.testMessage = in.readInt();
			this.traffic = in.readInt();
			this.callTime = in.readInt();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.testMessage + "\t" + this.traffic + "\t"
					+ this.callTime;
		}

	}

	public static class MyMapper extends Mapper<Object, Text, Text, MyWritable> {
		MyWritable val = new MyWritable();
		Text tkey = new Text();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, MyWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 4) {
				int testMessage = Integer.parseInt(data[1]);
				int traffic = Integer.parseInt(data[2]);
				int callTime = Integer.parseInt(data[3]);
				val.set(testMessage, traffic, callTime);
				tkey.set(data[0]);
				context.write(tkey, val);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("error--------------------------------");
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if (fs.exists(new Path(args[1]))) {
			fs.delete(new Path(args[1]), true);
		}
		Job job = new Job(new Configuration(),
				MyDefinedMR.class.getSimpleName());
		job.setJarByClass(MyDefinedMR.class);
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(MyWritable.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MyWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
