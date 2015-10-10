package org.ys.day1009;

public class GraphTest {
	
}
class GraphNode{
	int val;
	GraphNode next;
	GraphNode [] neighbors;
	boolean visited;
	GraphNode(int x){
		val = x;
	}
	GraphNode(int x,GraphNode[] n){
		val = x;
		neighbors = n;
	}
	
	public String toString(){
		return "value:" + this.val;
	}
}

class Queue{
	GraphNode first,last;
	
	public void enqueue(GraphNode n){
		if(first == null){
			first = n;
			last = first ;
		}else{
			last.next = n;
			last = n;
		}
	}
}