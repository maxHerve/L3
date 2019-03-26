package fr.istic.prg1.tree;

import java.util.Scanner;

import fr.istic.prg1.tree_util.AbstractImage;
import fr.istic.prg1.tree_util.Iterator;
import fr.istic.prg1.tree_util.Node;
import fr.istic.prg1.tree_util.NodeType;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2016-04-20
 * 
 *        Classe décrivant les images en noir et blanc de 256 sur 256 pixels
 *        sous forme d'arbres binaires.
 * 
 */

public class Image extends AbstractImage {
	private static final Scanner standardInput = new Scanner(System.in);

	public Image() {
		super();
	}

	public static void closeAll() {
		standardInput.close();
	}

	/**
	 * @param x
	 *            abscisse du point
	 * @param y
	 *            ordonnée du point
	 * @pre !this.isEmpty()
	 * @return true, si le point (x, y) est allumé dans this, false sinon
	 */
	@Override
	public boolean isPixelOn(int x, int y) {
		Iterator<Node> it = this.iterator();
		assert !this.isEmpty() : "...";
		goToNodeOfPixel(x,y,it);
		return it.getValue().state == 1;
	}
	
	/**
	 * Place it to the node related to the pixel specified
	 * @pre x >= MIN_X && x <= MAX_X
	 * @pre y >= MIN_Y && y <= MAX_Y
	 * @param x the abscissa of the pixel
	 * @param y the ordinate of the pixel
	 * @param it the iterator to update
	 */
	private void goToNodeOfPixel(int x, int y, Iterator<Node> it)
	{
		//assert x >= 0 && x <= 255 : "...";
		//assert y >= 0 && x <= 255 : "...";
		it.goRoot();
		//assert !it.isEmpty() : "...";

		int squareMinX = 0;
		int squareMaxX = 255;
		int squareMinY = 0;
		int squareMaxY = 255;

		boolean horizontalCut = true;

		while(it.getValue().state == 2)
		{
			int midleX = (squareMinX + squareMaxX)/2;
			int midleY = (squareMinY + squareMaxY)/2;

			if(horizontalCut)
			{
				if(y > midleY)
				{
					it.goRight();
					squareMinY = midleY;
				}
				else
				{
					it.goLeft();
					squareMaxY = midleY;
				}
			}
			else
			{
				if(x > midleX)
				{
					it.goRight();
					squareMinX = midleX;
				}
				else
				{
					it.goLeft();
					squareMaxX = midleX;
				}
			}
			horizontalCut = !horizontalCut;
		}
	}

	/**
	 * this devient identique à image2.
	 *
	 * @param image2
	 *            image à copier
	 *
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void affect(AbstractImage image2) 
	{
		//assert !image2.isEmpty() : "...";
		//assert this != image2 : "...";

		Iterator<Node> it1 = this.iterator();
		it1.clear();
		Iterator<Node> it2 = image2.iterator();

		affectFromNode(it1,it2,0);
	}
	
	/**
	 * Clone the sub-tree from it2 to it1
	 * @param it1 the destination iterator
	 * @param it2 the source iterator
	 */
	private void affectFromNode(Iterator<Node> it1, Iterator<Node> it2, int h)
	{
		if(!it2.isEmpty())
		{
			it1.addValue(it2.getValue());

			if(it2.getValue().state == 2)
			{
				if(h < 15)
				{
					it1.goRight(); it2.goRight();
					affectFromNode(it1,it2,h+1);
					it1.goUp(); it2.goUp();

					it1.goLeft(); it2.goLeft();
					affectFromNode(it1,it2,h+1);
					it1.goUp(); it2.goUp();
				}
				else
				{
					it1.goRight(); it2.goRight();
					it1.addValue(Node.valueOf(affectAndConvert(it2)));
					it1.goUp(); it2.goUp();

					it1.goLeft(); it2.goLeft();
					it1.addValue(Node.valueOf(affectAndConvert(it2)));
					it1.goUp(); it2.goUp();
					
				}
			}
		}
	}
	
