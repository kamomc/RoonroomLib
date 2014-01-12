package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Cliff Right
 * @author kamoc
 *
 */
public abstract class CliffRightListener implements SensorListener {

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
		return 12;
	}

	@Override
	public void onReceive(int value) {
		if (value == 1) {
			onReceive(true);
		} else {
			onReceive(false);
		}
	}

	/**
	 * @param cliff 壁があるか
	 */
	public abstract void onReceive(boolean cliff);
}
