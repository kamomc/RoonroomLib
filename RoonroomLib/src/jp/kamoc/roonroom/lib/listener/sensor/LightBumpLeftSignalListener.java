package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Light Bump Left Signal
 * @author kamoc
 *
 */
public abstract class LightBumpLeftSignalListener implements SensorListener {

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
		return 46;
	}

}
