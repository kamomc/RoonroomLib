package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.util.BitUtil;
import jp.kamoc.roonroom.lib.util.BitUtil.BIT;

/**
 * Charging Sources Available
 * @author kamoc
 *
 */
public abstract class ChargingSourcesAvailableListener implements SensorListener {

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
		return 34;
	}

	@Override
	public void onReceive(int value) {
		boolean homeBase = BitUtil.isTrue(value, BIT.BIT_1);
		boolean internalCharger = BitUtil.isTrue(value, BIT.BIT_0);
		onReceive(homeBase, internalCharger);
	}
	
	/**
	 * @param homeBase
	 * @param internalCharger
	 */
	public abstract void onReceive(boolean homeBase, boolean internalCharger);

}
