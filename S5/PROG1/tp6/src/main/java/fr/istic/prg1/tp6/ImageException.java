package fr.istic.prg1.tp6;

@SuppressWarnings("serial")

public class ImageException extends RuntimeException{
	public enum Type{
		test("test");

		private final String message;

		private Type(String message){
			this.message = message;
		}

		public String getMessage(){
			return this.message;
		}
	}

	private ImageException.Type exceptionType;

	public ImageException(ImageException.Type exceptionType){
		super(exceptionType.getMessage());
		this.exceptionType = exceptionType;
	}

	public Type getType(){
		return this.exceptionType;
	}

}
