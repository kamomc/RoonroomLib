package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.constants.RRL;

/**
 * OI Mode
 * @author kamoc
 *
 */
public abstract class OIModeListener implements SensorListener {

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
		return 35;
	}

	@Override
	public void onReceive(int value) {
		onReceive(RRL.OPERATIONG_MODE.getMode(value));
	}

	/**
	 * @param mode
	 */
	public abstract void onReceive(RRL.OPERATIONG_MODE mode);

}
