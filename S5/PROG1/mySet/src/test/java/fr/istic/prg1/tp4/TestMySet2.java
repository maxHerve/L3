package fr.istic.prg1.tp4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import fr.istic.prg1.tp4.Iterator;
import fr.istic.prg1.tp4.MySet;
import fr.istic.prg1.tp4.MySetException;
import fr.istic.prg1.tp4.SmallSet;
import fr.istic.prg1.tp4.SubSet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.security.*;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;



/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 2.0
 * @since 2015-06-15
 * 
 *        Classe contenant les tests unitaires pour la classe MySet.
 */

public class TestMySet2 {
	private static String testDataBaseDir = "test_data/";

	/**
	 * @param l1
	 *            premier ensemble
	 * @param l2
	 *            deuxième ensemble
	 * @return true si les ensembles l1 et l2 sont égaux, false sinon
	 */
	public static boolean compareMySets(MySet l1, MySet l2) {
		Iterator<SubSet> it1 = l1.iterator();
		Iterator<SubSet> it2 = l2.iterator();
		boolean bool = true;
		while (!it1.isOnFlag() && bool) {
			SubSet s1 = it1.getValue();
			SubSet s2 = it2.getValue();
			if (!compareSubSets(s1, s2)) {
				bool = false;
			}
			it1.goForward();
			it2.goForward();
		}
		return bool && it1.isOnFlag() && it2.isOnFlag();
	}

	public static boolean compareSubSets(SubSet s1, SubSet s2) {
		return s1.rank == s2.rank && compareSmallSets(s1.set, s2.set);
	}

	public static boolean compareSmallSets(SmallSet s1, SmallSet s2) {
		return (s1.size() == 0 || s2.size() == 0) ? false : s1.toString()
				.equals(s2.toString());
	}

	/**
	 * @param mySet
	 *            ensemble à tester
	 * @return true si mySet est bien un ensemble creux
	 */
	public static boolean testSparsity(MySet mySet) {
		Iterator<SubSet> it = mySet.iterator();
		while (!it.isOnFlag() && it.getValue().set.size() != 0) {
			it.goForward();
		}
		return it.isOnFlag();
	}

	public static MySet readFileToMySet(String fileName) {
		MySet set = new MySet();
		try {
			InputStream is = null;
			is = new FileInputStream(fileName);
			set.add(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Error("Fichier introuvable !", e);
		}
		return set;
	}

	@Test
	public void testSetCreation() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("test-desordre.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		assertEquals(true, compareMySets(mySet1, mySet2));
	}

	@Test
	public void testContainment1() {
		MySet mySet = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		boolean bool1 = mySet.contains(128);
		boolean bool2 = mySet.contains(129);
		boolean bool3 = mySet.contains(32767);
		boolean bool4 = mySet.contains(22222);
		assertEquals(true, bool1 && !bool2 && bool3 && !bool4);
	}

	@Test
	public void testContainment2() {
		MySet mySet = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		boolean bool = mySet.contains(32511);
		assertEquals(true, !bool);
	}

	@Test
	public void testSetAddition() throws FileNotFoundException {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		InputStream is = new FileInputStream(testDataBaseDir.concat("f1.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("test-u01.ens"));
		mySet1.add(is);
		assertEquals(true, compareMySets(mySet1, mySet2));
	}

	@Test
	public void testRemoval1() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet1.remove(64);
		mySet1.remove(32767);
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("test-d05.ens"));
		assertEquals(true, testSparsity(mySet1));
		assertEquals(true, compareMySets(mySet1, mySet2));
	}

	@Test
	public void testRemoval2() {
		MySet mySet1 = new MySet();
		mySet1.add(0);
		mySet1.add(512);
		MySet mySet2 = new MySet();
		mySet2.add(0);
		mySet2.add(512);

		mySet1.remove(256);
		assertEquals(true, testSparsity(mySet1));
		assertEquals(true, compareMySets(mySet1, mySet2));
	}

	@Test
	public void testRemoval3() {
		MySet mySet1 = new MySet();
		mySet1.add(64);
		mySet1.remove(64);
		assertEquals(true, testSparsity(mySet1));
		assertEquals(true, mySet1.isEmpty());
	}

	@Test
	public void testRemoval4() {
		MySet mySet1 = new MySet();
		mySet1.add(64);
		mySet1.add(3333);
		mySet1.remove(64);
		mySet1.remove(3333);
		assertEquals(true, testSparsity(mySet1));
		assertEquals(true, mySet1.isEmpty());
	}

	@Test
	public void testRemoval5() throws FileNotFoundException {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet1.remove(new FileInputStream(new File(testDataBaseDir.concat("f1.ens"))));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("test-d01.ens"));
		assertEquals(true, testSparsity(mySet1));
		assertEquals(true, compareMySets(mySet1, mySet2));
	}

