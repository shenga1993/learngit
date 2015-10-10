package day0908;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.zookeeper.common.IOUtils;

public class Mp3SequenceFileDemo {

	@SuppressWarnings({ "deprecation", "resource" })
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String localSrc = "/Users/Yson/Desktop/abc.mp3";
		String uri = "hdfs://master:9000/abc.seq";
		InputStream in = new FileInputStream(localSrc);
		Path path = new Path(uri);
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		IntWritable key = new IntWritable();
		BytesWritable val = new BytesWritable();
		SequenceFile.Writer writer = null;
		byte[] buf = new byte[1024];
		int a = 0;
		try
		{
			writer = new SequenceFile.Writer(fs, conf,path,key.getClass(),val.getClass());
			while((a=in.read(buf))!=-1){
				key.set(a);
				val.set(buf, 0, buf.length);
				writer.append(key, val);
			}
		}
		finally
		{
			IOUtils.closeStream(writer);
		}
	}
}
