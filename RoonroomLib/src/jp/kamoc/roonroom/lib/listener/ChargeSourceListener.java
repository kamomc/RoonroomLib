package jp.kamoc.roonroom.lib.listener;

import jp.kamoc.roonroom.lib.listener.BitUtil.BIT;

public abstract class ChargeSourceListener implements SensorListener {

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
		boolean internalChager = BitUtil.isTrue(value, BIT.BIT_0);
		onReceive(homeBase, internalChager);
	}
	
	public abstract void onReceive(boolean homeBase, boolean internalChager);

}
