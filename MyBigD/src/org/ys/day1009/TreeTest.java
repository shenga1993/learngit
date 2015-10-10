package org.ys.day1009;

public class TreeTest {
	public static void main(String[] args) {
		TreeNode tn = new TreeNode();
		for(int i=0;i<20;i++){
			tn.add((int)(Math.random()*100)+1);
		}
		tn.pre();
		tn.inorder();
		tn.midorder();
	}
	
}
class TreeNode{
	int value;
	TreeNode left;
	TreeNode right;
	void add(int value){
		if(value < this.value){
			if(this.left == null){
				this.left = new TreeNode();
				this.left.value = value;
			}else{
				this.left.add(value);
			}
		}else if(value > this.value ){
			if(this.right==null){
				this.right = new TreeNode();
				this.right.value = value;
			}else{
				this.right.add(value);
			}
		}
	}
	boolean find(int value){
		if(value == this.value){
			return true;
		}
		else{
			if(value < this.value){
				if(this.left != null){
					return this.left.find(value);
				}else{
					return false;
				}
			}else{
				if(this.right != null)
					return this.right.find(value);
				else
					return false;
			}
		}
	}
	void pre(){
		if(this.left!=null){
			this.left.pre();
		}
		System.out.println(this.value+",");
		if(this.right!=null){
			this.right.pre();
		}
	}
	
	void inorder(){
		if(this.right!=null){
			this.right.inorder();
		}
		System.out.println(this.value);
		if(this.left!=null){
			this.left.inorder();
		}
	}
	
	void midorder(){
		System.out.println(this.value);
		if(this.left != null){
			this.left.midorder();
		}
		if(this.right!=null){
			this.right.midorder();
		}
	}

}