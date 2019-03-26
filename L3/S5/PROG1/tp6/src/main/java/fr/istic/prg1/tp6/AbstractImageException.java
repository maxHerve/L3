package fr.istic.prg1.tp6;

@SuppressWarnings("serial")

public class AbstractImageException extends RuntimeException{
	public enum Type{
		EQUALS_EMPTY_TREE("Cannot determine if an empty image equals another image"),
		HEIGHT_EMPTY_TREE("Cannot compute the height of an empty tree !"),
		SAVE_EMPTY_TREE("Cannot save an empty tree in a file !"),
		MALFORMED_TREE("The tree is not well formed !"),
		NUMBER_NODES_EMPTY_TREE("Cannot compute the number of nodes of an empty tree !");

		private final String message;

		private Type(String message){
			this.message = message;
		}

		public String getMessage(){
			return this.message;
		}
	}

	private AbstractImageException.Type exceptionType;

	public AbstractImageException(AbstractImageException.Type exceptionType){
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
	}

	public Type getType(){
		return this.exceptionType;
	}
}
