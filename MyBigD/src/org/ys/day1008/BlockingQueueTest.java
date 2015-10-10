package org.ys.day1008;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {
	public static void main(String[] args) {
		final int QUEUE_SIZE = 10;
		final int SEARCH_THREADS = 100;
		BlockingQueue<Integer> bq = new ArrayBlockingQueue<Integer>(QUEUE_SIZE);

		Pro p = new Pro(10, 50, bq);
		Thread t1 = new Thread(p);
		t1.start();
		for (int i = 0; i < SEARCH_THREADS; i++) {
			new Thread(new Use(10, bq)).start();
			
		}

	}

}

class Pro implements Runnable {
	public static int total;
	private int produce;
	private final static int Max = 500;
	private BlockingQueue<Integer> bq;

	Pro(int produce, int breadCount, BlockingQueue<Integer> bq) {
		this.bq = bq;
		this.produce = produce;
		Pro.total = breadCount;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		proD();
		total = total - produce;
		System.out.println("多了");
	}
	public synchronized void proD(){
		while (total < Max) {
			try {
				total += produce;
				bq.put(total);
				System.out.println("生产" + produce + "个,现在有" + total + "个。");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

class Use implements Runnable {
	private BlockingQueue<Integer> bq;
	private final static int Min = 100;
	private int use;
	private static int all;

	Use(int use, BlockingQueue<Integer> bq) {
		this.use = use;
		this.bq = bq;
		try {
			Use.all = bq.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void useD(){
		boolean tag = false;
		while (!tag) {
			if (all > Min) {
				all -= use;
				try {
					Integer count = bq.take();
					System.out.println("购买了" + use + "个，现在还有" + all + "个。");
					if (count == Pro.total) {
						tag = true;
						Pro.total = Min;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		useD();
		System.out.println("少了");
	}

}