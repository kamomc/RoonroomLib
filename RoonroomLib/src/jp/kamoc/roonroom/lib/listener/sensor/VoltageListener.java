package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Voltage
 * @author kamoc
 *
 */
public abstract class VoltageListener implements SensorListener {

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
		return 22;
	}

}
