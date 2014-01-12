package jp.kamoc.roonroom.lib.listener.sensor;

import jp.kamoc.roonroom.lib.util.BitUtil;
import jp.kamoc.roonroom.lib.util.BitUtil.BIT;

/**
 * Wheel Overcurrents
 * @author kamoc
 *
 */
public abstract class WheelOvercurrentListener implements SensorListener {

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
		return 14;
	}

	@Override
	public void onReceive(int value) {
		boolean leftWheel = BitUtil.isTrue(value, BIT.BIT_4);
		boolean rightWheel = BitUtil.isTrue(value, BIT.BIT_3);
		boolean mainBrush = BitUtil.isTrue(value, BIT.BIT_2);
		boolean sideBrush = BitUtil.isTrue(value, BIT.BIT_0);
		onReceive(leftWheel, rightWheel, mainBrush, sideBrush);
	}

	/**
	 * @param leftWheel 左車輪がOvercurrentであるか
	 * @param rightWheel 右車輪がOvercurrentであるか
	 * @param mainBrush メインブラシがOvercurrentであるか
	 * @param sideBrush サイドブラシがOvercurrentであるか
	 */
	public abstract void onReceive(boolean leftWheel, boolean rightWheel,
			boolean mainBrush, boolean sideBrush);

}
