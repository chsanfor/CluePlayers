package main;

public class BadConfigFormatException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4409252344731273290L;
	private String message;
	public BadConfigFormatException(String message) {
		this.message = message;
		System.out.println(message);
	}
	public String toString() {
		return message;
	}
}
