package fr.istic.prg1.tp4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class TestSmallSet{
	@Test
	@DisplayName("Test size of an empty set")
	void testEmptySetSize(){
		SmallSet emptySet = new SmallSet();
		assertEquals(emptySet.size(), 0);
	}
	
	@Test
	@DisplayName("Test adding values")
	void testAdd(){
		SmallSet emptySet = new SmallSet();
		TestSmallSet.initSetA(emptySet);
	}

	private static void initSetA(SmallSet testSet){
		int[] values = {0,5,60,1,3,7};

		for(int i=0 ; i<values.length ; i++){
			testSet.add(values[i]);
		}
		
		for(int i=0 ; i<values.length ; i++){
			testSet.add(values[i]);
		}
	}

	@Test
	@DisplayName("Test if the set is empty")
	void testEmpty(){
		SmallSet emptySet = new SmallSet();
		assertEquals(emptySet.isEmpty(), true);
		TestSmallSet.initSetA(emptySet);
		assertEquals(emptySet.isEmpty(), false);
	}

	@Test
	@DisplayName("Test contains")
	void testContains(){
		SmallSet testSet = new SmallSet();
		TestSmallSet.initSetA(testSet);
		
		int[] valuesInSet = {0,5,60,1,3,7};
		int[] valuesNotInSet = {2,255};
		
		for(int i=0 ; i<valuesInSet.length ; i++){
			assertEquals(testSet.contains(valuesInSet[i]),true);
		}
		
		for(int i=0 ; i<valuesNotInSet.length ; i++){
			assertEquals(testSet.contains(valuesNotInSet[i]),false);
		}
	}

	@Test
	@DisplayName("Test remove")
	void testRemove(){
		SmallSet testSet = new SmallSet();
		TestSmallSet.initSetA(testSet);
		
		testSet.remove(5);
		testSet.remove(0);
		testSet.remove(7);

		int[] valuesInSet = {60,1,3};
		int[] valuesNotInSet = {2,255,5,0,7};
		
		for(int i=0 ; i<valuesInSet.length ; i++){
			assertEquals(testSet.contains(valuesInSet[i]),true);
		}
		
		for(int i=0 ; i<valuesNotInSet.length ; i++){
			assertEquals(testSet.contains(valuesNotInSet[i]),false);
		}
	}

	private static void initSetB(SmallSet testSet){
		testSet.addInterval(0,5);
		testSet.addInterval(3,5);
		testSet.addInterval(10,22);
		testSet.addInterval(255,255);
	}

	@Test
	@DisplayName("Test add interval")
	void testAddInterval(){
		SmallSet testSet = new SmallSet();
		TestSmallSet.initSetB(testSet);

		int[] intervals = {0,5,3,5,10,22,255,255};
		int[] wrongIntervals = {6,9,23,254};

		for(int i=0 ; i<intervals.length-1 ; i+=2){
			for(int y=intervals[i] ; y<=intervals[i+1] ; y++){
				assertEquals(testSet.contains(y),true);
			}
		}
		
		for(int i=0 ; i<wrongIntervals.length-1 ; i+=2){
			for(int y=wrongIntervals[i] ; y<=wrongIntervals[i+1] ; y++){
				assertEquals(testSet.contains(y),false);
			}
		}
	}

	@Test
	@DisplayName("Test remove interval")
	void testRemoveInterval(){
		SmallSet testSet = new SmallSet();

		testSet.addInterval(0,255);
		testSet.removeInterval(6,9);
		testSet.removeInterval(23,254);

		int[] intervals = {0,5,3,5,10,22,255,255};
		int[] wrongIntervals = {6,9,23,254};

		for(int i=0 ; i<intervals.length-1 ; i+=2){
			for(int y=intervals[i] ; y<=intervals[i+1] ; y++){
				assertEquals(testSet.contains(y),true);
			}
		}
		
		for(int i=0 ; i<wrongIntervals.length-1 ; i+=2){
			for(int y=wrongIntervals[i] ; y<=wrongIntervals[i+1] ; y++){
				assertEquals(testSet.contains(y),false);
			}
		}
	}

	private static void initSetC(SmallSet testSet){
		int[] values = {0,4,60,255};

		for(int i=0 ; i<values.length ; i++){
			testSet.add(values[i]);
		}
	}

	@Test
	@DisplayName("Test union")
	void testUnion(){
		SmallSet setA = new SmallSet();
		SmallSet setB = new SmallSet();

		initSetA(setA);
		initSetC(setB);

		setA.union(setB);

		int[] valuesInSet = {0,1,3,4,5,7,60,255};
		int[] valuesNotInSet = {2,20,30,45};
		
		for(int i=0 ; i<valuesInSet.length ; i++){
			assertEquals(setA.contains(valuesInSet[i]),true);
		}
		
		for(int i=0 ; i<valuesNotInSet.length ; i++){
			assertEquals(setA.contains(valuesNotInSet[i]),false);
		}
	}

	@Test
	@DisplayName("Test difference")
	void testDifference(){
		SmallSet setA = new SmallSet();
		SmallSet setB = new SmallSet();

		initSetA(setA);
		initSetC(setB);

		setA.difference(setB);
		
		int[] valuesInSet = {1,3,5,7};
		int[] valuesNotInSet = {0,60,255};
		
		for(int i=0 ; i<valuesInSet.length ; i++){
			assertEquals(setA.contains(valuesInSet[i]),true);
		}
		
		for(int i=0 ; i<valuesNotInSet.length ; i++){
			assertEquals(setA.contains(valuesNotInSet[i]),false);
		}
	}

	@Test
	@DisplayName("Test symmetric difference")
	void testSymmetricDifference(){
		SmallSet setA = new SmallSet();
		SmallSet setB = new SmallSet();

		initSetA(setA);
		initSetC(setB);

		setA.symmetricDifference(setB);
		
		int[] valuesInSet = {1,3,4,5,7,255};
		int[] valuesNotInSet = {0,60};
		
		for(int i=0 ; i<valuesInSet.length ; i++){
			assertEquals(setA.contains(valuesInSet[i]),true);
		}
		
		for(int i=0 ; i<valuesNotInSet.length ; i++){
			assertEquals(setA.contains(valuesNotInSet[i]),false);
		}
	}

	@Test
	@DisplayName("Test complement")
	void testComplement(){
		SmallSet testSet = new SmallSet();
		TestSmallSet.initSetA(testSet);

		testSet.complement();
		
		int[] valuesInSet = {2,255};
		int[] valuesNotInSet = {0,5,60,1,3,7};
		
		for(int i=0 ; i<valuesInSet.length ; i++){
			assertEquals(testSet.contains(valuesInSet[i]),true);
		}
		
		for(int i=0 ; i<valuesNotInSet.length ; i++){
			assertEquals(testSet.contains(valuesNotInSet[i]),false);
		}
	}

	@Test
	@DisplayName("Test clear")
	void testClear(){
		SmallSet testSet = new SmallSet();
		TestSmallSet.initSetA(testSet);

		testSet.clear();
		
		for(int i=0 ; i<255 ; i++){
			assertEquals(testSet.contains(i),false);
		}

	}

	@Test
	@DisplayName("Test is included in")
	void testIsIncludedIn(){
		SmallSet emptySet = new SmallSet();
		SmallSet setA = new SmallSet();
		SmallSet setC = new SmallSet();
		SmallSet setD = new SmallSet();
		
		TestSmallSet.initSetA(setA);
		TestSmallSet.initSetC(setC);
		setD.add(0);
		setD.add(60);
		setD.add(255);

		assertEquals(emptySet.isIncludedIn(emptySet),true);
		assertEquals(emptySet.isIncludedIn(setA),true);
		assertEquals(emptySet.isIncludedIn(setC),true);
		assertEquals(emptySet.isIncludedIn(setD),true);
		assertEquals(setA.isIncludedIn(emptySet),false);
		assertEquals(setA.isIncludedIn(setA),true);
		assertEquals(setA.isIncludedIn(setC),false);
		assertEquals(setA.isIncludedIn(setD),false);
		assertEquals(setC.isIncludedIn(emptySet),false);
		assertEquals(setC.isIncludedIn(setA),false);
		assertEquals(setC.isIncludedIn(setC),true);
		assertEquals(setC.isIncludedIn(setD),false);
		assertEquals(setD.isIncludedIn(emptySet),false);
		assertEquals(setD.isIncludedIn(setA),false);
		assertEquals(setD.isIncludedIn(setC),true);
		assertEquals(setD.isIncludedIn(setD),true);
	}

	@Test
	@DisplayName("Test equals")
	void testEquals(){
		SmallSet emptySet = new SmallSet();
		SmallSet setA = new SmallSet();
		SmallSet setC = new SmallSet();
		SmallSet setD = new SmallSet();
		SmallSet setE = new SmallSet();
		
		TestSmallSet.initSetA(setA);
		TestSmallSet.initSetC(setC);
		setD.add(0);
		setD.add(60);
		setD.add(255);
		setE.add(0);
		setE.add(60);
		setE.add(255);

		assertEquals(setA.equals(setA),true);
		assertEquals(setA.equals(setC),false);
		assertEquals(setA.equals(setD),false);
		assertEquals(setA.equals(emptySet),false);
		assertEquals(setC.equals(setA),false);
		assertEquals(setC.equals(setC),true);
		assertEquals(setC.equals(setD),false);
		assertEquals(setC.equals(emptySet),false);
		assertEquals(setD.equals(setA),false);
		assertEquals(setD.equals(setC),false);
		assertEquals(setD.equals(setD),true);
		assertEquals(setD.equals(emptySet),false);
		assertEquals(emptySet.equals(setA),false);
		assertEquals(emptySet.equals(setC),false);
		assertEquals(emptySet.equals(setD),false);
		assertEquals(emptySet.equals(emptySet),true);
		assertEquals(setE.equals(setA),false);
		assertEquals(setE.equals(setC),false);
		assertEquals(setE.equals(setD),true);
	}

	@Test
	@DisplayName("Test clone")
	void testClone(){
		SmallSet emptySet = new SmallSet();
		SmallSet setA = new SmallSet();

		initSetA(setA);

		SmallSet clonedSet = setA.clone();

		assertEquals(setA == clonedSet,false);
		assertEquals(setA.equals(clonedSet),true);
	}
}
