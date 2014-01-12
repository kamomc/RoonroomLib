package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Wall
 * @author kamoc
 *
 */
public abstract class WallListener implements SensorListener {

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
		return 8;
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
	 * @param wallSeen 壁があるか
	 */
	public abstract void onReceive(boolean wallSeen);

}
