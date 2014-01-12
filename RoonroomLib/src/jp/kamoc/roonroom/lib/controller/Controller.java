package jp.kamoc.roonroom.lib.controller;

import java.util.HashMap;
import java.util.Map;

import jp.kamoc.roonroom.lib.command.CommandSender;
import jp.kamoc.roonroom.lib.command.InputRequestHandler;
import jp.kamoc.roonroom.lib.command.SerialSequence;
import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.constants.RRL.BUTTON;
import jp.kamoc.roonroom.lib.constants.RRL.SONG;
import jp.kamoc.roonroom.lib.listener.PacketListener;
import jp.kamoc.roonroom.lib.listener.StreamListener;
import jp.kamoc.roonroom.lib.listener.sensor.SensorListener;
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

/**
 * コントローラクラス
 * @author kamoc
 *
 */
public class Controller implements Operation {
	private Operation currentOperatingMode;
	private Map<RRL.OPERATIONG_MODE, Operation> modeMap = new HashMap<RRL.OPERATIONG_MODE, Operation>();
	private CommandSender commandSender;
	private PacketListener packetListener;
	private InputRequestHandler inputRequestHandler;
	private SerialAdapter serialAdapter;

	/**
	 * コンストラクタ
	 * @param serialAdapter シリアルアダプタ
	 */
	public Controller(SerialAdapter serialAdapter) {
		this.serialAdapter = serialAdapter;
		try {
			serialAdapter.open();
		} catch (SerialConnectionException e) {
			System.out.println("Connection Open Failed.");
			return;
		}
		commandSender = new CommandSender(serialAdapter);
		packetListener = new PacketListener(serialAdapter);
		inputRequestHandler = new InputRequestHandler(packetListener);

		OperationImpl operation = new OperationImpl(commandSender,
				packetListener);
		modeMap.put(RRL.OPERATIONG_MODE.PASSIVE, new PassiveMode(operation));
		modeMap.put(RRL.OPERATIONG_MODE.SAFE, new SafeMode(operation));
		modeMap.put(RRL.OPERATIONG_MODE.FULL, new FullMode(operation));
		modeMap.put(RRL.OPERATIONG_MODE.OFF, new OffMode(operation));
		currentOperatingMode = modeMap.get(RRL.OPERATIONG_MODE.OFF);
	}

	private void changeCurrentMode(RRL.OPERATIONG_MODE mode) {
		currentOperatingMode = modeMap.get(mode);
	}

	/**
	 * 状態を無視して強制的にシリアルシーケンスを送信する
	 * @deprecated
	 * @param serialSequence
	 */
	public void exec(SerialSequence serialSequence) {
		commandSender.send(serialSequence);
	}

	/**
	 * シリアル通信と、実行中のスレッドを終了する
	 */
	public void finish() {
		serialAdapter.close();
		commandSender.finish();
		packetListener.finish();
	}

	@Override
	public void start() {
		currentOperatingMode.start();
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void changeMode(RRL.OPERATIONG_MODE mode) {
		currentOperatingMode.changeMode(mode);
		changeCurrentMode(mode);
	}

	@Override
	public void clean() {
		currentOperatingMode.clean();
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void clean(RRL.CLEANING_MODE mode) {
		currentOperatingMode.clean(mode);
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void setSchedule(CleaningSchedule cleaningSchedule) {
		currentOperatingMode.setSchedule(cleaningSchedule);
	}

	@Override
	public void clearSchedule() {
		currentOperatingMode.clearSchedule();
	}

	@Override
	public void setDayTime(RRL.DAY day, int hour, int min) {
		currentOperatingMode.setDayTime(day, hour, min);
	}

	@Override
	public void powerOff() {
		currentOperatingMode.powerOff();
		changeCurrentMode(RRL.OPERATIONG_MODE.PASSIVE);
	}

	@Override
	public void drive(int velocity, int radius) {
		currentOperatingMode.drive(velocity, radius);
	}

	@Override
	public void driveDirect(int rightVelocity, int leftVelocity) {
		currentOperatingMode.driveDirect(rightVelocity, leftVelocity);
	}

	@Override
	public void drivePWM(int rightWheel, int leftWheel) {
		currentOperatingMode.drivePWM(rightWheel, leftWheel);
	}

	@Override
	public void motors(MotorConfig motorConfig) {
		currentOperatingMode.motors(motorConfig);
	}

	@Override
	public void motorsPWM(int mainBrush, int sideBrush, int vacuum) {
		currentOperatingMode.motorsPWM(mainBrush, sideBrush, vacuum);
	}

	@Override
	public void led(LEDConfig ledConfig) {
		currentOperatingMode.led(ledConfig);
	}

	@Override
	public void schedulingLed(SchedulingLEDConfig config) {
		currentOperatingMode.schedulingLed(config);
	}

	@Override
	public void digitLedRaw(DigitLEDConfig config) {
		currentOperatingMode.digitLedRaw(config);
	}

	@Override
	public void digitLedRaw(int n) {
		currentOperatingMode.digitLedRaw(n);
	}

	@Override
	public void digitLedRaw(String str) {
		currentOperatingMode.digitLedRaw(str);
	}

	@Override
	public void button(BUTTON... buttons) {
		currentOperatingMode.button(buttons);
	}

	@Override
	public void setSong(SONG number, Song song) {
		currentOperatingMode.setSong(number, song);
	}

	@Override
	public void playSong(SONG number) {
		currentOperatingMode.playSong(number);
	}

	@Override
	public void listen(SensorListener listener) {
		inputRequestHandler.listen(currentOperatingMode, listener);
	}

	@Override
	public void pauseStream() {
		inputRequestHandler.pauseStream(currentOperatingMode);
	}

	@Override
	public void resumeStream() {
		inputRequestHandler.resumeStream(currentOperatingMode);
	}

	@Override
	public void stream(StreamListener listener) {
		inputRequestHandler.stream(currentOperatingMode, listener);
	}

}
