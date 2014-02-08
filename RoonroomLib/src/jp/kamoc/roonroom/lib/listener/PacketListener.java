package jp.kamoc.roonroom.lib.listener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.kamoc.roonroom.lib.command.InputRequestHandler;
import jp.kamoc.roonroom.lib.serial.SerialAdapter;

/**
 * パケットリスナ
 * 
 * @author kamoc
 * 
 */
public class PacketListener extends Thread {
	private BlockingQueue<PacketSequence> packetQueue = new LinkedBlockingQueue<PacketSequence>();
	private int interval = 10;
	private static final int MIN_INTERVAL = 10;
	private boolean loop = true;
	private InputRequestHandler inputRequestHandler;

	/**
	 * コンストラクタ
	 * 
	 * @param serialAdapter
	 *            シリアルアダプタ
	 */
	public PacketListener(SerialAdapter serialAdapter) {
		serialAdapter.setPacketListener(this);
		start();
	}

	/**
	 * インスタンスをセットする
	 * 
	 * @param inputRequestHandler
	 */
	public void setInputRequestHandler(InputRequestHandler inputRequestHandler) {
		this.inputRequestHandler = inputRequestHandler;
	}

	/**
	 * パケットを受信した時に呼ばれる
	 * 
	 * @param packetSequence
	 */
	public void receive(PacketSequence packetSequence) {
		// System.out.println(packetSequence);
		packetQueue.add(packetSequence);
	}

	/**
	 * 応答確認周期を設定する
	 * 
	 * @param interval
	 *            応答確認周期(ミリ秒)
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
			if (packetQueue.size() != 0 && inputRequestHandler != null) {
				PacketSequence packetSequence = packetQueue.remove();
				inputRequestHandler.receivePacketSequence(packetSequence);
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				;
			}
		}
	}

	/**
	 * スレッドを終了する
	 */
	public void finish() {
		loop = false;
	}
}
