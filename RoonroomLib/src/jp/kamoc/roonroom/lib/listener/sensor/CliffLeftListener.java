package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Cliff Left
 * @author kamoc
 *
 */
public abstract class CliffLeftListener implements SensorListener {

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
		return 9;
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
	 * @param cliff 壁があるか
	 */
	public abstract void onReceive(boolean cliff);

}
