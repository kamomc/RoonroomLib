package jp.kamoc.roonroom.lib.command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.kamoc.roonroom.lib.serial.SerialAdapter;
import jp.kamoc.roonroom.lib.serial.SerialConnectionException;

/**
 * コマンド送信クラス
 * 
 * @author kamoc
 * 
 */
public class CommandSender extends Thread {
	private BlockingQueue<SerialSequence> commandQueue = new LinkedBlockingQueue<SerialSequence>();
	private SerialAdapter serialAdapter;
	private int interval = 30;
	private final int MIN_INTERVAL = 15;
	private final int DEFAULT_TIMEOUT = 1000;
	private int timeout = DEFAULT_TIMEOUT;
	private long sendStartTime = 0;
	private boolean loop = true;
	private Byte headerCode;

	/**
	 * コンストラクタ
	 * 
	 * @param serialAdapter
	 *            利用機器に応じたSerialAdapterのインスタンス
	 */
	public CommandSender(SerialAdapter serialAdapter) {
		this.serialAdapter = serialAdapter;
		start();
	}

	/**
	 * シリアルシーケンスをルンバに送信する
	 * 
	 * @param serialSequence
	 *            送信するシリアルシーケンス
	 */
	public void send(SerialSequence serialSequence) {
		commandQueue.add(serialSequence);
	}

	/**
	 * シリアルシーケンスをルンバに送信する間隔を設定する
	 * 
	 * @param interval
	 *            送信間隔(ミリ秒)
	 */
	public void setInterval(int interval) {
		if (interval < MIN_INTERVAL) {
			return;
		}
		this.interval = interval;
	}

	@Override
	public void run() {
		while (loop) {
			if (commandQueue.size() != 0) {
				SerialSequence serialSequence = commandQueue.remove();
				if (sendSerialSequence(serialSequence)) {
					serialSequence.onSuccess();
				} else {
					serialSequence.onFailure(new CommandSendTimeoutException());
				}
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				;
			}
		}
	}

	private boolean sendSerialSequence(SerialSequence serialSequence) {
		System.out.println(serialSequence);
		sendStartTime = System.currentTimeMillis();
		if (headerCode != null) {
			sendCommand((int) headerCode);
			sendCommand(serialSequence.size());
		}
		for (Integer command : serialSequence) {
			if (sendCommand(command)) {
				continue;
			}
			return false;
		}
		return true;
	}

	private boolean sendCommand(Integer command) {
		try {
			serialAdapter.send(command);
		} catch (SerialConnectionException e) {
			if (System.currentTimeMillis() - sendStartTime > timeout) {
				return false;
			}
			return sendCommand(command);
		}
		return true;
	}

	/**
	 * シリアルシーケンスの送信におけるタイムアウト時間
	 * 
	 * @param timeout
	 *            タイムアウト時間(ミリ秒)
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * コマンド送信スレッドを終了する
	 */
	public void finish() {
		loop = false;
	}

	/**
	 * ヘッダコードを取得する
	 * 
	 * @return ヘッダコード
	 */
	public Byte getHeaderCode() {
		return headerCode;
	}

	/**
	 * ヘッダコードを設定する(ルンバに直接接続する場合はnullを設定する)
	 * 
	 * @param headerCode
	 *            ヘッダコード
	 */
	public void setHeaderCode(Byte headerCode) {
		this.headerCode = headerCode;
	}

}
