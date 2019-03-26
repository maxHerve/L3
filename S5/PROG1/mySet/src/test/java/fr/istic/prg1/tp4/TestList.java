package fr.istic.prg1.tp4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestList{

	@Test
	@DisplayName("Test if an empty list isEmpty")
	public void testIsEmpty(){
		List<Integer> liste = new List<Integer>();
		assertEquals(true,liste.isEmpty());
	}

	@Test
	@DisplayName("Test the size of an empty list")
	public void testGetSize(){
		List<Integer> liste = new List<Integer>();
		assertEquals(0,liste.getSize());
	}

	@Test
	@DisplayName("Test addLeft")
	public void testAddLeft(){
		List<Integer> liste = new List<Integer>();
		Iterator<Integer> it = liste.iterator();
		it.addLeft(new Integer(0));
		assertEquals(new Integer(0),it.getValue());
		it.addLeft(new Integer(Integer.MAX_VALUE));
		assertEquals(new Integer(Integer.MAX_VALUE),it.getValue());
		it.addLeft(new Integer(-40));
		assertEquals(new Integer(-40),it.getValue());
		it.addLeft(new Integer(Integer.MIN_VALUE));
		assertEquals(new Integer(Integer.MIN_VALUE),it.getValue());
		it.addLeft(new Integer(500));
		assertEquals(new Integer(500),it.getValue());

		it.restart();

		assertEquals(new Integer(500),it.getValue());
		assertEquals(new Integer(Integer.MIN_VALUE),it.goForward());
		assertEquals(new Integer(-40),it.goForward());
		assertEquals(new Integer(Integer.MAX_VALUE),it.goForward());
		assertEquals(new Integer(0),it.goForward());

		Throwable exception = assertThrows(ListException.class,() ->{it.addLeft(null);});
		assertEquals(ListException.type.ADDING_NULL_VALUE,((ListException)exception).getType());
	}
	
	@Test
	@DisplayName("Test addRight")
	public void testAddRight(){
		List<Integer> liste = new List<Integer>();
		Iterator<Integer> it = liste.iterator();
		it.addRight(new Integer(0));
		assertEquals(new Integer(0),it.getValue());
		it.addRight(new Integer(Integer.MAX_VALUE));
		assertEquals(new Integer(Integer.MAX_VALUE),it.getValue());
		it.addRight(new Integer(-40));
		assertEquals(new Integer(-40),it.getValue());
		it.addRight(new Integer(Integer.MIN_VALUE));
		assertEquals(new Integer(Integer.MIN_VALUE),it.getValue());
		it.addRight(new Integer(500));
		assertEquals(new Integer(500),it.getValue());

		it.restart();

		assertEquals(new Integer(0),it.getValue());
		assertEquals(new Integer(Integer.MAX_VALUE),it.goForward());
		assertEquals(new Integer(-40),it.goForward());
		assertEquals(new Integer(Integer.MIN_VALUE),it.goForward());
		assertEquals(new Integer(500),it.goForward());

		Throwable exception = assertThrows(ListException.class,() ->{it.addRight(null);});
		assertEquals(ListException.type.ADDING_NULL_VALUE,((ListException)exception).getType());
	}

	@Test
	@DisplayName("Test isOnFlag")
	public void testIsOnFlag(){
		List<Integer> liste = new List<Integer>();
		Iterator<Integer> it = liste.iterator();

		assertEquals(true,it.isOnFlag());
		it.goForward();
		assertEquals(true,it.isOnFlag());
		it.goBackward();
		assertEquals(true,it.isOnFlag());

		it.restart();
		assertEquals(true,it.isOnFlag());

		it.addLeft(new Integer(0));
		assertEquals(false,it.isOnFlag());
		it.goForward();
		assertEquals(true,it.isOnFlag());
		it.goForward();
		assertEquals(false,it.isOnFlag());
		it.goForward();
		assertEquals(true,it.isOnFlag());
		
		it.restart();
		it.goBackward();
		assertEquals(true,it.isOnFlag());
		it.addRight(new Integer(0));
		assertEquals(false,it.isOnFlag());
	}

	@Test
	@DisplayName("Test the size and isEmpty on a non empty list")
	public void testNonEmptyListSize(){
		List<Integer> liste = new List<Integer>();
		Iterator<Integer> it = liste.iterator();

		assertEquals(0,liste.getSize());
		assertEquals(true,liste.isEmpty());
		it.addLeft(new Integer(0));
		assertEquals(1,liste.getSize());
		assertEquals(false,liste.isEmpty());
		it.addRight(new Integer(5));
		assertEquals(2,liste.getSize());
		assertEquals(false,liste.isEmpty());
	}

	@Test
	@DisplayName("Test remove")
	public void testRemove(){
		List<Integer> liste = new List<Integer>();
		Iterator<Integer> it = liste.iterator();
		
		Throwable exception = assertThrows(ListException.class,() ->{it.remove();});
		assertEquals(ListException.type.DELETE_FLAG,((ListException)exception).getType());

		it.addLeft(new Integer(0));
		it.remove();
		assertEquals(null,it.getValue());
		assertEquals(true,liste.isEmpty());

		it.addRight(1);
		it.addRight(2);
		it.addRight(3);
		it.addRight(4);
		it.addRight(5);
		it.addRight(6);
		it.addRight(7);

		it.restart();

		it.remove();
		it.goForward(); // 2
		it.remove();
		it.goForward(); // 3
		it.goForward(); // 4
		it.goForward(); // 5
		it.remove();
		it.goForward(); // 6
		it.goForward(); // 7
		it.remove();

		it.restart();

		assertEquals(3,liste.getSize());
		assertEquals(new Integer(3),it.getValue());
		assertEquals(new Integer(4),it.goForward());
		assertEquals(new Integer(6),it.goForward());
	}

	@Test
	@DisplayName("Test setValue")
	public void testSetValue(){
		List<Integer> liste = new List<Integer>();
		Iterator<Integer> it = liste.iterator();
		
		Throwable exception = assertThrows(ListException.class,() ->{it.setValue(null);});
		assertEquals(ListException.type.ADDING_NULL_VALUE,((ListException)exception).getType());
	}

	/*
	@Test
	@DisplayName("Test to make a list with too much elements in it")
	public void tooLongList(){
		List<Integer> liste = new List<Integer>();
		Iterator<Integer> it = liste.iterator();

		for(int i=0 ; i<Integer.MAX_VALUE ; i++){
			it.addLeft(i);
		}

		assertEquals(Integer.MAX_VALUE,liste.getSize());
		Throwable exception = assertThrows(ListException.class,() ->{it.addLeft(0);});
		assertEquals(ListException.type.LIST_TOO_LONG,((ListException)exception).getType());
	}
	*/
}
