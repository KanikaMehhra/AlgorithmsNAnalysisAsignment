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
	protected int preceedingTime, succeedingTime = -1;

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
		// If the tree node is empty, return a new node.
		if (root == null) {
			root = newNode;
			return root;
		}
		// Otherwise, recur down the tree and add the element to the required position.
		if (newNode.getProcess().getVt() < root.getProcess().getVt())
			root.setLeftNode(addBSTNodeRecursively(root.getLeftNode(), newNode));
		else if (newNode.getProcess().getVt() >= root.getProcess().getVt())
			root.setRightNode(addBSTNodeRecursively(root.getRightNode(), newNode));
		// return the root
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
		// loop down to find the leftmost leaf
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
		if (checkNodeExistance(this.root, procLabel) != null) {
			return true;
		}
		return false;
	} // end of findProcess()

	private BSTNode checkNodeExistance(BSTNode root, String procLabel) {
		if (root != null) {
			if (root.getProcess().getLabel().equals(procLabel)) {
				return root;
			} else {
				if (checkNodeExistance(root.getLeftNode(), procLabel) != null)
					return checkNodeExistance(root.getLeftNode(), procLabel);
				return checkNodeExistance(root.getRightNode(), procLabel);
			}
		}
		return null;
	}

	@Override
	public boolean removeProcess(String procLabel) {
		if (findProcess(procLabel)) {
			BSTNode toBeDeleted = checkNodeExistance(this.root, procLabel);
			this.root = deleteNode(this.root, toBeDeleted.getProcess().getVt());
			return true;
		}
		return false;
	} // end of removeProcess()

	public BSTNode maximumKey(BSTNode ptr) {
		while (ptr.getRightNode() != null) {
			ptr = ptr.getRightNode();
		}
		return ptr;
	}

	public BSTNode deleteNode(BSTNode root, int key) {
		// base case: key not found in tree
		if (root == null) {
			return root;
		}
		// if given key is less than the root node, recur for left subtree
		if (key < root.getProcess().getVt()) {
			root.setLeftNode(deleteNode(root.getLeftNode(), key));
		}
		// if given key is more than the root node, recur for right subtree
		else if (key >= root.getProcess().getVt()) {
			root.setRightNode(deleteNode(root.getRightNode(), key));
		}
		// key found
		else {
			// Case 1: node to be deleted has no children (it is a leaf node)
			if (root.getLeftNode() == null && root.getRightNode() == null) {
				// update root to null
				return null;
			}
			// Case 2: node to be deleted has two children
			else if (root.getLeftNode() != null && root.getRightNode() != null) {
				// find its in-order predecessor node
				BSTNode predecessor = maximumKey(root.getLeftNode());
				// Copy the value of predecessor to current node
				root.getProcess().setVt(predecessor.getProcess().getVt());
				root.getProcess().setLabel(predecessor.getProcess().getLabel());
				// recursively delete the predecessor. Note that the predecessor will have
				// at-most one child (left child)
				root.setLeftNode(deleteNode(root.getLeftNode(), predecessor.getProcess().getVt()));
			}
			// Case 3: node to be deleted has only one child
			else {
				// find child node
				BSTNode child = (root.getLeftNode() != null) ? root.getLeftNode() : root.getRightNode();
				root = child;
			}
		}
		return root;
	}

	@Override
	public int precedingProcessTime(String procLabel) {
		// Implement me
		this.preceedingTime = -1;
		BSTNode nodeExists = checkNodeExistance(this.root, procLabel);
		if (nodeExists != null) {
			this.preceedingTime = 0;
			addPreceedingTime(this.root, nodeExists);	
		}
		return preceedingTime; // placeholder, modify this
	} // end of precedingProcessTime()

	public void addPreceedingTime(BSTNode root, BSTNode nodeExists) {
		boolean found=false;
		if (root == null) {
			return;
		}
		addPreceedingTime(root.getLeftNode(), nodeExists);
		if(root.getProcess().getLabel().equals(nodeExists.getProcess().getLabel())) {
			found=true;
			return;
		}
		if(found) 
			return;
		else if (root.getProcess().getVt() <= nodeExists.getProcess().getVt())
			this.preceedingTime += root.getProcess().getVt();
		addPreceedingTime(root.getRightNode(), nodeExists);
	}

	@Override
	public int succeedingProcessTime(String procLabel) {
		this.succeedingTime = -1;
		BSTNode nodeExists = checkNodeExistance(this.root, procLabel);
		if (nodeExists != null) {
			this.succeedingTime = 0;
			addSucceedingTime(this.root, nodeExists);	
			addSucceedingFIFOCount(nodeExists.getRightNode(),nodeExists.getProcess().getVt());
//			this.succeedingTime+=
		}
		return succeedingTime; 
	} // end of precedingProcessTime()

	public void addSucceedingTime(BSTNode root, BSTNode nodeExists) {
//		boolean found=false;
		if (root == null) {
			return;
		}
		addSucceedingTime(root.getLeftNode(), nodeExists);
		if(root.getProcess().getVt() > nodeExists.getProcess().getVt())
			this.succeedingTime += root.getProcess().getVt();
//		
//		if(root.getProcess().getLabel().equals(nodeExists.getProcess().getLabel()))
//			found=true;
//		if(root.getProcess().getVt()==nodeExists.getProcess().getVt() && !found)
//			return;
//		else if ((!root.getProcess().getLabel().equals(nodeExists.getProcess().getLabel())) && root.getProcess().getVt() >= nodeExists.getProcess().getVt())
//			this.succeedingTime += root.getProcess().getVt();
//		
		addSucceedingTime(root.getRightNode(), nodeExists);
	}
	
	public void addSucceedingFIFOCount(BSTNode root, int procVt) {
		if(root==null) {
			return;
		}
		addSucceedingFIFOCount(root.getLeftNode(), procVt);
		if(root.getProcess().getVt()==procVt)
			this.succeedingTime+=procVt;
		addSucceedingFIFOCount(root.getRightNode(), procVt);
	}
	// public int addSucceedingTime(BSTNode root)
	// {
	// if (root == null)
	// return 0;
	// return (root.getProcess().getVt() + addSucceedingTime(root.getLeftNode()) +
	// addSucceedingTime(root.getRightNode()));
	// }

	@Override
	public void printAllProcesses(PrintWriter os) {
		try {
			os.flush();
			printTimeLine(this.root, os);
			os.print("\n");
		} catch (Exception e) {
			System.out.println("Exception caught:" + e);
		} finally {
			os.close();
		}
	} // end of printAllProcess()

	public void printTimeLine(BSTNode node, PrintWriter os) {
		if (node == null)
			return;
		printTimeLine(node.getLeftNode(), os);
		os.print(node.getProcess().getLabel() + " ");
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
