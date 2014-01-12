package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Wall Signal
 * @author kamoc
 *
 */
public abstract class WallSignalListener implements SensorListener {

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
		return 27;
	}

}
