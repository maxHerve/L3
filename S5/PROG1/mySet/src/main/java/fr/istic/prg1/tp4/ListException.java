package fr.istic.prg1.tp4;
@SuppressWarnings("serial")

public class ListException extends RuntimeException{

	public enum type{
		DELETE_FLAG("You can't delete the flag !"),
		ADDING_NULL_VALUE("You can't set the value of an element of the list to null !"),
		LIST_TOO_LONG("This list is full ! You can't add any other element !");

		private final String message;

		private type(String message){
			this.message = message;
		}

		public String getMessage(){
			return this.message;
		}
	}

	private ListException.type exceptionType;

	public ListException(ListException.type exceptionType){
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
	}

	public ListException.type getType(){
		return this.exceptionType;
	}
}
