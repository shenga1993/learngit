package org.ys.day0917;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class GetUser {
	
	public static class UserMapper extends
			Mapper<Object, Text, Text, Text> {
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			Text tuid = new Text();
			Text tName = new Text();
			if(data.length==3){
				String uid = data[0];
				String name = data[1];
				tuid.set(uid);
				tName.set(name+1);
				context.write(tuid, tName);
			}
		}
	}
	
	public static class LoginMapper extends
			Mapper<Object, Text, Text, Text> {
		HashMap<String,String> hm = new HashMap<>();
		BufferedReader br ;
		@Override
		protected void setup(Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			br = new BufferedReader(new FileReader(new File("")));
			String line = null;
			while(null!=(line=br.readLine())){
				String []data = line.split("\t");
				String sexid = data[0];
				String sexname = data[1];
				hm.put(sexid, sexname);
			}
		}
		@Override
		protected void map(Object key, Text value,
				Mapper<Object, Text, Text, Text>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			String line = value.toString();
			String[] data = line.split("\t");
			Text tuid = new Text();
			Text tsex = new Text();
			if(data.length==3){
				String uid = data[0];
				String sex = data[1];
				String sexname = hm.get(sex);
				tuid.set(uid);
				tsex.set(sexname+2);
				context.write(tuid,tsex);
			}
		}
	}

	public static class MyReducer extends
			Reducer<Text, Text, Text, Text> {
		
		@Override
		protected void reduce(Text arg0, Iterable<Text> arg1,
				Reducer<Text, Text, Text, Text>.Context arg2)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			//用户登录表
			ArrayList<String> loginTable = new ArrayList<>();
			Iterator<Text> val = arg1.iterator();
			String userName = null;
			while(val.hasNext()){
				String str = val.next().toString();
				if(str.endsWith("1")){
					userName = str.substring(0, str.length()-1);
				}else if(str.endsWith("2")){
					loginTable.add(str.substring(0,str.length()-1));
				}
			}
			//登陆次数
			if(!userName.isEmpty()){
				int count = loginTable.size();
				Text key = new Text();
				Text value = new Text();
				key.set(userName);
				value.set(loginTable.get(0)+"\t"+count);
				arg2.write(key, value);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Job job = new Job(new Configuration(),GetUser.class.getSimpleName());
		job.setJarByClass(GetUser.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, UserMapper.class);
		MultipleInputs.addInputPath(job, new Path(args[2]), TextInputFormat.class, LoginMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.waitForCompletion(true);
	}
}
