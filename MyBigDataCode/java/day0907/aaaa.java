package day0907;



import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class aaaa {
	static int count;
	static char[] inputs;
	static char[] random;
	static int score;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		play();
		
		
	}
	@SuppressWarnings("resource")
	public static void play(){
		
		Random rm = new Random();
		Set<Integer> rmset = new HashSet<Integer>();
		while(rmset.size()<5){
			rmset.add(rm.nextInt(26));
		}
		char[] ch = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		Iterator<Integer> it = rmset.iterator();
		for(int i=0;it.hasNext();i++){
			random[i] = ch[it.next()];
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入长度为5的小写的字符串： ");
		inputs = sc.next().toCharArray();
		if(inputs.length!=5){
			System.out.println("请输入长度为5的字符串!!!");
			play();
		}
		count++;
		score-=10;
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(random[i]==inputs[i]){
					score+=100;
					break;
				}
				else if(random[i]==inputs[j]){
					score+=28;
					break;
				}
			}
		}
	}

}
