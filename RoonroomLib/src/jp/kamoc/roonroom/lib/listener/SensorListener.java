package jp.kamoc.roonroom.lib.listener;

public interface SensorListener {
	int getDataBytes();

	boolean isSigned();
	
	int getPacketId();

	void onReceive(int value);
}
