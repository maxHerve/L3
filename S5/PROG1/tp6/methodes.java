/**
 * @author Donatien Sattler, Maxime Hervé
 */
public void affect(AbstractImage image){
	assert !image.isEmpty() : "...";
	assert this != image : "...";
	this.clear();

	Iterator<Integer> it1 = this.iterator();
	Iterator<Integer> it2 = image.iterator();

	affectFromNode(it1,it2);
}

/**
 * Place it to the node related to the pixel specified
 * @pre x >= MIN_X && x <= MAX_X
 * @pre y >= MIN_Y && y <= MAX_Y
 * @param x the abscissa of the pixel
 * @param y the ordinate of the pixel
 * @param it the iterator to update
 */
private void goToNodeOfPixel(int x, int y, Iterator<Integer> it){
	assert x >= MIN_X && x <= MAX_X : "...";
	assert y >= MIN_Y && x <= MAX_Y : "...";
	it.goRoot();
	assert !it.isEmpty() : "...";

	int squareX = MIN_X;
	int squareX = MAX_X;
	int squareY = MIN_Y;
	int squareY = MAX_Y;

	boolean horizontalCut = true;

	while(it.nodeType != it.NodeType.DOUBLE){
		int midleX = (squareMinX + squareMaxX)/2;
		int midleY = (squareMinY + squareMaxY)/2;

		if(horizontalCut){
			if(y > midleY){
				it.goRight();
				squareMinY = midleY;
			}
			else
				it.goLeft();
				squareMaxY = midleY;
			}
		}
		else{
			if(x > midleX){
				it.goRight();
				squareMinX = midleX;
			}
			else
				it.goLeft();
				squareMaxX = midleX;
			}
		}
		horizontalCut = !horizontalCut;
	}
}

public boolean isPixelOn(int x, int y){
	Iterator<Integer> it = this.iterator();
	assert !this.isEmpty() : "...";
	goToNodeOfPixel(x,y,it);
	return it.getValue() == 1;
}

public void intersection(AbstractImage image1, AbstractImage image2){
	assert (!image1.isEmpty()) && (!image2.isEmpty()) : "...";
	assert (this != image1 && this != image2) : "...";

	Iterator<Integer> it1 = image1.iterator();
	Iterator<Integer> it2 = image2.iterator();
	Iterator<Integer> it = this.iterator();

	it.clear();
	recursiveIntersection(it,it1,it2);
}

/**
 * Make the intersection of it1 and it2 and put the result in it
 * @param it the destination iterator
 * @param it1 an iterator
 * @param it2 a second iterator
 */
private void recursiveIntersection(Iterator<Integer> it, Iterator<Integer> it1, Iterator<Integer> it2){
	if(it1.nodeType == NodeType.DOUBLE && it2.nodeType == NodeType.DOUBLE){
		it.addValue(0);

		int lightR = 0;
		int lightL = 0;

		it1.goLeft(); it2.goLeft(); this.goLeft();
		lightL = recursiveIntersection(it, it1, it2);

		it1.goUp(); it2.goUp(); this.goUp();
		it1.goRight(); it2.goRight(); this.goRight();
		lightR = recursiveIntersection(it, it1, it2);

		it1.goUp(); it2.goUp(); this.goUp();
		int value = (lightR == lightL) ? lightL : 2;
		it.setValue(new Integer(value));
	}
	else if(it1.nodeType == NodeType.LEAF && it2.nodeType == NodeType.LEAF){
		if(it1.getValue().isEqual(it2.getValue())){
			it.addValue(it1.getValue()); // Test case to do on addValue : the value added is a difference instance
		}
		else{
			it.addValue(new Integer(0));
		}
	}
	else if(it1.nodeType() != it2.nodeType()){
		if(it1.nodeType() == NodeType.DOUBLE){
			if(it2.getValue() == 0){
				it.addValue(0);
			}
			else{
				affectFromNode(it, it1);
			}
		}
		else{
			if(it2.getValue() == 0){
				it.addValue(0);
			}
			else{
				affectFromNode(it, it1);
			}
		}
	}
}

/**
 * Clone the sub-tree from it2 to it1
 * @param it1 the destination iterator
 * @param it2 the source iterator
 */
private void affectFromNode(Iterator<Integer> it1, Iterator<Integer> it2){
	if(it2.isEmpty()){
		it1.addValue(it2.getValue());

		if(it2.getValue() == 2){
			it1.goRight(); it2.goRight();
			affectFromNode(it1,it2);
			it1.goUp(); it2.goUp();

			it1.goLeft(); it2.goLeft();
			affectFromNode(it1,it2);
			it1.goUp(); it2.goUp();
		}
	}
}

public void goLeft(){
	stack.push(this.currentNode);
	this.currentNode = this.left;
}

public void goUp(){
	this.currentNode = this.stack.pop();
}

public void goRoot(){
	while(!this.stack.isEmpty()){
		this.goUp();
	}
}

public void remove(){
	assert this.currentNode.nodeType != NodeType.DOUBLE : "...";

	if(this.currentNode.SIMPLE_LEFT){
		Element tmp = this.currentNode;

		this.goUp();

		if(this.currentNode.left == tmp){
			this.currentNode.left = tmp.left;
		}
		else{
			this.currentNode.right = tmp.left;
		}
	}
	else if(this.currentNode.SIMPLE_RIGHT){
		Element tmp = this.currentNode;

		this.goUp();

		if(this.currentNode.left == tmp){
			this.currentNode.left = tmp.right;
		}
		else{
			this.currentNode.right = tmp.right;
		}
	}
	else{
		this.clear();
		this.goUp();
	}
}

/**
 * 
 * @pre !this.isEmpty()
 * @return the height of this
 */
public int height(){
	return 0;
}

/**
 * @return the number of nodes under this node
 */
public int numberOfNodes(){
	return 0;
}

