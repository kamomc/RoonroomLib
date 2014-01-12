package jp.kamoc.roonroom.lib.operation;

import java.util.HashMap;
import java.util.Map;

import jp.kamoc.roonroom.lib.constants.RRL;

/**
 * 掃除実行スケジュールのクラス
 * @author kamoc
 *
 */
public class CleaningSchedule {
	private static final int DEFAULT_DAYS = 7;

	private Map<RRL.DAY, Time> schedule = new HashMap<RRL.DAY, Time>();

	private int days = DEFAULT_DAYS;

	/**
	 * コンストラクタ
	 */
	public CleaningSchedule() {
		for (RRL.DAY day : RRL.DAY.values()) {
			schedule.put(day, new Time(0, 0));
		}
	}

	/**
	 * 掃除実行スケジュールを追加する
	 * @param day 曜日
	 * @param hour 時
	 * @param min 分
	 */
	public void addSchedule(RRL.DAY day, int hour, int min) {
		schedule.put(day, new Time(hour, min));
	}

	/**
	 * 掃除実行スケジュールの継続日数を取得する
	 * @return 継続日数
	 */
	public int getDays() {
		return days;
	}

	/**
	 * 掃除実行スケジュールの継続日数を設定する
	 * @param days
	 */
	public void setDays(int days) {
		this.days = days;
	}

	/**
	 * スケジュールを表すMapを取得する
	 * @return スケジュールを表すMap
	 */
	public Map<RRL.DAY, Time> getSchedule() {
		return schedule;
	}
}
