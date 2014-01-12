package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Infrared Character Omni
 * @author kamoc
 *
 */
public abstract class InfraredCharacterOmniListener implements SensorListener {

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
		return 17;
	}

}
