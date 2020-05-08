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
	private BSTNode root;
	private int preceedingTime, succeedingTime = -1;
	private BSTNode toBeDeletedNode = null;
	private BSTNode lastNode = null;

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
		this.lastNode = maximumKey(this.root);
	} // end of enqueue()

	private void addBSTNode(BSTNode newNode) {
		this.root = addBSTNodeRecursively(this.root, newNode);
	}

	private BSTNode addBSTNodeRecursively(BSTNode root, BSTNode newNode) {
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
		} else if (this.root.getLeftNode() == null && this.root.getRightNode() != null) {
			deletedProcessLabel = this.root.getProcess().getLabel();
			this.root = this.root.getRightNode();
		}
		else {
			deletedProcessLabel = minValue(root);
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
		if (checkNodeExistence(this.root, procLabel) != null) {
			return true;
		}
		return false;
	} // end of findProcess()

	private BSTNode checkNodeExistence(BSTNode root, String procLabel) {
		if (root != null) {
			if (root.getProcess().getLabel().equals(procLabel)) {
				return root;
			} else {
				if (checkNodeExistence(root.getLeftNode(), procLabel) != null)
					return checkNodeExistence(root.getLeftNode(), procLabel);
				return checkNodeExistence(root.getRightNode(), procLabel);
			}
		}
		return null;
	}

	@Override
	public boolean removeProcess(String procLabel) {
		if (findProcess(procLabel)) {
			this.toBeDeletedNode = checkNodeExistence(this.root, procLabel);
			this.root = deleteNode(this.root, this.toBeDeletedNode.getProcess().getVt());
			this.lastNode = maximumKey(this.root);
			return true;
		}
		return false;
	} // end of removeProcess()

	private BSTNode maximumKey(BSTNode node) {
		while (node.getRightNode() != null) {
			node = node.getRightNode();
		}
		return node;
	}

	private BSTNode deleteNode(BSTNode root, int key) {
		// base case: key not found in tree
		if (root == null) {
			return root;
		}

		// if given key is less than the root node, recur for left subtree
		if (key < root.getProcess().getVt()) {
			root.setLeftNode(deleteNode(root.getLeftNode(), key));
		}

		// if given key is more than the root node, recur for right subtree
		else if (key > root.getProcess().getVt()) {
			root.setRightNode(deleteNode(root.getRightNode(), key));
		} else if ((!root.getProcess().getLabel().equals(this.toBeDeletedNode.getProcess().getLabel()))
				&& root.getProcess().getVt() == key) {
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
				root.setProcess(predecessor.getProcess());

				// recursively delete the predecessor. Note that the
				// predecessor will have at-most one child (left child)
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
		this.preceedingTime = -1;
		BSTNode nodeExists = checkNodeExistence(this.root, procLabel);
		if (nodeExists != null) {
			this.preceedingTime = 0;
			addPreceedingTime(this.root, nodeExists);
		}
		return preceedingTime; // placeholder, modify this
	} // end of precedingProcessTime()

	private void addPreceedingTime(BSTNode root, BSTNode nodeExists) {
		boolean found = false;
		if (root == null) {
			return;
		}
		addPreceedingTime(root.getLeftNode(), nodeExists);
		if (root.getProcess().getLabel().equals(nodeExists.getProcess().getLabel())) {
			found = true;
			return;
		}
		if (found)
			return;
		else if (root.getProcess().getVt() <= nodeExists.getProcess().getVt())
			this.preceedingTime += root.getProcess().getVt();
		addPreceedingTime(root.getRightNode(), nodeExists);
	}

	@Override
	public int succeedingProcessTime(String procLabel) {
		this.succeedingTime = -1;
		BSTNode nodeExists = checkNodeExistence(this.root, procLabel);
		if (nodeExists != null) {
			this.succeedingTime = 0;
			addSucceedingTime(this.root, nodeExists);
			addSucceedingFIFOCount(nodeExists.getRightNode(), nodeExists.getProcess().getVt());
			// this.succeedingTime+=
		}
		return succeedingTime;
	} // end of precedingProcessTime()

	private void addSucceedingTime(BSTNode root, BSTNode nodeExists) {
		if (root == null) {
			return;
		}
		addSucceedingTime(root.getLeftNode(), nodeExists);
		if (root.getProcess().getVt() > nodeExists.getProcess().getVt())
			this.succeedingTime += root.getProcess().getVt();
		addSucceedingTime(root.getRightNode(), nodeExists);
	}

	private void addSucceedingFIFOCount(BSTNode root, int procVt) {
		if (root == null) {
			return;
		}
		addSucceedingFIFOCount(root.getLeftNode(), procVt);
		if (root.getProcess().getVt() == procVt)
			this.succeedingTime += procVt;
		addSucceedingFIFOCount(root.getRightNode(), procVt);
	}

	@Override
	public void printAllProcesses(PrintWriter os) {
		try {
			printTimeLine(this.root, os);
			os.print("\n");
		} catch (Exception e) {
			System.out.println("Exception caught:" + e);
		} finally {
			os.flush();
		}
	} // end of printAllProcess()

	private void printTimeLine(BSTNode node, PrintWriter os) {
		if (node == null)
			return;
		printTimeLine(node.getLeftNode(), os);
		if (node == this.lastNode)
			os.printf("%s", node.getProcess().getLabel());
		else
			os.printf("%s ", node.getProcess().getLabel());
		printTimeLine(node.getRightNode(), os);

	}
	
	//This method is used solely for the purpose of data generations.
	private String findMax(){
		return this.lastNode.getProcess().getLabel();
	}
	
	//This method is used solely for the purpose of data generations.
	private String findMin(){
		return this.root.getProcess().getLabel();
	}

	private class BSTNode {
		private Proc process;
		private BSTNode leftNode;
		private BSTNode rightNode;

		public BSTNode(Proc process) {
			this.process = process;
			this.leftNode = this.rightNode = null;
		}

		private void setProcess(Proc process) {
			this.process = process;
		}

		private Proc getProcess() {
			return this.process;
		}

		private BSTNode getLeftNode() {
			return this.leftNode;
		}

		private void setLeftNode(BSTNode leftNode) {
			this.leftNode = leftNode;
		}

		private BSTNode getRightNode() {
			return this.rightNode;
		}

		private void setRightNode(BSTNode rightNode) {
			this.rightNode = rightNode;
		}

	}

} // end of class BinarySearchTreeRQ
