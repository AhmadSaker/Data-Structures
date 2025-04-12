import java.util.*;

public class AVLTree implements Iterable<Integer> {
	// You may edit the following nested class:
	protected Deque<Node []> s=new LinkedList<>();


	protected class Node {
		public Node left = null;
		public Node right = null;
		public Node parent = null;
		public int height = 0;
		public int value;
		protected  String undo="";
		protected int size =1;// the number of the  all the nodes in the sub tree without current node
		protected int less=1; // the size of the left subtree + 1

		public Node(int val) {
			this.value = val;

		}

		public void updateHeight() {
			int leftHeight = (left == null) ? -1 : left.height;
			int rightHeight = (right == null) ? -1 : right.height;

			height = Math.max(leftHeight, rightHeight) + 1;
		}

		public int getBalanceFactor() {
			int leftHeight = (left == null) ? -1 : left.height;
			int rightHeight = (right == null) ? -1 : right.height;

			return leftHeight - rightHeight;
		}
	}

	protected Node root;

	//You may add fields here.

	public AVLTree() {
		this.root = null;
	}

	/*
	 * IMPORTANT: You may add code to both "insert" and "insertNode" functions.
	 */
	public void insert(int value) {
		Node [] ar=new Node[5];
		Node a=new Node(value);
		ar[4]=a;
		Node b=new Node(value);
		b.undo="null";
		ar[0]=b;ar[1]=b;ar[2]=b;ar[3]=b;
		s.addFirst(ar);
		root = insertNode(root, value,ar);

	}

	protected Node insertNode(Node node, int value,Node [] ar) {
		// Perform regular BST insertion

		if (node == null) {
			Node insertedNode = new Node(value);
			return insertedNode;
		}
		if (value < node.value) {
			node.left  = insertNode(node.left, value,ar);
			node.left.parent = node;
			node.less++;// if node smaller than current then add it 
		}
		else {
			node.right = insertNode(node.right, value,ar);
			node.right.parent = node;
		}

		node.updateHeight();
		node.size++;
		/* 
		 * Check For Imbalance, and fix according to the AVL-Tree Definition
		 * If (balance > 1) -> Left Cases, (balance < -1) -> Right cases
		 */
		int balance = node.getBalanceFactor();

		if (balance > 1) {
			if (value > node.left.value) {
				Node a=new Node(value);
				a.undo="LR";ar[0]=a;

				node.left = rotateLeft(node.left);
				ar[1]=node.left;
			}
			Node a=new Node(value);
			a.undo="L";
			ar[2]=a;

			node = rotateRight(node);
			ar[3]=node;
		} else if (balance < -1) {
			if (value < node.right.value) {
				Node a=new Node(value);
				a.undo="RL";
				ar[0]=a;

				node.right = rotateRight(node.right);
				ar[1]=node.right;
			}

			Node a=new Node(value);
			a.undo="R";
			ar[2]=a;

			node = rotateLeft(node);
			ar[3]=node;
		}

		return node;
	}

	// You may add additional code to the next two functions.
	protected Node rotateRight(Node y) {


		Node x = y.left;
		Node T2 = x.right;
		// Perform rotation
		x.right = y;
		y.left = T2;

		//Update parents
		if (T2 != null) {
			T2.parent = y;
		}
		if (side(y)==1) 
			y.parent.right=x;
		else if (side(y)==0)
			y.parent.left=x;
		else root=x;

		x.parent = y.parent;
		y.parent = x;
		int a=y.size;
		if (T2!=null) {
			y.size=a-x.size+T2.size;    y.less=T2.less+1;

		}
		else {
			y.size=a-x.size;
			y.less=1;}

		x.size=a;



		y.updateHeight();
		x.updateHeight();

		// Return new root
		return x;
	}
	private int side(Node node) {
		if(node.parent!=null) {
			if(node.parent.right!=null&&node.parent.right==node)
				return 1;
			else return 0;}
		return -1;


	}

