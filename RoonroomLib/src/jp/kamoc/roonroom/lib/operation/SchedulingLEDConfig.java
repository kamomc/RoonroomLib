package jp.kamoc.roonroom.lib.operation;

import java.util.HashMap;
import java.util.Map;

import jp.kamoc.roonroom.lib.constants.RRL;

/**
 * スケジューリングLEDの設定クラス
 * @author kamoc
 *
 */
public class SchedulingLEDConfig {
	private Map<RRL.DAY, Boolean> weekday = new HashMap<RRL.DAY, Boolean>();
	private boolean colon;
	private boolean pm;
	private boolean am;
	private boolean clock;
	private boolean schedule;
	private boolean[] schedulingReserved;
	private boolean[] weekdayReserved;

	/**
	 * コンストラクタ
	 */
	public SchedulingLEDConfig() {
		schedulingReserved = new boolean[] { false, false, false };
		weekdayReserved = new boolean[] { false };
		for (RRL.DAY day : RRL.DAY.values()) {
			weekday.put(day, false);
		}
	}

	/**
	 * コロンの点灯状態を設定する
	 * @param colon
	 */
	public void setColon(boolean colon) {
		this.colon = colon;
	}

	/**
	 * PMの点灯状態を設定する
	 * @param pm
	 */
	public void setPm(boolean pm) {
		this.pm = pm;
	}

	/**
	 * AMの点灯状態を設定する
	 * @param am
	 */
	public void setAm(boolean am) {
		this.am = am;
	}

	/**
	 * CLOCKの点灯状態を設定する
	 * @param clock
	 */
	public void setClock(boolean clock) {
		this.clock = clock;
	}

	/**
	 * SCHEDULEの点灯状態を設定する
	 * @param schedule
	 */
	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}

	/**
	 * 曜日ランプの点灯状態を設定する
	 * @param day 曜日
	 * @param state 状態
	 */
	public void setWeekday(RRL.DAY day, boolean state) {
		weekday.put(day, state);
	}

	/**
	 * WeekdayLEDの未割り当てビットを設定する
	 * @param bit
	 * @param state
	 */
	public void setWeekdayReserved(RRL.WEEKDAY_LED_RESERVED bit,
			boolean state) {
		switch (bit) {
		case BIT_7:
			weekdayReserved[0] = state;
			break;
		default:
			break;
		}
	}

	/**
	 * SchedulingLEDの未割り当てビットを設定する
	 * @param bit
	 * @param state
	 */
	public void setSchedulingReserved(RRL.SCHEDULING_LED_RESERVED bit,
			boolean state) {
		switch (bit) {
		case BIT_5:
			schedulingReserved[0] = state;
			break;
		case BIT_6:
			schedulingReserved[1] = state;
			break;
		case BIT_7:
			schedulingReserved[2] = state;
			break;
		default:
			break;
		}
	}

	/**
	 * Weekdayの命令値を取得する
	 * @return 命令値
	 */
	public int getWeekdayValue() {
		int result = 0;
		for (RRL.DAY day : RRL.DAY.values()) {
			Boolean state = weekday.get(day);
			if (state) {
				result = result | (1 << day.getCode());
			}
		}
		if(weekdayReserved[0]){
			result = result | (1 << 7);
		}
		return result;
	}
	
	/**
	 * Schedulingの命令値を取得する
	 * @return 命令値
	 */
	public int getSchedulingValue(){
		int result = 0;
		if(colon){
			result = result | (1<<0);
		}
		if(pm){
			result = result | (1<<1);
		}
		if(am){
			result = result | (1<<2);
		}
		if(clock){
			result = result | (1<<3);
		}
		if(schedule){
			result = result | (1<<4);
		}
		if(schedulingReserved[0]){
			result = result | (1<<5);
		}
		if(schedulingReserved[1]){
			result = result | (1<<6);
		}
		if(schedulingReserved[2]){
			result = result | (1<<7);
		}
		return result;
	}

}
