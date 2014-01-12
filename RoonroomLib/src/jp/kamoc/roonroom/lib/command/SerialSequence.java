package jp.kamoc.roonroom.lib.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * シリアルシーケンスクラス
 * @author kamoc
 *
 */
public class SerialSequence implements Iterable<Integer> {
	private List<Integer> serialSequence = new ArrayList<Integer>();
	private List<SerialSequenceListener> listenerList =
			new ArrayList<SerialSequenceListener>();
	
	/**
	 * コンストラクタ
	 * @param sequence シリアルシーケンスを構成する数値
	 */
	public SerialSequence(int... sequence) {
		for(int command : sequence){
			this.serialSequence.add(command);
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return serialSequence.iterator();
	}
	
	/**
	 * シリアル通信の成功／失敗時に呼ばれるリスナを追加する
	 * @param listener 追加するリスナ
	 */
	public void addSerialSequenceListener(SerialSequenceListener listener){
		listenerList.add(listener);
	}
	
	/**
	 * シリアル通信の成功／失敗時に呼ばれるリスナを削除する
	 * @param listener 削除するリスナ
	 */
	public void removeSerialSequenceListener(SerialSequenceListener listener){
		listenerList.remove(listener);
	}
	
	/**
	 * シリアル通信が成功した場合に呼ばれる
	 */
	public void onSuccess() {
		for(SerialSequenceListener listener : listenerList){
			listener.onSuccess();
		}
	}
	
	/**
	 * シリアル通信が失敗した場合に呼ばれる
	 * @param e 発生した例外
	 */
	public void onFailure(Exception e) {
		for(SerialSequenceListener listener : listenerList){
			listener.onFailure(e);
		}
	}

	@Override
	public String toString() {
		return "SerialSequence" + serialSequence;
	}
}
