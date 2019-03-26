package fr.istic.prg1.tp4;
@SuppressWarnings("serial")

public class MySetException extends RuntimeException{

	public enum type{
		VALUE_OUT_OF_RANGE("This set cannot hold values that are out of the range MIN_VALUE to MAX_VALUE"),
		VALUE_OUT_OF_SMALLSET_RANGE("The value has to be in range 0 255 !");

		private final String message;

		private type(String message){
			this.message = message;
		}

		public String getMessage(){
			return this.message;
		}
	}

	private MySetException.type exceptionType;

	public MySetException(MySetException.type exceptionType){
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
	}

	public MySetException(MySetException.type exceptionType, Throwable cause){
		super(exceptionType.getMessage(), cause);
		this.exceptionType = exceptionType;
	}

	public MySetException.type getType(){
		return this.exceptionType;
	}
}
