package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Left Encoder Counts
 * @author kamoc
 *
 */
public abstract class LeftEncoderCountListener implements SensorListener {

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
		return 44;
	}

}
