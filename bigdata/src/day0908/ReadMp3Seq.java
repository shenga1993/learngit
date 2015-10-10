package day0908;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.zookeeper.common.IOUtils;

public class ReadMp3Seq {

	@SuppressWarnings({ "deprecation"})
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String uri = "hdfs://master:9000/abc.seq";
		FileOutputStream fout = new FileOutputStream(new File("/Users/Yson/Desktop/abc1.mp3"));
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		IntWritable key = new IntWritable();
		BytesWritable val = new BytesWritable();
		SequenceFile.Reader reader = null;
		try
		{
			reader = new SequenceFile.Reader(fs, new Path(uri), conf);
			while(reader.next(key, val)){
				byte[] buf = new byte[1024];
				val.readFields(new DataInputStream(new ByteArrayInputStream(buf)));
				fout.write(buf);
			}
		}
		finally
		{
			IOUtils.closeStream(fout);
			IOUtils.closeStream(reader);
		}
	}
}
