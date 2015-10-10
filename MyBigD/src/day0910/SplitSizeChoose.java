package day0910;


import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import day0910.SmallFilesToSequenceFileConverter.MergeMapper;
import day0910.SmallFilesToSequenceFileConverter.MergeReducer;
import day0910.SmallFilesToSequenceFileConverter.WholeFileInputFormat;


public class SplitSizeChoose {

	// @Override
	// public int run(String[] args) throws Exception {
	// if (args.length != 2) {
	// System.err.printf("Usage: %s [generic options] <input> <output>\n");
	// getClass().getSimpleName();
	// ToolRunner.printGenericCommandUsage(System.err);
	// return -1;
	// }
	// Job job = new Job(getConf());
	// job.setJarByClass(SplitSizeChoose.class);
	// job.setMapOutputKeyClass(theClass);
	// job.setMapOutputValueClass(theClass);
	// job.setOutputFormatClass(cls);
	// job.setInputFormatClass(cls);
	// int chooseCode = Integer.parseInt(args[2]);
	// 1表示更改split大小
	// if (chooseCode == 1) {
	// FileInputFormat.setMaxInputSplitSize(job, 50 * 1024 * 1024);
	// }
	// return 0;
	// }
	public static class MyMap extends Mapper<NullWritable, BytesWritable, BytesWritable, NullWritable> {
		BytesWritable bw = new BytesWritable();

		@Override
		protected void map(NullWritable key, BytesWritable value,
				Mapper<NullWritable, BytesWritable, BytesWritable, NullWritable>.Context context)
						throws IOException, InterruptedException {
			bw.set(value);
			context.write(bw, NullWritable.get());
		}

	}

	public static class MyReduce extends Reducer<BytesWritable, NullWritable, Text, NullWritable> {

		@Override
		protected void reduce(BytesWritable arg0, Iterable<NullWritable> arg1,
				Reducer<BytesWritable, NullWritable, Text, NullWritable>.Context arg2)
						throws IOException, InterruptedException {
			byte[] buf = arg0.copyBytes();
			Text tx = new Text(buf);
			arg2.write(tx, NullWritable.get());
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
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(NullWritable.class);
			job.setInputFormatClass(WholeFileInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);
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

