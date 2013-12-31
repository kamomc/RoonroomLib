package jp.kamoc.roonroom.lib.operation;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import jp.kamoc.roonroom.lib.command.CommandSender;
import jp.kamoc.roonroom.lib.command.SerialSequence;
import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.constants.RRL.SONG;

public class OperationImpl implements Operation {
	CommandSender commandSender;

	private static final int OPCODE_START = 128;
	
	private static final int OPCODE_PASSIVE = 128;
	private static final int OPCODE_SAFE = 131;
	private static final int OPCODE_FULL = 132;

	private static final int OPCODE_CLEAN = 135;
	private static final int OPCODE_MAX = 136;
	private static final int OPCODE_SPOT = 134;
	private static final int OPCODE_DOCK = 143;
	
	private static final int OPCODE_SCHEDULE = 167;

	private static final int OPCODE_POWER = 133;

	private static final int OPCODE_SET_DAY_TIME = 168;
	
	private static final int MAX_VELOCITY = 500;
	private static final int MIN_VELOCITY = -500;
	private static final int MAX_RADIUS = 2000;
	private static final int MIN_RADIUS = -2000;

	private static final int OPCODE_DRIVE = 137;

	private static final int OPCODE_DRIVE_DIRECT = 145;

	private static final int MAX_WHEEL_PWM = 255;

	private static final int MIN_WHEEL_PWM = -255;

	private static final int OPCODE_DRIVE_PWM = 146;

	private static final int OPCODE_MOTORS = 138;

	private static final int MAX_BRUSH_PWM = 127;

	private static final int MIN_BRUSH_PWM = -127;

	private static final int MAX_VACUUM_PWM = 127;

	private static final int MIN_VACUUM_PWM = 0;

	private static final int OPCODE_PWM_MOTORS = 144;

	private static final int OPCODE_LEDS = 139;

	private static final int OPCODE_SCHEDULING_LEDS = 162;

	private static final int OPCODE_DIGIT_LEDS_RAW = 163;

	private static final String CHARSET = "US-ASCII";

	private static final int OPCODE_DIGIT_LEDS_ASCII = 164;

	private static final int OPCODE_BUTTONS = 165;

	private static final int OPCODE_SONG = 140;

	private static final int OPCODE_PLAY = 141;

	public OperationImpl(CommandSender commandSender) {
		this.commandSender = commandSender;
	}
	

	private int adjustRadius(int radius) {
		if (radius > MAX_RADIUS) {
			return MAX_RADIUS;
		}
		if (radius < MIN_RADIUS) {
			return MIN_RADIUS;
		}
		return radius;
	}

	private int adjustVelocity(int velocity) {
		if (velocity > MAX_VELOCITY) {
			return MAX_VELOCITY;
		}
		if (velocity < MIN_VELOCITY) {
			return MIN_VELOCITY;
		}
		return velocity;
	}
	
	private int adjustWheelPWM(int pwm) {
		if (pwm > MAX_WHEEL_PWM) {
			return MAX_WHEEL_PWM;
		}
		if (pwm < MIN_WHEEL_PWM) {
			return MIN_WHEEL_PWM;
		}
		return pwm;
	}
	
	private int adjustBrushPWM(int pwm){
		if(pwm > MAX_BRUSH_PWM){
			return MAX_BRUSH_PWM;
		}
		if(pwm < MIN_BRUSH_PWM){
			return MIN_BRUSH_PWM;
		}
		return pwm;
	}
	
	private int adjustVacuumPWM(int pwm){
		if(pwm > MAX_VACUUM_PWM){
			return MAX_VACUUM_PWM;
		}
		if(pwm < MIN_VACUUM_PWM){
			return MIN_VACUUM_PWM;
		}
		return pwm;
	}
	
