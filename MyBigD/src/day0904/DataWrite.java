package day0904;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class DataWrite {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String uri = "hdfs://192.168.25.141:9000/500w";
		String localSrc = "sogou.500w.utf8";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
		FSDataOutputStream out = fs.create(new Path(uri), new Progressable() {
			@Override
			public void progress() {
				// TODO Auto-generated method stub
				System.out.println("u stupid");
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
