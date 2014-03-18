package jp.kamoc.roonroom.lib.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.kamoc.roonroom.lib.command.SerialSequence;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Bluetoothのクライアント側クラス
 * 
 * @author kamoc
 * 
 */
public class AndroidBluetoothReceiver {

	private static final String ERR_MSG_UNSUPPORTED = "Bluetoothがサポートされていません。";
	private static final String ERR_MSG_OFF = "BluetoothがOFFになっています。";
	private static final String ERR_MSG_NOT_FOUND = "指定した名前のBluetoothデバイスが見つからないか、接続したことがありません。";
	private static final String ERR_MSG_CREATE_SCOKET = "ソケットの作成に失敗しました。";

	private BluetoothSocket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private boolean pause;
	private BlockingQueue<Byte> queue = new LinkedBlockingQueue<Byte>();
	private byte id;
	private Controller controller;

	/**
	 * コンストラクタ
	 * 
	 * @param deviceName
	 *            デバイスの名前
	 * @param uuid
	 *            UUID文字列
	 * @param id
	 *            ルンバ命令の識別子
	 */
	public AndroidBluetoothReceiver(String deviceName, String uuid, byte id) {
		this.id = id;
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			throw new IllegalStateException(ERR_MSG_UNSUPPORTED);
		}
		if (!adapter.isEnabled()) {
			throw new IllegalStateException(ERR_MSG_OFF);
		}

		Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
		for (BluetoothDevice device : bondedDevices) {
			if (!device.getName().equals(deviceName)) {
				continue;
			}
			// 接続を開始する
			try {
				openSocket(device, uuid);
			} catch (IOException e) {
				throw new RuntimeException(ERR_MSG_CREATE_SCOKET);
			}
			return;
		}
		// 対象が見つからなかった場合は例外を投げる
		throw new IllegalStateException(ERR_MSG_NOT_FOUND);
	}

	/**
	 * ソケットを開く
	 * @param device
	 * @param uuid
	 * @throws IOException
	 */
	private void openSocket(BluetoothDevice device, String uuid)
			throws IOException {
		socket = device
				.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					socket.connect();
				} catch (IOException e) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
				}
				try {
					inputStream = socket.getInputStream();
					outputStream = socket.getOutputStream();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				sendQueue();
				while (!pause) {
					byte[] buf = new byte[512];
					try {
						int len = inputStream.read(buf);
						if (len == -1) {
							break;
						}
						for (int i = 0; i < len; i++) {
							queue.add(buf[i]);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	/**
	 * キューにたまったパケットをルンバに送る
	 */
	private void sendQueue() {
		new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				int len = -1;
				List<Integer> processing = new ArrayList<Integer>();
				while (!pause) {
					if (queue.size() == 0) {
						continue;
					}
					Byte b = queue.remove();
					int val = 0;
					val = val | b;
					val = val << 24;
					val = val >>> 24;
					if (processing.size() == 0) {
						if (id == b) {
							processing.add(val);
						}
						continue;
					}
					if (processing.size() == 1) {
						processing.add(val);
						len = val;
						continue;
					}
					processing.add(val);
					if (processing.size() != len + 2) {
						continue;
					}
					processing.remove(0);
					processing.remove(0);
					int[] sequence = new int[processing.size()];
					for (int i = 0; i < sequence.length; i++) {
						sequence[i] = processing.get(i);
					}
					System.out.println(sequence);
					controller.exec(new SerialSequence(sequence));
					processing.clear();

				}
			}
		}).start();
	}

	/**
	 * ループをやめる
	 * 
	 * @param pause
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/**
	 * クライアントからサーバへデータを送る
	 * 
	 * @param buf
	 * @throws IOException
	 */
	public void send(byte[] buf) throws IOException {
		outputStream.write(buf);
	}

	/**
	 * コントローラをセットする
	 * 
	 * @param controller
	 *            コントローラクラス
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

}
