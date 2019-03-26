package fr.istic.prg1.tp4;

public interface Iterator<T>{
	/**
	 * Go to the next element
	 */
	public T goForward();

	/**
	 * Go to the previous element
	 */
	public T goBackward();

	/**
	 * @return true if and only if the current element is the flag of the collection
	 */
	public boolean isOnFlag();

	/**
	 * Remove the current element of the collection, except if the current element is the flag of the collection
	 */
	public void remove();

	/**
	 * @return the value stored in the current element
	 */
	public T getValue();

	/**
	 * Add an element to the left of the current one, with the value v
	 * @param v the value of the element
	 */
	public void addLeft(T v);

	/**
	 * Add an element to the right of the current one, with the value v
	 * @param v the value of the element
	 */
	public void addRight(T v);

	/**
	 * Set the value of the current element to v
	 * @param v the value of the element
	 */
	public void setValue(T v);

	/**
	 * Put the iterator on the head of the list
	 */
	public void restart();
}
