package jp.kamoc.roonroom.lib.midi;

import jp.kamoc.roonroom.lib.operation.Song;

/**
 * 連続再生用の楽曲クラス
 * 
 * @author kamoc
 * 
 */
public class SerialSong extends Song {
	private long startAt;

	/**
	 * コンストラクタ
	 * 
	 * @param startAt
	 *            演奏開始ミリ秒
	 */
	public SerialSong(long startAt) {
		this.startAt = startAt;
	}

	/**
	 * 楽曲の演奏開始ミリ秒を取得する
	 * 
	 * @return 演奏開始ミリ秒
	 */
	public long getStartAt() {
		return startAt;
	}

	/**
	 * 楽曲の演奏開始ミリ秒を設定する
	 * 
	 * @param startAt
	 *            演奏開始ミリ秒
	 */
	public void setStartAt(long startAt) {
		this.startAt = startAt;
	}
}
