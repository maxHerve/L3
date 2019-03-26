package fr.istic.prg1.tp4;

import java.util.*;
import java.io.*;
import java.nio.file.*;

@SuppressWarnings("overrides")

/**
 * Define a class to manipulate sets of integers, that are in range MIN_VALUE to MAX_VALUE
 */
public class MySet extends List<SubSet>{
	/** The rank of the flag */
	private static final int MAX_RANG = 128;
	/** Defines the flag */
	private static final SubSet FLAG_VALUE = new SubSet(MAX_RANG, new SmallSet());
	/** Minimum value that can be stored in the set */
	private static final int MIN_VALUE = 0;
	/** Maximum value that can be stored in the set */
	private static final int MAX_VALUE = 32767;

	/** The number of Integers presents in this set */
	private int numberOfElements;

	public MySet(){
		super();

		this.setFlag(FLAG_VALUE);
		this.numberOfElements = 0;
	}

	/**
	 * @param flag a SubSet that is used as the value of the list's flag
	 */
	public void setFlag(SubSet flag){
		Iterator<SubSet> it = super.iterator();
		it.setValue(flag);
	}

	/**
	 * @return true if and only if the number writen by the user is in the this set, false otherwise
	 */
	public boolean contains(){
		return this.contains(System.in);
	}

	/**
	 * @param in the InputStream used to retrieve the value to search
	 * @return true if and only if the number writen by the user is in the this set, false otherwise
	 */
	public boolean contains(InputStream in){
		Scanner sc = new Scanner(in);

		int value = 0;
		
		try{
			value = sc.nextInt();
		}
		catch(InputMismatchException e){
			throw new MySetException(MySetException.type.VALUE_OUT_OF_RANGE,e);
		}
		catch(NoSuchElementException e){
			throw new MySetException(MySetException.type.VALUE_OUT_OF_RANGE,e);
		}

		return this.contains(value);
	}


	/**
	 * @param value a positive integer
	 * @return true if and only value is in the this set, false otherwise
	 * @throws MySetException with VALUE_OUT_OF_RANGE type if the value is not between MIN_VALUE and MAX_VALUE
	 */
	public boolean contains(int value){
		ensureNotOutOfRangeValue(value);
		ListIterator it = (ListIterator)super.iterator();
		return (iteratorOnSubset(it,value) && it.getValue().set.contains(value%256));
	}


	/**
	 * Add all the values entered by the user to this set and print the new content (end with -1)
	 */
	public void add(){
		add(System.in);
	}

	public void add(InputStream in){
		Scanner sc = new Scanner(in);


		int value = MIN_VALUE;
		while(!((value < MIN_VALUE || value > MAX_VALUE || value == -1))){
			try{
				value = sc.nextInt();
				if(value != -1){
					this.add(value);
				}
			}
			catch(InputMismatchException e){
				throw new MySetException(MySetException.type.VALUE_OUT_OF_RANGE,e);
			}
			catch(NoSuchElementException e){
				throw new MySetException(MySetException.type.VALUE_OUT_OF_RANGE,e);
			}
		}
	}


	/**
	 * Add the integer value to this set, if it's already there, does nothing
	 * @param value a positive integer
	 * @throws MySetException with VALUE_OUT_OF_RANGE type if the value is not between MIN_VALUE and MAX_VALUE
	 * @ensure the number of elements will be set to the good value
	 */
	public void add(int value){
		ensureNotOutOfRangeValue(value);

		if(!this.contains(value)){
			ListIterator it = (ListIterator)super.iterator();

			if(!iteratorOnSubset(it,value)){
				int rank = value/256;
				it.addRight(new SubSet(rank, new SmallSet()));
			}

			it.getValue().set.add(value%256);
			
			numberOfElements++;
		}
	}

	/**
	 * Place the pointer it to the SubSet that may contains value, or on the one just before if it doesn't exists
	 * @return true if and only if the SubSet returned may contain the value value
	 */
	private boolean iteratorOnSubset(ListIterator it,int value){
		it.restart();

		int rank = value/256;
		while(it.getValue().rank <= rank && !it.isOnFlag()){
			it.goForward();
		}
		it.goBackward();

		return it.getValue().rank == rank;
	}
	
	/**
	 * Remove all the values entered by the user from this set and print the new content (end with -1)
	 */
	public void remove(){
		remove(System.in);
	}

