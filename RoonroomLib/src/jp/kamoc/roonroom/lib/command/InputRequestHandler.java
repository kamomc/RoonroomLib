package jp.kamoc.roonroom.lib.command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.kamoc.roonroom.lib.listener.PacketListener;
import jp.kamoc.roonroom.lib.listener.PacketSequence;
import jp.kamoc.roonroom.lib.listener.StreamListener;
import jp.kamoc.roonroom.lib.listener.sensor.SensorListener;
import jp.kamoc.roonroom.lib.operation.Operation;

/**
 * センサー値取得要求と応答を扱うクラス
 * @author kamoc
 *
 */
public class InputRequestHandler {
	private BlockingQueue<SensorListener> listenerQueue = new LinkedBlockingQueue<SensorListener>();
	private SensorListener processingListener;
	private Operation operation;
	private List<Byte> processingBytes = new ArrayList<Byte>();

	/**
	 * コンストラクタ
	 * @param packetListener パケット応答を行うオブジェクト
	 */
	public InputRequestHandler(PacketListener packetListener) {
		packetListener.setInputRequestHandler(this);
	}

	/**
	 * センサー値の取得を要求する
	 * @param operation オペレーションオブジェクト
	 * @param listener 取得するセンサー値のリスナ
	 */
	public void listen(Operation operation, SensorListener listener) {
		this.operation = operation;
		listenerQueue.add(listener);
		if (processingListener == null) {
			send();
		}
	}

	/**
	 * ストリーミングを停止する
	 * @param operation オペレーションオブジェクト
	 */
	public void pauseStream(Operation operation) {
		this.operation = operation;
		operation.pauseStream();
	}

	/**
	 * ストリーミングを再開する
	 * @param operation オペレーションオブジェクト
	 */
	public void resumeStream(Operation operation) {
		this.operation = operation;
		operation.resumeStream();
	}

	/**
	 * ストリーミングを開始する
	 * @param operation 
	 * @param listener
	 */
	public void stream(Operation operation, StreamListener listener) {
		this.operation = operation;
		operation.stream(listener);
	}

	private void send() {
		processingListener = listenerQueue.remove();
		operation.listen(processingListener);
	}

	/**
	 * パケットを受信した時に呼ばれるメソッド
	 * @param packetSequence パケット
	 */
	public void receivePacketSequence(PacketSequence packetSequence) {
		for (Byte packet : packetSequence) {
			processingBytes.add(packet);
		}
		if (processingListener == null) {
			return;
		}
		if (processingBytes.size() < processingListener.getDataBytes()) {
			return;
		}
		if (processingListener instanceof StreamListener) {
			stream();
		} else {
			listen();
		}

	}

	private void stream() {
		byte[] bytes = new byte[processingListener.getDataBytes()];
		for (int i = 0; i < processingListener.getDataBytes(); i++) {
			bytes[i] = processingBytes.remove(0);
		}
		for (int i = 0; i < bytes[1]; i++) {
			//TODO:ここから実装
		}
	}

	private void listen() {
		byte[] bytes = new byte[processingListener.getDataBytes()];
		for (int i = 0; i < processingListener.getDataBytes(); i++) {
			bytes[i] = processingBytes.remove(0);
		}
		int value = convertBytesToInt(bytes, processingListener.isSigned());
		processingListener.onReceive(value);

		processingListener = null;
		if (listenerQueue.size() != 0) {
			send();
		}
	}

	private Integer convertBytesToInt(byte[] bytes, boolean isSigned) {
		int result = 0;
		for (int i = 0; i < bytes.length; i++) {
			int b = 0;
			b = b | bytes[i];
			b = b << 24;
			if (i == 0 && isSigned) {
				b = b >> 24;
			} else {
				b = b >>> 24;
			}
			b = b << (((bytes.length - 1) - i) * 8);
			result = result | b;
		}
		return result;
	}

}
