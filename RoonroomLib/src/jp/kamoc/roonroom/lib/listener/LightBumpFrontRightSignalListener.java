package jp.kamoc.roonroom.lib.listener;
import jp.kamoc.roonroom.lib.listener.SensorListener;


public abstract class LightBumpFrontRightSignalListener implements SensorListener {

	@Override
	public int getDataBytes() {
		return 2;
	}

	@Override
	public boolean isSigned() {
		return false;
	}

	@Override
	public int getPacketId() {
		return 50;
	}
	
}
