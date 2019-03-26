package fr.istic.prg1.tp6;

public class Image extends AbstractImage{
	/**
	 * @param x abscissa of the point
	 * @param y ordinate of the point
	 * @pre !this.isEmpty()
	 * @return true if and only if the pixel defined by the point coordinates is on
	 */
	public boolean isPixelOn(int x, int y){
		return false;
	}

	/**
	 * this becomes identical to image
	 * @param image an image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public void affect(AbstractImage image){
	}

	/**
	 * this becomes the negative of itself
	 * @pre !this.isEmpty(){
	}
	 */
	public void videoInverse(){
	}

	/**
	 * this becomes the image rotated by 180 degrees
	 * @param image image for rotation
	 * @pre !image.isEmpty(){
	}
	 * @pre this != image
	 */
	public void rotate180(AbstractImage image){
	}

	/**
	 * this becomes the vertical mirror of image
	 * @param image an image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public void mirrorV(AbstractImage image){
	}

	/**
	 * this becomes the horizontal mirror of image
	 * @param image an image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public void mirrorH(AbstractImage image){
	}

	/**
	 * this becomes the left superior square of the image
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public void zoomIn(AbstractImage image){
	}

	/**
	 * the left superior square of the image becomes this, the rest of this is off
	 * @pre !image.isEmpty()
	 * @pre this != image
	 */
	public void zoomOut(AbstractImage image){
	}

	/**
	 * this becomes the intersection of image1 and image2, pixel by pixel
	 * @param image an image
	 * @param image another image
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * @pre this != image1
	 * @pre this != image2
	 */
	public void intersection(AbstractImage image1, AbstractImage image2){
	}

	/**
	 * this becomes the union of image 1 and image2, pixel by pixel
	 * @param image an image
	 * @param image another image
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * @pre this != image1
	 * @pre this != image2
	 */
	public void union(AbstractImage image1, AbstractImage image2){
	}

	/**
	 * WARNING : This function must not use isPixelOn
	 * @pre !this.isEmpty(){
	}
	 * @return true if and only if the pixels of the form (x,x) in the image are all on or all off
	 */
	public boolean testDiagonal(){
		return false;
	}

	/**
	 * @param x abscissa of the first point
	 * @param y ordinate of the second point
	 * @param x2 abscissa of the first point
	 * @param y2 ordinate of the second point
	 * @pre !this.isEmpty()
	 * @return true if and only if the two points (x1,y1) and (x2,y2) are represented by the same leaf
	 */
	public boolean sameLeaf(int x, int y, int x2, int y2){
		return false;
	}

	/**
	 * @param image an image
	 * @pre !this.isEmpty() && !image.isEmpty()
	 * @pre this != image
	 * @return true if and only if this is included in the image
	 */
	public boolean isIncludedIn(AbstractImage image){
		return false;
	}
}
