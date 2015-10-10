package day0908;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.apache.zookeeper.common.IOUtils;

public class MapFileReadDemo {
	@SuppressWarnings("deprecation")
	public static void main(String[]args) throws IOException{
		String uri = args[0];
		Configuration conf = new Configuration();;
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		@SuppressWarnings("unused")
		Path path = new Path(uri);
		IntWritable key = new IntWritable();
		Text value = new Text();
		MapFile.Reader reader = null;
		try
		{
			reader = new MapFile.Reader(fs, uri, conf);
			
			while(reader.next(key, value)){
				System.out.println(key+"\t"+value);
			}
		}
		finally
		{
			IOUtils.closeStream(reader);
		}
	}
}
