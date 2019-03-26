/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  fr.istic.prg1.tp4.Iterator
 *  fr.istic.prg1.tp4.MySet
 *  fr.istic.prg1.tp4.MySetException
 *  fr.istic.prg1.tp4.MySetException$type
 *  fr.istic.prg1.tp4.SmallSet
 *  fr.istic.prg1.tp4.SubSet
 *  org.junit.jupiter.api.Assertions
 *  org.junit.jupiter.api.DisplayName
 *  org.junit.jupiter.api.Test
 */
package fr.istic.prg1.tp4;

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
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;

public class TestMySet {
	@Test
		@DisplayName("Test empty set size")
		public void testEmptySetSize() {
			MySet set = new MySet();
			assertEquals(0, set.size());
		}

	@Test
		@DisplayName("Test add")
		public void testAdd() {
			MySet set = new MySet();
			Iterator<SubSet> it = set.iterator();
			assertEquals(false, set.contains(4));
			set.add(4);
			assertEquals(true, set.contains(4));
			assertEquals(false, set.contains(5));
			set.add(5);
			assertEquals(true, set.contains(5));
			assertEquals(false, set.contains(400));
			set.add(400);
			assertEquals(true, set.contains(400));
			assertEquals(false, set.contains(0));
			set.add(0);
			assertEquals(true, set.contains(0));
			assertEquals(false, set.contains(32767));
			set.add(32767);
			assertEquals(true, set.contains(32767));
			assertEquals(false, set.contains(1000));
			set.add(1000);
			assertEquals(true, set.contains(1000));
			assertEquals(128, (it.getValue()).rank);
			it.goForward();
			assertEquals(0, (it.getValue()).rank);
			it.goForward();
			assertEquals(1, (it.getValue()).rank);
			it.goForward();
			assertEquals(3, (it.getValue()).rank);
			it.goForward();
			assertEquals(127, (it.getValue()).rank);
			it.goForward();
			assertEquals(128, (it.getValue()).rank);
			it.goForward();
			assertEquals(0, (it.getValue()).rank);
			assertEquals(true, (it.getValue()).set.contains(0));
			assertEquals(true, (it.getValue()).set.contains(4));
			assertEquals(true, (it.getValue()).set.contains(5));
			assertEquals(false, (it.getValue()).set.contains(10));
			assertEquals(false, (it.getValue()).set.contains(254));
			it.goForward();
			assertEquals(1, (it.getValue()).rank);
			assertEquals(true, (it.getValue()).set.contains(144));
			assertEquals(false, (it.getValue()).set.contains(10));
			assertEquals(false, (it.getValue()).set.contains(254));
			it.goForward();
			assertEquals(3, (it.getValue()).rank);
			assertEquals(true, (it.getValue()).set.contains(232));
			assertEquals(false, (it.getValue()).set.contains(10));
			assertEquals(false, (it.getValue()).set.contains(254));
			it.goForward();
			assertEquals(127, (it.getValue()).rank);
			assertEquals(true, (it.getValue()).set.contains(255));
			assertEquals(false, (it.getValue()).set.contains(10));
			assertEquals(false, (it.getValue()).set.contains(254));
			Throwable exception1 = assertThrows(MySetException.class, () -> set.add(32768));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception1).getType());
			Throwable exception2 = assertThrows(MySetException.class, () -> set.add(-1));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception2).getType());
		}

	@Test
		@DisplayName("Test contains with out of range values")
		public void testContains() {
			MySet set = new MySet();
			Throwable exception1 = assertThrows(MySetException.class, () -> set.contains(32768));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception1).getType());
			Throwable exception2 = assertThrows(MySetException.class, () -> set.contains(-1));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception2).getType());
		}

	@Test
		@DisplayName("Test size")
		public void testSize() {
			MySet set = new MySet();
			assertEquals(0, set.size());
			set.add(4);
			assertEquals(1, set.size());
			set.add(400);
			assertEquals(2, set.size());
			set.add(400);
			assertEquals(2, set.size());
			set.add(32767);
			assertEquals(3, set.size());
		}

	@Test
		@DisplayName("Test remove")
		public void testRemove() {
			MySet set = new MySet();
			Iterator<SubSet> it = set.iterator();
			set.remove(0);
			assertEquals(0, set.size());
			set.remove(32767);
			assertEquals(0, set.size());
			set.remove(1);
			assertEquals(0, set.size());
			assertEquals(0, set.getSize());
			set.add(3);
			set.add(100);
			it.restart();
			assertEquals(0, (it.getValue()).rank);
			assertEquals(1, set.getSize());
			set.add(403);
			assertEquals(2, set.getSize());
			it.restart();
			it.goForward();
			assertEquals(1, (it.getValue()).rank);
			set.add(227);
			set.add(32767);
			set.add(32760);
			assertEquals(3, set.getSize());
			it.restart();
			it.goForward();
			it.goForward();
			assertEquals(127, (it.getValue()).rank);
			set.remove(0);
			assertEquals(6, set.size());
			set.remove(32761);
			assertEquals(6, set.size());
			set.remove(1);
			assertEquals(6, set.size());
			set.remove(32767);
			assertEquals(3, set.getSize());
			assertEquals(5, set.size());
			set.remove(32760);
			assertEquals(2, set.getSize());
			assertEquals(4, set.size());
			it.restart();
			it.goForward();
			it.goForward();
			assertEquals(128, (it.getValue()).rank);
			set.remove(100);
			assertEquals(2, set.getSize());
			assertEquals(3, set.size());
			assertEquals(true, set.contains(3));
			assertEquals(false, set.contains(100));
			assertEquals(true, set.contains(403));
			assertEquals(true, set.contains(227));
			assertEquals(false, set.contains(32760));
			assertEquals(false, set.contains(32767));
			Throwable exception1 = assertThrows(MySetException.class, () -> set.add(32768));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception1).getType());
			Throwable exception2 = assertThrows(MySetException.class, () -> set.add(-1));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception2).getType());
		}

	@Test
		@DisplayName("Test equals")
		public void testEquals() {
			MySet set1 = new MySet();
			MySet set2 = new MySet();
			assertEquals(true, set1.equals(set2));
			assertEquals(true, set2.equals(set1));
			set1.add(30851);
			set1.add(1504);
			set1.add(2710);
			set1.add(18197);
			set1.add(26946);
			set1.add(15013);
			set1.add(31628);
			set1.add(28984);
			set1.add(23937);
			assertEquals(false, set1.equals(set2));
			assertEquals(false, set2.equals(set1));
			set2.add(30851);
			set2.add(1504);
			set2.add(2710);
			set2.add(18197);
			set2.add(26946);
			set2.add(15013);
			set2.add(31628);
			set2.add(28984);
			assertEquals(false, set1.equals(set2));
			assertEquals(false, set2.equals(set1));
			set2.add(23937);
			assertEquals(true, set1.equals(set2));
			assertEquals(true, set2.equals(set1));
			set2.add(3);
			assertEquals(false, set1.equals(set2));
			assertEquals(false, set2.equals(set1));
		}

	@Test
		@DisplayName("Test toString")
		public void testToString() {
			MySet set1 = new MySet();
			assertEquals(new String("{}"), set1.toString());
			set1.add(0);
			assertEquals(new String("{0}"), set1.toString());
			set1.add(400);
			set1.add(32767);
			assertEquals(new String("{0, 400, 32767}"), set1.toString());
			set1.add(1001);
			set1.add(1002);
			set1.add(1003);
			set1.add(1004);
			set1.add(1005);
			set1.add(1006);
			set1.add(1007);
			set1.add(1008);
			set1.add(1009);
			set1.add(1010);
			assertEquals(new String("{0, 400, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008,\n1009, 1010, 32767}"), set1.toString());
		}

	@Test
		@DisplayName("Test union with one emtpy set")
		public void testUnionWithOneEmptySet() {
			MySet set1 = new MySet();
			MySet set2 = new MySet();
			set1.add(1);
			set1.add(32760);
			set1.add(400);
			set2.add(2620);
			set2.add(32760);
			set1.union(set2);
			assertEquals(true, set1.contains(1));
			assertEquals(true, set1.contains(400));
			assertEquals(true, set1.contains(2620));
			assertEquals(true, set1.contains(32760));
		}

	private void prepareSetsForTestSetOperations(MySet set1, MySet set2) {
		set1.add(0);
		set1.add(100);
		set1.add(600);
		set1.add(603);
		set1.add(605);
		set1.add(1600);
		set1.add(1603);
		set1.add(1605);
		set1.add(2600);
		set1.add(2603);
		set1.add(2605);
		set2.add(0);
		set2.add(3);
		set2.add(100);
		set2.add(1000);
		set2.add(1600);
		set2.add(1605);
		set2.add(2600);
		set2.add(2603);
		set2.add(2605);
		set2.add(2620);
		set2.add(32760);
		set2.add(32767);
	}

	@Test
		@DisplayName("Test union")
		public void testUnion() {
			MySet set1 = new MySet();
			MySet set2 = new MySet();
			assertEquals(true, set1.equals(set2));
			assertEquals(true, set2.equals(set1));
			this.prepareSetsForTestSetOperations(set1, set2);
			set1.union(set2);
			assertEquals(true, set1.contains(0));
			assertEquals(true, set1.contains(3));
			assertEquals(true, set1.contains(100));
			assertEquals(true, set1.contains(600));
			assertEquals(true, set1.contains(603));
			assertEquals(true, set1.contains(605));
			assertEquals(true, set1.contains(1000));
			assertEquals(true, set1.contains(1600));
			assertEquals(true, set1.contains(1603));
			assertEquals(true, set1.contains(1605));
			assertEquals(true, set1.contains(2600));
			assertEquals(true, set1.contains(2603));
			assertEquals(true, set1.contains(2605));
			assertEquals(true, set1.contains(2620));
			assertEquals(true, set1.contains(32760));
			assertEquals(true, set1.contains(32767));
			assertEquals(16, set1.size());
			assertEquals(6, set1.getSize());
			Iterator<SubSet> it = set1.iterator();
			it.goBackward();
			for (int i = 0; i < 6; ++i) {
				it.goForward();
				assertEquals(false, it.isOnFlag());
			}
			it.goForward();
			assertEquals(true, it.isOnFlag());
		}

	@Test
		@DisplayName("Test difference")
		public void testDifference() {
			MySet set1 = new MySet();
			MySet set2 = new MySet();
			MySet set1D2 = new MySet();
			MySet set2D1 = new MySet();
			this.prepareSetsForTestSetOperations(set1, set2);
			this.prepareSetsForTestSetOperations(set1D2, set2D1);
			set1D2.difference(set2);
			set2D1.difference(set1);
			assertEquals(false, set1D2.contains(0));
			assertEquals(false, set1D2.contains(3));
			assertEquals(false, set1D2.contains(100));
			assertEquals(true, set1D2.contains(600));
			assertEquals(true, set1D2.contains(603));
			assertEquals(true, set1D2.contains(605));
			assertEquals(false, set1D2.contains(1000));
			assertEquals(false, set1D2.contains(1600));
			assertEquals(true, set1D2.contains(1603));
			assertEquals(false, set1D2.contains(1605));
			assertEquals(false, set1D2.contains(2600));
			assertEquals(false, set1D2.contains(2603));
			assertEquals(false, set1D2.contains(2605));
			assertEquals(false, set1D2.contains(2620));
			assertEquals(false, set1D2.contains(32760));
			assertEquals(false, set1D2.contains(32767));
			assertEquals(false, set2D1.contains(0));
			assertEquals(true, set2D1.contains(3));
			assertEquals(false, set2D1.contains(100));
			assertEquals(false, set2D1.contains(600));
			assertEquals(false, set2D1.contains(603));
			assertEquals(false, set2D1.contains(605));
			assertEquals(true, set2D1.contains(1000));
			assertEquals(false, set2D1.contains(1600));
			assertEquals(false, set2D1.contains(1603));
			assertEquals(false, set2D1.contains(1605));
			assertEquals(false, set2D1.contains(2600));
			assertEquals(false, set2D1.contains(2603));
			assertEquals(false, set2D1.contains(2605));
			assertEquals(true, set2D1.contains(2620));
			assertEquals(true, set2D1.contains(32760));
			assertEquals(true, set2D1.contains(32767));
			assertEquals(4, set1D2.size());
			assertEquals(2, set1D2.getSize());
			Iterator<SubSet> it1D2 = set1D2.iterator();
			it1D2.goBackward();
			for (int i = 0; i < 2; ++i) {
				it1D2.goForward();
				assertEquals(false, it1D2.isOnFlag());
			}
			it1D2.goForward();
			assertEquals(true, it1D2.isOnFlag());
			assertEquals(5, set2D1.size());
			assertEquals(4, set2D1.getSize());
			Iterator<SubSet> it2D1 = set2D1.iterator();
			it2D1.goBackward();
			for (int i = 0; i < 4; ++i) {
				it2D1.goForward();
				assertEquals(false, it2D1.isOnFlag());
			}
			it2D1.goForward();
			assertEquals(true, it2D1.isOnFlag());
		}

	@Test
		@DisplayName("Test symmetricDifference")
		public void testSymmetricDifference() {
			MySet set1 = new MySet();
			MySet set2 = new MySet();
			this.prepareSetsForTestSetOperations(set1, set2);
			set1.symmetricDifference(set2);
			assertEquals(false, set1.contains(0));
			assertEquals(true, set1.contains(3));
			assertEquals(false, set1.contains(100));
			assertEquals(true, set1.contains(600));
			assertEquals(true, set1.contains(603));
			assertEquals(true, set1.contains(605));
			assertEquals(true, set1.contains(1000));
			assertEquals(false, set1.contains(1600));
			assertEquals(true, set1.contains(1603));
			assertEquals(false, set1.contains(1605));
			assertEquals(false, set1.contains(2600));
			assertEquals(false, set1.contains(2603));
			assertEquals(false, set1.contains(2605));
			assertEquals(true, set1.contains(2620));
			assertEquals(true, set1.contains(32760));
			assertEquals(true, set1.contains(32767));
			assertEquals(9, set1.size());
			assertEquals(6, set1.getSize());
			Iterator<SubSet> it = set1.iterator();
			it.goBackward();
			for (int i = 0; i < 6; ++i) {
				it.goForward();
				assertEquals(false, it.isOnFlag());
			}
			it.goForward();
			assertEquals(true, it.isOnFlag());
		}

	@Test
		@DisplayName("Test testIntersection")
		public void testIntersection() {
			MySet set1 = new MySet();
			MySet set2 = new MySet();
			this.prepareSetsForTestSetOperations(set1, set2);
			set1.intersection(set2);
			assertEquals(true, set1.contains(0));
			assertEquals(false, set1.contains(3));
			assertEquals(true, set1.contains(100));
			assertEquals(false, set1.contains(600));
			assertEquals(false, set1.contains(603));
			assertEquals(false, set1.contains(605));
			assertEquals(false, set1.contains(1000));
			assertEquals(true, set1.contains(1600));
			assertEquals(false, set1.contains(1603));
			assertEquals(true, set1.contains(1605));
			assertEquals(true, set1.contains(2600));
			assertEquals(true, set1.contains(2603));
			assertEquals(true, set1.contains(2605));
			assertEquals(false, set1.contains(2620));
			assertEquals(false, set1.contains(32760));
			assertEquals(false, set1.contains(32767));
			assertEquals(7, set1.size());
			assertEquals(3, set1.getSize());
			Iterator<SubSet> it = set1.iterator();
			it.goBackward();
			for (int i = 0; i < 3; ++i) {
				it.goForward();
				assertEquals(false, it.isOnFlag());
			}
			it.goForward();
			assertEquals(true, it.isOnFlag());
		}

	@Test
		@DisplayName("Test testIsIncludedIn")
		public void testIsIncludedIn() {
			MySet setU = new MySet();
			MySet setI = new MySet();
			MySet set1 = new MySet();
			MySet set2 = new MySet();
			MySet set3 = new MySet();
			setU.add(0);
			setU.add(3);
			setU.add(100);
			setU.add(600);
			setU.add(603);
			setU.add(605);
			setU.add(1000);
			setU.add(1600);
			setU.add(1603);
			setU.add(1605);
			setU.add(2600);
			setU.add(2603);
			setU.add(2605);
			setU.add(2620);
			setU.add(32760);
			setU.add(32767);
			setI.add(0);
			setI.add(100);
			setI.add(1600);
			setI.add(1605);
			setI.add(2600);
			setI.add(2603);
			setI.add(2605);
			set1.add(0);
			set1.add(100);
			set1.add(600);
			set1.add(603);
			set1.add(605);
			set1.add(1600);
			set1.add(1603);
			set1.add(1605);
			set1.add(2600);
			set1.add(2603);
			set1.add(2605);
			set2.add(0);
			set2.add(3);
			set2.add(100);
			set2.add(1000);
			set2.add(1600);
			set2.add(1605);
			set2.add(2600);
			set2.add(2603);
			set2.add(2605);
			set2.add(2620);
			set2.add(32760);
			set2.add(32767);
			set3.add(3);
			set3.add(600);
			set3.add(603);
			set3.add(605);
			set3.add(1000);
			set3.add(1603);
			set3.add(2620);
			set3.add(32760);
			set3.add(32767);
			assertEquals(true, setU.isIncludedIn(setU));
			assertEquals(false, setU.isIncludedIn(setI));
			assertEquals(false, setU.isIncludedIn(set1));
			assertEquals(false, setU.isIncludedIn(set2));
			assertEquals(false, setU.isIncludedIn(set3));
			assertEquals(true, setI.isIncludedIn(setU));
			assertEquals(true, setI.isIncludedIn(setI));
			assertEquals(true, setI.isIncludedIn(set1));
			assertEquals(true, setI.isIncludedIn(set2));
			assertEquals(false, setI.isIncludedIn(set3));
			assertEquals(true, set1.isIncludedIn(setU));
			assertEquals(false, set1.isIncludedIn(setI));
			assertEquals(true, set1.isIncludedIn(set1));
			assertEquals(false, set1.isIncludedIn(set2));
			assertEquals(false, set1.isIncludedIn(set3));
			assertEquals(true, set2.isIncludedIn(setU));
			assertEquals(false, set2.isIncludedIn(setI));
			assertEquals(false, set2.isIncludedIn(set1));
			assertEquals(true, set2.isIncludedIn(set2));
			assertEquals(false, set2.isIncludedIn(set3));
			assertEquals(true, set3.isIncludedIn(setU));
			assertEquals(false, set3.isIncludedIn(setI));
			assertEquals(false, set3.isIncludedIn(set1));
			assertEquals(false, set3.isIncludedIn(set2));
			assertEquals(true, set3.isIncludedIn(set3));
		}

	@Test
		@DisplayName("Test contains with user input")
		public void containsUserInput() {
			MySet set = new MySet();
			set.add(0);
			set.add(32767);
			set.add(600);
			ByteArrayInputStream in = new ByteArrayInputStream("0\r\n15\r\n".getBytes());
			assertEquals(true, set.contains((InputStream)in));
			in = new ByteArrayInputStream("2\r\n".getBytes());
			assertEquals(false, set.contains((InputStream)in));
			in = new ByteArrayInputStream("32767\r\n".getBytes());
			assertEquals(true, set.contains((InputStream)in));
			in = new ByteArrayInputStream("600\r\n".getBytes());
			assertEquals(true, set.contains((InputStream)in));
			Throwable exception1 = assertThrows(MySetException.class, () -> set.contains((InputStream)new ByteArrayInputStream("32768\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception1).getType());
			Throwable exception2 = assertThrows(MySetException.class, () -> set.contains((InputStream)new ByteArrayInputStream("-1\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception2).getType());
			Throwable exception3 = assertThrows(MySetException.class, () -> set.contains((InputStream)new ByteArrayInputStream("\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception3).getType());
			Throwable exception4 = assertThrows(MySetException.class, () -> set.contains((InputStream)new ByteArrayInputStream("a\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception4).getType());
		}

	@Test
		@DisplayName("Test add with user input")
		public void addUserInput() {
			MySet set = new MySet();
			String data = "0\r\n3\r\n100\r\n600\r\n603\r\n605\r\n1000\r\n1600\r\n1603\r\n1605\r\n2600\r\n2603\r\n2605\r\n2620\r\n32760\r\n32767\r\n-1\r\n1\r\n99\r\n32766";
			ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
			set.add((InputStream)in);
			assertEquals(true, set.contains(0));
			assertEquals(true, set.contains(3));
			assertEquals(true, set.contains(100));
			assertEquals(true, set.contains(600));
			assertEquals(true, set.contains(603));
			assertEquals(true, set.contains(605));
			assertEquals(true, set.contains(1000));
			assertEquals(true, set.contains(1600));
			assertEquals(true, set.contains(1603));
			assertEquals(true, set.contains(1605));
			assertEquals(true, set.contains(2600));
			assertEquals(true, set.contains(2603));
			assertEquals(true, set.contains(2605));
			assertEquals(true, set.contains(2620));
			assertEquals(true, set.contains(32760));
			assertEquals(true, set.contains(32767));
			assertEquals(false, set.contains(1));
			assertEquals(false, set.contains(99));
			assertEquals(false, set.contains(32766));
			Throwable exception1 = assertThrows(MySetException.class, () -> set.add((InputStream)new ByteArrayInputStream("32768\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception1).getType());
			Throwable exception3 = assertThrows(MySetException.class, () -> set.add((InputStream)new ByteArrayInputStream("\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception3).getType());
			Throwable exception4 = assertThrows(MySetException.class, () -> set.add((InputStream)new ByteArrayInputStream("a\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception4).getType());
		}

	@Test
		@DisplayName("Test remove with user input")
		public void removeUserInput() {
			MySet set = new MySet();
			for (int i = 0; i < 32767; ++i) {
				set.add(i);
			}
			String data = "0\r\n3\r\n100\r\n600\r\n603\r\n605\r\n1000\r\n1600\r\n1603\r\n1605\r\n2600\r\n2603\r\n2605\r\n2620\r\n32760\r\n32767\r\n-1\r\n1\r\n99\r\n32766";
			ByteArrayInputStream in = new ByteArrayInputStream(data.getBytes());
			set.remove((InputStream)in);
			assertEquals(false, set.contains(0));
			assertEquals(false, set.contains(3));
			assertEquals(false, set.contains(100));
			assertEquals(false, set.contains(600));
			assertEquals(false, set.contains(603));
			assertEquals(false, set.contains(605));
			assertEquals(false, set.contains(1000));
			assertEquals(false, set.contains(1600));
			assertEquals(false, set.contains(1603));
			assertEquals(false, set.contains(1605));
			assertEquals(false, set.contains(2600));
			assertEquals(false, set.contains(2603));
			assertEquals(false, set.contains(2605));
			assertEquals(false, set.contains(2620));
			assertEquals(false, set.contains(32760));
			assertEquals(false, set.contains(32767));
			assertEquals(true, set.contains(1));
			assertEquals(true, set.contains(99));
			assertEquals(true, set.contains(32766));
			Throwable exception1 = assertThrows(MySetException.class, () -> set.remove((InputStream)new ByteArrayInputStream("32768\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception1).getType());
			Throwable exception3 = assertThrows(MySetException.class, () -> set.remove((InputStream)new ByteArrayInputStream("\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception3).getType());
			Throwable exception4 = assertThrows(MySetException.class, () -> set.remove((InputStream)new ByteArrayInputStream("a\r\n".getBytes())));
			assertEquals(MySetException.type.VALUE_OUT_OF_RANGE, ((MySetException)exception4).getType());
		}

	@Test
		@DisplayName("Test save")
		public void testSave() throws IOException {
			MySet setU = new MySet();
			String outputFile = "test_data/liste";
			String referenceFile = "test_data/listeDeReference";
			Files.deleteIfExists(FileSystems.getDefault().getPath(outputFile, new String[0]));
			setU.add(0);
			setU.add(3);
			setU.add(100);
			setU.add(600);
			setU.add(603);
			setU.add(605);
			setU.add(1000);
			setU.add(1600);
			setU.add(1603);
			setU.add(1605);
			setU.add(2600);
			setU.add(2603);
			setU.add(2605);
			setU.add(2620);
			setU.add(32760);
			setU.add(32767);
			setU.save(outputFile);
			assertEquals(this.computeDigestOfFile("test_data/listeDeReference"), this.computeDigestOfFile("test_data/liste"));
		}

	@Test
		@DisplayName("Test restore")
		public void testRestore() throws IOException {
			MySet setU = new MySet();
			MySet setTest = new MySet();
			String inputFile = "test_data/listeDeReference";
			setU.add(0);
			setU.add(3);
			setU.add(100);
			setU.add(600);
			setU.add(603);
			setU.add(605);
			setU.add(1000);
			setU.add(1600);
			setU.add(1603);
			setU.add(1605);
			setU.add(2600);
			setU.add(2603);
			setU.add(2605);
			setU.add(2620);
			setU.add(32760);
			setU.add(32767);
			assertEquals(false, setU.equals(setTest));
			setTest.restore(inputFile);
			assertEquals(true, setU.equals(setTest));
		}

	private String computeDigestOfFile(String filename) throws IOException {
		MessageDigest md;
		md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException ex) {
			throw new Error("The algorithm used to make the digest seems to be unsupported by your system !", ex);
		}
		try (InputStream is = Files.newInputStream(Paths.get(filename, new String[0]), new OpenOption[0]);
				DigestInputStream dis = new DigestInputStream(is, md);){
			while (dis.read() != -1) {
			}
		}
		byte[] hashInBytes = md.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : hashInBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}
