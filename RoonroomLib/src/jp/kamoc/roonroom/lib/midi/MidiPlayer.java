package jp.kamoc.roonroom.lib.midi;

import java.util.ArrayList;
import java.util.List;

import jp.kamoc.roonroom.lib.constants.RRL;
import jp.kamoc.roonroom.lib.controller.Controller;
import jp.kamoc.roonroom.lib.midi.meta.Midi;
import jp.kamoc.roonroom.lib.operation.Song;
import jp.kamoc.roonroom.lib.operation.Song.Note;

/**
 * MIDI再生クラス ルンバは和音を演奏できないため、単音のトラックに自動分割される。
 * 
 * @author kamoc
 * 
 */
public class MidiPlayer {

	private static final int ROOMBA_TICK = 64;
	private Controller controller;
	private RRL.SONG playing;
	private List<SerialSong> songList = new ArrayList<SerialSong>();
	private List<List<MidiNote>> separatedTracks;
	private List<NoteEventListener> listeners = new ArrayList<NoteEventListener>();
	private long startAt;
	private int playingNo;
	private boolean pause;
	private long INTERVAL = 5;
	private Song playing1;
	private Song playing0;
	private boolean mute;
	private boolean interrupt = true;

	/**
	 * コンストラクタ
	 * 
	 * @param controller
	 */
	public MidiPlayer(final Controller controller) {
		this.controller = controller;
	}

	/**
	 * 音符再生イベントリスナを追加する
	 * 
	 * @param listener
	 */
	public void addNoteEventListener(NoteEventListener listener) {
		listeners.add(listener);
	}

	/**
	 * 音符再生イベントリスナを削除する
	 * 
	 * @param listener
	 */
	public void removeNoteEventListener(NoteEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * 指定したトラックを再生する
	 * @param midi MIDIオブジェクト
	 * @param trackNo 再生するトラックナンバー
	 * 
	 */
	public void play(Midi midi, int trackNo) {
		startAt = System.currentTimeMillis();
		songList = MidiUtil.convert2SerialSong(midi, trackNo);
//		System.out.println("シリアルソングを生成しました。 Track: " + trackNo);
		playingNo = -1;
		playing = RRL.SONG.NUMBER_0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				SerialSong nextSong = songList.get(0);
//				System.out.println("スレッドを開始しました。Next Start At: " + nextSong.getStartAt());
				setSong(controller, nextSong);
				while (!pause) {
					if (nextSong.getStartAt() <= System.currentTimeMillis()
							- startAt) {
						if (playingNo == songList.size() - 1) {
							// 最後の音符演奏中は何もしない
							;
						} else if (playingNo == songList.size() - 2) {
							// 最後の手前では演奏開始のみ行う
							playSong(controller);
							playingNo++;
//							System.out.println("最後の演奏を開始します。 At: " + (System.currentTimeMillis() - startAt) + ", #" + playingNo);
							break;
						} else {
							// それ以外では演奏開始と次のロードを行う
							playSong(controller);
							playingNo++;
							nextSong = songList.get(playingNo + 1);
//							System.out.println("次の演奏を開始します。 At: " + (System.currentTimeMillis() - startAt) + ", #" + playingNo);
//							System.out.println("Next Start At: " + nextSong.getStartAt());
							setSong(controller, nextSong);
						}
					}
					try {
						Thread.sleep(INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * トラックの数を取得する
	 * 
	 * @return トラックの数
	 */
	public int trackSize() {
		return separatedTracks.size();
	}

	private void playSong(final Controller controller) {
		noteOff();
		if (interrupt) {
			controller.changeMode(RRL.OPERATIONG_MODE.PASSIVE);
			controller.changeMode(RRL.OPERATIONG_MODE.SAFE);
		}
		final Song playingSong;
		if (playing.equals(RRL.SONG.NUMBER_0)) {
			if (!mute) {
				controller.playSong(RRL.SONG.NUMBER_1);
			}
			playing = RRL.SONG.NUMBER_1;
			playingSong = playing1;
		} else {
			if (!mute) {
				controller.playSong(RRL.SONG.NUMBER_0);
			}
			playing = RRL.SONG.NUMBER_0;
			playingSong = playing0;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Note> notes = playingSong.getNotes();
				for (Note note : notes) {
					if (note.pitch != Song.NO_SOUND_PITCH) {
						noteOn(note);
					}
					try {
						Thread.sleep(note.duration * 1000 / ROOMBA_TICK);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (note.pitch != Song.NO_SOUND_PITCH) {
						noteOff();
					}
				}
			}
		}).start();
	}

	private void setSong(final Controller controller, Song song) {
		if (playing.equals(RRL.SONG.NUMBER_0)) {
			controller.setSong(RRL.SONG.NUMBER_1, song);
			playing1 = song;
		} else {
			controller.setSong(RRL.SONG.NUMBER_0, song);
			playing0 = song;
		}
	}

	/**
	 * ノートオンのリスナを呼び出す
	 * 
	 * @param note
	 *            再生する音符
	 */
	private void noteOn(Note note) {
		for (NoteEventListener listener : listeners) {
			listener.noteOn(note.pitch, note.duration);
		}
	}

	/**
	 * ノートオフのリスナを呼び出す
	 */
	private void noteOff() {
		for (NoteEventListener listener : listeners) {
			listener.noteOff();
		}
	}

	/**
	 * ミュートのON/OFFを設定する
	 * 
	 * @param mute
	 *            ON:true, OFF:false
	 */
	public void mute(boolean mute) {
		this.mute = mute;
	}

	/**
	 * 割り込み演奏のON/OFFを設定する
	 * 
	 * @param interrupt
	 *            true:割り込みON, false:割り込みOFF
	 */
	public void setInterrupt(boolean interrupt) {
		this.interrupt = interrupt;
	}

}
