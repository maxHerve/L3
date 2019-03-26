package fr.istic.prg1.tp6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class TestBinaryTree{

	@Test
	@DisplayName("setValue on the root")
	public void TestSetValue1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.setValue(4);
		assertEquals(new Integer(4),it.getValue());
	}

	@Test
	@DisplayName("setValue test value is not cloned")
	public void TestSetValue2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		Integer value = new Integer(3);
		it.setValue(value);
		assertEquals(new Integer(3),it.getValue());
		assertEquals(true,it.getValue() == value);
	}

	@Test
	@DisplayName("getValue")
	public void TestGetValue1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		assertEquals(null,it.getValue());
	}

	@Test
	@DisplayName("nodeType of the root in an empty tree")
	public void TestNodeType1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		assertEquals(NodeType.SENTINEL, it.nodeType());
	}

	@Test
	@DisplayName("addValue on the root")
	public void TestAddValue1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(-4);
		assertEquals(new Integer(-4),it.getValue());
	}
	
	@Test
	@DisplayName("isEmpty on an empty node that is the root")
	public void TestIsEmpty1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		assertEquals(true,it.isEmpty());
	}

	@Test
	@DisplayName("isEmpty on a non empty node that is the root")
	public void TestIsEmpty2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(-4);

		assertEquals(false,it.isEmpty());
	}


	@Test
	@DisplayName("addValue test that the value is not cloned")
	public void TestAddValue2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		Integer value = new Integer(3);
		it.addValue(value);
		assertEquals(new Integer(3),it.getValue());
		assertEquals(true,it.getValue() == value);
	}

	@Test
	@DisplayName("goLeft on a the root node in an emtpy tree")
	public void TestGoLeft1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.goLeft();});
		assertEquals(BinaryTreeException.Type.MOVE_AFTER_STOP_NODE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("goLeft on the root node")
	public void TestGoLeft2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(0);
		it.goLeft();
		assertNotEquals(NodeType.LEAF,it.nodeType());
	}

	@Test
	@DisplayName("goLeft on a stop node")
	public void TestGoLeft3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(0);
		it.goLeft();
		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.goLeft();});
		assertEquals(BinaryTreeException.Type.MOVE_AFTER_STOP_NODE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("goRight on a the root node in an emtpy tree")
	public void TestGoRight1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.goRight();});
		assertEquals(BinaryTreeException.Type.MOVE_AFTER_STOP_NODE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("goRight on the root node")
	public void TestGoRight2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(0);
		it.goRight();
		assertNotEquals(NodeType.LEAF,it.nodeType());
	}

	@Test
	@DisplayName("goRight on a stop node")
	public void TestGoRight3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(0);
		it.goRight();
		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.goRight();});
		assertEquals(BinaryTreeException.Type.MOVE_AFTER_STOP_NODE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("isEmpty on a non empty node that is not the root")
	public void TestIsEmpty3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(-4);
		it.goLeft();
		it.addValue(-4);

		assertEquals(false,it.isEmpty());
	}

	@Test
	@DisplayName("isEmpty on a non empty node that is not the root")
	public void TestIsEmpty4(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(-4);
		it.goLeft();

		assertEquals(true,it.isEmpty());
	}

	@Test
	@DisplayName("addValue on a non stop node")
	public void TestAddValue4(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();
		
		it.addValue(0);
		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.addValue(0);});
		assertEquals(BinaryTreeException.Type.ADD_ON_NON_STOP_NODE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("goUp on the root node")
	public void TestGoUp1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.goUp();});
		assertEquals(BinaryTreeException.Type.MOVE_BEFORE_ROOT, ((BinaryTreeException)exception).getType());

	}

	@Test
	@DisplayName("goUp on a node that is stored at the left")
	public void TestGoUp2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		Integer integer = new Integer(3);
		it.addValue(integer);
		it.goLeft();
		assertNotEquals(integer,it.getValue());
		it.goUp();

		assertEquals(integer,it.getValue());
	}

	@Test
	@DisplayName("goUp on a node that is stored at the right")
	public void TestGoUp3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		Integer integer = new Integer(3);
		it.addValue(integer);
		it.goRight();
		assertNotEquals(integer,it.getValue());
		it.goUp();

		assertEquals(integer,it.getValue());
	}

	@Test
	@DisplayName("goRight and goLeft check")
	public void TestGoRight4(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		Integer leftValue = new Integer(1);
		Integer rightValue = new Integer(1);

		it.addValue(0);
		it.goLeft();
		it.addValue(leftValue);
		it.goUp();
		it.goRight();
		it.addValue(rightValue);

		it.goUp();

		assertEquals(new Integer(0),it.getValue());
		it.goLeft();
		assertEquals(true, leftValue == it.getValue());
		it.goUp();
		it.goRight();
		assertEquals(true, rightValue == it.getValue());
	}


	@Test
	@DisplayName("nodeType of a simple left node")
	public void TestNodeType3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(-4);
		it.goLeft();
		it.addValue(5);
		it.goUp();

		assertEquals(NodeType.SIMPLE_LEFT, it.nodeType());
	}

	@Test
	@DisplayName("nodeType of a simple right node")
	public void TestNodeType4(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(-4);
		it.goRight();
		it.addValue(5);
		it.goUp();

		assertEquals(NodeType.SIMPLE_RIGHT, it.nodeType());
	}

	@Test
	@DisplayName("nodeType of a double node")
	public void TestNodeType5(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(-4);
		it.goLeft();
		it.addValue(2);
		it.goUp();
		it.goRight();
		it.addValue(1);
		it.goUp();

		assertEquals(NodeType.DOUBLE, it.nodeType());
	}

	@Test
	@DisplayName("isEmpty on an empty tree")
	public void TestTreeIsEmpty1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();

		assertEquals(true,tree.isEmpty());
	}

	@Test
	@DisplayName("isEmpty on a non empty tree")
	public void TestTreeIsEmpty2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(3);
		assertEquals(false,tree.isEmpty());
	}

	@Test
	@DisplayName("remove an empty node")
	public void TestRemove1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.remove();});
		assertEquals(BinaryTreeException.Type.REMOVE_EMPTY_NODE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("remove a double node")
	public void TestRemove2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(0);
		it.goLeft();
		it.addValue(0);
		it.goUp();
		it.goRight();
		it.addValue(0);
		it.goUp();
		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.remove();});
		assertEquals(BinaryTreeException.Type.REMOVE_DOUBLE_NODE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("remove a leaf node")
	public void TestRemove3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(1);
		it.remove();
		
		assertEquals(true, it.isEmpty());
	}

	@Test
	@DisplayName("remove a simple node")
	public void TestRemove4(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(1);
		it.goLeft();
		it.addValue(2);
		it.goUp();
		it.remove();

		assertEquals(new Integer(2),it.getValue());
		assertEquals(true,it.nodeType() == NodeType.LEAF);
	}

	@Test
	@DisplayName("remove some nodes then move")
	public void TestRemove5(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(1);
		it.goLeft();
		it.addValue(2);
		it.goLeft();
		it.addValue(4);
		it.goUp();
		it.goRight();
		it.addValue(5);
		it.goUp();
		it.goUp();
		it.goRight();
		it.addValue(3);
		it.goLeft();
		it.addValue(6);

		it.goUp();
		it.goUp();

		it.goLeft();
		it.goRight();
		it.remove();
		assertEquals(true, it.isEmpty());

		it.goUp();
		assertEquals(new Integer(2), it.getValue());
		it.remove();
		assertEquals(new Integer(4), it.getValue());
		it.remove();
		assertEquals(true, it.isEmpty());
		it.goUp();
		it.remove();
		assertEquals(new Integer(3), it.getValue());
		it.goLeft();
		it.remove();
		assertEquals(true, it.isEmpty());
		it.goUp();
		it.remove();
		assertEquals(true, it.isEmpty());
		assertEquals(true, tree.isEmpty());
	}

	@Test
	@DisplayName("goRoot in an empty tree")
	public void TestGoRoot1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.goRoot();

		assertEquals(true,it.isEmpty());
	}

	@Test
	@DisplayName("goRoot in a non empty tree")
	public void TestGoRoot3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(1);
		it.goLeft();
		it.addValue(2);
		it.goRight();
		it.addValue(3);
		it.goLeft();
		it.addValue(4);
		it.goLeft();
		it.addValue(5);
		it.goLeft();
		it.addValue(6);
		it.goLeft();
		it.addValue(7);
		it.goRight();
		it.addValue(8);
		it.goUp();
		it.goLeft();
		it.addValue(9);
		it.goLeft();
		it.addValue(10);
		it.goRoot();

		assertEquals(new Integer(1),it.getValue());

		it.goLeft();
		it.goRight();
		it.goLeft();
		it.goRoot();

		assertEquals(new Integer(1),it.getValue());
	}

	@Test
	@DisplayName("goRoot on a stop node")
	public void TestGoRoot2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(3);
		it.goLeft();
		it.addValue(4);
		it.goRight();
		it.goRoot();

		assertEquals(new Integer(3),it.getValue());
	}

	@Test
	@DisplayName("clear on a root node in a non empty tree")
	public void TestClear1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(1);
		it.goLeft();
		it.addValue(2);
		it.goRight();
		it.addValue(3);
		it.goLeft();
		it.addValue(4);
		it.goRight();
		it.addValue(5);
		it.goUp();
		it.goLeft();
		it.addValue(6);
		it.goRoot();
		it.clear();

		assertEquals(true,it.isEmpty());
	}

	@Test
	@DisplayName("clear on some nodes in a non empty tree")
	public void TestClear2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(1);
		it.goLeft();
		it.addValue(2);
		it.goRight();
		it.addValue(3);
		it.goLeft();
		it.addValue(4);
		it.goRight();
		it.addValue(5);
		it.goUp();
		it.goLeft();
		it.addValue(6);

		it.goUp();
		it.clear();
		
		assertEquals(true,it.isEmpty());

		it.goUp();
		assertEquals(new Integer(3),it.getValue());
		it.clear();
		assertEquals(true,it.isEmpty());
		it.clear();
		assertEquals(true,it.isEmpty());
		it.goUp();
		assertEquals(new Integer(2),it.getValue());
		it.goUp();
		assertEquals(new Integer(1),it.getValue());
		it.clear();
		assertEquals(true, tree.isEmpty());
	}

	@Test
	@DisplayName("clear verify that father doesnt contains the removed node")
	public void TestClear3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(3);
		it.goLeft();
		it.addValue(2);
		it.clear();
		assertEquals(true, it.isEmpty());
		it.goUp();
		it.goLeft();
		assertEquals(true, it.isEmpty());
	}

	@Test
	@DisplayName("switchValue with i too high")
	public void TestSwitchValue1(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.switchValue(1);});
		assertEquals(BinaryTreeException.Type.INDEX_OUT_OF_RANGE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("switchValue with i too low")
	public void TestSwitchValue2(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();
		
		Throwable exception = assertThrows(BinaryTreeException.class, () -> {it.switchValue(0);});
		assertEquals(BinaryTreeException.Type.INDEX_OUT_OF_RANGE, ((BinaryTreeException)exception).getType());
	}

	@Test
	@DisplayName("switchValue on a non empty tree")
	public void TestSwitchValue3(){
		BinaryTree<Integer> tree = new BinaryTree<Integer>();
		Iterator<Integer> it = tree.iterator();

		it.addValue(1);
		it.goLeft();
		it.addValue(2);
		it.goUp();
		it.goRight();
		it.addValue(3);
		it.goLeft();
		it.addValue(4);
		it.goRight();
		it.addValue(5);
		it.goUp();
		it.goLeft();
		it.addValue(6);
		it.goRight();
		it.addValue(7);

		it.switchValue(4);
		assertEquals(new Integer(1), it.getValue());
		it.goRoot();

		assertEquals(new Integer(7), it.getValue());
		it.goLeft();
		assertEquals(new Integer(2), it.getValue());
		it.goUp();
		it.goRight();
		assertEquals(new Integer(3), it.getValue());
		it.goLeft();
		assertEquals(new Integer(4), it.getValue());
		it.goRight();
		assertEquals(new Integer(5), it.getValue());
		it.goUp();
		it.goLeft();
		assertEquals(new Integer(6), it.getValue());
		it.goRight();
		assertEquals(new Integer(1), it.getValue());
	}
}
