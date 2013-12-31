package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.constants.RRL.BUTTON;
import jp.kamoc.roonroom.lib.constants.RRL.SONG;

public class OffMode implements Operation {
	private OperationImpl operation;

	public OffMode(OperationImpl operation) {
		this.operation = operation;
	}

	@Override
	public void start() {
		operation.start();
	}

	@Override
	public void changeMode(RRL.OPERATIONG_MODE mode) {
		throw new IllegalStateException();
	}

	@Override
	public void clean() {
		throw new IllegalStateException();
	}

	@Override
	public void clean(RRL.CLEANING_MODE mode) {
		throw new IllegalStateException();
	}

	@Override
	public void setSchedule(CleaningSchedule cleaningSchedule) {
		throw new IllegalStateException();
	}

	@Override
	public void clearSchedule() {
		throw new IllegalStateException();
	}

	@Override
	public void setDayTime(RRL.DAY day, int hour, int min) {
		throw new IllegalStateException();
	}

	@Override
	public void powerOff() {
		throw new IllegalStateException();
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
		throw new IllegalStateException();
	}

	@Override
	public void setSong(SONG number, Song song) {
		throw new IllegalStateException();
	}

	@Override
	public void playSong(SONG number) {
		throw new IllegalStateException();
	}

}
