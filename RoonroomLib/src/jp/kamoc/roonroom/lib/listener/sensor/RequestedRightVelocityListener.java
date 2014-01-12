package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Requested Right Velocity
 * @author kamoc
 *
 */
public abstract class RequestedRightVelocityListener implements SensorListener {

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
		return 41;
	}
	
}
