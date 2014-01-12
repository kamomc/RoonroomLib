package jp.kamoc.roonroom.lib.listener.sensor;


/**
 * Distance
 * 前回呼び出し時からの移動距離を取得する(単位:cm)
 * @author kamoc
 *
 */
public abstract class DistanceListener implements SensorListener {

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
		return 19;
	}

}
