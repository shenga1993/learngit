package org.ys.day0918;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;


public class AllFileToSequenceFileDemo {
	
	public static class MyRecordReader extends RecordReader<NullWritable, BytesWritable> {
		private FileSplit fileSplit;
		private JobContext jobContext;
		private boolean tag = false;
		private BytesWritable mybw ;

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			this.fileSplit=(FileSplit)split;
			this.jobContext = context;
			context.getConfiguration().set("map.input.file", fileSplit.getPath().getName());
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(mybw==null){
				mybw = new BytesWritable();
			}
			if(!tag){
				int len = (int) fileSplit.getLength();
				byte [] contents = new byte[len];
				Path file = fileSplit.getPath();
				FileSystem fs = file.getFileSystem(jobContext.getConfiguration());
				FSDataInputStream in = null;
				try{
					in = fs.open(file);
					IOUtils.readFully(in, contents, 0, len);
					mybw.set(contents, 0, len);
				}finally{
					IOUtils.closeStream(in);
				}
				tag=true;
				return true;
			}
			return false;
		}

		@Override
		public NullWritable getCurrentKey() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return NullWritable.get();
		}

		@Override
		public BytesWritable getCurrentValue() throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			return mybw;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return tag?1:0;
		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			
		}
		
	}

	public static class MyInputFormat extends FileInputFormat<NullWritable, BytesWritable> {

		@Override
		public RecordReader<NullWritable,BytesWritable> createRecordReader(InputSplit split,
				TaskAttemptContext context) throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			return new MyRecordReader();
		}
		
		@Override
		protected boolean isSplitable(JobContext context, Path filename) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public static class MyMapper extends
			Mapper<NullWritable,BytesWritable,BytesWritable, NullWritable> {
		Text t = new Text();
		@Override
		protected void map(NullWritable key, BytesWritable value,
				Mapper<NullWritable, BytesWritable, BytesWritable, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
//			byte [] buf = value.copyBytes();
//			t.set(buf, 0, buf.length);
			context.write(value, NullWritable.get());
		}
	}
	
	public static class MyReducer extends
			Reducer<BytesWritable, NullWritable, Text, Text> {
		private final static Text val = new Text("");
		Text tmp = new Text();
		@Override
		protected void reduce(BytesWritable arg0, Iterable<NullWritable> arg1,
				Reducer<BytesWritable, NullWritable, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			byte[] buf = arg0.copyBytes();
			tmp.set(buf, 0, buf.length);
			arg2.write(tmp, val);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		if(args.length!=2){
			System.err.println("error-------------------------------------");
			System.exit(0);
		}
		FileSystem fs = FileSystem.get(new Configuration());
		if(fs.exists(new Path(args[1]))){
			fs.delete(new Path(args[1]),true);
		}
		Job job = new Job(new Configuration(),AllFileToSequenceFileDemo.class.getSimpleName());
		job.setJarByClass(AllFileToSequenceFileDemo.class);
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(BytesWritable.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		job.setInputFormatClass(MyInputFormat.class);
		MyInputFormat.addInputPath(job, new Path(args[0]));
		SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
