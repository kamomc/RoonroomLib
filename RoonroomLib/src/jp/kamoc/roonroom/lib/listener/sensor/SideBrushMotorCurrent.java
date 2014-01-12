package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Side Brush Motor Current
 * @author kamoc
 *
 */
public abstract class SideBrushMotorCurrent implements SensorListener {

	@Override
	public int getDataBytes() {
		return 2;
	}

	@Override
	public boolean isSigned() {
		return true;
	}

	@Override
	public int getPacketId() {
		return 57;
	}

}
