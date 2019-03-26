package fr.istic.prg1.tp4;

/**
 * An implementation of a double linked List, similar to the one defined in TD5, iterable with an iterator
 * @author Donatien Sattler
 */
public class List<T>{
	/** This Element is used to avoid special cases such as when the list is empty */
	private Element flag;
	/** The number of elements in the list */
	private int size;

	private static final int MAX_SIZE = Integer.MAX_VALUE;

	/**
	 * Create an empty List with just the flag
	 * @ensure flag.next = flag, flag.previous = flag
	 */
	public List(){
		this.flag = new Element();
		this.flag.next = flag;
		this.flag.previous = flag;
		this.flag.value = null;
		
		this.size = 0;
	}
	
	/**
	 * @return an iterator that points to the flag
	 */
	public Iterator<T> iterator(){
		return new ListIterator();
	}

	/**
	 * @return true if and only if the size of this list is equals to 0
	 */
	public boolean isEmpty(){
		return this.size == 0;
	}

	/**
	 * @return the size of the list
	 */
	public int getSize(){
		return this.size;
	}

	private class Element{
		private T value;
		private Element next;
		private Element previous;
	}

	public class ListIterator implements Iterator<T>{
		/** Current element */
		private Element current;

		/** 
		 * Make a new iterator 
		 * @ensure this.current == this.flag
		 */
		public ListIterator(){
			this.current = flag.next;
		}

		/**
		 * @ensure this.current = this.current.next
		 * @return the value in the new current element
		 */
		public T goForward(){
			this.current = this.current.next;
			return this.current.value;
		}

		/**
		 * @ensure this.current = this.current.previous
		 * @return the value in the new current element
		 */
		public T goBackward(){
			this.current = this.current.previous;
			return this.current.value;
		}

		/**
		 * @return true if and only if this.current == this.flag
		 */
		public boolean isOnFlag(){
			return this.current == flag;
		}

		/**
		 * Remove the current element of the list, except if the current element is the flag
		 * @ensure the current element of the list will be set to the element that was at the left of the one deleted
		 * @throws ListException with the type DELETE_FLAG
		 */
		public void remove(){
			if(isOnFlag()){
				throw new ListException(ListException.type.DELETE_FLAG);
			}

			this.current.previous.next = this.current.next;
			this.current.next.previous = this.current.previous;
			this.current = this.current.previous;

			size--;
		}

		/**
		 * @return the value stored in the current element
		 */
		public T getValue(){
			return this.current.value;
		}

		/**
		 * Add an element to the left of the current one, with the value v
		 * @param v the value of the element
		 * @ensure the current element becomes the one added
		 */
		public void addLeft(T v){
			ensureListNotTooLong();
			ensureNewValueNotNull(v);
			Element newElement = new Element();

			newElement.value = v;
			newElement.next = this.current;
			newElement.previous = this.current.previous;

			this.current.previous.next = newElement;
			this.current.previous = newElement;
			this.current = newElement;

			size++;
		}

		/**
		 * Add an element to the right of the current one, with the value v
		 * @param v the value of the element
		 * @ensure the current element becomes the one added
		 */
		public void addRight(T v){
			ensureListNotTooLong();
			ensureNewValueNotNull(v);
			Element newElement = new Element();

			newElement.value = v;
			newElement.next = this.current.next;
			newElement.previous = this.current;

			this.current.next.previous = newElement;
			this.current.next = newElement;
			this.current = newElement;

			size++;
		}

		/**
		 * Sets the value of the current element to v
		 * @param v the value of the element
		 * @throws ListException with the type ADDING_NULL_VALUE
		 */
		public void setValue(T v){
			ensureNewValueNotNull(v);
			this.current.value = v;
		}
	
		/**
		 * Put the iterator on the flag
		 */
		public void restart(){
			this.current = flag.next;
		}

		private void ensureNewValueNotNull(T v){
			if(v == null){
				throw new ListException(ListException.type.ADDING_NULL_VALUE);
			}
		}

		private void ensureListNotTooLong(){
			if(size == MAX_SIZE){
				throw new ListException(ListException.type.LIST_TOO_LONG);
			}
		}
	}
}
