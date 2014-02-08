package jp.kamoc.roonroom.lib.operation;

import java.util.ArrayList;
import java.util.List;

/**
 * 楽曲クラス
 * @author kamoc
 *
 */
public class Song {
	/**
	 * 楽曲の最大長
	 */
	public static final int MAX_LENGTH = 16;
	private static final int MAX_PITCH = 127;
	private static final int MIN_PITCH = 31;
	/**
	 * 最大の長さ
	 */
	public static final int MAX_DURATION = 255;
	/**
	 * 最小の長さ
	 */
	public static final int MIN_DURATION = 0;
	/**
	 * 休符として扱うピッチ
	 */
	public static final int NO_SOUND_PITCH = 0;
	private List<Note> list = new ArrayList<Song.Note>();

	/**
	 * 音符
	 * @author kamoc
	 *
	 */
	public class Note {
		/**
		 * 音階
		 */
		public int pitch;
		/**
		 * 長さ(1あたり1/64秒)
		 */
		public int duration;

		/**
		 * コンストラクタ
		 * @param pitch
		 * @param duration
		 */
		public Note(int pitch, int duration) {
			this.pitch = pitch;
			this.duration = duration;
		}
	}

	/**
	 * コンストラクタ
	 */
	public Song() {
	}

	/**
	 * 音符を追加する
	 * @param pitch 音階(31〜107)
	 * @param duration 長さ(1あたり1/64秒)
	 * @return 自身のインスタンス
	 */
	public Song putNote(int pitch, int duration) {
		if (list.size() > MAX_LENGTH) {
			return this;
		}
		int p = pitch;
		int d = duration;
		if (pitch > MAX_PITCH) {
			p = NO_SOUND_PITCH;
		}
		if (pitch < MIN_PITCH) {
			p = NO_SOUND_PITCH;
		}
		if (duration > MAX_DURATION) {
			d = NO_SOUND_PITCH;
		}
		if (duration < MIN_DURATION) {
			d = NO_SOUND_PITCH;
		}
		list.add(new Note(p, d));
		return this;
	}
	
	/**
	 * 楽曲の音符の数を取得する(休符を含む)
	 * @return 長さ
	 */
	public int length(){
		return list.size();
	}
	
	/**
	 * 楽曲の長さを取得する
	 * @return 楽曲の長さ(1あたり1/64秒)
	 */
	public int velocity(){
		int velocity = 0;
		for(Note note : list){
			velocity += note.duration;
		}
		return velocity;
	}
	
	/**
	 * 命令値を取得する
	 * @return 命令値
	 */
	public int[] getSequence(){
		int[] result;
		result = new int[length()*2];
		int i=0;
		for(Note note : list){
			result[i++] = note.pitch;
			result[i++] = note.duration;
		}
		return result;
	}
	
	/**
	 * 音符のリストを取得する
	 * @return 音符のリスト
	 */
	public List<Note> getNotes(){
		return list;
	}
}
