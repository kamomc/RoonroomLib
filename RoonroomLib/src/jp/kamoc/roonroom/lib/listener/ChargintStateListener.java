package jp.kamoc.roonroom.lib.listener;

import jp.kamoc.roonroom.lib.constants.RRL;

public abstract class ChargintStateListener implements SensorListener {

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

	public abstract void onReceive(RRL.CHARGING_STATE state);

}
