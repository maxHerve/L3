package fr.istic.prg1.tp6;

import java.util.*;

public class BinaryTree<T>{
	private Element root;

	private class Element{
		public T value;
		public Element left, right;
		public Element(){
			value = null;
			left = null;
			right = null;
		}
	}

	public class TreeIterator implements Iterator<T>{
		private Element currentNode;
		private Stack<Element> stack;

		public TreeIterator(){
			this.currentNode = BinaryTree.this.root;
			this.stack = new Stack<Element>();
		}

		/**
		 * @pre the current node is not a stop node
		 * Place the current node on it's left
		 * @post place the old current node on the stack
		 * @throws BinaryTreeException with MOVE_AFTER_STOP_NODE if the first precondition is not verified
		 */
		public void goLeft(){
			if(this.isEmpty()){
				throw new BinaryTreeException(BinaryTreeException.Type.MOVE_AFTER_STOP_NODE);
			}
			
			this.stack.push(this.currentNode);
			this.currentNode = this.currentNode.left;
		}

		/**
		 * @pre the current node is not a stop node
		 * Place the current node on it's right
		 * @post place the old current node on the stack
		 * @throws BinaryTreeException with MOVE_AFTER_STOP_NODE if the first precondition is not verified
		 */
		public void goRight(){
			if(this.isEmpty()){
				throw new BinaryTreeException(BinaryTreeException.Type.MOVE_AFTER_STOP_NODE);
			}

			this.stack.push(this.currentNode);
			this.currentNode = this.currentNode.right;

		}

		/**
		 * Place the current node on the root node
		 */
		public void goRoot(){
			while(!this.stack.isEmpty()){
				this.currentNode = this.stack.pop();
			}
		}

		/**
		 * @pre the current node is not the root
		 * Place the current node on it's father
		 * @throws BinaryTreeException with MOVE_BEFORE_ROOT if the first precondition is not verified
		 */
		public void goUp(){
			if(this.isRoot()){
				throw new BinaryTreeException(BinaryTreeException.Type.MOVE_BEFORE_ROOT);
			}
			this.currentNode = this.stack.pop();
		}


		/**
		 * @pre the current's node type is not a double node
		 * @pre the current's node type is not an empty node
		 * @post the current node becomes the node 
		 * Delete the current node and connect it's father with it's son
		 * @throws BinaryTreeException with REMOVE_EMPTY_NODE if the first precondition is not verified
		 * @throws BinaryTreeException with REMOVE_EMPTY_NODE if the second precondition is not verified
		 */
		public void remove(){
			if(elementIsEmpty(this.currentNode)){
				throw new BinaryTreeException(BinaryTreeException.Type.REMOVE_EMPTY_NODE);
			}
			if(elementIsDouble(this.currentNode)){
				throw new BinaryTreeException(BinaryTreeException.Type.REMOVE_DOUBLE_NODE);
			}

			Element elementSon = new Element();

			if(elementIsLeftNode(this.currentNode)){
				elementSon = this.currentNode.left;
			}
			else if(elementIsRightNode(this.currentNode)){
				elementSon = this.currentNode.right;
			}

			if(this.currentNode != BinaryTree.this.root){
				if(this.currentNode == this.stack.peek().left){
					this.stack.peek().left = elementSon;
					this.currentNode = this.stack.peek().left;
				}
				else if(this.currentNode == this.stack.peek().right){
					this.stack.peek().right = elementSon;
					this.currentNode = this.stack.peek().right;
				}
			}
			else{
				BinaryTree.this.root = elementSon;
				this.currentNode = BinaryTree.this.root;
			}
		}

		/**
		 * Empty the sub-tree of the current node
		 * @post the current node becomes a stop node 
		 */
		public void clear(){
			if(!this.isEmpty()){
				if(this.currentNode != BinaryTree.this.root){
					if(this.stack.peek().left == this.currentNode){
						this.stack.peek().left = new Element();
						this.currentNode = this.stack.peek().left;
					}
					else if(this.stack.peek().right == this.currentNode){
						this.stack.peek().right = new Element();
						this.currentNode = this.stack.peek().right;
					}
					else{
						throw new Error("Fatal error !");
					}
				}
				else{
					BinaryTree.this.root = new Element();
					this.currentNode = BinaryTree.this.root;
				}
			}
		}

		/**
		 * Verify if the node is a stop node
		 * @return true if and only if the current node is a stop node
		 */
		public boolean isEmpty(){
			return (this.currentNode.left == null && this.currentNode.right == null);
		}

		/**
		 * Verify if the current node is the root node
		 * @return true if and only if the current node is the root node
		 */
		private boolean isRoot(){
			return this.currentNode == BinaryTree.this.root;
		}

		/**
		 * Verify if the element passed in parameter is an empty node
		 * @param e an element
		 * @return true if e is an empty node
		 */
		private boolean elementIsEmpty(Element e){
			return (e.left == null && e.right == null);
		}

