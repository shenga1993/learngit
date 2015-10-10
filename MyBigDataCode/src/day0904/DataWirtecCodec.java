package day0904;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.util.Progressable;
import org.apache.hadoop.util.ReflectionUtils;

public class DataWirtecCodec {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String uri = args[1];
		String codecName = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		InputStream in = new BufferedInputStream(new FileInputStream(
				"sogou.500w.utf8"));
		Class<?> codecClassName = Class.forName(codecName);
		CompressionCodec codec = (CompressionCodec) ReflectionUtils
				.newInstance(codecClassName, conf);
		OutputStream out = codec.createOutputStream(fs.create(new Path(uri),
				new Progressable() {
					@Override
					public void progress() {
						// TODO Auto-generated method stub
						System.out.println("李康是sb");
					}
				}));
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
