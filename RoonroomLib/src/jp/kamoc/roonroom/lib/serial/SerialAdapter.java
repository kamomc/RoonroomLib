package jp.kamoc.roonroom.lib.serial;

import jp.kamoc.roonroom.lib.listener.PacketListener;

/**
 * シリアルアダプタのインタフェース
 * @author kamoc
 *
 */
public interface SerialAdapter {
	/**
	 * ポーレート
	 */
	static final int BAUD = 115200;
	
	/**
	 * コネクションを開く
	 * @throws SerialConnectionException
	 */
	void open() throws SerialConnectionException;
	/**
	 * コマンドを送信する
	 * @param command
	 * @throws SerialConnectionException
	 */
	void send(int command) throws SerialConnectionException;
	/**
	 * コネクションを閉じる
	 */
	void close();
	/**
	 * パケットを受信する
	 * @throws SerialConnectionException
	 */
	void receive() throws SerialConnectionException;
	/**
	 * パケットリスナを設定する
	 * @param packetListener
	 */
	void setPacketListener(PacketListener packetListener);
}
