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
 * 
 * @author kamoc
 * 
 */
public class InputRequestHandler {
	private static final int PACKET_ID_STREAM = 19;
	private BlockingQueue<SensorListener> listenerQueue = new LinkedBlockingQueue<SensorListener>();
	private SensorListener processingListener;
	private Operation operation;
	private List<Byte> processingBytes = new ArrayList<Byte>();

	/**
	 * コンストラクタ
	 * 
	 * @param packetListener
	 *            パケット応答を行うオブジェクト
	 */
	public InputRequestHandler(PacketListener packetListener) {
		packetListener.setInputRequestHandler(this);
	}

	/**
	 * センサー値の取得を要求する
	 * 
	 * @param operation
	 *            オペレーションオブジェクト
	 * @param listener
	 *            取得するセンサー値のリスナ
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
	 * 
	 * @param operation
	 *            オペレーションオブジェクト
	 */
	public void pauseStream(Operation operation) {
		this.operation = operation;
		operation.pauseStream();
	}

	/**
	 * ストリーミングを再開する
	 * 
	 * @param operation
	 *            オペレーションオブジェクト
	 */
	public void resumeStream(Operation operation) {
		this.operation = operation;
		operation.resumeStream();
	}

	/**
	 * ストリーミングを開始する
	 * 
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
	 * 
	 * @param packetSequence
	 *            パケット
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
		if (processingListener.getPacketId() == PACKET_ID_STREAM) {
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
		if (!checksum(bytes)) {
			processingBytes.clear();
			return;
		}
		StreamListener streamListener = (StreamListener) processingListener;
		int offset = 2;
		while (offset < bytes.length - 1) {
			int packetId = bytes[offset++];
			List<SensorListener> listeners = streamListener
					.getListenerList(packetId);
			SensorListener headListener = listeners.get(0);
			int dataBytes = headListener.getDataBytes();
			byte[] byteValue = getValueBytes(bytes, offset, dataBytes);
			int value = convertBytesToInt(byteValue, headListener.isSigned());
			for (SensorListener listener : listeners) {
				listener.onReceive(value);
			}
			offset += dataBytes;
		}
		processingBytes.clear();
		return;
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

	private boolean checksum(byte[] bytes) {
		byte sum = 0;
		for (int i = 1; i < bytes.length; i++) {
			sum += bytes[i];
		}
		if ((sum & 0xFF) == 0) {
			return true;
		}
		return false;
	}

	private byte[] getValueBytes(byte[] src, int offset, int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = src[offset + i];
		}
		return result;
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
