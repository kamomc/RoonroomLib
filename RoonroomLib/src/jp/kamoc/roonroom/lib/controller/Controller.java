package jp.kamoc.roonroom.lib.controller;

import java.util.HashMap;
import java.util.Map;

import jp.kamoc.roonroom.lib.command.CommandSender;
import jp.kamoc.roonroom.lib.command.SerialSequence;
import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.constants.RRL.BUTTON;
import jp.kamoc.roonroom.lib.constants.RRL.SONG;
import jp.kamoc.roonroom.lib.operation.CleaningSchedule;
import jp.kamoc.roonroom.lib.operation.DigitLEDConfig;
import jp.kamoc.roonroom.lib.operation.FullMode;
import jp.kamoc.roonroom.lib.operation.LEDConfig;
import jp.kamoc.roonroom.lib.operation.MotorConfig;
import jp.kamoc.roonroom.lib.operation.OffMode;
import jp.kamoc.roonroom.lib.operation.Operation;
import jp.kamoc.roonroom.lib.operation.OperationImpl;
import jp.kamoc.roonroom.lib.operation.PassiveMode;
import jp.kamoc.roonroom.lib.operation.SafeMode;
import jp.kamoc.roonroom.lib.operation.SchedulingLEDConfig;
import jp.kamoc.roonroom.lib.operation.Song;
import jp.kamoc.roonroom.lib.serial.SerialAdapter;
import jp.kamoc.roonroom.lib.serial.SerialConnectionException;

public class Controller implements Operation {
	private Operation currentMode;
	private Map<RRL.OPERATIONG_MODE, Operation> modeMap = 
			new HashMap<RRL.OPERATIONG_MODE, Operation>();
	private CommandSender commandSender;
	
	
	public Controller(SerialAdapter serialAdapter) {
		try {
			serialAdapter.open();
		} catch (SerialConnectionException e) {
			System.out.println("Connection Open Failed.");
			return;
		}
		commandSender = new CommandSender(serialAdapter);
//		PacketListener packetListener = new PacketListener(serialAdapter);
		
		OperationImpl operation = new OperationImpl(commandSender);
		modeMap.put(RRL.OPERATIONG_MODE.PASSIVE, new PassiveMode(operation));
		modeMap.put(RRL.OPERATIONG_MODE.SAFE, new SafeMode(operation));
		modeMap.put(RRL.OPERATIONG_MODE.FULL, new FullMode(operation));
		currentMode = new OffMode(operation);
	}
	
	private void changeCurrentMode(RRL.OPERATIONG_MODE mode){
		currentMode = modeMap.get(mode);
	}
	
	public void exec(SerialSequence serialSequence) {
		commandSender.send(serialSequence);
	}

	@Override
	public void start() {
		currentMode.start();
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void changeMode(RRL.OPERATIONG_MODE mode) {
		currentMode.changeMode(mode);
		changeCurrentMode(mode);
	}

	@Override
	public void clean() {
		currentMode.clean();
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void clean(RRL.CLEANING_MODE mode) {
		currentMode.clean(mode);
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void setSchedule(CleaningSchedule cleaningSchedule) {
		currentMode.setSchedule(cleaningSchedule);
	}

	@Override
	public void clearSchedule() {
		currentMode.clearSchedule();
	}

	@Override
	public void setDayTime(RRL.DAY day, int hour, int min) {
		currentMode.setDayTime(day, hour, min);
	}

	@Override
	public void powerOff() {
		currentMode.powerOff();
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void drive(int velocity, int radius) {
		currentMode.drive(velocity, radius);
	}

	@Override
	public void driveDirect(int rightVelocity, int leftVelocity) {
		currentMode.driveDirect(rightVelocity, leftVelocity);
	}

	@Override
	public void drivePWM(int rightWheel, int leftWheel) {
		currentMode.drivePWM(rightWheel, leftWheel);
	}

	@Override
	public void motors(MotorConfig motorConfig) {
		currentMode.motors(motorConfig);
	}

	@Override
	public void motorsPWM(int mainBrush, int sideBrush, int vacuum) {
		currentMode.motorsPWM(mainBrush, sideBrush, vacuum);
	}

	@Override
	public void led(LEDConfig ledConfig) {
		currentMode.led(ledConfig);
	}

	@Override
	public void schedulingLed(SchedulingLEDConfig config) {
		currentMode.schedulingLed(config);
	}

	@Override
	public void digitLedRaw(DigitLEDConfig config) {
		currentMode.digitLedRaw(config);
	}

	@Override
	public void digitLedRaw(int n) {
		currentMode.digitLedRaw(n);
	}

	@Override
	public void digitLedRaw(String str) {
		currentMode.digitLedRaw(str);
	}

	@Override
	public void button(BUTTON... buttons) {
		currentMode.button(buttons);
	}

	@Override
	public void setSong(SONG number, Song song) {
		currentMode.setSong(number, song);
	}

	@Override
	public void playSong(SONG number) {
		currentMode.playSong(number);
	}

}
