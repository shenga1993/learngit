package day0911;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import day0911.SequenceFileMRDemo.SFMapper;

public class MultipleInputsTest {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		Job job = new Job(new Configuration(),
				MultipleInputsTest.class.getSimpleName());
		MultipleInputs.addInputPath(job, new Path(args[0]),TextInputFormat.class,Mapper.class);
		MultipleInputs.addInputPath(job, new Path(args[1]),TextInputFormat.class,SFMapper.class);
		FileInputFormat.addInputPath(job, new Path(args[2]));
	}
}