	public void remove(InputStream in){
		Scanner sc = new Scanner(in);

		int value = MIN_VALUE;
		while(!((value < MIN_VALUE || value > MAX_VALUE || value == -1))){
			try{
				value = sc.nextInt();
				if(value != -1){
					this.remove(value);
				}
			}
			catch(InputMismatchException e){
				throw new MySetException(MySetException.type.VALUE_OUT_OF_RANGE,e);
			}
			catch(NoSuchElementException e){
				throw new MySetException(MySetException.type.VALUE_OUT_OF_RANGE,e);
			}
		}
	}

	
	/**
	 * Remove an integer value from this set, if it's not there, does nothing
	 * @param value a positive integer
	 * @ensure the number of elements will be set to the good value
	 */
	public void remove(int value){
		ensureNotOutOfRangeValue(value);

		if(this.contains(value)){
			ListIterator it = (ListIterator)super.iterator();
			if(!iteratorOnSubset(it,value)){
				throw new Error("An error happend !");
			}

			it.getValue().set.remove(value%256);

			if(it.getValue().set.isEmpty()){
				it.remove();
			}

			this.numberOfElements--;
		}
	}
	
	/**
	 * Make the difference between this set and set2
	 * @param set2 another set
	 * @ensure the number of elements will be set to the good value
	 */
	public void difference(MySet set2){
		ListIterator it1 = (ListIterator) this.iterator();
		ListIterator it2 = (ListIterator) set2.iterator();

		while(!it1.isOnFlag()){
			if(it1.getValue().rank == it2.getValue().rank){
				numberOfElements -= it1.getValue().set.size();

				it1.getValue().set.difference(it2.getValue().set);

				numberOfElements += it1.getValue().set.size();
				if(it1.getValue().set.isEmpty()){
					it1.remove();
				}

				it1.goForward();
			}
			else if(it1.getValue().rank < it2.getValue().rank){
				it1.goForward();
			}
			else if(it1.getValue().rank > it2.getValue().rank){
				it2.goForward();
			}
		}
	}

	/**
	 * Make the symmetric difference between this set and set2
	 * @param set2 another set
	 * @ensure the number of elements will be set to the good value
	 */
	public void symmetricDifference(MySet set2){
		ListIterator it1 = (ListIterator) this.iterator();
		ListIterator it2 = (ListIterator) set2.iterator();

			
		while(!it2.isOnFlag()){
			if(it1.getValue().rank == it2.getValue().rank){
				numberOfElements -= it1.getValue().set.size();

				it1.getValue().set.symmetricDifference(it2.getValue().set);
				
				if(it1.getValue().set.isEmpty()){
					it1.remove();
				}

				numberOfElements += it1.getValue().set.size();

				it1.goForward();
				it2.goForward();
			}
			else if(it1.getValue().rank > it2.getValue().rank){
				it1.addLeft(new SubSet(it2.getValue().rank, it2.getValue().set.clone()));

				numberOfElements += it1.getValue().set.size();

				it2.goForward();
			}
			else{
				it1.goForward();
			}
		}

	}

	/**
	 * Make the intersection between this set and set2, and put the result in this set
	 * @param set2 another set
	 * @ensure the number of elements will be set to the good value
	 */
	public void intersection(MySet set2){
		ListIterator it1 = (ListIterator) this.iterator();
		ListIterator it2 = (ListIterator) set2.iterator();

			
		while(!it1.isOnFlag()){
			if(it1.getValue().rank == it2.getValue().rank){
				numberOfElements -= it1.getValue().set.size();
				it1.getValue().set.intersection(it2.getValue().set);

				numberOfElements += it1.getValue().set.size();

				if(it1.getValue().set.isEmpty()){
					it1.remove();
				}

				it1.goForward();
			}
			else if(it1.getValue().rank < it2.getValue().rank){
				numberOfElements -= it1.getValue().set.size();
				it1.remove();
				it1.goForward();
			}
			else if(it1.getValue().rank > it2.getValue().rank){
				it2.goForward();
			}
		}
	}

	/**
	 * Make the union between this set and set2, and but the result in this set
	 * @param set2 another set
	 * @ensure the number of elements will be set to the good value
	 */
	public void union(MySet set2){
		ListIterator it1 = (ListIterator) this.iterator();
		ListIterator it2 = (ListIterator) set2.iterator();

		while(!it2.isOnFlag()){

			if(it1.getValue().rank == it2.getValue().rank){
				numberOfElements -= it1.getValue().set.size();

				it1.getValue().set.union(it2.getValue().set);
				it2.goForward();
				
				numberOfElements += it1.getValue().set.size();
			}
			else if(it1.getValue().rank > it2.getValue().rank){
				it1.addLeft(new SubSet(it2.getValue().rank, it2.getValue().set.clone()));
				it2.goForward();

				numberOfElements += it1.getValue().set.size();
			}
			else if(it1.getValue().rank < it2.getValue().rank){
				while(it1.getValue().rank < it2.getValue().rank){
					it1.goForward();
				}
			}
			
		}
	}

