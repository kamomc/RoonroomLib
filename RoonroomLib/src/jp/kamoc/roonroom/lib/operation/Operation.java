package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.listener.StreamListener;
import jp.kamoc.roonroom.lib.listener.sensor.SensorListener;

public interface Operation {

	abstract void start();

	abstract void changeMode(RRL.OPERATIONG_MODE mode);

	abstract void clean();

	abstract void clean(RRL.CLEANING_MODE mode);

	abstract void setSchedule(CleaningSchedule cleaningSchedule);

	abstract void clearSchedule();

	abstract void setDayTime(RRL.DAY day, int hour, int min);

	abstract void powerOff();

	abstract void drive(int velocity, int radius);

	abstract void driveDirect(int rightVelocity, int leftVelocity);

	abstract void drivePWM(int rightWheel, int leftWheel);

	abstract void motors(MotorConfig motorConfig);

	abstract void motorsPWM(int mainBrush, int sideBrush, int vacuum);

	abstract void led(LEDConfig ledConfig);

	abstract void schedulingLed(SchedulingLEDConfig config);

	abstract void digitLedRaw(DigitLEDConfig config);

	abstract void digitLedRaw(int n);

	abstract void digitLedRaw(String str);

	abstract void button(RRL.BUTTON... buttons);

	abstract void setSong(RRL.SONG number, Song song);

	abstract void playSong(RRL.SONG number);
	
	abstract void listen(SensorListener listener);
	
	abstract void pauseStream();
	
	abstract void resumeStream();
	
	abstract void stream(StreamListener listener);
}