	public int affectAndConvert(Iterator<Node> it2)
	{
		if(it2.getValue().state == 2)
		{
			int [] tab = affectAndConvertRecur(it2);
			
			if(tab[0] > tab[1])
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		else
		{
			return it2.getValue().state;
		}
	}
	public int [] affectAndConvertRecur(Iterator<Node> it2)
	{
		int [] tab = new int [2];
		int [] tmp;
		
		if(it2.getValue().state == 2)
		{
			it2.goRight();
			tab = affectAndConvertRecur(it2);
			it2.goUp();
			it2.goLeft();
			tmp = affectAndConvertRecur(it2);
			tab[0] += tmp[0]; tab[1] += tmp[1];
			it2.goUp();
		}
		else
		{
			if(it2.getValue().state == 0)
			{
				tab[0] += 1;
			}
			else
			{
				tab[1] += 1;
			}
		}
		return tab;
	}

	/**
	 * this devient rotation de image2 à 180 degrés.
	 *
	 * @param image2
	 *            image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate180(AbstractImage image2) 
	{
		Iterator<Node> it = this.iterator();
		it.clear();
		Iterator<Node> it2 = image2.iterator();
		
		rotate180Rec(it,it2);	
	}
	
	private void rotate180Rec(Iterator<Node> it, Iterator<Node> it2)
	{
		it.addValue(it2.getValue());
		if(it2.getValue().state == 2)
		{
			it.goRight();
			it2.goLeft();
			rotate180Rec(it,it2);
			it.goUp();
			it2.goUp();
			it.goLeft();
			it2.goRight();
			rotate180Rec(it,it2);
			it.goUp();
			it2.goUp();
		}
	}

	/**
	 * this devient rotation de image2 à 90 degrés dans le sens des aiguilles
	 * d'une montre.
	 *
	 * @param image2
	 *            image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate90(AbstractImage image2) {
			    
		Iterator<Node> it = this.iterator();
		it.clear();
		Iterator<Node> it2 = image2.iterator();
		Iterator<Node> it3 = image2.iterator();
		
		rotate90Rec(it,it2,it3,true);
	}
	
	private void rotate90Rec(Iterator<Node> it, Iterator<Node> it2, Iterator<Node> it3, boolean inverse)
	{
		
		if(inverse)
		{
			if(it2.getValue().state == 2)
			{
				it.addValue(Node.valueOf(2));
				it.goRight(); it2.goLeft(); it3.goLeft();
				rotate90Rec(it,it2,it3,false);
				it.goUp(); it2.goUp(); it3.goUp();
				it.goLeft(); it2.goRight(); it3.goRight();
				rotate90Rec(it,it2,it3,false);
				it.goUp(); it2.goUp(); it3.goUp();
			}
			else
			{
				it.addValue(Node.valueOf(it2.getValue().state));
			}
		}
		else
		{
			if(it3.getValue().state == 2)
			{
				it.addValue(Node.valueOf(2));
				it.goRight(); it2.goRight(); it3.goLeft();
				rotate90Rec(it,it2,it3,true);
				it.goUp(); it2.goUp(); it3.goUp();
				it.goLeft(); it2.goLeft(); it3.goRight();
				rotate90Rec(it,it2,it3,true);
				it.goUp(); it2.goUp(); it3.goUp();
			}
			else
			{
				it.addValue(Node.valueOf(it3.getValue().state));
			}
		}
		
	}

	/**
	 * this devient inverse vidéo de this, pixel par pixel.
	 *
	 * @pre !image.isEmpty()
	 */
	@Override
	public void videoInverse() {
		
		Iterator<Node> it = this.iterator();
		
		videoInverseRecur(it);
	}
	
	private void videoInverseRecur(Iterator<Node> it)
	{
		if(it.getValue().state == 2)
		{
			it.goRight();
			videoInverseRecur(it);
			it.goUp();
			it.goLeft();
			videoInverseRecur(it);
			it.goUp();
		}
		else if(it.nodeType() == NodeType.LEAF)
		{
			if(it.getValue().state == 1)
			{
				it.setValue(Node.valueOf(0));
			}
			else
			{
				it.setValue(Node.valueOf(1));
			}
		}
	}

	/**
	 * this devient image miroir verticale de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorV(AbstractImage image2) {
		
		Iterator<Node> it = this.iterator();
		it.clear();
		Iterator<Node> it2 = image2.iterator();
		
		mirrorVRec(it, it2, true);
		
	}
	
	public void mirrorVRec(Iterator<Node> it, Iterator<Node> it2, boolean inverse)
	{
		
		if(it2.getValue().state == 2)
		{
			it.addValue(Node.valueOf(2));
			
			if(inverse == true)
			{
				it.goRight();
				it2.goLeft();
				mirrorHRec(it,it2,true);
				it.goUp(); it2.goUp();
				it.goLeft();
				it2.goRight();
				mirrorHRec(it,it2,true);
				it.goUp(); it2.goUp();
			}
			else
			{
				it.goRight(); it2.goRight();
				mirrorHRec(it,it2,false);
				it.goUp(); it2.goUp();
				it.goLeft(); it2.goLeft();
				mirrorHRec(it,it2,false);
				it.goUp(); it2.goUp();
			}
		}
		else
		{
			it.addValue(Node.valueOf(it2.getValue().state));
		}
		
	}
	
	

	/**
	 * this devient image miroir horizontale de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorH(AbstractImage image2) {
		
		Iterator<Node> it = this.iterator();
		it.clear();
		Iterator<Node> it2 = image2.iterator();
		
		mirrorHRec(it, it2, true);
	}
	
	private void mirrorHRec(Iterator<Node> it, Iterator<Node> it2, boolean inverse)
	{
		
		if(it2.getValue().state == 2)
		{
			it.addValue(Node.valueOf(2));
			
			if(inverse == false)
			{
				it.goRight();
				it2.goLeft();
				mirrorHRec(it,it2,true);
				it.goUp(); it2.goUp();
				it.goLeft();
				it2.goRight();
				mirrorHRec(it,it2,true);
				it.goUp(); it2.goUp();
			}
			else
			{
				it.goRight(); it2.goRight();
				mirrorHRec(it,it2,false);
				it.goUp(); it2.goUp();
				it.goLeft(); it2.goLeft();
				mirrorHRec(it,it2,false);
				it.goUp(); it2.goUp();
			}
		}
		else
		{
			it.addValue(Node.valueOf(it2.getValue().state));
		}
		
	}

	/**
	 * this devient quart supérieur gauche de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * 
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomIn(AbstractImage image2) {
		
		Iterator<Node> it = this.iterator();
		it.clear();
		Iterator<Node> it2 = image2.iterator();
		
		boolean zoom = false;
		int rank = 1;
		
		while(!zoom)
		{
			if(it2.getValue().state == 2)
			{
				if(rank != 3)
				{
					it2.goLeft();
					rank++;
				}
				else
				{
					affectFromNode(it, it2, 0);
					zoom = true;
				}
			}
			else 
			{
				affectFromNode(it, it2, 0);
				zoom = true;
			}
		}
		
	}

	/**
	 * Le quart supérieur gauche de this devient image2, le reste de this
	 * devient éteint.
	 * 
	 * @param image2
	 *            image à réduire
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomOut(AbstractImage image2) 
	{
		Iterator<Node> it = this.iterator();
		Iterator<Node> it2 = image2.iterator();
		
		it.clear();
		it.addValue(Node.valueOf(2));
		it.goRight();
		it.addValue(Node.valueOf(0));
		it.goUp();
		it.goLeft();
		it.addValue(Node.valueOf(2));
		it.goRight();
		it.addValue(Node.valueOf(0));
		it.goUp();
		it.goLeft();
		affectFromNode(it, it2, 2);
		it.goRoot();
		reorganiseTree(it);
	}

	/**
	 * this devient l'intersection de image1 et image2 au sens des pixels
	 * allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void intersection(AbstractImage image1, AbstractImage image2) {
		//assert (!image1.isEmpty()) && (!image2.isEmpty()) : "...";
		//assert (this != image1 && this != image2) : "...";

		Iterator<Node> it1 = image1.iterator();
		Iterator<Node> it2 = image2.iterator();
		Iterator<Node> it = this.iterator();

		it.clear();
		recursiveIntersection(it,it1,it2,0);
		it.goRoot();
		reorganiseTree(it);
	}
	
	/**
	 * Make the intersection of it1 and it2 and put the result in it
	 * @param it the destination iterator
	 * @param it1 an iterator
	 * @param it2 a second iterator
	 */
	private void recursiveIntersection(Iterator<Node> it, Iterator<Node> it1, Iterator<Node> it2, int h)
	{
		if(it1.getValue().state == 2 && it2.getValue().state == 2)
		{
			it.addValue(Node.valueOf(2));


			it1.goLeft(); it2.goLeft(); it.goLeft();
			recursiveIntersection(it, it1, it2, h+1);

			it1.goUp(); it2.goUp(); it.goUp();
			it1.goRight(); it2.goRight(); it.goRight();
			recursiveIntersection(it, it1, it2, h+1);

			it1.goUp(); it2.goUp(); it.goUp();

		}
		else if(it1.getValue().state != 2 && it2.getValue().state != 2)
		{
			if(it1.getValue().state == it2.getValue().state)
			{
				it.addValue(it1.getValue()); // Test case to do on addValue : the value added is a difference instance
			}
			else
			{
				it.addValue(Node.valueOf(0));
			}
		}
		else if(it1.getValue().state != it2.getValue().state)
		{
			if(it1.getValue().state == 2)
			{
				if(it2.getValue().state == 0)
				{
					it.addValue(Node.valueOf(0));
				}
				else
				{
					affectFromNode(it, it1, h);
				}
			}
			else
			{
				if(it2.getValue().state == 0)
				{
					it.addValue(Node.valueOf(0));
				}
				else
				{
					affectFromNode(it, it1, h);
				}
			}
		}
	}

	/**
	 * this devient l'union de image1 et image2 au sens des pixels allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void union(AbstractImage image1, AbstractImage image2) {
		
		Iterator<Node> it1 = image1.iterator();
		Iterator<Node> it2 = image2.iterator();
		Iterator<Node> it = this.iterator();

		it.clear();
		recursiveUnion(it,it1,it2,0);
		it.goRoot();
		reorganiseTree(it);
	}
	
	private void recursiveUnion(Iterator<Node> it, Iterator<Node> it1, Iterator<Node> it2, int h)
	{
		if(it1.getValue().state == 2 && it2.getValue().state == 2)
		{
			it.addValue(Node.valueOf(2));


			it1.goLeft(); it2.goLeft(); it.goLeft();
			recursiveUnion(it, it1, it2, h+1);

			it1.goUp(); it2.goUp(); it.goUp();
			it1.goRight(); it2.goRight(); it.goRight();
			recursiveUnion(it, it1, it2, h+1);

			it1.goUp(); it2.goUp(); it.goUp();

		}
		else if(it1.getValue().state == 1 || it2.getValue().state == 1)
		{
			it.addValue(Node.valueOf(1));
		}
		else
		{
			if(it1.getValue().state == 2)
			{
				affectFromNode(it, it1, h);
			}
			else
			{
				affectFromNode(it, it2, h);
			}
			
		}
	}

	/**
	 * Attention : cette fonction ne doit pas utiliser la commande isPixelOn
	 * 
	 * @return true si tous les points de la forme (x, x) (avec 0 <= x <= 255)
	 *         sont allumés dans this, false sinon
	 */
	@Override
	public boolean testDiagonal() {
		
		Iterator<Node> it = this.iterator();
		
		
		return testDiagonalRec(it, true, true);
		
	}
	
	public boolean testDiagonalRec(Iterator<Node> it, boolean carre, boolean gauche)
	{
		if(!it.isEmpty())
		{
			if(it.getValue().state == 1)
			{
				return true;
			}
			if(!carre)
			{
				
				if(it.getValue().state == 2)
				{
					boolean test;
					if(gauche)
					{
						it.goLeft();
					}
					else
					{
						it.goRight();
					}
					
					test = testDiagonalRec(it,true,gauche);
					it.goUp();
					return test;
				}
				else { return false; }
			}
			else
			{
				if(it.getValue().state == 2)
				{
					boolean hautGauche, basDroit;
					
					it.goLeft();
					hautGauche = testDiagonalRec(it,false,true);
					it.goUp();
					it.goRight();
					basDroit = testDiagonalRec(it,false,false);
					it.goUp();
					
					if(hautGauche && basDroit)
					{
						return true;
					}		
				}
			}
		}
		
		return false;
	}

	/**
	 * @param x1
	 *            abscisse du premier point
	 * @param y1
	 *            ordonnée du premier point
	 * @param x2
	 *            abscisse du deuxième point
	 * @param y2
	 *            ordonnée du deuxième point
	 * @pre !this.isEmpty()
	 * @return true si les deux points (x1, y1) et (x2, y2) sont représentés par
	 *         la même feuille de this, false sinon
	 */
	@Override
	public boolean sameLeaf(int x1, int y1, int x2, int y2) 
	{
		
		boolean horizontal = true;
		
		Iterator<Node> it = iterator();
		
		int xMin = 0;
		int xMax = 255;
		int yMin = 0;
		int yMax = 255;
	
		
		while(!it.isEmpty())
		{
			if(it.getValue().state == 2)
			{
				int midleX = (xMin + xMax)/2;
				int midleY = (yMin + yMax)/2;

				if(horizontal)
				{
					if(y1 > midleY && y2 > midleY) 
					{
						it.goRight();
						yMin = midleY;
					}
					else if(y1 <= midleY && y2 <= midleY)
					{
						it.goLeft();
						yMax = midleY;
					}
					else
					{
						return false;
					}
				}
				else
				{
					if(x1 > midleX && x2 > midleX)
					{
						it.goRight();
						xMin = midleX;
					}
					else if(x1 <= midleX && x2 <= midleX)
					{
						it.goLeft();
						xMax = midleX;
					}
					else
					{
						return false;
					}
				}
				horizontal = !horizontal;
			}
			else
			{
				return true;
			}
			
		}
		
		return false;
		
	}

	/**
	 * @param image2
	 *            autre image
	 * @pre !this.isEmpty() && !image2.isEmpty()
	 * @return true si this est incluse dans image2 au sens des pixels allumés
	 *         false sinon
	 */
	@Override
	public boolean isIncludedIn(AbstractImage image2) {
		
		Iterator<Node> it = this.iterator();
		Iterator<Node> it2 = image2.iterator();
		
		return isIncludedInRec(it, it2);
		
	}
	
	private boolean isIncludedInRec(Iterator<Node> it, Iterator<Node> it2)
	{
		if(it2.getValue().state == 2 && it.getValue().state == 2)
		{
			boolean inc1, inc2;
			
			it.goRight(); it2.goRight();
			inc1 = isIncludedInRec(it, it2);
			it.goUp(); it2.goUp();
			it.goLeft(); it2.goLeft();
			inc2 = isIncludedInRec(it, it2);
			it.goUp(); it2.goUp();
			
			if(inc1 && inc2)
			{
				return true;
			}
			
			return false;
		}
		else if(it2.getValue().state == it.getValue().state)
		{
			return true;
		}
		else if(it.getValue().state == 1)
		{
			return false;
		}
		else if(it.getValue().state == 2 && it2.getValue().state == 0)
		{
			return false;
		}
		else { return true; }
	}
	
	private void reorganiseTree(Iterator<Node> it)
	{
		if(it.getValue().state == 2)
		{
			int right,left;
			
			it.goLeft();
			reorganiseTree(it);
			left = it.getValue().state;
			it.goUp();
			it.goRight();
			reorganiseTree(it);
			right = it.getValue().state;
			it.goUp();
			
			if(right == left && right != 2)
			{
				it.clear();
				it.addValue(Node.valueOf(right));
			}
		}
	}

}
