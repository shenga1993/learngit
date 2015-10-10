package org.ys.day1008;

import org.apache.hadoop.hive.ql.exec.UDF;

public class MyTrim extends UDF{
	private static final String space = " ";
 	public MyTrim(){
		
	}
 	public String evaluate(String keyword){
 		String[] data = keyword.split(space);
 		String line = "";
 		for(String tmp:data){
 			 line+=tmp;
 		}
 		return line;
 	}
}
