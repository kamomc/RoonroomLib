package jp.kamoc.roonroom.lib.serial;

@SuppressWarnings({ "serial", "javadoc" })
public class SerialConnectionException extends Exception {
	public SerialConnectionException() {
		super();
	}
	
	public SerialConnectionException(String message){
		super(message);
	}
	
	public SerialConnectionException(String message, Throwable cause){
		super(message, cause);
	}
	
	public SerialConnectionException(Throwable cause){
		super(cause);
	}
}
