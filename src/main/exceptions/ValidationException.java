package exceptions;

public class ValidationException extends RuntimeException {
	private final String location;
	
	public ValidationException(String message, String location) {
		super(message);
	}
}