	@Test
	public void testSize1() {
		MySet mySet = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		int size = mySet.size();
		assertEquals(true, size == 14);
	}

	@Test
	public void testSize2() {
		MySet mySet = new MySet();
		mySet.iterator().getValue().set.add(22);
		;
		int size = mySet.size();
		assertEquals(true, size == 0);
	}

	@Test
	public void testDifference1() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-d03.ens"));
		mySet1.difference(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testDifference2() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-d30.ens"));
		mySet1.difference(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testDifference3() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f1.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-d01.ens"));
		mySet1.difference(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testDifference4() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		MySet mySet3 = new MySet();

		mySet1.add(100);
		mySet1.add(300);

		mySet2.add(100);

		mySet3.add(300);

		mySet1.difference(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testDifference5() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);

		mySet2.add(301);
		mySet2.add(100);

		mySet1.difference(mySet2);
		assertEquals(true, mySet1.isEmpty());
	}

	@Test
	public void testDifference6() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet1.difference(mySet2);
		assertEquals(true, mySet1.isEmpty());
	}

	@Test
	public void testDifference7() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet1.difference(mySet1);
		assertEquals(true, mySet1.isEmpty());
	}

	@Test
	public void testIntersection1() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-i03.ens"));
		mySet1.intersection(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testIntersection2() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-i03.ens"));
		mySet1.intersection(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testIntersection3() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		MySet mySet3 = new MySet();

		mySet1.add(100);
		mySet1.add(300);

		mySet2.add(100);
		mySet2.add(301);

		mySet3.add(100);

		mySet1.intersection(mySet2);
		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testIntersection4() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);
		mySet1.add(300);

		mySet2.add(100);

		mySet1.intersection(mySet2);
		assertEquals(true,
				compareMySets(mySet1, mySet2));
	}

	@Test
	public void testIntersection5() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		MySet mySet3 = new MySet();

		mySet1.add(100);

		mySet2.add(100);
		mySet2.add(301);

		mySet3.add(100);

