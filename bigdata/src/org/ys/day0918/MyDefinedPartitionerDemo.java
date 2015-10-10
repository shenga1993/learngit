package org.ys.day0918;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

public class MyDefinedPartitionerDemo {

	public static class MyDefinedRecordWriter extends
			RecordWriter<KeyWritable, MyWritable> {

		private static final String utf8 = "UTF-8";
		private static final byte[] newline;
		static {
			try {
				newline = "\n".getBytes(utf8);
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("can't find " + utf8
						+ " encoding");
			}
		}

		protected DataOutputStream out;
		private final byte[] keyValueSeparator;

		public MyDefinedRecordWriter(DataOutputStream out,
				String keyValueSeparator) {
			this.out = out;
			try {
				this.keyValueSeparator = keyValueSeparator.getBytes(utf8);
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("can't find " + utf8
						+ " encoding");
			}
		}

		public MyDefinedRecordWriter(DataOutputStream out) {
			this(out, "\n\t");
		}

		private void writeKey(Object o) throws IOException {
			if (o instanceof KeyWritable) {
				KeyWritable to = (KeyWritable) o;
				out.write(to.toString().getBytes(), 0, to.toString().length());
			} else {
				out.write(o.toString().getBytes(utf8));
			}
		}

		private void writeValue(Object o) throws IOException {
			if (o instanceof MyWritable) {
				MyWritable to = (MyWritable) o;
				out.write(to.toString().getBytes(), 0, to.toString().length());
			} else {
				out.write(o.toString().getBytes(utf8));
			}
		}

		public synchronized void write(KeyWritable key, MyWritable value)
				throws IOException {

			boolean nullKey = key == null;
			boolean nullValue = value == null;
			if (nullKey && nullValue) {
				return;
			}
			if (!nullKey) {
				writeKey(key);
			}
			if (!(nullKey || nullValue)) {
				out.write(keyValueSeparator);
			}
			if (!nullValue) {
				writeValue(value);
			}
			out.write(newline);
		}

		// @Override
		// public void write(KeyWritable key, MyWritable value)
		// throws IOException, InterruptedException {
		// // TODO Auto-generated method stub
		//
		// }

		public synchronized void close(TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			out.close();
		}

	}

