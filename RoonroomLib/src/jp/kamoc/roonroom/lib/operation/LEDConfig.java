package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;

/**
 * LEDの設定クラス
 * @author kamoc
 *
 */
public class LEDConfig {
	private static final int MAX_COLOR = 255;
	private static final int MIN_COLOR = 0;
	private static final int MAX_INTENSITY = 255;
	private static final int MIN_INTENSITY = 0;
	private boolean checkRobot;
	private boolean dock;
	private boolean spot;
	private boolean debris;
	private boolean[] reserved;
	private int color;
	private int intensity;
	
	/**
	 * コンストラクタ
	 */
	public LEDConfig() {
		reserved = new boolean[]{ false, false, false, false };
	}
	
	private void adjust(){
		if(color > MAX_COLOR){
			color = MAX_COLOR;
		}
		if(color < MIN_COLOR){
			color = MIN_COLOR;
		}
		if(intensity > MAX_INTENSITY){
			intensity = MAX_INTENSITY;
		}
		if(intensity < MIN_INTENSITY){
			intensity = MIN_INTENSITY;
		}
	}
	
	/**
	 * CheckRobotLEDの点灯を設定する
	 * @param checkRobot
	 */
	public void setCheckRobot(boolean checkRobot) {
		this.checkRobot = checkRobot;
	}

	/**
	 * DockLEDの点灯を設定する
	 * @param dock
	 */
	public void setDock(boolean dock) {
		this.dock = dock;
	}

	/**
	 * SpotLEDの点灯を設定する
	 * @param spot
	 */
	public void setSpot(boolean spot) {
		this.spot = spot;
	}

	/**
	 * DebrisLEDの点灯を設定する
	 * @param debris
	 */
	public void setDebris(boolean debris) {
		this.debris = debris;
	}

	/**
	 * バッテリLEDの点灯色を設定する
	 * @param color 0:緑 〜 255:赤
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * バッテリLEDの明るさを設定する
	 * @param intensity 0:消灯 〜 255: 全灯
	 */
	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	/**
	 * 未割り当てビットの値を設定する(ルンバのバージョン・機種によっては使える場合あり)
	 * @param bit
	 * @param state
	 */
	public void setReserved(RRL.LED_RESERVED bit, boolean state){
		switch (bit) {
		case BIT_4:
			reserved[0] = state;
			break;
		case BIT_5:
			reserved[1] = state;
			break;
		case BIT_6:
			reserved[2] = state;
			break;
		case BIT_7:
			reserved[3] = state;
			break;
		default:
			break;
		}
	}

	/**
	 * 命令値を取得する
	 * @return 命令値
	 */
	public int getValue() {
		adjust();
		int result = 0;
		if(debris){
			result = result | 1;
		}
		if(spot){
			result = result | (1<<1);
		}
		if(dock){
			result = result | (1<<2);
		}
		if(checkRobot){
			result = result | (1<<3);
		}
		if(reserved[0]){
			result = result | (1<<4);
		}
		if(reserved[1]){
			result = result | (1<<5);
		}
		if(reserved[2]){
			result = result | (1<<6);
		}
		if(reserved[3]){
			result = result | (1<<7);
		}
		return result;
	}
	
	/**
	 * バッテリLEDの色の値を取得する
	 * @return 色の値
	 */
	public int getColor() {
		return color;
	}
	/**
	 * バッテリLEDの明るさの値を取得する
	 * @return 明るさの値
	 */
	public int getIntensity() {
		return intensity;
	}
	
	/**
	 * 全てのLEDを点灯する設定にする(バッテリーは緑色)
	 */
	public void allLighting(){
		setCheckRobot(true);
		setColor(MIN_COLOR);
		setDebris(true);
		setDock(true);
		setIntensity(MAX_INTENSITY);
		setSpot(true);
		setReserved(RRL.LED_RESERVED.BIT_4, true);
		setReserved(RRL.LED_RESERVED.BIT_5, true);
		setReserved(RRL.LED_RESERVED.BIT_6, true);
		setReserved(RRL.LED_RESERVED.BIT_7, true);
	}
	
	
}
