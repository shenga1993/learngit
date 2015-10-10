package MyList;

public class StackAndQueue {
	public static void main(String[] args) {
		Stack t = new Stack();
		t.push(new Node(5));
		t.push(new Node(4));
		t.push(new Node(3));
		Node n ;
		while((n=t.pop())!=null){
			System.out.println(n.val);
		}
		Queue q = new Queue();
		q.enqueue(new Node(1));
		q.enqueue(new Node(2));
		q.enqueue(new Node(3));
		q.enqueue(new Node(4));
		while((n=q.dequeue())!=null){
			System.out.println(n.val);
		}
	}
}
class Node{
	int val;
	Node next;
	Node(int x){
		this.val = x;
		this.next = null;
	}
}
class Stack{
	Node top;
	public Node peek(){
		if(top != null){
			return top;
		}
		return null;
	}
	public Node pop(){
		if(top == null){
			return null;
		}else{
			Node tmp = new Node(top.val);
			top = top.next;
			return tmp;
		}
	}
	public void push(Node n){
		if(n != null){
			n.next = top;
			top = n;
		}
	}
}
class Queue{
	Node first,last;
	public void enqueue(Node n){
		if(first == null){
			first = n;
			last = first;
		}else{
			last.next = n;
			last = n;
		}
	}
	public Node dequeue(){
		if(first == null){
			return null;
		}else{
			Node temp = new Node(first.val);
			first = first.next;
			return temp;
		}
	}
}
