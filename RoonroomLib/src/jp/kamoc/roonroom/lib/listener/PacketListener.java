package jp.kamoc.roonroom.lib.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.kamoc.roonroom.lib.serial.SerialAdapter;

public class PacketListener extends Thread {
	private BlockingQueue<PacketSequence> packetQueue = new LinkedBlockingQueue<PacketSequence>();
	private int interval = 10;
	private static final int MIN_INTERVAL = 10;
	private SensorListener listener;
	private List<Byte> processingBytes = new ArrayList<Byte>();
	private boolean loop = true;

	private enum STATE {
		OFF, STREAMING, LISTENING
	}

	public PacketListener(SerialAdapter serialAdapter) {
		serialAdapter.setPacketListener(this);
		start();
	}

	public void receive(PacketSequence packetSequence) {
		System.out.println(packetSequence);
		packetQueue.add(packetSequence);
	}

	public void setInterval(int interval) {
		if (interval < MIN_INTERVAL) {
			return;
		}
		this.interval = interval;
	}

	@Override
	public void run() {
		while (loop) {
			if (packetQueue.size() != 0) {
				PacketSequence packetSequence = packetQueue.remove();
				receivePacketSequence(packetSequence);
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				;
			}
		}
	}

	private void receivePacketSequence(PacketSequence packetSequence) {
		for (Byte packet : packetSequence) {
			processingBytes.add(packet);
		}
		if (processingBytes.size() < listener.getDataBytes()) {
			return;
		}
		byte[] bytes = new byte[listener.getDataBytes()];
		for (int i = 0; i < listener.getDataBytes(); i++) {
			bytes[i] = processingBytes.remove(0);
		}
		int value = convertBytesToInt(bytes, listener.isSigned());
		listener.onReceive(value);
	}

	public void setListener(SensorListener listener) {
		this.listener = listener;
	}

	private Integer convertBytesToInt(byte[] bytes, boolean isSigned) {
		int result = 0;
		for (int i = 0; i < bytes.length; i++) {
			int b = 0;
			b = b | bytes[i];
			b = b << 24;
			if(i==0 && isSigned){
				b = b >> 24;
			}else{
				b = b >>> 24;
			}
			b = b << (((bytes.length - 1) - i) * 8);
			result = result | b;
		}
		return result;
	}

	public void finish() {
		loop = false;
	}
}
