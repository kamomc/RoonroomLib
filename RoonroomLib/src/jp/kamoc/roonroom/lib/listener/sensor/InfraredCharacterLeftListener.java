package jp.kamoc.roonroom.lib.listener.sensor;


public abstract class InfraredCharacterLeftListener implements SensorListener {

	@Override
	public int getDataBytes() {
		return 1;
	}

	@Override
	public boolean isSigned() {
		return false;
	}

	@Override
	public int getPacketId() {
		return 52;
	}

}