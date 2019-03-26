package fr.istic.prg1.tp6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/*
TODO : write the test of the following methods

constructFromString_and_imageToString
height
numberOfNodes
isPixelOn
affect
videoInverse
rotate180
mirrorV
mirrorH
zoomIn
zoomOut
intersection
union
testDiagonal
sameLeaf
isIncludedIn
 */

public class TestImage{
	@Test
	@DisplayName("equals with one image empty")
	public void TestEquals1(){
		Image image = new Image();
		Image image2 = new Image();

		Iterator<Node> it = image.iterator();
		it.addValue(Node.OFF);

		Throwable exception = assertThrows(AbstractImageException.class, () -> {image.equals(image2);});
		assertEquals(AbstractImageException.Type.EQUALS_EMPTY_TREE, ((AbstractImageException)exception).getType());

		Throwable exception2 = assertThrows(AbstractImageException.class, () -> {image2.equals(image);});
		assertEquals(AbstractImageException.Type.EQUALS_EMPTY_TREE, ((AbstractImageException)exception2).getType());
	}

	@Test
	@DisplayName("equals with a random image")
	public void TestEquals2(){
		Image image = new Image();
		Image image2 = new Image();

		Iterator<Node> it = image.iterator();
		Iterator<Node> it2 = image2.iterator();

		it.addValue(Node.BOTH);
		it2.addValue(Node.BOTH);
		it.goLeft();
		it2.goLeft();
		it.addValue(Node.BOTH);
		it2.addValue(Node.BOTH);
		it.goLeft();
		it2.goLeft();
		it.addValue(Node.OFF);
		assertEquals(false, image.equals(image2));
		assertEquals(false, image2.equals(image));
		it2.addValue(Node.OFF);
		it.goUp();
		it2.goUp();
		it.goRight();
		it2.goRight();
		it.addValue(Node.ON);
		assertEquals(false, image.equals(image2));
		assertEquals(false, image2.equals(image));
		it2.addValue(Node.ON);
		assertEquals(true, image.equals(image2));
		assertEquals(true, image2.equals(image));
		it.goRoot();
		it2.goRoot();
		it.goRight();
		it2.goRight();
		it.addValue(Node.BOTH);
		it2.addValue(Node.BOTH);
		it.goLeft();
		it2.goLeft();
		assertEquals(true, image.equals(image2));
		assertEquals(true, image2.equals(image));
		it.addValue(Node.OFF);
		assertEquals(false, image.equals(image2));
		assertEquals(false, image2.equals(image));
		it2.addValue(Node.OFF);
		it.goUp();
		it2.goUp();
		it.goRight();
		it2.goRight();
		it.addValue(Node.BOTH);
		it2.addValue(Node.BOTH);
		it.goLeft();
		it2.goLeft();
		assertEquals(true, image.equals(image2));
		assertEquals(true, image2.equals(image));
		it.addValue(Node.ON);
		it2.addValue(Node.ON);
		it.goUp();
		it2.goUp();
		it.goRight();
		it2.goRight();
		it.addValue(Node.OFF);
		it2.addValue(Node.OFF);

		assertEquals(true, image.equals(image2));
		assertEquals(true, image2.equals(image));
	}

	@Test
	@DisplayName("imageToString on an empty one")
	public void TestImageToString3(){
		Image image = new Image();

		Throwable exception = assertThrows(AbstractImageException.class, () -> {image.imageToString();});
		assertEquals(AbstractImageException.Type.SAVE_EMPTY_TREE, ((AbstractImageException)exception).getType());
	}

	@Test
	@DisplayName("imageToString a white image, then constructFromString")
	public void TestConstructFromString_and_imageToString2(){
		Image image = new Image();
		Image image2 = new Image();
		Iterator<Node> it = image.iterator();

		it.addValue(Node.ON);

		
		image2.constructFromString(image.imageToString());
		
		assertEquals(true, image.equals(image2));
	}

	@Test
	@DisplayName("imageToString with one node")
	public void TestImageToString1(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.ON);

