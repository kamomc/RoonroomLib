package jp.kamoc.roonroom.lib.operation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jp.kamoc.roonroom.lib.constants.RRL;

/**
 * デジタルLED設定のクラス
 * @author kamoc
 *
 */
public class DigitLEDConfig {
	Map<RRL.DIGIT_LED, Map<RRL.DIGIT_LED_BIT, Boolean>> ledMap;

	/**
	 * コンストラクタ
	 */
	public DigitLEDConfig() {
		ledMap = new HashMap<RRL.DIGIT_LED, Map<RRL.DIGIT_LED_BIT, Boolean>>();
		for (RRL.DIGIT_LED raw : RRL.DIGIT_LED.values()) {
			Map<RRL.DIGIT_LED_BIT, Boolean> rawMap = new HashMap<RRL.DIGIT_LED_BIT, Boolean>();
			for (RRL.DIGIT_LED_BIT bit : RRL.DIGIT_LED_BIT.values()) {
				rawMap.put(bit, false);
			}
			ledMap.put(raw, rawMap);
		}
	}

	/**
	 * LEDを点灯する
	 * @param raw LEDの列
	 * @param bits 点灯する箇所
	 */
	public void setRightOn(RRL.DIGIT_LED raw, RRL.DIGIT_LED_BIT... bits) {
		Map<RRL.DIGIT_LED_BIT, Boolean> map = new HashMap<RRL.DIGIT_LED_BIT, Boolean>();
		for (RRL.DIGIT_LED_BIT bit : RRL.DIGIT_LED_BIT.values()) {
			map.put(bit, Arrays.asList(bits).contains(bit));
		}
		ledMap.put(raw, map);
	}

	/**
	 * 命令値を取得する
	 * @param raw LEDの列
	 * @return 命令値
	 */
	public int getValue(RRL.DIGIT_LED raw) {
		Map<RRL.DIGIT_LED_BIT, Boolean> map = ledMap.get(raw);
		int result = 0;
		if (map.get(RRL.DIGIT_LED_BIT.A)) {
			result = result | (1 << 0);
		}
		if (map.get(RRL.DIGIT_LED_BIT.B)) {
			result = result | (1 << 1);
		}
		if (map.get(RRL.DIGIT_LED_BIT.C)) {
			result = result | (1 << 2);
		}
		if (map.get(RRL.DIGIT_LED_BIT.D)) {
			result = result | (1 << 3);
		}
		if (map.get(RRL.DIGIT_LED_BIT.E)) {
			result = result | (1 << 4);
		}
		if (map.get(RRL.DIGIT_LED_BIT.F)) {
			result = result | (1 << 5);
		}
		if (map.get(RRL.DIGIT_LED_BIT.G)) {
			result = result | (1 << 6);
		}
		return result;
	}
}