	private int[] convertTwoSequence(int val) {
		try {
			String hex = String.format("%032x", val);
			String s1 = hex.substring(28, 30);
			String s2 = hex.substring(30, 32);
			int i1 = Integer.decode("0x" + s1);
			int i2 = Integer.decode("0x" + s2);
			return new int[] { i1, i2 };
		} catch (Exception e) {
			return new int[] { 0, 0 };
		}
	}
	
	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#start()
	 */
	@Override
	public void start(){
		commandSender.send(new SerialSequence(OPCODE_START));
	}

	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#changeMode(jp.kamoc.roonroom.lib.controller.InstructionSet.OperatingMode)
	 */
	@Override
	public void changeMode(RRL.OPERATIONG_MODE mode) {
		switch (mode) {
		case PASSIVE:
			commandSender.send(new SerialSequence(OPCODE_PASSIVE));
			break;
		case SAFE:
			commandSender.send(new SerialSequence(OPCODE_SAFE));
			break;
		case FULL:
			commandSender.send(new SerialSequence(OPCODE_FULL));
			break;
		default:
			break;
		}
	}

	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#clean()
	 */
	@Override
	public void clean() {
		clean(RRL.CLEANING_MODE.DEFAULT);
	}

	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#clean(jp.kamoc.roonroom.lib.controller.InstructionSet.CleaningMode)
	 */
	@Override
	public void clean(RRL.CLEANING_MODE mode) {
		switch (mode) {
		case DEFAULT:
			commandSender.send(new SerialSequence(OPCODE_CLEAN));
			break;
		case MAX:
			commandSender.send(new SerialSequence(OPCODE_MAX));
			break;
		case SPOT:
			commandSender.send(new SerialSequence(OPCODE_SPOT));
			break;
		case DOCK:
			commandSender.send(new SerialSequence(OPCODE_DOCK));
			break;

		default:
			break;
		}
	}

	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#setSchedule(jp.kamoc.roonroom.lib.controller.CleaningSchedule)
	 */
	@Override
	public void setSchedule(CleaningSchedule cleaningSchedule) {
		Map<RRL.DAY, Time> schedule = cleaningSchedule.getSchedule();
		commandSender.send(new SerialSequence(
				OPCODE_SCHEDULE,
				cleaningSchedule.getDays(),
				schedule.get(RRL.DAY.SUNDAY).h,
				schedule.get(RRL.DAY.SUNDAY).m,
				schedule.get(RRL.DAY.MONDAY).h,
				schedule.get(RRL.DAY.MONDAY).m,
				schedule.get(RRL.DAY.TUESDAY).h,
				schedule.get(RRL.DAY.TUESDAY).m,
				schedule.get(RRL.DAY.WEDNESDAY).h,
				schedule.get(RRL.DAY.WEDNESDAY).m,
				schedule.get(RRL.DAY.TUESDAY).h,
				schedule.get(RRL.DAY.TUESDAY).m,
				schedule.get(RRL.DAY.FRIDAY).h,
				schedule.get(RRL.DAY.FRIDAY).m,
				schedule.get(RRL.DAY.SATURDAY).h,
				schedule.get(RRL.DAY.SATURDAY).m
				));
	}
	
	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#clearSchedule()
	 */
	@Override
	public void clearSchedule() {
		commandSender.send(new SerialSequence(
				OPCODE_SCHEDULE, 0, 
				0, 0, 
				0, 0, 
				0, 0, 
				0, 0, 
				0, 0, 
				0, 0
				));
	}
	
	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#setDayTime(jp.kamoc.roonroom.lib.controller.Day, int, int)
	 */
	@Override
	public void setDayTime(RRL.DAY day, int hour, int min){
		Time time = new Time(hour, min);
		commandSender.send(new SerialSequence(
				OPCODE_SET_DAY_TIME, 
				day.getCode(), time.h, time.m));
	}

	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#powerOff()
	 */
	@Override
	public void powerOff() {
		commandSender.send(new SerialSequence(OPCODE_POWER));
	}
	
	/* (非 Javadoc)
	 * @see jp.kamoc.roonroom.lib.controller.Operation#drive(int, int)
	 */
	@Override
	public void drive(int velocity, int radius){
		int v = adjustVelocity(velocity);
		int r = adjustRadius(radius);
		int[] v2 = convertTwoSequence(v);
		int[] r2 = convertTwoSequence(r);
		commandSender.send(new SerialSequence(
				OPCODE_DRIVE, 
				v2[0], v2[1], 
				r2[0], r2[1]));
	}