		assertEquals(new String("(1 0 0 255 255)\n(-1)"),image.imageToString());
	}

	@Test
	@DisplayName("imageToString with multiple nodes")
	public void TestImageToString2(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.ON);
		it.goUp();
		it.goRight();
		it.addValue(Node.BOTH);
		it.goRight();
		it.addValue(Node.ON);
		it.goUp();
		it.goLeft();
		it.addValue(Node.OFF);

		assertEquals(new String("(1 0 0 255 127)\n(0 0 128 127 255)\n(1 128 128 255 255)\n(-1)"),image.imageToString());
	}

	@Test
	@DisplayName("imageToString with a malformed tree (missing node)")
	public void TestImageToString4(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();

		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.ON);

		Throwable exception = assertThrows(AbstractImageException.class, () -> {image.imageToString();});
		assertEquals(AbstractImageException.Type.MALFORMED_TREE, ((AbstractImageException)exception).getType());
	}

	@Test
	@DisplayName("imageToString with a malformed tree (both on leaf)")
	public void TestImageToString5(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();

		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.ON);
		it.goUp();
		it.goRight();
		it.addValue(Node.BOTH);

		Throwable exception = assertThrows(AbstractImageException.class, () -> {image.imageToString();});
		assertEquals(AbstractImageException.Type.MALFORMED_TREE, ((AbstractImageException)exception).getType());
	}

	@Test
	@DisplayName("imageToString a black image, then constructFromString")
	public void TestConstructFromString_and_imageToString3(){
		Image image = new Image();
		Image image2 = new Image();
		Iterator<Node> it = image.iterator();

		it.addValue(Node.OFF);

		image2.constructFromString(image.imageToString());

		assertEquals(true, image.equals(image2));
	}

	@Test
	@DisplayName("imageToString a random image, then constructFromString")
	public void TestConstructFromString_and_imageToString4(){
		Image image = new Image();
		Image image2 = new Image();
		Iterator<Node> it = image.iterator();

		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.BOTH);
		assertEquals(1, image.height());
		it.goLeft();
		it.addValue(Node.OFF);
		assertEquals(2, image.height());
		it.goUp();
		it.goRight();
		it.addValue(Node.ON);
		it.goRoot();
		it.goRight();
		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.OFF);
		it.goUp();
		it.goRight();
		it.addValue(Node.BOTH);
		assertEquals(2, image.height());
		it.goLeft();
		it.addValue(Node.ON);
		it.goUp();
		it.goRight();
		it.addValue(Node.OFF);

		
		image2.constructFromString(image.imageToString());

		assertEquals(true, image.equals(image2));
	}

	@Test
	@DisplayName("height of an image of only one color")
	public void TestHeight1(){
		Image image = new Image();
		Throwable exception = assertThrows(AbstractImageException.class, () -> {image.height();});
		assertEquals(AbstractImageException.Type.HEIGHT_EMPTY_TREE, ((AbstractImageException)exception).getType());
	}

	@Test
	@DisplayName("height of a an image of one color")
	public void TestHeight2(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		
		it.addValue(Node.ON);
		assertEquals(0, image.height());
	}

	@Test
	@DisplayName("height of a random image")
	public void TestHeight3(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();

		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.BOTH);
		assertEquals(1, image.height());
		it.goLeft();
		it.addValue(Node.OFF);
		assertEquals(2, image.height());
		it.goUp();
		it.goRight();
		it.addValue(Node.ON);
		it.goRoot();
		it.goRight();
		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.OFF);
		it.goUp();
		it.goRight();
		it.addValue(Node.BOTH);
		assertEquals(2, image.height());
		it.goLeft();
		it.addValue(Node.ON);
		it.goUp();
		it.goRight();
		it.addValue(Node.OFF);

		assertEquals(3, image.height());
	}

	@Test
	@DisplayName("numberOfNodes of an image of only one color")
	public void TestNumberOfNodes1(){
		Image image = new Image();
		Throwable exception = assertThrows(AbstractImageException.class, () -> {image.numberOfNodes();});
		assertEquals(AbstractImageException.Type.NUMBER_NODES_EMPTY_TREE, ((AbstractImageException)exception).getType());
	}

	@Test
	@DisplayName("numberOfNodes of a an image of one color")
	public void TestNumberOfNodes2(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();
		
		it.addValue(Node.ON);
		assertEquals(1, image.numberOfNodes());
	}

	@Test
	@DisplayName("numberOfNodes of a random image")
	public void TestNumberOfNodes3(){
		Image image = new Image();
		Iterator<Node> it = image.iterator();

		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.BOTH);
		assertEquals(2, image.numberOfNodes());
		it.goLeft();
		it.addValue(Node.OFF);
		assertEquals(3, image.numberOfNodes());
		it.goUp();
		it.goRight();
		it.addValue(Node.ON);
		it.goRoot();
		it.goRight();
		it.addValue(Node.BOTH);
		it.goLeft();
		it.addValue(Node.OFF);
		it.goUp();
		it.goRight();
		it.addValue(Node.BOTH);
		assertEquals(7, image.numberOfNodes());
		it.goLeft();
		it.addValue(Node.ON);
		it.goUp();
		it.goRight();
		it.addValue(Node.OFF);

		assertEquals(9, image.numberOfNodes());
	}

	@Test
	@DisplayName("")
	public void TestIsPixelOn1(){
	}

	@Test
	@DisplayName("")
	public void TestAffect1(){
	}

	@Test
	@DisplayName("")
	public void TestVideoInverse1(){
	}

	@Test
	@DisplayName("")
	public void TestRotate1801(){
	}

	@Test
	@DisplayName("")
	public void TestMirrorV1(){
	}

	@Test
	@DisplayName("")
	public void TestMirrorH1(){
	}

	@Test
	@DisplayName("")
	public void TestZoomIn1(){
	}

	@Test
	@DisplayName("")
	public void TestZoomOut1(){
	}

	@Test
	@DisplayName("")
	public void TestIntersection1(){
	}

	@Test
	@DisplayName("")
	public void TestUnion1(){
	}

	@Test
	@DisplayName("")
	public void TestTestDiagonal1(){
	}

	@Test
	@DisplayName("")
	public void TestSameLeaf1(){
	}

	@Test
	@DisplayName("")
	public void TestIsIncludedIn1(){
	}
}
