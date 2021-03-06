package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Stasis
 * @author kamoc
 *
 */
public abstract class StasisListener implements SensorListener {

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
		return 58;
	}

	@Override
	public void onReceive(int value) {
		if(value == 1){
			onReceive(true);
		}else{
			onReceive(false);
		}
	}
	
	/**
	 * @param stasis
	 */
	public abstract void onReceive(boolean stasis);

}
