package jp.kamoc.roonroom.lib.midi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.kamoc.roonroom.lib.operation.Song;
import jp.kamoc.roonroom.lib.operation.Song.Note;

import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;

/**
 * MIDIファイルをルンバで再生可能な形式に変換する
 * 
 * @author kamoc
 * 
 */
public class MidiConverter {
	private static final int ROOMBA_TICK = 64;
	private int resolution;
	private float tempo;
	private List<List<MidiNote>> separatedTracks;

	/**
	 * コンストラクタ
	 * 
	 * @param resolution
	 */
	public MidiConverter(int resolution) {
		this.resolution = resolution;
	}

	/**
	 * 変換を実行する
	 * 
	 * @param tracks
	 *            MIDIのトラック
	 */
	public void convert(List<MidiTrack> tracks) {
		List<MidiNote> notes = convertTracks(tracks);
		separatedTracks = separateTracks(notes);
		for (List<MidiNote> noteList : separatedTracks) {
			System.out.println(noteList.size());
		}
	}

	/**
	 * ルンバで再生可能なSong型のリストを取得する
	 * 
	 * @param trackNum
	 *            変換したトラックのナンバー
	 * @return 変換したトラック
	 */
	public List<SerialSong> getSongList(int trackNum) {
		return convertToSongs(separatedTracks.get(trackNum));
	}

	/**
	 * MIDIのトラックを音符のリストに変換する
	 * 
	 * @param tracks
	 *            トラックのリスト
	 * @return 音符のリスト
	 */
	private List<MidiNote> convertTracks(List<MidiTrack> tracks) {
		List<MidiNote> resultNotes = new ArrayList<MidiNote>();
		List<MidiNote> processingNotes = new ArrayList<MidiNote>();
		for (MidiTrack track : tracks) {
			Set<MidiEvent> events = track.getEvents();
			for (MidiEvent event : events) {
				if (event instanceof Tempo) {
					Tempo tempo = (Tempo) event;
					this.tempo = tempo.getBpm();
					System.out.println("MPQN: " + tempo.getMpqn());
					System.out.println("BPM: " + tempo.getBpm());
					continue;
				}
				if (event instanceof NoteOn) {
					NoteOn noteOn = (NoteOn) event;
					MidiNote pNote = popProcessingNote(processingNotes, noteOn);
					if (pNote == null) {
						processingNotes.add(new MidiNote(noteOn.getTick(), 0,
								noteOn.getNoteValue()));
					} else {
						resultNotes.add(pNote);
					}
					continue;
				}
				if (event instanceof NoteOff) {
					NoteOff noteOff = (NoteOff) event;
					MidiNote pNote = popProcessingNote(processingNotes, noteOff);
					if (pNote == null) {
						;
					} else {
						resultNotes.add(pNote);
					}
				}
			}
		}
		return resultNotes;
	}

	/**
	 * 次に処理対象とする音符を取り出す
	 * 
	 * @param processingNotes
	 * @param noteOn
	 * @return
	 */
	private MidiNote popProcessingNote(List<MidiNote> processingNotes,
			NoteOn noteOn) {
		long tick = noteOn.getTick();
		int pitch = noteOn.getNoteValue();
		int velocity = noteOn.getVelocity();
		for (MidiNote note : processingNotes) {
			if (note.pitch != pitch || velocity != 0) {
				continue;
			}
			note.endTick = tick;
			processingNotes.remove(note);
			return note;
		}
		return null;
	}

	/**
	 * 次に処理対象とする音符を取り出す
	 * 
	 * @param processingNotes
	 *            処理中の音符
	 * @param noteOff
	 *            休符
	 * @return
	 */
	private MidiNote popProcessingNote(List<MidiNote> processingNotes,
			NoteOff noteOff) {
		long tick = noteOff.getTick();
		int pitch = noteOff.getNoteValue();
		int velocity = noteOff.getVelocity();
		for (MidiNote note : processingNotes) {
			if (note.pitch != pitch || velocity != 0) {
				continue;
			}
			note.endTick = tick;
			processingNotes.remove(note);
			return note;
		}
		return null;
	}

	/**
	 * 音符のリストを和音を避けたトラックのリストに変換する
	 * 
	 * @param notes
	 *            音符のリスト
	 * @return 和音を避けたトラックのリスト
	 */
	private List<List<MidiNote>> separateTracks(List<MidiNote> notes) {
		List<List<MidiNote>> resultList = new ArrayList<List<MidiNote>>();

		while (notes.size() != 0) {
			List<MidiNote> track = new ArrayList<MidiNote>();
			MidiNote note = null;
			MidiNote currentNote = notes.remove(0);
			track.add(currentNote);
			while ((note = getNextNote(notes, currentNote)) != null) {
				currentNote = note;
				track.add(note);
			}
			resultList.add(track);
		}

		return resultList;
	}

