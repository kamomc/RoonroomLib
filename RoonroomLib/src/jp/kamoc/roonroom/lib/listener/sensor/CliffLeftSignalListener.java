package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Cliff Left Signal
 * @author kamoc
 *
 */
public abstract class CliffLeftSignalListener implements SensorListener {

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
		return 28;
	}

}
