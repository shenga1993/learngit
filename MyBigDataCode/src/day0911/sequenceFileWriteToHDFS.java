package day0911;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

public class sequenceFileWriteToHDFS {

	@SuppressWarnings({ "deprecation", "resource" })
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[1])));
		FileSystem fs = FileSystem.get(URI.create(args[0]), conf);
		SequenceFile.Writer writer = null;
		NullWritable key = NullWritable.get();
		Text value = new Text();
		String line = null;
		try {
			writer = new SequenceFile.Writer(fs, conf,new Path(args[0]),key.getClass(),
					value.getClass());
			while((line=in.readLine())!=null){
				value.set(line);
				writer.append(key, value);
			}
		} finally {
			IOUtils.closeStream(writer);
		}
	}
}
