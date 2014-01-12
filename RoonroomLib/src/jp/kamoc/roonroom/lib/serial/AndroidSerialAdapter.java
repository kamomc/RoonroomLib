package jp.kamoc.roonroom.lib.serial;

import java.io.IOException;

import jp.kamoc.roonroom.lib.listener.PacketListener;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;

/**
 * Android用シリアルアダプタ
 * @author kamoc
 *
 */
public class AndroidSerialAdapter implements SerialAdapter {
	private Activity activity;
	private UsbSerialDriver usbSerialDriver;
	
	/**
	 * コンストラクタ
	 * @param activity
	 */
	public AndroidSerialAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void open() throws SerialConnectionException {
		try {
			UsbManager usbManager = 
					(UsbManager) activity.getSystemService(Context.USB_SERVICE);
			usbSerialDriver = UsbSerialProber.acquire(usbManager);
			usbSerialDriver.open();
			usbSerialDriver.setBaudRate(BAUD);
		} catch (Exception e) {
			throw new SerialConnectionException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void send(int command) throws SerialConnectionException {
		byte[] b = new byte[] { (byte) (command & 0xFF) };
		try {
			usbSerialDriver.write(b, b.length);
		} catch (IOException e) {
			throw new SerialConnectionException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void close() {
		try {
			usbSerialDriver.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receive() throws SerialConnectionException {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void setPacketListener(PacketListener packetListener) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
