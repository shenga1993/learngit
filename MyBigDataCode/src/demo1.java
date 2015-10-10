public class demo1{
	public static void main(String[] args) {
		new Thread(new Runnable(){  //匿名
	           public void run(){
	              System.out.println("runnable run");
	           }
	       })
	       {
	           public void run(){
	              System.out.println("subthread run");
	           }
	       }.start();
	       new Thread1().start();
	       new Thread1().start();
	}
}
class Thread1 extends Thread implements Runnable{
	static int time = 0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		time ++;
		System.out.println(this.time+"asd");
	}
}