	public static class MyOutputFormat extends
			FileOutputFormat<KeyWritable, MyWritable> {
		public static String SEPERATOR = "mapreduce.output.textoutputformat.separator";

		@Override
		public RecordWriter<KeyWritable, MyWritable> getRecordWriter(
				TaskAttemptContext job) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			Configuration conf = job.getConfiguration();
			boolean isCompressed = getCompressOutput(job);
			CompressionCodec codec = null;
			String extension = "";
			if (isCompressed) {
				Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(
						job, GzipCodec.class);
				codec = (CompressionCodec) ReflectionUtils.newInstance(
						codecClass, conf);
				extension = codec.getDefaultExtension();
			}
			Path file = getDefaultWorkFile(job, extension);
			FileSystem fs = file.getFileSystem(conf);
			if (!isCompressed) {
				FSDataOutputStream fileOut = fs.create(file, false);
				return new MyDefinedRecordWriter(fileOut);
			} else {
				FSDataOutputStream fileOut = fs.create(file, false);
				return new MyDefinedRecordWriter(new DataOutputStream(
						codec.createOutputStream(fileOut)));
			}
		}

	}

	public static class KeyWritable implements WritableComparable<KeyWritable> {
		private Text keyword;
		private MyWritable mw;

		public KeyWritable() {
			// TODO Auto-generated constructor stub
			set(new Text(), new MyWritable());
		}

		public KeyWritable(Text keyword, MyWritable mw) {
			set(keyword, mw);
		}

		private void set(Text keyword, MyWritable mw) {
			// TODO Auto-generated method stub
			this.keyword = keyword;
			this.mw = mw;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			// TODO Auto-generated method stub
			keyword.write(out);
			mw.write(out);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			keyword.readFields(in);
			mw.readFields(in);
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return keyword.toString() + "\t" + mw.toString();
		}

		@Override
		public int compareTo(KeyWritable o) {
			// TODO Auto-generated method stub
			if (o.keyword.toString().hashCode() != this.keyword.toString()
					.hashCode()) {
				return o.keyword.toString().hashCode()
						- this.keyword.toString().hashCode();
			} else if (o.mw.compareTo(this.mw) != 0) {
				return o.mw.compareTo(this.mw);
			}
			return 0;
		}

	}

	public static class MyWritable implements WritableComparable<MyWritable> {
		private Text uid;
		private Text url;

		public MyWritable() {
			// TODO Auto-generated constructor stub
			set(new Text(), new Text());
		}

		public MyWritable(Text uid, Text url) {
			this.uid = uid;
			this.url = url;
		}

		public void set(Text uid, Text url) {
			// TODO Auto-generated method stub
			this.uid = uid;
			this.url = url;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			// TODO Auto-generated method stub
			uid.write(out);
			url.write(out);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			// TODO Auto-generated method stub
			uid.readFields(in);
			url.readFields(in);
		}

		public Text getUid() {
			return uid;
		}

		public Text getUrl() {
			return url;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.uid.toString() + "," + this.url.toString();
		}

		@Override
		public int compareTo(MyWritable o) {
			// TODO Auto-generated method stub
			String s1 = o.getUid().toString();
			String s2 = o.getUrl().toString();
			if (s1.hashCode() != this.getUid().toString().hashCode()) {
				return s1.hashCode() - this.getUid().toString().hashCode();
			} else if (s2.hashCode() != this.getUrl().toString().hashCode()) {
				return s2.hashCode() - this.getUrl().toString().hashCode();
			}
			return 0;
		}

	}

	public static class MyMapper extends
			Mapper<Object, Text, KeyWritable, MyWritable> {
		private Text tkey = new Text();
		private Text t1 = new Text();
		private Text t2 = new Text();
		private MyWritable mw = new MyWritable();
		private KeyWritable kw = new KeyWritable();

		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, KeyWritable, MyWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 6) {
				String keyword = data[2];
				String uid = data[1];
				String url = data[5];
				tkey.set(keyword);
				t1.set(uid);
				t2.set(url);
				mw.set(t1, t2);
				kw.set(tkey, mw);
				context.write(kw, mw);
			}
		}
	}

	public static class MyReducer extends
			Reducer<KeyWritable, MyWritable, KeyWritable, MyWritable> {
		@Override
		protected void reduce(
				KeyWritable arg0,
				Iterable<MyWritable> arg1,
				Reducer<KeyWritable, MyWritable, KeyWritable, MyWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			for (MyWritable mw : arg1) {
				arg2.write(arg0, mw);
			}
		}
	}

	public static class MyPartitioner extends
			Partitioner<KeyWritable, MyWritable> {

		@Override
		public int getPartition(KeyWritable key, MyWritable value,
				int numPartitions) {
			// TODO Auto-generated method stub
			if (key.keyword.toString().contains("爱奇艺")) {
				return 1%numPartitions;
			} else if (key.keyword.toString().contains("百度")) {
				return 2%numPartitions;
			} else if (key.keyword.toString().contains("赶集网")) {
				return 3%numPartitions;
			} else {
				return 4%numPartitions;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		try {
			FileSystem fs = FileSystem.get(conf);
			if (fs.exists(new Path(args[1]))) {
				fs.delete(new Path(args[1]), true);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Job job = new Job(conf,
					MyDefinedPartitionerDemo.class.getSimpleName());
			job.setJarByClass(MyDefinedPartitionerDemo.class);
			job.setMapperClass(MyMapper.class);
			job.setMapOutputKeyClass(KeyWritable.class);
			job.setMapOutputValueClass(MyWritable.class);
			job.setReducerClass(MyReducer.class);
			job.setOutputKeyClass(KeyWritable.class);
			job.setNumReduceTasks(4);
			job.setOutputValueClass(MyWritable.class);
			job.setOutputFormatClass(MyOutputFormat.class);
			MyOutputFormat.setOutputPath(job, new Path(args[1]));
			job.setInputFormatClass(TextInputFormat.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
			job.setPartitionerClass(MyPartitioner.class);
			try {
				job.waitForCompletion(true);
			} catch (ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
