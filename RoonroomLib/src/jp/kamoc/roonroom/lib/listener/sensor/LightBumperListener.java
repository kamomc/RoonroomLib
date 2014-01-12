package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.util.BitUtil;
import jp.kamoc.roonroom.lib.util.BitUtil.BIT;

public abstract class LightBumperListener implements SensorListener {

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
		return 45;
	}

	@Override
	public void onReceive(int value) {
		boolean right = BitUtil.isTrue(value, BIT.BIT_5);
		boolean frontRight = BitUtil.isTrue(value, BIT.BIT_4);
		boolean centerRight = BitUtil.isTrue(value, BIT.BIT_3);
		boolean centerLeft = BitUtil.isTrue(value, BIT.BIT_2);
		boolean frontLeft = BitUtil.isTrue(value, BIT.BIT_1);
		boolean left = BitUtil.isTrue(value, BIT.BIT_0);
		onReceive(right, frontRight, centerRight, centerLeft, frontLeft, left);
	}
	
	public abstract void onReceive(boolean right, boolean frontRight, boolean centerRight, boolean centerLeft, boolean frontLeft, boolean left);

}
