package jp.kamoc.roonroom.lib.listener.sensor;

public abstract class RightEncoderCountListener implements SensorListener {

	@Override
	public int getDataBytes() {
		return 2;
	}

	@Override
	public boolean isSigned() {
		return false;
	}

	@Override
	public int getPacketId() {
		return 42;
	}

}
