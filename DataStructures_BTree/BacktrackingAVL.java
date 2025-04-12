
import java.util.LinkedList;
import java.util.List;

import javax.swing.plaf.synth.SynthOptionPaneUI;


import javax.swing.plaf.synth.SynthOptionPaneUI;



public class BacktrackingAVL extends AVLTree {

	// For clarity only, this is the default ctor created implicitly.
	public BacktrackingAVL() {
		super();

	}// we start from the last insertion in the deque , if the tree had a rotation by inserting this node 
		// then take the type of the rotation and do the opposite rotation.
		//finally after doing the opposite rotations remove the inserted node from the tree

	//You are to implement the function Backtrack.
	public void Backtrack() {
		if(root!=null) {
			Node [] arr=s.removeFirst();
			if(arr[0].undo=="LR") {

				Node f=arr[3];

				arr[3]=rotateLeft(arr[3]);

				f=rotateRight(f);


				remove(root,0,0,arr[4].value);

			}else if(arr[0].undo=="RL") {

				Node f=arr[3];
				arr[3]=rotateRight(arr[3]);
				f=rotateLeft(f);


				remove(root,0,0,arr[4].value);
			}
			else if(arr[2].undo=="R") {


				arr[3]=rotateRight(arr[3]);





				remove(root,0,0,arr[4].value);

			}else  if(arr[2].undo=="L") {

				arr[3]=rotateLeft(arr[3]);
				remove(root,0,0,arr[4].value);}
			else {
				remove(root,0,0,arr[4].value);

			}
		}
	}
	
	private  void remove(Node node ,int R ,int L,int v) { //removing the node from the tree because we did a rotation before deleting
	//so we just delete a leaf in the tree , the R and L indicates that if the node a right child or left child
		if(node!=null) {
			
node.size--; // all node we meet in our way to the needed value we decrease the size
			if(root.value==v&node.parent==null)
				root=null;

			else {
				if(node.value==v) {

					if(R==1) {
						if(node.parent.left!=null)
							node.parent.height=node.parent.left.height+1;
						else node.parent.height=0;
					
						node.parent.right=null;}
					else {
						if(node.parent.right!=null)
							node.parent.height=node.parent.right.height+1;
						else node.parent.height=0;
						node.parent.left=null;
					}
					node.parent=null;
					node=null;

				}
				else { if(node.value>v) {

					remove(node.left,0,1,v);
				
					}else remove(node.right,1,0,v);
				int rightt = (node.right == null) ? 0 : node.right.size;
				node.less=node.size-rightt;

				
				int leftHeight = (node.left == null) ? -1 : node.left.height;
				int rightHeight = (node.right == null) ? -1 : node.right.height;

				node.height = Math.max(leftHeight, rightHeight) + 1;
				}
				//updating the height and less after deleting 

			}}}

	//Change the list returned to a list of integers answering the requirements
	public static List<Integer> AVLTreeBacktrackingCounterExample() {
	List<Integer> a= new LinkedList<Integer>();
	a.add(4);
	a.add(2);
	a.add(1);
	return a;
	}
	private int Select(Node node, int i,int r) {//"r"= number of all nodes that smaller than node , and above him im the tree
		int curr=node.less+r;	// take size of the left subtree and add to it "r" , 
		if(curr==i)
			return node.value;
		if(curr>i)
			return Select(node.left,i,r);
		else 
			return Select(node.right,i,curr);
	}


	public int Select(int index) {
		if(root.size+1<index||index<1)
			throw new IllegalArgumentException("enter index from 1 to tree size");
		else return Select(root,index,0);	 
	}

	public int Rank(int value) {
		return Rank(root,value,0);
	}

	private int Rank(Node node,int value, int r) { //"r"= number of all nodes that smaller than node , and above him im the tree
		if( node.value == value) //if reached the needed node return the size of left tree and how much nodes smaller than it and above it in the tree -1 because its accounted in left subtree
			return node.less+r-1;
		if (node.value<value)
			if (node.right!=null)
				return Rank(node.right,value,node.less+r);
			else return node.less+r;
		else 
			if(node.left!=null)
				return Rank(node.left,value,r);
			else return node.less+r-1;
	}


}