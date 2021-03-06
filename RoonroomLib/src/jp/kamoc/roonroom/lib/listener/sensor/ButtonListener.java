package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.listener.BitUtil;
import jp.kamoc.roonroom.lib.listener.BitUtil.BIT;

/**
 * Buttons
 * @author kamoc
 *
 */
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
	
	/**
	 * @param clock clockボタンが押されているか
	 * @param schedule scheduleボタンが押されているか
	 * @param day dayボタンが押されているか
	 * @param hour hourボタンが押されているか
	 * @param minute minuteボタンが押されているか
	 * @param dock dockボタンが押されているか
	 * @param spot spotボタンが押されているか
	 * @param clean cleanボタンが押されているか
	 */
	public abstract void onReceive(boolean clock, boolean schedule, boolean day, boolean hour, boolean minute, boolean dock, boolean spot, boolean clean);

}
