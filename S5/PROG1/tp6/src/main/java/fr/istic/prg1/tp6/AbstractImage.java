package fr.istic.prg1.tp6;

import java.util.*;
import java.util.regex.*;

/**
 * Manage a white and black image based on a binary tree.
 * To call some of the methods of this class such as saveImage the tree must be well formed. This means that every nodes that is not a node must contains the value Node.BOTH, any other node must contains either Node.OFF or Node.ON. Moreover a node that contains Node.BOTH must as his NodeType equals to NodeType.DOUBLE.
 */
public abstract class AbstractImage extends BinaryTree<Node>{
	private static final int MIN_X = 0;
	private static final int MAX_X = 255;
	private static final int MIN_Y = 0;
	private static final int MAX_Y = 255;

	/**
	 * Determine if two images are identical
	 * @param image an image
	 * @return true if and only if images are equal node by node
	 * @pre !this.isEmpty()
	 * @pre !image.isEmpty()
	 */
	public boolean equals(AbstractImage image){
		if(this.isEmpty() || image.isEmpty()){
			throw new AbstractImageException(AbstractImageException.Type.EQUALS_EMPTY_TREE);
		}

		return recursiveEquals(this.iterator(),image.iterator());
	}

	/**
	 * Determine if two sub-images are identical recursively
	 * @param it1 an iterator
	 * @param it2 another iterator
	 * @return true if and only if the sub-images are equal
	 * @pre !it1.isEmpty()
	 * @pre !it2.isEmpty()
	 */
	public boolean recursiveEquals(Iterator<Node> it1, Iterator<Node> it2){
		boolean res = false;

		if(!it1.isEmpty() && !it2.isEmpty()){
			res = it1.getValue() == it2.getValue();
			if(res){
				it1.goLeft();
				it2.goLeft();
				res = res && recursiveEquals(it1,it2);
				it1.goUp();
				it2.goUp();

				it1.goRight();
				it2.goRight();
				res = res && recursiveEquals(it1,it2);
				it1.goUp();
				it2.goUp();
			}
		}
		else if(it1.isEmpty() && it2.isEmpty()){
			res = true;
		}
		return res;
	}

	/**
	 * Create this from a text file and print it in a graphical interface.
	 * Each lines of the file is like this : (e x1 y1 x2 y2) and indicate if the pixels in the rectangular zone are on or off, depending of e
	 * The file terminates by (-1)
	 */
	public void constructTreeFromFile(){
	}

	/**
	 * Create this from a text file and print it in a graphical interface.
	 * Each lines of the file is like this : (e x1 y1 x2 y2) and indicate if the pixels in the rectangular zone are on or off, depending of e
	 * The file terminates by -1
	 * @post the old tree that was in this is overwrited
	 */
	public void constructTreeFromFile(String filePath){
	}

	/**
	 * Save this in a text file, readable by the method constructTreeFromFile
	 */
	public void saveImage(){
	}

	/**
	 * @pre the tree is well formed
	 * Transform this image to string
	 */
	public String imageToString(){
		if(this.isEmpty()){
			throw new AbstractImageException(AbstractImageException.Type.SAVE_EMPTY_TREE);
		}
		StringBuilder stringOfImage = new StringBuilder();

		recursiveImageToString(this.iterator(), stringOfImage, MIN_X, MIN_Y, MAX_X, MAX_Y);

		return stringOfImage.append("(-1)").toString();
	}

	/**
	 * @pre the tree is well formed
	 * @param it an iterator
	 */
	public void recursiveImageToString(Iterator<Node> it, StringBuilder str, int x1, int y1, int x2, int y2){
		if(!it.isEmpty()){
			boolean horizontalCut = ((x2-x1) == (y2-y1));

			if(it.nodeType() == NodeType.LEAF){
				switch(it.getValue()){
					case OFF :
						str.append("(0 ");
						break;
					case ON :
						str.append("(1 ");
						break;
					case BOTH :
						throw new AbstractImageException(AbstractImageException.Type.MALFORMED_TREE);

				}
				str
				.append(Integer.toString(x1))
				.append(" ")
				.append(Integer.toString(y1))
				.append(" ")
				.append(Integer.toString(x2))
				.append(" ")
				.append(Integer.toString(y2))
				.append(")\n");

			}
			else if(it.nodeType() == NodeType.DOUBLE){
				if(horizontalCut){
					int midleY = (y1 + y2)/2;

					it.goLeft();
					recursiveImageToString(it, str, x1, y1, x2, midleY);
					it.goUp();
					it.goRight();
					recursiveImageToString(it, str, x1, midleY+1, x2, y2);
					it.goUp();
				}
				else{
					int midleX = (x1 + x2)/2;

					it.goLeft();
					recursiveImageToString(it, str, x1, y1, midleX, y2);
					it.goUp();
					it.goRight();
					recursiveImageToString(it, str, midleX+1, y1, x2, y2);
					it.goUp();
				}
			}
			else{
				throw new AbstractImageException(AbstractImageException.Type.MALFORMED_TREE);
			}
		}
	}	

