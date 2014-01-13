package jp.kamoc.roonroom.lib.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.kamoc.roonroom.lib.listener.sensor.SensorListener;

/**
 * ストリームリスナ
 * 
 * @author kamoc
 * 
 */
public class StreamListener implements SensorListener {
	private Map<Integer, List<SensorListener>> listenerMap = new HashMap<Integer, List<SensorListener>>();

	/**
	 * コンストラクタ
	 */
	public StreamListener() {
		;
	}

	/**
	 * センサリスナを追加する
	 * 
	 * @param listener
	 *            追加するリスナ
	 */
	public void add(SensorListener listener) {
		List<SensorListener> listenerList = getListenerList(listener
				.getPacketId());
		if (listenerList != null) {
			listenerList.add(listener);
			return;
		}
		listenerList = new ArrayList<SensorListener>();
		listenerList.add(listener);
		listenerMap.put(listener.getPacketId(), listenerList);
	}

	/**
	 * センサリスナを削除する
	 * 
	 * @param listener
	 *            削除するリスナ
	 */
	public void remove(SensorListener listener) {
		List<SensorListener> listenerList = getListenerList(listener
				.getPacketId());
		if (listenerList == null) {
			return;
		}
		listenerList.remove(listener);
		if (listenerList.size() == 0) {
			listenerMap.remove(listener.getPacketId());
		}
	}

	/**
	 * パケットIDを指定して登録されているリスナを取得する
	 * 
	 * @param packetId
	 *            パケットID
	 * @return リスナのリスト
	 */
	public List<SensorListener> getListenerList(int packetId) {
		return listenerMap.get(packetId);
	}

	/**
	 * 登録されているリスナのパケットID数を取得する
	 * 
	 * @return パケットID数
	 */
	public int size() {
		return listenerMap.size();
	}

	/**
	 * パケットIDの配列を取得する
	 * 
	 * @return パケットIDの配列
	 */
	public int[] getPacketIds() {
		int[] ids = new int[size()];
		int i = 0;
		for (Integer packetId : listenerMap.keySet()) {
			ids[i++] = packetId;
		}
		return ids;
	}

	@Override
	public int getDataBytes() {
		int dataBytes = 3; //header, n, checksum
		for (Integer key : listenerMap.keySet()) {
			dataBytes++;
			SensorListener listener = listenerMap.get(key).get(0);
			dataBytes += listener.getDataBytes();
		}
		return dataBytes;
	}

	@Override
	public boolean isSigned() {
		return false;
	}

	@Override
	public int getPacketId() {
		return 19;
	}

	@Override
	public void onReceive(int value) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
