package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Cliff Front Right
 * @author kamoc
 *
 */
public abstract class CliffFrontRightListener implements SensorListener {

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
		return 11;
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
