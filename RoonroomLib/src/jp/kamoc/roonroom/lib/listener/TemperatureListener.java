package jp.kamoc.roonroom.lib.listener;

public abstract class TemperatureListener implements SensorListener {

	@Override
	public int getDataBytes() {
		return 1;
	}

	@Override
	public boolean isSigned() {
		return true;
	}

	@Override
	public int getPacketId() {
		return 24;
	}

}
