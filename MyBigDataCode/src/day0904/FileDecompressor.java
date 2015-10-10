package day0904;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

public class FileDecompressor {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String codec = args[0];
		Path path = new Path(codec);
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(codec), conf);
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		CompressionCodec ccodec = factory.getCodec(path);
		if (ccodec == null) {
			System.out.println("It's not a codec" + codec);
			System.exit(1);
		}
		String outputuri = CompressionCodecFactory.removeSuffix(codec,
				ccodec.getDefaultExtension());
		InputStream in = null;
		OutputStream out = null;
		try {
			in = ccodec.createInputStream(fs.open(path));
			out = fs.create(new Path(outputuri));
			IOUtils.copyBytes(in, out, conf);
		} finally {
			IOUtils.closeStream(out);
			IOUtils.closeStream(in);
		}
	}

}