		mySet1.intersection(mySet2);
		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testIntersection6() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);
		mySet1.add(999);

		mySet2.add(200);
		mySet2.add(301);

		mySet1.intersection(mySet2);
		assertEquals(true, mySet1.isEmpty());
	}

	@Test
	public void testUnion1() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-u03.ens"));
		mySet1.union(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testUnion2() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-u03.ens"));
		mySet1.union(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testUnion3() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		MySet mySet3 = new MySet();

		mySet1.add(100);
		mySet1.add(300);

		mySet2.add(100);

		mySet3.add(100);
		mySet3.add(300);

		mySet1.union(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testUnion4() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);

		mySet2.add(100);
		mySet2.add(301);

		mySet1.union(mySet2);
		assertEquals(true, compareMySets(mySet1, mySet2));
	}

	@Test
	public void testUnion5() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-u03.ens"));
		mySet1.union(mySet2);
		mySet2.add(8201);
		assertEquals(true, compareMySets(mySet1, mySet3));
	}

	@Test
	public void testSymmetricDifference1() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f1.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-s01.ens"));
		mySet1.symmetricDifference(mySet2);
		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testSymmetricDifference2() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f1.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-s01.ens"));
		mySet1.symmetricDifference(mySet2);
		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testSymmetricDifference3() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		MySet mySet3 = new MySet();

		mySet1.add(100);
		mySet1.add(300);

		mySet2.add(100);

		mySet3.add(300);

		mySet1.symmetricDifference(mySet2);
		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testSymmetricDifference4() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		MySet mySet3 = new MySet();

		mySet1.add(100);

		mySet2.add(100);
		mySet2.add(301);

		mySet3.add(301);

		mySet1.symmetricDifference(mySet2);
		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testSymmetricDifference5() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet1.symmetricDifference(mySet2);
		assertEquals(true,
				mySet1.isEmpty());
	}

	@Test
	public void testSymmetricDifference6() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet1.symmetricDifference(mySet1);
		assertEquals(true,
				mySet1.isEmpty());
	}

	@Test
	public void testSymmetricDifference7() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f1.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet3 = readFileToMySet(testDataBaseDir.concat("test-s01.ens"));
		mySet1.symmetricDifference(mySet2);
		mySet2.add(5001);
		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testSymmetricDifference8() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		MySet mySet3 = new MySet();
		mySet1.add(100);
		mySet1.add(300);

		mySet2.add(150);
		mySet2.add(800);

		mySet3.add(100);
		mySet3.add(150);
		mySet3.add(300);
		mySet3.add(800);

		mySet1.symmetricDifference(mySet2);

		assertEquals(true,
				compareMySets(mySet1, mySet3));
	}

	@Test
	public void testEquality1() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		assertEquals(true, mySet1.equals(mySet2));
	}

	@Test
	public void testEquality2() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet2.add(8888);
		assertEquals(true, !mySet1.equals(mySet2));
	}

	@Test
	public void testEquality3() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet2.add(5001);
		assertEquals(true, !mySet1.equals(mySet2));
	}

	@Test
	public void testEquality4() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet2.add(8888);
		assertEquals(true, !mySet2.equals(mySet1));
	}

	@Test
	public void testEquality5() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		mySet2.add(5001);
		assertEquals(true, !mySet2.equals(mySet1));
	}

	@Test
	public void testEquality6() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();
		mySet1.add(100);

		mySet2.add(100);
		mySet2.add(300);

		assertEquals(true, !mySet2.equals(mySet1));
	}

	@Test
	public void testEquality7() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);

		mySet2.add(100);
		mySet2.add(300);

		assertEquals(true, !mySet1.equals(mySet2));
	}

	@Test
	public void testEquality8() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(0);
		mySet1.add(1000);

		mySet2.add(256);
		mySet2.add(1000);

		assertEquals(true, !mySet1.equals(mySet2));
	}

	@Test
	public void testInclusion1() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		mySet1.union(mySet2);
		assertEquals(true, mySet2.isIncludedIn(mySet1));
	}

	@Test
	public void testInclusion2() {
		MySet mySet1 = readFileToMySet(testDataBaseDir.concat("f0.ens"));
		MySet mySet2 = readFileToMySet(testDataBaseDir.concat("f3.ens"));
		assertEquals(true, !mySet2.isIncludedIn(mySet1));
	}

	@Test
	public void testInclusion3() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);
		mySet1.add(101);
		mySet1.add(300);

		mySet2.add(100);
		mySet2.add(300);

		assertEquals(true, !mySet1.isIncludedIn(mySet2));
	}

	@Test
	public void testInclusion4() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);
		mySet1.add(101);
		mySet1.add(300);

		mySet2.add(300);

		assertEquals(true, !mySet1.isIncludedIn(mySet2));
	}

	@Test
	public void testInclusion5() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(100);
		mySet1.add(200);
		mySet1.add(300);

		mySet2.add(100);
		mySet2.add(200);

		assertEquals(true, !mySet1.isIncludedIn(mySet2));
	}


	@Test
	public void testInclusion6() {
		MySet mySet1 = new MySet();
		MySet mySet2 = new MySet();

		mySet1.add(10);

		mySet2.add(1034);
		mySet2.add(5555);

		assertEquals(true, !mySet1.isIncludedIn(mySet2));
	}
}
