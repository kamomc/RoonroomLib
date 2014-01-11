package jp.kamoc.roonroom.lib.listener;

public abstract class DistanceListener implements SensorListener {

	@Override
	public int getDataBytes() {
		return 2;
	}

	@Override
	public boolean isSigned() {
		return true;
	}

	@Override
	public int getPacketId() {
		return 19;
	}

}