	@Override
	public void driveDirect(int rightVelocity, int leftVelocity) {
		int r = adjustVelocity(rightVelocity);
		int l = adjustVelocity(leftVelocity);
		int[] r2 = convertTwoSequence(r);
		int[] l2 = convertTwoSequence(l);
		commandSender.send(new SerialSequence(
				OPCODE_DRIVE_DIRECT,
				r2[0], r2[1],
				l2[0], l2[1]));
	}


	@Override
	public void drivePWM(int rightWheel, int leftWheel) {
		int r = adjustWheelPWM(rightWheel);
		int l = adjustWheelPWM(leftWheel);
		int[] r2 = convertTwoSequence(r);
		int[] l2 = convertTwoSequence(l);
		commandSender.send(new SerialSequence(
				OPCODE_DRIVE_PWM,
				r2[0], r2[1],
				l2[0], l2[1]));
	}

	@Override
	public void motors(MotorConfig motorConfig) {
		commandSender.send(new SerialSequence(
				OPCODE_MOTORS, 
				motorConfig.getValue()));
	}


	@Override
	public void motorsPWM(int mainBrush, int sideBrush, int vacuum) {
		commandSender.send(new SerialSequence(
				OPCODE_PWM_MOTORS,
				adjustBrushPWM(mainBrush),
				adjustBrushPWM(sideBrush),
				adjustVacuumPWM(vacuum)));
	}


	@Override
	public void led(LEDConfig ledConfig) {
		commandSender.send(new SerialSequence(
				OPCODE_LEDS,
				ledConfig.getValue(),
				ledConfig.getColor(),
				ledConfig.getIntensity()));
	}


	@Override
	public void schedulingLed(SchedulingLEDConfig config) {
		commandSender.send(new SerialSequence(
				OPCODE_SCHEDULING_LEDS,
				config.getWeekdayValue(),
				config.getSchedulingValue()));
	}


	@Override
	public void digitLedRaw(DigitLEDConfig config) {
		commandSender.send(new SerialSequence(
				OPCODE_DIGIT_LEDS_RAW,
				config.getValue(RRL.DIGIT_LED.RAW_3),
				config.getValue(RRL.DIGIT_LED.RAW_2),
				config.getValue(RRL.DIGIT_LED.RAW_1),
				config.getValue(RRL.DIGIT_LED.RAW_0)));
	}


	@Override
	public void digitLedRaw(int n) {
		int[] r = new int[]{ 10, 10, 10, 10 };
		if (n >= 0) {
			r[0] = n % 10;
		}
		if (n >= 10) {
			r[1] = (n / 10) % 10;
		}
		if (n >= 100) {
			r[2] = (n / 100) % 10;
		}
		if (n >= 1000) {
			r[3] = (n / 1000) % 10;
		}
		commandSender.send(new SerialSequence(
				OPCODE_DIGIT_LEDS_RAW,
				r[3], r[2], r[1], r[0]));
	}


	@Override
	public void digitLedRaw(String str) {
		byte[] asciiCodes;
		try {
			asciiCodes = str.getBytes(CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		int[] r = new int[] { 32, 32, 32, 32 };
		if (asciiCodes.length > 0) {
			r[0] = asciiCodes[0];
		}
		if (asciiCodes.length > 1) {
			r[1] = asciiCodes[1];
		}
		if (asciiCodes.length > 2) {
			r[2] = asciiCodes[2];
		}
		if (asciiCodes.length > 3) {
			r[3] = asciiCodes[3];
		}
		commandSender.send(new SerialSequence(
				OPCODE_DIGIT_LEDS_ASCII,
				r[0], r[1], r[2], r[3]));
	}


	@Override
	public void button(RRL.BUTTON... buttons) {
		int val = 0;
		for (RRL.BUTTON button : RRL.BUTTON.values()) {
			if (Arrays.asList(buttons).contains(button)) {
				val = val | (0 << button.getCode());
			}
		}
		commandSender.send(new SerialSequence(OPCODE_BUTTONS, val));
	}


	@Override
	public void setSong(SONG number, Song song) {
		commandSender.send(new SerialSequence(OPCODE_SONG, number.getCode(), song.length()));
		commandSender.send(new SerialSequence(song.getSequence()));
	}


	@Override
	public void playSong(SONG number) {
		commandSender.send(new SerialSequence(OPCODE_PLAY, number.getCode()));
	}
}
