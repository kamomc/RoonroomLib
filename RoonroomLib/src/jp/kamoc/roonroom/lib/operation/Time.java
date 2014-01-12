package jp.kamoc.roonroom.lib.operation;

/**
 * 時間を表すクラス
 * @author kamoc
 *
 */
public class Time {
	/**
	 * 時
	 */
	public int h = 0;
	/**
	 * 分
	 */
	public int m = 0;

	/**
	 * コンストラクタ
	 * @param hour 時
	 * @param min 分
	 */
	public Time(int hour, int min) {
		if (hour >= 24) {
			return;
		}
		if (min >= 60) {
			return;
		}
		h = hour;
		m = min;
	}
}