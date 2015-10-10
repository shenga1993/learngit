package day0904;

import static org.hamcrest.core.Is.is;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.junit.Test;
import org.junit.Before;

public class WriterableDemo {

	int len;
	IntWritable newWritable;
	Text t;

	@Before
	public void setUp() throws IOException {
		IntWritable writable = new IntWritable(163);
		byte[] bytes = serialize(writable);
		len = bytes.length;
		newWritable = new IntWritable();
		deserialize(newWritable, bytes);
		t = new Text("hadoop");
	}

	@Test
	public void test() {
		assertThat(len, is(4));
		assertThat(newWritable.get(), is(163));
		assertThat(t.getLength(), is(6));
		assertThat(t.getBytes().length, is(6));
		assertThat(t.charAt(2), is((int) 'd'));
		assertThat("Out of bounds", t.charAt(100), is(-1));
		assertThat(100,is((int)'d'));
	}

	public static byte[] serialize(Writable writable) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		writable.write(dataOut);
		dataOut.close();
		return out.toByteArray();
	}

	public static byte[] deserialize(Writable writable, byte[] bytes)
			throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		DataInputStream dataIn = new DataInputStream(in);
		writable.readFields(dataIn);
		return bytes;
	}
}
