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
	private StreamListener streamListener;
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
	 * センサー値の取得を要求する(ストリーミング中は無視される)
	 * 
	 * @param operation
	 *            オペレーションオブジェクト
	 * @param listener
	 *            取得するセンサー値のリスナ
	 */
	public void listen(Operation operation, SensorListener listener) {
		if (isStreaming()) {
			return;
		}
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
		if (processingListener == null) {
			return;
		}
		if (processingListener.getPacketId() == PACKET_ID_STREAM) {
			streamListener = (StreamListener) processingListener;
			processingListener = null;
		}
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
		if (streamListener != null) {
			processingListener = streamListener;
		}
	}

	/**
	 * ストリーミングを開始する
	 * 
	 * @param operation
	 * @param listener
	 */
	public void stream(Operation operation, StreamListener listener) {
		this.operation = operation;
		listenerQueue.add(listener);
		if (processingListener == null) {
			send();
		}
	}

	private void send() {
		processingListener = listenerQueue.remove();
		if (processingListener.getPacketId() == PACKET_ID_STREAM) {
			operation.stream((StreamListener) processingListener);
		} else {
			operation.listen(processingListener);
		}
	}

	/**
	 * パケットを受信した時に呼ばれるメソッド
	 * 
	 * @param packetSequence
	 *            パケット
	 */
	public void receivePacketSequence(PacketSequence packetSequence) {
		if (processingListener == null) {
			return;
		}
		for (Byte packet : packetSequence) {
			processingBytes.add(packet);
		}
		if (processingBytes.size() < processingListener.getDataBytes()) {
			return;
		}
		if (processingListener.getPacketId() == PACKET_ID_STREAM) {
			try {
				stream();
			} catch (Exception e) {
				processingBytes.clear();
			}
		} else {
			listen();
		}

	}

	private void stream() throws Exception {
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
		int sum = 0;
		for (int i = 0; i < bytes.length; i++) {
			byte[] b = new byte[1];
			b[0] = bytes[i];
			sum += convertBytesToInt(b, false);
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

	private boolean isStreaming() {
		if (processingListener == null) {
			return false;
		}
		if (processingListener.getPacketId() == PACKET_ID_STREAM) {
			return false;
		}
		return true;
	}

}
