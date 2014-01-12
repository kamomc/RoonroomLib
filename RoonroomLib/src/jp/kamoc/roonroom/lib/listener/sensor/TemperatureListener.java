package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * Temperature
 * バッテリーの温度を取得する(-128〜128 単位:セルシウス温度)
 * @author kamoc
 *
 */
public abstract class TemperatureListener implements SensorListener {

	@Override
	public int getDataBytes() {
		return 1;
	}

	@Override
	public boolean isSigned() {
		return true;
	}

	@Override
	public int getPacketId() {
		return 24;
	}

}
