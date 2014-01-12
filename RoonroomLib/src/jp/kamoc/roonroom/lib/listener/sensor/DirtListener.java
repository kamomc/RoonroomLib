package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Dirt
 * @author kamoc
 *
 */
public abstract class DirtListener implements SensorListener {

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
		return 15;
	}
	
}
