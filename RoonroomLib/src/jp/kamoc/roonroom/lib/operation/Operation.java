package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.listener.StreamListener;
import jp.kamoc.roonroom.lib.listener.sensor.SensorListener;

/**
 * 命令クラス
 * @author kamoc
 *
 */
public interface Operation {

	/**
	 * 通信を開始する
	 */
	abstract void start();

	/**
	 * 動作モードを変更する
	 * @param mode
	 */
	abstract void changeMode(RRL.OPERATIONG_MODE mode);

	/**
	 * 掃除を開始する
	 */
	abstract void clean();

	/**
	 * 掃除モードを指定して掃除を開始する
	 * @param mode
	 */
	abstract void clean(RRL.CLEANING_MODE mode);

	/**
	 * 掃除実行スケジュールを設定する
	 * @param cleaningSchedule
	 */
	abstract void setSchedule(CleaningSchedule cleaningSchedule);

	/**
	 * 掃除実行スケジュールを消去する
	 */
	abstract void clearSchedule();

	/**
	 * 時刻を設定する
	 * @param day 曜日
	 * @param hour 時
	 * @param min 分
	 */
	abstract void setDayTime(RRL.DAY day, int hour, int min);

	/**
	 * 電源を切る
	 */
	abstract void powerOff();

	/**
	 * 走行する(速度とカーブの半径を指定)
	 * @param velocity 速度(-500 〜 500 mm/s)
	 * @param radius カーブの半径(-2000 〜 2000 mm)
	 */
	abstract void drive(int velocity, int radius);

	/**
	 * 走行する(モータ速度を直接指定)
	 * @param rightVelocity 右車輪の速度(-500 〜 500 mm/s)
	 * @param leftVelocity 左車輪の速度(-500 〜 500 mm/s)
	 */
	abstract void driveDirect(int rightVelocity, int leftVelocity);

	/**
	 * 走行する(モータの出力を指定)
	 * @param rightWheel 右車輪の出力(-255〜255)
	 * @param leftWheel 左車輪の出力(-255〜255)
	 */
	abstract void drivePWM(int rightWheel, int leftWheel);

	/**
	 * モータを動かす(出力は常に最高値)
	 * @param motorConfig
	 */
	abstract void motors(MotorConfig motorConfig);

	/**
	 * モータを動かす(出力値を指定)
	 * @param mainBrush メインブラシの出力 (-127〜127)
	 * @param sideBrush サイドブラシの出力 (-127〜127)
	 * @param vacuum 吸引パワーの出力 (0〜127)
	 */
	abstract void motorsPWM(int mainBrush, int sideBrush, int vacuum);

	/**
	 * LEDを点灯する
	 * @param ledConfig
	 */
	abstract void led(LEDConfig ledConfig);

	/**
	 * SchedulingLEDを点灯する
	 * @param config
	 */
	abstract void schedulingLed(SchedulingLEDConfig config);

	/**
	 * デジタルLEDを点灯する
	 * @param config
	 */
	abstract void digitLedRaw(DigitLEDConfig config);

	/**
	 * デジタルLEDに数値を表示する
	 * @param n 表示する数値
	 */
	abstract void digitLedRaw(int n);

	/**
	 * デジタルLEDに文字列を表示する
	 * @param str 文字列
	 */
	abstract void digitLedRaw(String str);

	/**
	 * ボタンを押下する
	 * @param buttons 押下するボタン
	 */
	abstract void button(RRL.BUTTON... buttons);

	/**
	 * 楽曲を設定する
	 * @param number
	 * @param song
	 */
	abstract void setSong(RRL.SONG number, Song song);

	/**
	 * 楽曲を再生する
	 * @param number
	 */
	abstract void playSong(RRL.SONG number);
	
	/**
	 * 単一のセンサ値を取得する
	 * @param listener
	 */
	abstract void listen(SensorListener listener);
	
	/**
	 * ストリーミングを停止する
	 */
	abstract void pauseStream();
	
	/**
	 * ストリーミングを再開する
	 */
	abstract void resumeStream();
	
	/**
	 * ストリーミングを開始する
	 * @param listener
	 */
	abstract void stream(StreamListener listener);
}