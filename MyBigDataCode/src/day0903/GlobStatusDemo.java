package day0903;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class GlobStatusDemo {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(
				URI.create("hdfs://192.168.2.227:9000/"), conf);
		FileStatus[] status = fs.globStatus(new Path("/*/*/*"),
				new PathFilter() {
					@Override
					public boolean accept(Path path) {
						// TODO Auto-generated method stub
						String regex = "/*/*/*";
						return !path.toString().matches(regex);
					}
				});
		Path[] path = FileUtil.stat2Paths(status);
		for (Path p : path) {
			System.out.println(p);
		}
	}

}