	/**
	 * 次の音符を抽出する
	 * 
	 * @param notes
	 *            音符のリスト
	 * @param currentNote
	 *            現在の音符
	 * @return 次の音符
	 */
	private MidiNote getNextNote(List<MidiNote> notes, MidiNote currentNote) {
		for (MidiNote note : notes) {
			if ((note.startTick - currentNote.endTick) < 0) {
				continue;
			}
			notes.remove(note);
			return note;
		}
		return null;
	}

	/**
	 * 音符のリストをルンバで再生可能なSong型のリストに変換する
	 * 
	 * @param notes
	 *            音符のリスト
	 * @return Songのリスト
	 */
	private List<SerialSong> convertToSongs(List<MidiNote> notes) {
		List<SerialSong> resultSongs = new ArrayList<SerialSong>();
		MidiNote headNote = notes.get(0);
		SerialSong song = new SerialSong(getMs(headNote.startTick));
		MidiNote prevNote = new MidiNote(0, 0, Song.NO_SOUND_PITCH);
		long error = 0;
		for (MidiNote note : notes) {
			// 最大の音符数に達したら新たなSongを作る
			if (judgeSeparation(song, note.startTick)) {
				resultSongs.add(song);
				song = new SerialSong(getMs(note.startTick));
			}

			MidiNote restNote = new MidiNote(prevNote.endTick, note.startTick,
					Song.NO_SOUND_PITCH);
			int restDuration = getRoombaDuration(restNote);
			long restTick = getTick(restDuration);
			error += (restNote.endTick - restNote.startTick) - restTick;

			// 休符追加が必要な場合は追加する
			// long restIntervalTick = 0;
			if (restDuration != Song.MIN_DURATION) {
				if (song.length() != 0) {
					resultSongs.add(song);
					song = new SerialSong(getMs(note.startTick));
				}
			}
			// while (restDuration != Song.MIN_DURATION) {
			// // 累積誤差が1tickを超えたら補正する
			// if (error >= getTick(1)) {
			// error -= getTick(1);
			// restDuration++;
			// }
			// if (restDuration > Song.MAX_DURATION) {
			// song.putNote(Song.NO_SOUND_PITCH, Song.MAX_DURATION);
			// restDuration -= Song.MAX_DURATION;
			// restIntervalTick += getTick(Song.MAX_DURATION);
			// } else {
			// song.putNote(Song.NO_SOUND_PITCH, restDuration);
			// restDuration -= restDuration;
			// restIntervalTick += getTick(restDuration);
			// }
			// // 最大の音符数に達したら新たなSongを作る
			// if (judgeSeparation(song)) {
			// resultSongs.add(song);
			// long endMs = getMs(restNote.startTick + restIntervalTick);
			// song = new SerialSong(endMs);
			// }
			// }

			int duration = getRoombaDuration(note);
			long tick = getTick(duration);
			error += ((note.endTick - note.startTick) - tick);

			// 累積誤差が1tickを超えたら補正する
			if (error >= getTick(1)) {
				error -= getTick(1);
				duration++;
			}

			prevNote = note;
			song.putNote(note.pitch, duration);
		}
		resultSongs.add(song);
		return resultSongs;
	}

	private boolean judgeSeparation(SerialSong song, long endTick) {
		if (song.length() == Song.MAX_LENGTH) {
			return true;
		}
		// if (endTick % (resolution * 4) == 0 && song.length() > 8) {
		// return true;
		// }
		return false;
	}

	private boolean judgeSeparation(SerialSong song) {
		if (song.length() == Song.MAX_LENGTH) {
			return true;
		}
		return false;
	}

	/**
	 * 音符のTickからルンバの再生時間(1あたり1/64秒)を計算する
	 * 
	 * @param note
	 *            音符
	 * @return ルンバの再生時間
	 */
	private int getRoombaDuration(MidiNote note) {
		return (int) ((note.endTick - note.startTick) * (60 * ROOMBA_TICK / (resolution * tempo)));
	}

	/**
	 * ルンバの再生時間(1あたり1/64秒)からTick数を計算する
	 * 
	 * @param roombaDuration
	 * @return
	 */
	private long getTick(int roombaDuration) {
		return (long) ((roombaDuration * resolution * tempo) / (60 * ROOMBA_TICK));
	}

	/**
	 * Tick数からミリ秒を計算する
	 * 
	 * @param tick
	 * @return
	 */
	private long getMs(long tick) {
		return (long) ((tick * 60 * 1000) / (resolution * tempo));
	}
}
