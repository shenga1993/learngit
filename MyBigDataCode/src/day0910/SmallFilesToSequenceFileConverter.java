package day0910;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class SmallFilesToSequenceFileConverter {

	public static class MergeMapper extends
			Mapper<NullWritable, BytesWritable, BytesWritable, NullWritable> {

		@Override
		public void map(
				NullWritable key,
				BytesWritable value,
				Mapper<NullWritable, BytesWritable, BytesWritable, NullWritable>.Context context)
				throws IOException, InterruptedException {
			context.write(value, NullWritable.get());
		}

	}

	public static class MergeReducer extends
			Reducer<BytesWritable, NullWritable, BytesWritable, NullWritable> {
		@Override
		public void reduce(
				BytesWritable arg0,
				Iterable<NullWritable> arg1,
				Reducer<BytesWritable, NullWritable, BytesWritable, NullWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			BytesWritable val = new BytesWritable();
			byte[] buf = arg0.copyBytes();
			val.set(buf, 0, buf.length);
			arg2.write(val, NullWritable.get());
		}
	}

	public static class WholeFileRecordReader extends
			RecordReader<NullWritable, BytesWritable> {
		private FileSplit fileSplit;
		private JobContext jobcontext;
		private boolean processed = false;
		private BytesWritable currentvalue;

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			this.fileSplit = (FileSplit) split;
			this.jobcontext = context;
			context.getConfiguration().set("map.input.file",
					fileSplit.getPath().getName());
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if (currentvalue == null) {
				currentvalue = new BytesWritable();
			}
			if (!processed) {
				int len = (int) fileSplit.getLength();
				byte[] contents = new byte[len];
				Path file = fileSplit.getPath();
				FileSystem fs = file.getFileSystem(jobcontext
						.getConfiguration());
				FSDataInputStream in = null;
				try {
					in = fs.open(file);
					IOUtils.readFully(in, contents, 0, len);
					currentvalue.set(contents, 0, len);

				} finally {
					IOUtils.closeStream(in);
				}
				processed = true;
				return true;
			}
			return false;
		}

		@Override
		public NullWritable getCurrentKey() throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			return NullWritable.get();
		}

		@Override
		public BytesWritable getCurrentValue() throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			return currentvalue;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return processed ? 1.0f : 0.0f;
		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub

		}

	}

	public static class WholeFileInputFormat extends
			FileInputFormat<NullWritable, BytesWritable> {

		@Override
		public boolean isSplitable(JobContext context, Path filename) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public RecordReader<NullWritable, BytesWritable> createRecordReader(
				InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return new WholeFileRecordReader();
		}

	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String input = null;
		String output = null;
		if (args.length != 2) {
			System.err.println("oh burden");
			System.exit(-1);
		}
		try {
			input = args[0];
			output = args[1];
			Job job = new Job(new Configuration(), "Merge Task");
			job.setJarByClass(SmallFilesToSequenceFileConverter.class);
			job.setMapperClass(MergeMapper.class);
			job.setReducerClass(MergeReducer.class);
			job.setMapOutputKeyClass(BytesWritable.class);
			job.setMapOutputValueClass(NullWritable.class);
			job.setOutputKeyClass(BytesWritable.class);
			job.setOutputValueClass(NullWritable.class);
			job.setInputFormatClass(WholeFileInputFormat.class);
			job.setOutputFormatClass(SequenceFileOutputFormat.class);
			FileInputFormat.addInputPath(job, new Path(input));
			FileOutputFormat.setOutputPath(job, new Path(output));
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