		/**
		 * Verify if the element passed in parameter is a leaf node
		 * @pre (e.left == null && e.right == null) || (e.left =! null && e.right =! null)
		 * @param e an element
		 * @return true if e is a leaf node
		 */
		private boolean elementIsLeaf(Element e){
			boolean res = false;

			if(!elementIsEmpty(e)){
				if(elementIsEmpty(e.right) && elementIsEmpty(e.left)){
					res = true;
				}
			}
			return res;
		}

		/**
		 * Verify if the element passed in parameter is a double node
		 * @pre (e.left == null && e.right == null) || (e.left =! null && e.right =! null)
		 * @param e an element
		 * @return true if e is a double node
		 */
		private boolean elementIsDouble(Element e){
			boolean res = false;

			if(!elementIsEmpty(e)){
				if(!elementIsEmpty(e.right) && !elementIsEmpty(e.left)){
					res = true;
				}
			}
			return res;
		}

		/**
		 * Verify if the element passed in parameter is a simple left node
		 * @pre (e.left == null && e.right == null) || (e.left =! null && e.right =! null)
		 * @param e an element
		 * @return true if e is a simple left node
		 */
		private boolean elementIsLeftNode(Element e){
			boolean res = false;

			if(!elementIsEmpty(e)){
				if(elementIsEmpty(e.right) && !elementIsEmpty(e.left)){
					res = true;
				}
			}
			return res;
		}

		/**
		 * Verify if the element passed in parameter is a simple right node
		 * @pre (e.left == null && e.right == null) || (e.left =! null && e.right =! null)
		 * @param e an element
		 * @return true if e is a simple right node
		 */
		private boolean elementIsRightNode(Element e){
			boolean res = false;

			if(!elementIsEmpty(e)){
				if(!elementIsEmpty(e.right) && elementIsEmpty(e.left)){
					res = true;
				}
			}
			return res;
		}

		/**
		 * @return the node type of the current node
		 */
		public NodeType nodeType(){
			if(this.isEmpty()){
				return NodeType.SENTINEL;
			}
			else if(elementIsLeaf(this.currentNode)){
				return NodeType.LEAF;
			}
			else if(elementIsLeftNode(this.currentNode)){
				return NodeType.SIMPLE_LEFT;
			}
			else if(elementIsRightNode(this.currentNode)){
				return NodeType.SIMPLE_RIGHT;
			}
			else if(elementIsDouble(this.currentNode)){
				return NodeType.DOUBLE;
			}
			else{
				throw new Error("Fatal error !");
			}
		}


		/**
		 * @return the value of the current node
		 */
		public T getValue(){
			return this.currentNode.value;
		}

		/**
		 * @pre the current node is a stop node
		 * @param v the value to add
		 * @post the value v added in the tree is not cloned !
		 * @throws BinaryTreeException with ADD_ON_NON_STOP_NODE if the first precondition is not verified
		 */
		public void addValue(T v){
			if(this.nodeType() == NodeType.SENTINEL){
				this.currentNode.value = v;

				this.currentNode.left = new Element();
				this.currentNode.right = new Element();
			}
			else{
				throw new BinaryTreeException(BinaryTreeException.Type.ADD_ON_NON_STOP_NODE);
			}
		}

		/**
		 * @param v the value to add
		 * @post the value v added in the tree is not cloned !
		 */
		public void setValue(T v){
			this.currentNode.value = v;
		}

		/**
		 * Exchange the values associated with the current node with the one of his father of order i
		 * @pre i >= 0
		 * @pre the current node as a rank >= i
		 * @param i 
		 * @throws BinaryTreeException with INDEX_OUT_OF_RANGE if the first or the second precondition is not verified
		 */
		public void switchValue(int i){
			if(i == 0 || this.stack.size() < i){
				throw new BinaryTreeException(BinaryTreeException.Type.INDEX_OUT_OF_RANGE);
			}

			Element theCurrentNode = this.currentNode;

			Stack<Element> sonStack = new Stack<Element>();
			for(int y=0 ; y<i ; y++){
				sonStack.push(this.stack.pop());
				this.currentNode = sonStack.peek();
			}

			T tmp = theCurrentNode.value;
			theCurrentNode.value = this.currentNode.value;
			this.currentNode.value = tmp;

			while(!sonStack.isEmpty()){
				this.stack.push(sonStack.pop());
			}

			this.currentNode = theCurrentNode;
		}
	}

	public BinaryTree(){
		this.root = new Element();
	}

	/**
	 * @post the current node of the iterator is the root of the tree
	 * @return an iterator
	 */
	public Iterator<T> iterator(){
		return new TreeIterator();
	}

	/**
	 * Verify that the tree is empty
	 * @return true if and only if the root of the tree is a stop node
	 */
	public boolean isEmpty(){
		TreeIterator it = new TreeIterator();
		return it.isEmpty();
	}
}
