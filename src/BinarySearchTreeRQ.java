import java.io.PrintWriter;
import java.lang.String;

/**
 * Implementation of the Runqueue interface using a Binary Search Tree.
 *
 * Your task is to complete the implementation of this class. You may add
 * methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class BinarySearchTreeRQ implements Runqueue {
	protected BSTNode root;
	// protected Proc[] timeLine=new Proc[5000];
	// protected int actualLength=0;

	/**
	 * Constructs empty queue
	 */
	public BinarySearchTreeRQ() {
		this.root = null;
	} // end of BinarySearchTreeRQ()

	
	@Override
	public void enqueue(String procLabel, int vt) {
		Proc newProcess = new Proc(procLabel, vt);
		BSTNode newNode = new BSTNode(newProcess);
		addBSTNode(newNode);
	} // end of enqueue()

	public void addBSTNode(BSTNode newNode) {
		this.root = addBSTNodeRecursively(this.root, newNode);
	}

	public BSTNode addBSTNodeRecursively(BSTNode root, BSTNode newNode) {
		/* If the tree node is empty, return a new node. */
		if (root == null) {
			root = newNode;
			return root;
		}

		/*
		 * Otherwise, recur down the tree and add the element to the required position.
		 */
		if (newNode.getProcess().getVt() < root.getProcess().getVt())
			root.setLeftNode(addBSTNodeRecursively(root.getLeftNode(), newNode));
		else if (newNode.getProcess().getVt() >= root.getProcess().getVt())
			root.setRightNode(addBSTNodeRecursively(root.getRightNode(), newNode));

		/* return the root */
		return root;
	}

	@Override
	public String dequeue() {
		String deletedProcessLabel;
		if (this.root == null)
			deletedProcessLabel = "";
		else if (this.root.getLeftNode() == null && this.root.getRightNode() == null) {
			deletedProcessLabel = this.root.getProcess().getLabel();
			this.root = null;
		} else if (this.root.getLeftNode() != null)
			deletedProcessLabel = minValue(root);
		else {
			deletedProcessLabel = this.root.getProcess().getLabel();
			this.root = this.root.getRightNode();
		}
		return deletedProcessLabel;
	} // end of dequeue()

	private String minValue(BSTNode node) {
		BSTNode current = node;
		BSTNode parentNode = null;

		/* loop down to find the leftmost leaf */
		while (current.getLeftNode() != null) {
			parentNode = current;
			current = current.getLeftNode();
		}
		parentNode.setLeftNode(current.getRightNode());
		String deletedProcessLabel = current.getProcess().getLabel();
		current = null;
		return (deletedProcessLabel);
	}

	@Override
	public boolean findProcess(String procLabel) {
		// Implement me

		return false; // placeholder, modify this
	} // end of findProcess()

	@Override
	public boolean removeProcess(String procLabel) {
		// Implement me

		return false; // placeholder, modify this
	} // end of removeProcess()

	@Override
	public int precedingProcessTime(String procLabel) {
		// Implement me

		return -1; // placeholder, modify this
	} // end of precedingProcessTime()

	@Override
	public int succeedingProcessTime(String procLabel) {
		// Implement me

		return -1; // placeholder, modify this
	} // end of precedingProcessTime()

	@Override
	public void printAllProcesses(PrintWriter os) {
		// os.flush();
		printTimeLine(this.root, os);

		// timeLine

		// Implement me

	} // end of printAllProcess()

	public void printTimeLine(BSTNode node, PrintWriter os) {
		if (node == null)
			return;
		printTimeLine(node.getLeftNode(), os);
		os.flush();
		os.print(node.getProcess().getLabel());
		printTimeLine(node.getRightNode(), os);

	}

	private class BSTNode {
		protected Proc process;
		protected BSTNode leftNode;
		protected BSTNode rightNode;

		public BSTNode(Proc process) {
			this.process = process;
			this.leftNode = this.rightNode = null;
		}

		public Proc getProcess() {
			return this.process;
		}

		// public void setProcess(Proc process) {
		// this.process = process;
		// }

		public BSTNode getLeftNode() {
			return this.leftNode;
		}

		public void setLeftNode(BSTNode leftNode) {
			this.leftNode = leftNode;
		}

		public BSTNode getRightNode() {
			return this.rightNode;
		}

		public void setRightNode(BSTNode rightNode) {
			this.rightNode = rightNode;
		}

	}

} // end of class BinarySearchTreeRQ
