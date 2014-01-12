package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Angle
 * @author kamoc
 *
 */
public abstract class AngleListener implements SensorListener {

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
		return 20;
	}

}
