package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.util.BitUtil;
import jp.kamoc.roonroom.lib.util.BitUtil.BIT;

public abstract class ButtonListener implements SensorListener {

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
		return 18;
	}

	@Override
	public void onReceive(int value) {
		boolean clock = BitUtil.isTrue(value, BIT.BIT_7);
		boolean schedule = BitUtil.isTrue(value, BIT.BIT_6);
		boolean day = BitUtil.isTrue(value, BIT.BIT_5);
		boolean hour = BitUtil.isTrue(value, BIT.BIT_4);
		boolean minute = BitUtil.isTrue(value, BIT.BIT_3);
		boolean dock = BitUtil.isTrue(value, BIT.BIT_2);
		boolean spot = BitUtil.isTrue(value, BIT.BIT_1);
		boolean clean = BitUtil.isTrue(value, BIT.BIT_0);
		onReceive(clock, schedule, day, hour, minute, dock, spot, clean);
	}
	
	public abstract void onReceive(boolean clock, boolean schedule, boolean day, boolean hour, boolean minute, boolean dock, boolean spot, boolean clean);

}
