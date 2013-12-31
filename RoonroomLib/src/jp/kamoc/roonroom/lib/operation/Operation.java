package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;

public interface Operation {

	public abstract void start();

	public abstract void changeMode(RRL.OPERATIONG_MODE mode);

	public abstract void clean();

	public abstract void clean(RRL.CLEANING_MODE mode);

	public abstract void setSchedule(CleaningSchedule cleaningSchedule);

	public abstract void clearSchedule();

	public abstract void setDayTime(RRL.DAY day, int hour, int min);

	public abstract void powerOff();

	public abstract void drive(int velocity, int radius);

	public abstract void driveDirect(int rightVelocity, int leftVelocity);

	public abstract void drivePWM(int rightWheel, int leftWheel);

	public abstract void motors(MotorConfig motorConfig);

	public abstract void motorsPWM(int mainBrush, int sideBrush, int vacuum);

	public abstract void led(LEDConfig ledConfig);

	public abstract void schedulingLed(SchedulingLEDConfig config);

	public abstract void digitLedRaw(DigitLEDConfig config);

	public abstract void digitLedRaw(int n);

	public abstract void digitLedRaw(String str);

	public abstract void button(RRL.BUTTON... buttons);

	public abstract void setSong(RRL.SONG number, Song song);

	public abstract void playSong(RRL.SONG number);
}