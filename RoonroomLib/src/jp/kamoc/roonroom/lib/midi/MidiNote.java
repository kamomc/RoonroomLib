package jp.kamoc.roonroom.lib.midi;

/**
 * MIDIの音符クラス
 * @author kamoc
 *
 */
public class MidiNote {
	/**
	 * 開始ティック
	 */
	public long startTick;
	/**
	 * 終了ティック
	 */
	public long endTick;
	/**
	 * 音階
	 */
	public int pitch;
	/**
	 * コンストラクタ
	 * @param startTick
	 * @param endTick
	 * @param pitch
	 */
	public MidiNote(long startTick, long endTick, int pitch) {
		this.startTick = startTick;
		this.endTick = endTick;
		this.pitch = pitch;
	}
}
