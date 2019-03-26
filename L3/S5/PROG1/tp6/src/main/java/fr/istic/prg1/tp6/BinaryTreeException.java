package fr.istic.prg1.tp6;

@SuppressWarnings("serial")

public class BinaryTreeException extends RuntimeException{
	public enum Type{
		ADD_ON_NON_STOP_NODE("Current node is not a stop node, can't add value on it !"),
		MOVE_AFTER_STOP_NODE("Current node is a stop node, can't go right or left !"),
		REMOVE_DOUBLE_NODE("Current node is a double one, can't remove it !"),
		REMOVE_EMPTY_NODE("Current node is a stop node, can't remove it !"),
		MOVE_BEFORE_ROOT("Current node is the root, can't go up another time !"),
		INDEX_OUT_OF_RANGE("Cannot switch values : i out of range !");

		private final String message;

		private Type(String message){
			this.message = message;
		}

		public String getMessage(){
			return this.message;
		}
	}

	private BinaryTreeException.Type exceptionType;

	public BinaryTreeException(BinaryTreeException.Type exceptionType){
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
	}

	public Type getType(){
		return this.exceptionType;
	}

}
