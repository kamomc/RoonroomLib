package jp.kamoc.roonroom.lib.serial;

public interface SerialAdapter {
	static final int BAUD = 115200;
	
	void open() throws SerialConnectionException;
	void send(int command) throws SerialConnectionException;
	void close();
	int receive() throws SerialConnectionException;
}
