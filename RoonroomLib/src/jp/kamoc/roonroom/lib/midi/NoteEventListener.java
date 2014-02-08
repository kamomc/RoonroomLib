package jp.kamoc.roonroom.lib.midi;

/**
 * 音符再生イベントのリスナ
 * @author kamoc
 *
 */
public interface NoteEventListener {
	/**
	 * 音符演奏開始
	 * @param pitch
	 * @param duration 
	 */
	public abstract void noteOn(int pitch, int duration);
	/**
	 * 音符演奏終了
	 */
	public abstract void noteOff();
}
