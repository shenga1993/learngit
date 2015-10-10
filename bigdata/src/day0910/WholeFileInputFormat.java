package day0910;


import java.io.IOException;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class WholeFileInputFormat extends FileInputFormat<NullWritable, BytesWritable> {


	@Override
	protected boolean isSplitable(JobContext context, Path filename) {
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RecordReader createRecordReader(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		return new WholeFileRecordReader();
	}

	public static class WholeFileRecordReader extends RecordReader<NullWritable, BytesWritable> {
		private FileSplit fs;
		private boolean flag = false;
		private BytesWritable value;
		private JobContext jc;

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
			fs = (FileSplit) split;
			jc = context;
			context.getConfiguration().set("map.input.file", fs.getPath().getName());
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			if (value == null) {
				value = new BytesWritable();
			}
			if (!flag) {
				byte[] contents = new byte[(int) fs.getLength()];
				Path file = fs.getPath();
				FileSystem fs = file.getFileSystem(jc.getConfiguration());
				FSDataInputStream in = null;
				try {
					in = fs.open(file);
					IOUtils.readFully(in, contents, 0, contents.length);
					value.set(contents, 0, contents.length);
				} finally {
					IOUtils.closeStream(in);
				}
				flag = true;
				return true;
			}
			return false;

		}

		@Override
		public NullWritable getCurrentKey() throws IOException, InterruptedException {
			return NullWritable.get();
		}

		@Override
		public BytesWritable getCurrentValue() throws IOException, InterruptedException {
			return value;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return flag ? 1 : 0;
		}

		@Override
		public void close() throws IOException {
		}

	}
}