	/**
	 * @return true if and only if this set and o contains exactly the same values
	 * @param o another set
	 */
	public boolean equals(Object o){
		if(o instanceof MySet){
			MySet otherSet = (MySet) o;
			
			if(otherSet.size() == this.size() && otherSet.getSize() == this.getSize()){
				ListIterator it1 = (ListIterator) this.iterator();
				ListIterator it2 = (ListIterator) otherSet.iterator();

				boolean res = true;

				while(!it1.isOnFlag() && !it2.isOnFlag() && res){
					res = res && it1.getValue().rank == it2.getValue().rank;
					res = res && it1.getValue().set.equals(it2.getValue().set);
					
					it1.goForward();
					it2.goForward();
				}

				return res && (it1.isOnFlag() && it2.isOnFlag());
			}
		}
		return false;
	}

	/**
	 * @param set2 another set
	 * @return true if and only if all the values of this set are in set2
	 */
	public boolean isIncludedIn(MySet set2){
		ListIterator it1 = (ListIterator) this.iterator();
		ListIterator it2 = (ListIterator) set2.iterator();

		boolean res = true;
			
		while(!it1.isOnFlag()){
			if(it1.getValue().rank == it2.getValue().rank){
				res = res && it1.getValue().set.isIncludedIn(it2.getValue().set);
				it1.goForward();
			}
			else if(it1.getValue().rank < it2.getValue().rank){
				return false;
			}
			else if(it1.getValue().rank > it2.getValue().rank){
				it2.goForward();
			}
		}
		return res;
	}

	/**
	 * @return the number of elements in this set
	 */
	public int size(){
		return numberOfElements;
	}

	/**
	 * Add the positive integers contained in the file specified by the user to this set (end with -1)
	 */
	public void restore(){
		Scanner sc = new Scanner(System.in);

		String filename = sc.next();

		try{
			restore(filename);
		}
		catch(IOException e){
			throw new Error("Impossible to save into this file !",e);
		}
	}

	/**
	 * Add the positive integers contained in the file specified by the user to this set (end with -1)
	 */
	public void restore(String filename) throws IOException{
		Scanner fileScanner = new Scanner(Paths.get(filename));

		boolean stopIfOne = false;

		while(!(stopIfOne || !fileScanner.hasNextInt())){
			int value = fileScanner.nextInt();

			if(value != -1){
				this.add(value);
			}
			else{
				stopIfOne = true;
			}
		}
	}

	/**
	 * Save this set in the file specified by the user (end with -1)
	 */
	public void save(){
		Scanner sc = new Scanner(System.in);

		String filename = sc.next();

		try{
			save(filename);
		}
		catch(IOException e){
			throw new Error("Impossible to save into this file !",e);
		}
	}

	/**
	 * Save this set to the file specified
	 * @param filename a filename
	 */
	public void save(String filename) throws IOException{
		PrintWriter fileWriter = new PrintWriter(filename,"UTF-8");
		Iterator<SubSet> it = this.iterator();

		for(int i=MIN_VALUE ; i<=MAX_VALUE ; i++){
			if(this.contains(i)){
				fileWriter.println(Integer.toString(i));
			}
		}

		fileWriter.println(Integer.toString(-1));

		fileWriter.close();
	}

	/**
	 * Prints all the integers in this set (10 integers per lines)
	 */
	public void print(){
		System.out.println(this.toString());
	}

	/**
	 * @ensure that the value passed in parameter is in range MIN_VALUE MAX_VALUE
	 * @throws MySetException with VALUE_OUT_OF_RANGE type if the value is not between MIN_VALUE and MAX_VALUE
	 */
	private void ensureNotOutOfRangeValue(int value){
		if(value < MIN_VALUE || value > MAX_VALUE){
			throw new MySetException(MySetException.type.VALUE_OUT_OF_RANGE);
		}
	}
	
	/**
	 * @return the string representation of this set
	 */
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("{");

		int counter = MIN_VALUE;

		for(int i=MIN_VALUE ; i<=MAX_VALUE ; i++){
			if(this.contains(i)){
				if(counter != MIN_VALUE){
					if(counter % 10 != 0){
						str.append(", ");
					}
					else{
						str.append(",\n");
					}
				}
				str.append(Integer.toString(i));
				counter++;
			}
		}

		str.append("}");
		return str.toString();
	}
}
