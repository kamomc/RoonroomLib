package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Right Motor Current
 * @author kamoc
 *
 */
public abstract class RightMotorCurrentListener implements SensorListener {

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
		return 55;
	}

}
