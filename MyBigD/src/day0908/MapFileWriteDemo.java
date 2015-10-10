package day0908;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.zookeeper.common.IOUtils;

public class MapFileWriteDemo {
	private static final String[] DATA = {"one","two","three","four","five","six","seven"};
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException{
		String uri = "hdfs://master:9000/demo";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		IntWritable key = new IntWritable();
		Text value = new Text();
		MapFile.Writer writer = null;
		try
		{
			writer = new MapFile.Writer(conf,fs,uri,key.getClass(),value.getClass());
			for(int i=0;i<1024;i++){
				key.set(i+1);
				value.set(DATA[i%DATA.length]);
				writer.append(key, value);
			}
		}
		finally
		{
			IOUtils.closeStream(writer);
		}
	}
}
