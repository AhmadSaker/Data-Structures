import java.util.LinkedList;
import java.util.List;

public class BacktrackingBTree<T extends Comparable<T>> extends BTree<T> {
	// For clarity only, this is the default ctor created implicitly.
	public BacktrackingBTree() {
		super();
	}

	public BacktrackingBTree(int order) {
		super(order);
	}

	//You are to implement the function Backtrack.
	public void Backtrack() {
		if(actions.isEmpty()) {
			return;
		}
		if ((int)actions.getLast()==0) {
			actions.removeLast();
			remove((T)actions.getLast());	
			actions.removeLast();
		}
		else {
			
			int numofsplits=(int)actions.removeLast();;
			T newkey = (T)actions.removeLast(); 
			remove(newkey);
			for (int i=0; i<numofsplits ; i++) {
				T midkey=(T)actions.removeLast();
				Node<T> currNode =getNode(midkey);
				int index = currNode.indexOf(midkey);
				Node<T> left = currNode.getChild(index);
				Node<T> right = currNode.getChild(index+1);
				unsplit(currNode,left,right,midkey);
				
			}
			
			
		}
    }
	
	//Change the list returned to a list of integers answering the requirements
	public static List<Integer> BTreeBacktrackingCounterExample(){
		List example = new LinkedList<Integer>() ;
		for (int i =1 ; i<7 ; i++) {
			example.add(i);
		}
		
		return example;
	}
	
	//undo split action to use in the backtrack 
	private Node<T> unsplit(Node<T> parent , Node<T> left, Node<T> right , T midkey ){
		
		Node<T> newNode = left;
		newNode.addKey(midkey);
		for (int i=0; i<right.getNumberOfKeys();i++) {
			newNode.addKey(right.getKey(i));
		}
		for (int i=0;i<right.numOfChildren;i++) {
			newNode.addChild(right.getChild(i));
		}
		if (parent.numOfKeys==1) {
			this.root=newNode;
		}
		    else {
		    parent.removeKey(midkey);	
		    parent.removeChild(left);
		    parent.removeChild(right);
		    parent.addChild(newNode);
		    }
		return parent;
	}
	
	// remove function that removes just the new key that was added in the last insertion 
	
	private void remove (T key) {
		if (this.getNode(key).numOfKeys==1 &&size==1) {
			root=null;
		}
		else  {this.getNode(key).removeKey(key);
		}
		size--;
	}}