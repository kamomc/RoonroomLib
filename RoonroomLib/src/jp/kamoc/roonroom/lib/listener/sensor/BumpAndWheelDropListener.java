package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.util.BitUtil;
import jp.kamoc.roonroom.lib.util.BitUtil.BIT;

public abstract class BumpAndWheelDropListener implements SensorListener {

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
		return 0;
	}

	@Override
	public void onReceive(int value) {
		boolean wheelDropLeft = BitUtil.isTrue(value, BIT.BIT_3);
		boolean wheelDropRight = BitUtil.isTrue(value, BIT.BIT_2);
		boolean bumpLeft = BitUtil.isTrue(value, BIT.BIT_1);
		boolean bumpRight = BitUtil.isTrue(value, BIT.BIT_0);
		onReceive(wheelDropLeft, wheelDropRight, bumpLeft, bumpRight);
	}
	
	abstract public void onReceive(boolean wheelDropLeft, boolean wheelDropRight, boolean bumpLeft, boolean bumpRight);
}
