package jp.kamoc.roonroom.lib.serial;

import jp.kamoc.roonroom.lib.listener.PacketListener;

public interface SerialAdapter {
	static final int BAUD = 115200;
	
	void open() throws SerialConnectionException;
	void send(int command) throws SerialConnectionException;
	void close();
	void receive() throws SerialConnectionException;
	void setPacketListener(PacketListener packetListener);
}