	/**
	 * Create this image from a string, the previous content of this is overwriten
	 */
	public void constructFromString(String str){
		String[] arr = str.split("\n");

		if(arr.length < 2){
			throw new Error("Error while constructing this from string");
		}

		Iterator<Node> it = this.iterator();
		it.clear();
		it.goRoot();

		for(int i=0 ; i < arr.length - 1 ; i++){
			Pattern p = Pattern.compile("\\(|\\)");

			str = p.matcher(str).replaceAll("");
			String[] nodeString = str.split(" ");

			int state = new Integer(nodeString[0]);
			int x1    = new Integer(nodeString[1]);
			int y1    = new Integer(nodeString[2]);
			int x2    = new Integer(nodeString[3]);
			int y2    = new Integer(nodeString[4]);

			createNode(state, x1, y1, x2, y2, MIN_X, MIN_Y, MAX_X, MAX_Y, it);
		}
	}

	public void createNode(int state, int x1, int y1, int x2, int y2, int squareX1, int squareY1, int squareX2, int squareY2, Iterator<Node> it){
		int midleX = (x1+x2)/2;
		int midleY = (y1+y2)/2;

		if(it.isEmpty() && x1 == squareX1 && x2 == squareX2 && y1 == squareY1 && y2 == squareY2){
			switch(state){
				case 0:
					it.addValue(Node.OFF);
					break;
				case 1:
					it.addValue(Node.ON);
					break;
				case 2:
					it.addValue(Node.BOTH);
					break;
			}
		}
	}
	
	/**
	 * @pre !this.isEmpty()
	 * @return the height of this
	 */
	public int height(){
		if(this.isEmpty()){
			throw new AbstractImageException(AbstractImageException.Type.HEIGHT_EMPTY_TREE);
		}
		Iterator<Node> it = iterator();
		return recursiveHeight(it) - 1;
	}

	/**
	 * Compute the height recursively
	 * @param it an iterator
	 * @return the height in the sub-tree that begins under the current node
	 */
	private int recursiveHeight(Iterator<Node> it){
		int res = 0;

		if(!it.isEmpty()){
			int leftHeight = 0;
			int rightHeight = 0;

			it.goLeft();
			leftHeight = recursiveHeight(it);
			it.goUp();

			it.goRight();
			rightHeight = recursiveHeight(it);
			it.goUp();

			res = Math.max(leftHeight,rightHeight) + 1;
		}
		return res;
	}

	/**
	 * @return the number of nodes under this node
	 */
	public int numberOfNodes(){
		if(this.isEmpty()){
			throw new AbstractImageException(AbstractImageException.Type.NUMBER_NODES_EMPTY_TREE);
		}
		Iterator<Node> it = iterator();
		return recursiveNumberOfNodes(it);

	}

	/**
	 * Compute the number of nodes recursively
	 * @param it an iterator
	 * @return the number of nodes under the current node
	 */
	private int recursiveNumberOfNodes(Iterator<Node> it){
		int res = 0;

		if(!it.isEmpty()){
			res += 1;

			it.goLeft();
			res += recursiveNumberOfNodes(it);
			it.goUp();

			it.goRight();
			res += recursiveNumberOfNodes(it);
			it.goUp();
		}
		return res;
	}
	
	/**
	 * Prints this tree in a graphical interface
	 */
	public void plotTree(){
	}

	/**
	 * @param x abscissa of the point
	 * @param y ordinate of the point
	 * @pre !this.isEmpty()
	 * @return true if and only if the pixel defined by the point coordinates is on
	 */
	public abstract boolean isPixelOn(int x, int y);

	/**
	 * this becomes identical to image
	 * @param image an image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public abstract void affect(AbstractImage image);

	/**
	 * this becomes the negative of itself
	 * @pre !this.isEmpty();
	 */
	public abstract void videoInverse();

	/**
	 * this becomes the image rotated by 180 degrees
	 * @param image image for rotation
	 * @pre !image.isEmpty();
	 * @pre this != image
	 */
	public abstract void rotate180(AbstractImage image);

	/**
	 * this becomes the vertical mirror of image
	 * @param image an image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public abstract void mirrorV(AbstractImage image);

	/**
	 * this becomes the horizontal mirror of image
	 * @param image an image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public abstract void mirrorH(AbstractImage image);

	/**
	 * this becomes the left superior square of the image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public abstract void zoomIn(AbstractImage image);

	/**
	 * the left superior square of the image becomes this, the rest of this is off
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public abstract void zoomOut(AbstractImage image);

	/**
	 * this becomes the intersection of image1 and image2, pixel by pixel
	 * @param image an image
	 * @param image another image
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * @pre this != image1
	 * @pre this != image2
	 */
	public abstract void intersection(AbstractImage image1, AbstractImage image2);

	/**
	 * this becomes the union of image 1 and image2, pixel by pixel
	 * @param image an image
	 * @param image another image
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * @pre this != image1
	 * @pre this != image2
	 */
	public abstract void union(AbstractImage image1, AbstractImage image2);

	/**
	 * WARNING : This function must not use isPixelOn
	 * @pre !this.isEmpty();
	 * @return true if and only if the pixels of the form (x,x) in the image are all on or all off
	 */
	public abstract boolean testDiagonal();

	/**
	 * @param x abscissa of the first point
	 * @param y ordinate of the second point
	 * @param x2 abscissa of the first point
	 * @param y2 ordinate of the second point
	 * @pre !this.isEmpty()
	 * @return true if and only if the two points (x1,y1) and (x2,y2) are represented by the same leaf
	 */
	public abstract boolean sameLeaf(int x, int y, int x2, int y2);

	/**
	 * @param image an image
	 * @pre !this.isEmpty() && !image.isEmpty()
	 * @pre this != image
	 * @return true if and only if this is included in the image
	 */
	public abstract boolean isIncludedIn(AbstractImage image);

}
