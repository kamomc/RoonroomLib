package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Virtual Wall
 * @author kamoc
 *
 */
public abstract class VirtualWallListener implements SensorListener {

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
		return 13;
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
	 * @param virtualWall バーチャルウォールがあるか
	 */
	public abstract void onReceive(boolean virtualWall);

}
