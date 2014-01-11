package jp.kamoc.roonroom.lib.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.kamoc.roonroom.lib.listener.PacketListener;
import jp.kamoc.roonroom.lib.listener.PacketSequence;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class StandardSerialAdapter implements SerialAdapter {
	private String deviceName;
	private String appName;
	private int timeout;

	private SerialPort port;

	private OutputStream outputStream;
	private InputStream inputStream;
	private PacketListener packetListener;

	public StandardSerialAdapter(String deviceName, String appName, int timeout) {
		this.deviceName = deviceName;
		this.appName = appName;
		this.timeout = timeout;
	}

	@Override
	public void open() throws SerialConnectionException {
		try {
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(deviceName);
			port = (SerialPort) portId.open(appName, timeout);
			port.setSerialPortParams(BAUD, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			outputStream = port.getOutputStream();

			port.addEventListener(new SerialPortEventListener() {
				@Override
				public void serialEvent(SerialPortEvent event) {
					if (event.getEventType() != SerialPortEvent.DATA_AVAILABLE) {
						return;
					}
					try {
						receive();
					} catch (SerialConnectionException e) {
						e.printStackTrace();
					}
				}
			});
			port.notifyOnDataAvailable(true);
			inputStream = port.getInputStream();
		} catch (Exception e) {
			throw new SerialConnectionException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void send(int command) throws SerialConnectionException {
		try {
			outputStream.write(command);
		} catch (IOException e) {
			throw new SerialConnectionException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void receive() throws SerialConnectionException {
		if (packetListener == null) {
			return;
		}
		try {
			int bytes = inputStream.available();
			byte[] buf = new byte[bytes];
			inputStream.read(buf);
			packetListener.receive(new PacketSequence(buf));
		} catch (IOException e) {
			throw new SerialConnectionException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void close() {
		port.close();
	}

	@Override
	public void setPacketListener(PacketListener packetListener) {
		this.packetListener = packetListener;
	}
}
