package day0903;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

public class CreateDir {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Path[] path = new Path[] {
				new Path(
						"hdfs://192.168.2.227:9000/Sheng/0903/oooopg/99999.txt"),
				new Path(
						"hdfs://192.168.2.227:9000/Sheng/0903/123/999/jdk2222.txt"),
				new Path("hdfs://192.168.2.227:9000/sheng/0903/124/9999.file"),
				new Path(
						"hdfs://192.168.2.227:9000/sheng/0903/125/199/000.file") };
		Configuration conf = new Configuration();
		for (int i = 0; i < path.length; i++) {
			FileSystem.create(
					FileSystem.get(URI.create(path[i].toString()), conf),
					path[i], FsPermission.getDirDefault());
		}
	}
}
