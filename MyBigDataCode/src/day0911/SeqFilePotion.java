package day0911;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class SeqFilePotion {

	public static class SeqFileReduce extends
			Reducer<Text, NullWritable, Text,NullWritable> {
		@Override
		protected void reduce(Text arg0, Iterable<NullWritable> arg1,
				Reducer<Text, NullWritable, Text, NullWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			arg2.write(arg0, NullWritable.get());
		}
	}

	public static class SeqFileMapper extends
			Mapper<NullWritable, Text,Text, NullWritable> {
		@Override
		protected void map(NullWritable key, Text value,
				Mapper<NullWritable, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			Text val = new Text();
			String line = value.toString();
			String[] data = line.split("\t");
			if (data.length == 6) {
				String uid = data[1];
				val.set(uid);
				context.write(val, NullWritable.get());
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Job job = new Job(new Configuration(),
				SeqFileMapper.class.getSimpleName());
		job.setJarByClass(SeqFileMapper.class);
		job.setMapperClass(SeqFileMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setReducerClass(SeqFileReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
		SequenceFileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
