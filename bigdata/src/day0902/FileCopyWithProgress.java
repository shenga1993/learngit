package day0902;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class FileCopyWithProgress {
	static FileSystem fs;
	static double count = 1;
	static double s;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String localSrc = "/home/sheng/Desktop/50M.file";
		String dst = "hdfs://192.168.2.227:9000/50M2.file";
		InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
		Configuration conf = new Configuration();
		fs = FileSystem.get(URI.create(dst), conf);
		OutputStream out = fs.create(new Path(dst), new Progressable() {
			public void progress() {
				s = count * 64;
				count++;
				double b = (s * 100) / (50 * 1024);
				HashSet<Double> hs = new HashSet<Double>();
				if (hs.add(b)) {
					System.out.println(b + "%.....................");
				}
				System.out.println(count);
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
