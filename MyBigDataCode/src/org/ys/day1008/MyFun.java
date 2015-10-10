package org.ys.day1008;

import org.apache.hadoop.hive.ql.exec.UDF;

public class MyFun extends UDF{
	public String evaluate(String name){
		return name+"是SB";
	}
}
