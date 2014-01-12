package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Infrared Character Right
 * @author kamoc
 *
 */
public abstract class InfraredCharacterRightListener implements SensorListener {

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
		return 52;
	}

}
