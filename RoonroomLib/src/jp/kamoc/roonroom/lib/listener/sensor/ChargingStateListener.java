package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.constants.RRL;

/**
 * Charging State
 * @author kamoc
 *
 */
public abstract class ChargingStateListener implements SensorListener {

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
		return 21;
	}

	@Override
	public void onReceive(int value) {
		onReceive(RRL.CHARGING_STATE.getState(value));
	}

	/**
	 * @param state 充電状態
	 */
	public abstract void onReceive(RRL.CHARGING_STATE state);

}
