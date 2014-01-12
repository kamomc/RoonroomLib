package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Song Playing
 * @author kamoc
 *
 */
public abstract class SongPlayListener implements SensorListener {

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
		return 37;
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
	 * @param play
	 */
	public abstract void onReceive(boolean play);
}
