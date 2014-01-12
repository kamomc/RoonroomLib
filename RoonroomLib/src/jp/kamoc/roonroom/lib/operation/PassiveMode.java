package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.constants.RRL.BUTTON;
import jp.kamoc.roonroom.lib.constants.RRL.SONG;
import jp.kamoc.roonroom.lib.listener.StreamListener;
import jp.kamoc.roonroom.lib.listener.sensor.SensorListener;

public class PassiveMode implements Operation {
	private OperationImpl operation;
	
	public PassiveMode(OperationImpl operation) {
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
		throw new IllegalStateException();
	}

	@Override
	public void driveDirect(int rightVelocity, int leftVelocity) {
		throw new IllegalStateException();
	}

	@Override
	public void drivePWM(int rightWheel, int leftWheel) {
		throw new IllegalStateException();
	}

	@Override
	public void motors(MotorConfig motorConfig) {
		throw new IllegalStateException();
	}

	@Override
	public void motorsPWM(int mainBrush, int sideBrush, int vacuum) {
		throw new IllegalStateException();
	}

	@Override
	public void led(LEDConfig ledConfig) {
		throw new IllegalStateException();
	}

	@Override
	public void schedulingLed(SchedulingLEDConfig config) {
		throw new IllegalStateException();
	}

	@Override
	public void digitLedRaw(DigitLEDConfig config) {
		throw new IllegalStateException();
	}

	@Override
	public void digitLedRaw(int n) {
		throw new IllegalStateException();
	}

	@Override
	public void digitLedRaw(String str) {
		throw new IllegalStateException();
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
		throw new IllegalStateException();
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
