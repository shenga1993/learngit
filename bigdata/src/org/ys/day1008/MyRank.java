package org.ys.day1008;

import org.apache.hadoop.hive.ql.exec.UDF;

public class MyRank extends UDF{
	public MyRank(){
		
	}
	public String evaluate(int rank,int order){
		return "第"+((rank-1)*10+order)+"行";
	}
}