	protected Node rotateLeft(Node x) {


		Node y = x.right;
		Node T2 = y.left;

		// Perform rotation
		y.left = x;
		x.right = T2;

		//Update parents
		if (T2 != null) {
			T2.parent = x;
		}
		if (side(x)==1) 
			x.parent.right=y;
		else if (side(x)==0)
			x.parent.left=y;
		else root=y;



		y.parent = x.parent;
		x.parent = y;
		int a=x.size;
		if(T2!=null) {
			x.size=a-y.size+T2.size;
		}else 
			x.size=a-y.size;


		y.less=x.size+1;


		y.size=a;


		x.updateHeight();
		y.updateHeight();

		// Return new root
		return y;
	}

	public void printTree() {
		TreePrinter.print(this.root);
	}

	/***
	 * A Printer for the AVL-Tree. Helper Class for the method printTree().
	 * Not relevant to the assignment.
	 */
	private static class TreePrinter {
		private static void print(Node root) {
			if(root == null) {
				System.out.println("(XXXXXX)");
			} else {    
				final int height = root.height + 1;
				final int halfValueWidth = 4;
				int elements = 1;

				List<Node> currentLevel = new ArrayList<Node>(1);
				List<Node> nextLevel    = new ArrayList<Node>(2);
				currentLevel.add(root);

				// Iterating through the tree by level
				for(int i = 0; i < height; i++) {
					String textBuffer = createSpaceBuffer(halfValueWidth * ((int)Math.pow(2, height-1-i) - 1));

					// Print tree node elements
					for(Node n : currentLevel) {
						System.out.print(textBuffer);

						if(n == null) {
							System.out.print("        ");
							nextLevel.add(null);
							nextLevel.add(null);
						} else {
							System.out.printf("(%6d)", n.value);
							nextLevel.add(n.left);
							nextLevel.add(n.right);
						}

						System.out.print(textBuffer);
					}

					System.out.println();

					if(i < height - 1) {
						printNodeConnectors(currentLevel, textBuffer);
					}

					elements *= 2;
					currentLevel = nextLevel;
					nextLevel = new ArrayList<Node>(elements);
				}
			}
		}

		private static String createSpaceBuffer(int size) {
			char[] buff = new char[size];
			Arrays.fill(buff, ' ');

			return new String(buff);
		}

		private static void printNodeConnectors(List<Node> current, String textBuffer) {
			for(Node n : current) {
				System.out.print(textBuffer);
				if(n == null) {
					System.out.print("        ");
				} else {
					System.out.printf("%s      %s",
							n.left == null ? " " : "/", n.right == null ? " " : "\\");
				}

				System.out.print(textBuffer);
			}

			System.out.println();
		}
	}

	/***
	 * A base class for any Iterator over Binary-Search Tree.
	 * Not relevant to the assignment, but may be interesting to read!
	 * DO NOT WRITE CODE IN THE ITERATORS, THIS MAY FAIL THE AUTOMATIC TESTS!!!
	 */
	private abstract class BaseBSTIterator implements Iterator<Integer> {
		private List<Integer> values;
		private int index;
		public BaseBSTIterator(Node root) {
			values = new ArrayList<>();
			addValues(root);

			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < values.size();
		}

		@Override
		public Integer next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return values.get(index++);
		}

		protected void addNode(Node node) {
			values.add(node.value);
		}

		abstract protected void addValues(Node node);
	}

	public class InorderIterator extends BaseBSTIterator {
		public InorderIterator(Node root) {
			super(root);
		}

		@Override
		protected void addValues(Node node) {
			if (node != null) {
				addValues(node.left);
				addNode(node);
				addValues(node.right);
			}
		}    

	}

	public class PreorderIterator extends BaseBSTIterator {

		public PreorderIterator(Node root) {
			super(root);
		}

		@Override
		protected void addValues(AVLTree.Node node) {
			if (node != null) {
				addNode(node);
				addValues(node.left);
				addValues(node.right);
			}
		}        
	}

	@Override
	public Iterator<Integer> iterator() {
		return getInorderIterator();
	}

	public Iterator<Integer> getInorderIterator() {
		return new InorderIterator(this.root);
	}

	public Iterator<Integer> getPreorderIterator() {
		return new PreorderIterator(this.root);
	}
}
