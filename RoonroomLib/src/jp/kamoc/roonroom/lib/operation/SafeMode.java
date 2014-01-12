package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.constants.RRL.BUTTON;
import jp.kamoc.roonroom.lib.constants.RRL.SONG;
import jp.kamoc.roonroom.lib.listener.StreamListener;
import jp.kamoc.roonroom.lib.listener.sensor.SensorListener;

/**
 * SAFEモード
 * @author kamoc
 *
 */
public class SafeMode implements Operation {
	private OperationImpl operation;
	
	/**
	 * コンストラクタ
	 * @param operation
	 */
	public SafeMode(OperationImpl operation) {
		this.operation = operation;
	}

	@Override
	public void start() {
		operation.start();
	}

	@Override
	public void changeMode(RRL.OPERATIONG_MODE mode) {
		operation.changeMode(mode);
	}

	@Override
	public void clean() {
		operation.clean();
	}

	@Override
	public void clean(RRL.CLEANING_MODE mode) {
		operation.clean(mode);
	}

	@Override
	public void setSchedule(CleaningSchedule cleaningSchedule) {
		operation.setSchedule(cleaningSchedule);
	}

	@Override
	public void clearSchedule() {
		operation.clearSchedule();
	}

	@Override
	public void setDayTime(RRL.DAY day, int hour, int min) {
		operation.setDayTime(day, hour, min);
	}

	@Override
	public void powerOff() {
		operation.powerOff();
	}

	@Override
	public void drive(int velocity, int radius) {
		operation.drive(velocity, radius);
	}

	@Override
	public void driveDirect(int rightVelocity, int leftVelocity) {
		operation.driveDirect(rightVelocity, leftVelocity);
	}

	@Override
	public void drivePWM(int rightWheel, int leftWheel) {
		operation.drivePWM(rightWheel, leftWheel);
	}

	@Override
	public void motors(MotorConfig motorConfig) {
		operation.motors(motorConfig);
	}

	@Override
	public void motorsPWM(int mainBrush, int sideBrush, int vacuum) {
		operation.motorsPWM(mainBrush, sideBrush, vacuum);
	}

	@Override
	public void led(LEDConfig ledConfig) {
		operation.led(ledConfig);
	}

	@Override
	public void schedulingLed(SchedulingLEDConfig config) {
		operation.schedulingLed(config);
	}

	@Override
	public void digitLedRaw(DigitLEDConfig config) {
		operation.digitLedRaw(config);
	}

	@Override
	public void digitLedRaw(int n) {
		operation.digitLedRaw(n);
	}

	@Override
	public void digitLedRaw(String str) {
		operation.digitLedRaw(str);
	}

	@Override
	public void button(BUTTON... buttons) {
		operation.button(buttons);
	}

	@Override
	public void setSong(SONG number, Song song) {
		operation.setSong(number, song);
	}

	@Override
	public void playSong(SONG number) {
		operation.playSong(number);
	}

	@Override
	public void listen(SensorListener listener) {
		operation.listen(listener);
	}

	@Override
	public void pauseStream() {
		operation.pauseStream();
	}

	@Override
	public void resumeStream() {
		operation.resumeStream();
	}

	@Override
	public void stream(StreamListener listener) {
		operation.stream(listener);
	}

}
