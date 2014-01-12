package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Cliff Right Signal
 * @author kamoc
 *
 */
public abstract class CliffRightSignalListener implements SensorListener {

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
		return 31;
	}

}
