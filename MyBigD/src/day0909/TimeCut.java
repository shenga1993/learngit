package day0909;

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
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class TimeCut {
	public static class WholeFileRecordReader extends RecordReader<NullWritable,BytesWritable>{
		private FileSplit fileSplit;
		private Configuration conf;
		private boolean processed=false;
		public WholeFileRecordReader(FileSplit fileSplit,Configuration conf) throws IOException{
			// TODO Auto-generated constructor stub
			this.fileSplit = fileSplit;
			this.conf = conf;
		}
		@Override
		public void initialize(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			if(!processed){
				byte[] contents = new byte[(int) fileSplit.getLength()];
				Path file = fileSplit.getPath();
				FileSystem fs = file.getFileSystem(conf);
				FSDataInputStream in = null;
				try
				{
					in = fs.open(file);
					IOUtils.readFully(in, contents, 0,contents.length);
					
				}
				finally
				{
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
			return new BytesWritable();
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

	public static class WholeFileInputFormat extends FileInputFormat<NullWritable,BytesWritable> {

		@Override
		protected boolean isSplitable(JobContext context, Path filename) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public RecordReader<NullWritable, BytesWritable> createRecordReader(
				InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return new WholeFileRecordReader((FileSplit) split, conf);
		}
		
	}

	public static class NonSplittableTextInputFormat extends TextInputFormat {
		@Override
		protected boolean isSplitable(JobContext context, Path file) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	// 20111230000005 57375476989eea12893c0c3811607bcf 奇艺高清 1 1
	// http://www.qiyi.com/
	static Configuration conf = new Configuration();

	public static class CutMapper extends Mapper<Object, Text, Text, Text> {
		Text old = new Text();
		Text news = new Text();

		@Override
		protected void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] arr = line.split("\t");
			if (arr == null || arr[0].length() != 14) {
				System.exit(2);
			}
			String time = arr[0];
			String[] times = { "年：" + arr[0].substring(0, 4),
					"月：" + arr[0].substring(4, 6),
					"日：" + arr[0].substring(6, 8),
					"小时：" + arr[0].substring(8, 10) };
			time = "";
			for (String str : times) {
				time += ("\t" + str);
			}
			old.set(line);
			news.set(time);
			context.write(old, news);
		}

	}

	public static class CutReducer extends Reducer<Text, Text, Text, Text> {
		Text t = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> val, Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String time = "";
			for (Text value : val) {
				time += value.toString();
			}
			t.set(time);
			context.write(key, t);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String input = null;
		String output = null;
		/*
		 * 0 default 1 change split size
		 */
		int isChangeSplitSize = 0;
		isChangeSplitSize = Integer.parseInt(args[2]);
		if (args.length == 3) {
			input = args[0];
			output = args[1];

			try {
				Job job = new Job(conf, "New Text");
				job.setJarByClass(TimeCut.class);
				job.setMapperClass(CutMapper.class);
				job.setReducerClass(CutReducer.class);
				job.setOutputKeyClass(Text.class);
				job.setOutputValueClass(Text.class);
//				FileInputFormat.addInputPath(job, new Path(input));
//				job.setInputFormatClass(NonSplittableTextInputFormat.class);
//				job.setOutputFormatClass(TextOutputFormat.class);
//				job.setInputFormatClass(cls);
				NonSplittableTextInputFormat.addInputPath(job,new Path(input));
				FileOutputFormat.setOutputPath(job, new Path(output));
				if (isChangeSplitSize == 1) {
					FileInputFormat.setMaxInputSplitSize(job, 50 * 1024 * 1024);
				}
				try {
					job.waitForCompletion(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
