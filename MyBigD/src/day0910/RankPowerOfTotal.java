package day0910;

import java.io.IOException;
import java.util.HashSet;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RankPowerOfTotal {
	public static class CollectMapper extends
			Mapper<Object, Text, IntWritable, IntWritable> {
		private static final IntWritable one = new IntWritable(1);
		IntWritable k = new IntWritable();
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, IntWritable, IntWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] vals = line.split("\t");
			if (vals.length == 6) {
				int rank = Integer.parseInt(vals[3]);
				k.set(rank);
				context.write(one,k);
			}
		}
	}

	public static class CollectReducer extends
			Reducer<IntWritable, IntWritable, IntWritable,DoubleWritable> {
		TreeMap<Integer,Integer> tm = new TreeMap<Integer,Integer>();
		HashSet<Integer> hs = new HashSet<>(); 
		@Override
		protected void reduce(
				IntWritable key,
				Iterable<IntWritable> val,
				Reducer<IntWritable, IntWritable, IntWritable, DoubleWritable>.Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			// TODO Auto-generated method stub
			for(IntWritable value:val){
				// key    sum
				sum++;
				if(hs.add(value.get())){
					tm.put(value.get(),key.get());
				}
				tm.put(value.get(),tm.get(value.get())+1);
			}
			for(int i=1;i<=10;i++){
				context.write(new IntWritable(i),new DoubleWritable((double)tm.get(i)/(double)sum));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String input = null;
		String output = null;
		if(args.length!=2){
			System.err.println("heheheheh");
			System.exit(-1);
		}
		input = args[0];
		output = args[1];
		try {
			Job job = new Job(new Configuration(),"YeSheng Task");
			job.setJarByClass(RankPowerOfTotal.class);
			job.setMapperClass(CollectMapper.class);
			job.setReducerClass(CollectReducer.class);
			job.setMapOutputKeyClass(IntWritable.class);
			job.setMapOutputValueClass(IntWritable.class);
			job.setOutputKeyClass(IntWritable.class);
			job.setOutputValueClass(DoubleWritable.class);
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
