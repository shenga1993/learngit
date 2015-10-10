package day0911;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.google.common.primitives.Bytes;

public class SequenceFileMRDemo {
	public static class SFMapper extends
			Mapper<Object,BytesWritable, Object, BytesWritable> {
		@Override
		protected void map(
				Object key,
				BytesWritable value,
				Mapper<Object, BytesWritable, Object, BytesWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			super.map(key, value, context);
		}
	}	
	public static class SFReducer extends
			Reducer<Object, BytesWritable, Object, BytesWritable> {
		@Override
		protected void reduce(
				Object arg0,
				Iterable<BytesWritable> arg1,
				Reducer<Object, BytesWritable, Object, BytesWritable>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			super.reduce(arg0, arg1, arg2);
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Job job = new Job(new Configuration(),"seq test");
		job.setJarByClass(SequenceFileMRDemo.class);
		job.setMapperClass(SFMapper.class);
		job.setReducerClass(SFReducer.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(BytesWritable.class);
		job.setOutputValueClass(BytesWritable.class);
		job.setOutputValueClass(Bytes.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
	}
}
