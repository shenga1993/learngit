package org.ys.day1008;


public class test{
	public static void main(String[] args) {
		Singleton s = Singleton.get();
	}
}


class Singleton {
	private static Singleton sl = new Singleton();
	private Singleton(){
		System.out.println("123");
	}
	public static Singleton get(){
		return sl;
	}
}
