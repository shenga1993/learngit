package day0903;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class ListPath {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String uri = "hdfs://192.168.2.227:9000/";
		getPath(uri);
	}

	public static Path[] getPath(String uri) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		FileStatus[] status = fs.listStatus(new Path(uri));
		Path[] path = FileUtil.stat2Paths(status);
		for (Path p : path) {
			if (fs.isDirectory(p)) {
				getPath(p.toString());
				System.out.println("Dir" + "....." + p);
			} else {
				System.out.println("File" + "....." + p);
			}
		}
		return null;
	}
}
