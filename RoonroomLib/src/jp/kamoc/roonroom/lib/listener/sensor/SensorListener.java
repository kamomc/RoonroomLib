package jp.kamoc.roonroom.lib.listener.sensor;

/**
 * センサー値のリスナインタフェース
 * @author kamoc
 *
 */
public interface SensorListener {
	/**
	 * @return データバイト数
	 */
	int getDataBytes();

	/**
	 * @return 符号付きか
	 */
	boolean isSigned();
	
	/**
	 * @return パケットID
	 */
	int getPacketId();

	/**
	 * @param value センサー値
	 */
	void onReceive(int value);
}
