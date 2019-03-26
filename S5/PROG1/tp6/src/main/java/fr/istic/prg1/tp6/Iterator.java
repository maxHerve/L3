package fr.istic.prg1.tp6;

public interface Iterator<T>{
	public void goLeft();
	public void goRight();
	public void goRoot();
	public void goUp();

	public void remove();
	public void clear();
	
	public boolean isEmpty();
	public NodeType nodeType();

	public T getValue();
	public void addValue(T v);
	public void setValue(T v);
	public void switchValue(int i);
}
