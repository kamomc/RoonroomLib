package jp.kamoc.roonroom.lib.command;

@SuppressWarnings("serial")
public class CommandSendTimeoutException extends Exception {
	public CommandSendTimeoutException() {
		super();
	}

	public CommandSendTimeoutException(String message) {
		super(message);
	}

	public CommandSendTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandSendTimeoutException(Throwable cause) {
		super(cause);
	}
}
