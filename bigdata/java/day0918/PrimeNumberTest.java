package day0918;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrimeNumberTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		input();
	}
	public static boolean isNumber(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	@SuppressWarnings("resource")
	public static void input(){
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入您要判断的正整数：");
		String num = sc.next();
		if(isNumber(num)&&Integer.parseInt(num)>0){
			isPrimeNumber(Integer.parseInt(num));
			System.out.println("是否要继续判断？输入Y继续，输入其他退出！");
			String str = sc.next();
			if(str.equals("Y")||str.equals("y")){
				input();
			}else{
				System.out.println("已退出质数判断系统！");
			}
		}else{
			System.out.println("您输入的不是一个正整数！请重新输入：");
			input();
		}
	}
	public static void isPrimeNumber(int num){
		ArrayList<Integer> al = new ArrayList<>();
		if(num==1||num==2){
			System.out.println(num+"是质数");
			return;
		}
		for(int i=2;i<num;i++){
			al.add(i);
		}
		for(int i:al){
			int j = num%i;
			if(j==0){
				System.out.println(num+"不是质数");
				return;
			}
		}
		System.out.println(num+"是质数");
	}
}
