package day0907;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class SogouWritable implements WritableComparable<SogouWritable>{
	private Text time;
	private Text uid;
	private Text keyword;
	private IntWritable ordel;
	private IntWritable range;
	private Text url;
	
	
	
	public SogouWritable() {
		this.time=new Text();
		this.uid=new Text();
		this.keyword=new Text();
		this.ordel=new IntWritable();
		this.range=new IntWritable();
		this.url = new Text();
	}

	public SogouWritable(Text time, Text uid, Text keyword, IntWritable ordel,
			IntWritable range, Text url) {
		super();
		this.time = time;
		this.uid = uid;
		this.keyword = keyword;
		this.ordel = ordel;
		this.range = range;
		this.url = url;
	}
	
	public void set(Text time,Text uid,Text keyword,IntWritable ordel,IntWritable range,Text url){
		this.time = time;
		this.uid = uid;
		this.keyword = keyword;
		this.ordel = ordel;
		this.range = range;
		this.url = url;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		this.time.write(out);
		this.uid.write(out);
		this.keyword.write(out);
		this.ordel.write(out);
		this.range.write(out);
		this.url.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		this.time.readFields(in);
		this.uid.readFields(in);
		this.keyword.readFields(in);
		this.ordel.readFields(in);
		this.range.readFields(in);
		this.url.readFields(in);
	}

	@Override
	public int compareTo(SogouWritable o) {
		// TODO Auto-generated method stub
		int tmp = 0;
		tmp=this.time.compareTo(o.getTime());
		if(tmp!=0){
			return tmp;
		}
		tmp=this.uid.compareTo(o.getUid());
		if(tmp!=0){
			return tmp;
		}
		tmp=this.keyword.compareTo(o.getKeyword());
		if(tmp!=0){
			return tmp;
		}
		tmp=this.ordel.compareTo(o.getOrdel());
		if(tmp!=0){
			return tmp;
		}
		tmp=this.range.compareTo(o.getRange());
		if(tmp!=0){
			return tmp;
		}
		tmp=this.url.compareTo(o.getUrl());
		if(tmp!=0){
			return tmp;
		}
		return tmp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		result = prime * result + ((ordel == null) ? 0 : ordel.hashCode());
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SogouWritable other = (SogouWritable) obj;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		if (ordel == null) {
			if (other.ordel != null)
				return false;
		} else if (!ordel.equals(other.ordel))
			return false;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return time+"\t"+uid+"\t"+keyword+"\t"+ordel+"\t"+range+"\t"+url;
	}

	public Text getTime() {
		return time;
	}

	public void setTime(Text time) {
		this.time = time;
	}

	public Text getUid() {
		return uid;
	}

	public void setUid(Text uid) {
		this.uid = uid;
	}

	public Text getKeyword() {
		return keyword;
	}

	public void setKeyword(Text keyword) {
		this.keyword = keyword;
	}

	public IntWritable getOrdel() {
		return ordel;
	}

	public void setOrdel(IntWritable ordel) {
		this.ordel = ordel;
	}

	public IntWritable getRange() {
		return range;
	}

	public void setRange(IntWritable range) {
		this.range = range;
	}

	public Text getUrl() {
		return url;
	}

	public void setUrl(Text url) {
		this.url = url;
	}


}
