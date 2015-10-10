package day0907;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CodecPool;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.util.ReflectionUtils;

public class PoolStreamCompressor {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String codecClassName = args[0];
		String uri = args[1];
//		Configuration conf = new Configuration();
//		FileSystem fs = FileSystem.get(URI.create(uri), conf);
//		FSDataInputStream in = fs.open(new Path(uri));
//		Class<?> codecClass = Class.forName(codecClassName);
//		CompressionCodec codec = (CompressionCodec) ReflectionUtils
//				.newInstance(codecClass, conf);
//		OutputStream out = fs
//				.create(new Path(uri + codec.getDefaultExtension()));
//		Compressor compressor = null;
//		CompressionOutputStream cpout = null;
//		try {
//			compressor = CodecPool.getCompressor(codec);
//			cpout = codec.createOutputStream(out, compressor);
//			IOUtils.copyBytes(in, cpout, 4096, false);
//		} finally {
//			IOUtils.closeStream(in);
//			IOUtils.closeStream(cpout);
//			CodecPool.returnCompressor(compressor);
//		}
		Configuration conf = new Configuration();
		InputStream in = createInputStream(uri,conf);
		CompressionCodec codec = getCodec(codecClassName,conf);
		OutputStream out = createOutputStream(uri,codec);
		Compressor compressor = getCompressor(codec);
		CompressionOutputStream cpout = getCompressionOutputStream(codec,out,compressor);
		IOUtils.copyBytes(in,cpout,4096,true);
		CodecPool.returnCompressor(compressor);
	}
	public static InputStream createInputStream(String uri,Configuration conf) throws IOException{
		FileSystem fs = FileSystem.get(URI.create(uri),conf);
		InputStream in = fs.open(new Path(uri));
		return in;
	}
	public static InputStream createInputStream(String path) throws FileNotFoundException{
		InputStream in = new FileInputStream(new File(path));
		return in;
	}
	public static CompressionCodec getCodec(String codecClassName,Configuration conf) throws ClassNotFoundException{
		Class<?> codecClass = Class.forName(codecClassName);
		CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
		return codec;
	}
	public static OutputStream createOutputStream(String uri,FileSystem fs,CompressionCodec codec) throws IOException{
		return fs.create(new Path(uri+codec.getDefaultExtension()));
	}
	public static OutputStream createOutputStream(String path,CompressionCodec codec) throws IOException{
		return new FileOutputStream(new File(path+codec.getDefaultExtension()));
	}
	public static Compressor getCompressor(CompressionCodec codec){
		return  CodecPool.getCompressor(codec);
	}
	public static CompressionOutputStream getCompressionOutputStream(CompressionCodec codec,OutputStream out,Compressor compressor) throws IOException{
		return codec.createOutputStream(out, compressor);
	}
}
