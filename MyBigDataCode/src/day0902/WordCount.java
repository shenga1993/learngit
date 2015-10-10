package day0902;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;

public class WordCount {
	public static void main(String[] args) throws IOException {
		BufferedReader bfr = null;
		String line = null;
		Set<Integer> set = new HashSet<Integer>();
		TreeMap<Integer, Integer> map = new TreeMap<Integer,Integer>();
		String url = "hdfs://192.168.2.227:9000/KeywordCount.txt";
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(url), conf);
		OutputStream out = fs.create(new Path(url), new Progressable() {
			@Override
			public void progress() {
				// TODO Auto-generated method stub
				System.out.print("  li kang shi sb  ");
			}
		});
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(out));
		try {

			// 创建输入输出流reader和writer
			bfr = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File("sogou.500w.utf8")), "utf-8"));
			while ((line = bfr.readLine()) != null) {
				int count = 1;
				int Cnum = 0;
				int Enum = 0;
				// 分割词汇split
				String[] word = line.split("\t");
				if (word.length == 6) {
					String keyword = word[2];
					// 分割关键字并判断中文和英文,计算长度
					String[] str = keyword.split("");
					for (String s : str) {
						if (isChinese(s)) {
							if (str[str.length - 1].equals(s)) {
								Cnum++;
							} else
								Cnum += 2;
						} else {
							Enum++;
						}
					}
					int len = Cnum + Enum;
					if (!set.add(len)) {
						count = map.get(len);
						count++;
					}
					map.put(len, count);
				}
			}
			// 长度分布计算
			Set<Map.Entry<Integer, Integer>> hashset = map.entrySet();
			int tlen = 0;
			int tnum = 0;
			for (Map.Entry<Integer, Integer> entryset : hashset) {
				tlen += entryset.getKey() * entryset.getValue();
				tnum += entryset.getValue();
				// bfw.write(entryset.getKey()+"\t"+entryset.getValue()+"\n");
			}
			// 平均长度计算
			bfw.write("aaaaaaaaaaaaaaaaa" + tlen / tnum);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭输入输出流
			bfw.close();
			bfr.close();
		}
	}

	public static boolean isChinese(String str) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find())
			return true;
		return false;
	}
}
