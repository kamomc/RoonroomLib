package jp.kamoc.roonroom.lib.midi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;

import com.leff.midi.MidiFile;

import jp.kamoc.roonroom.lib.midi.meta.Midi;
import jp.kamoc.roonroom.lib.midi.meta.Note;
import jp.kamoc.roonroom.lib.midi.meta.Tempo;
import jp.kamoc.roonroom.lib.midi.meta.Track;
import jp.kamoc.roonroom.lib.operation.Song;

/**
 * MIDIオブジェクトを操作するユーティリティクラス
 * @author kamoc
 *
 */
public class MidiUtil {
	static final int ROOMBA_TICK = 64;

	/**
	 * Midiデータをルンバの命令の形式に変換する
	 * 
	 * @param midi
	 * @param trackNo
	 * @return ルンバが演奏可能なSerialSongのリスト
	 */
	public static List<SerialSong> convert2SerialSong(Midi midi, int trackNo) {
		int resolution = midi.resolution;

		List<SerialSong> resultSongs = new ArrayList<SerialSong>();
		Track track = midi.track.get(trackNo);
		List<Note> notes = track.note;
		Note headNote = notes.get(0);
		Tempo tempo = MidiUtil.getTempo(midi, headNote);
		SerialSong song = new SerialSong(MidiUtil.getHeadMs(headNote.on, midi));
		Note prevNote = new Note();
		prevNote.on = 0;
		prevNote.off = 0;
		prevNote.pitch = Song.NO_SOUND_PITCH;
		// 誤差値
		long error = 0;
		for (Note note : notes) {
			tempo = MidiUtil.getTempo(midi, note);

			// 最大の音符数に達したら新たなSongを作る
			if (MidiUtil.judgeSeparation(song)) {
				resultSongs.add(song);
				song = new SerialSong(MidiUtil.getHeadMs(note.on, midi));
			}

			// 前の音符との間隔となる休符を作成する
			Note restNote = new Note();
			restNote.on = prevNote.off;
			restNote.off = note.on;
			restNote.pitch = Song.NO_SOUND_PITCH;
			Tempo restTempo = MidiUtil.getTempo(midi, restNote);
			int restDuration = MidiUtil.getRoombaDuration(restNote, restTempo,
					resolution);

			// 休符の長さが0でない場合
			if (restDuration != Song.MIN_DURATION) {
				if (song.length() != 0) {

					if (restDuration > MidiFileUtil.IGNORE_REST) {
						// 休符が長い場合は新たなSongを作る
						resultSongs.add(song);
						song = new SerialSong(MidiUtil.getHeadMs(note.on, midi));
					} else {
						// 短い場合は手前の音符を延ばして休符を無視
						List<Song.Note> noteList = song.getNotes();
						noteList.remove(noteList.size() - 1);
						prevNote.off = note.on;
						Tempo prevTempo = MidiUtil.getTempo(midi, prevNote);
						int d = MidiUtil.getRoombaDuration(prevNote, prevTempo,
								resolution);
						song.putNote(prevNote.pitch, d);
					}
				}
			}

			int duration = MidiUtil.getRoombaDuration(note, tempo, resolution);
			long tick = MidiUtil.getTick(duration, tempo, resolution);
			error += ((note.off - note.on) - tick);

			// 累積誤差が1tickを超えたら補正する
			if (error >= MidiUtil.getTick(1, tempo, resolution)) {
				error -= MidiUtil.getTick(1, tempo, resolution);
				duration++;
			}

			prevNote = note;
			song.putNote(note.pitch, duration);
		}

		resultSongs.add(song);
		return resultSongs;
	}

	static boolean judgeSeparation(SerialSong song) {
		if (song.length() == Song.MAX_LENGTH) {
			return true;
		}
		return false;
	}

	static Tempo getTempo(Midi midi, Note note) {
		Tempo candidate = null;
		for (Tempo tempo : midi.tempo) {
			if (tempo.from <= note.on) {
				candidate = tempo;
			} else {
				return candidate;
			}
		}
		return candidate;
	}

	/**
	 * 音符のTickからルンバの再生時間(1あたり1/64秒)を計算する
	 * 
	 * @param restNote
	 *            音符
	 * @return ルンバの再生時間
	 */
	static int getRoombaDuration(Note restNote, Tempo tempo, int resolution) {
		int duration = (int) ((restNote.off - restNote.on) * (60 * MidiUtil.ROOMBA_TICK / (resolution * tempo.bpm)));
		if (duration > 255) {
			return 255;
		}
		return duration;
	}

	/**
	 * ルンバの再生時間(1あたり1/64秒)からTick数を計算する
	 * 
	 * @param roombaDuration
	 * @return
	 */
	static long getTick(int roombaDuration, Tempo tempo, int resolution) {
		return (long) ((roombaDuration * resolution * tempo.bpm) / (60 * MidiUtil.ROOMBA_TICK));
	}

	/**
	 * Tick数からミリ秒を計算する
	 * 
	 * @param tick
	 * @return
	 */
	static long getMs(long tick, Tempo tempo, int resolution) {
		return (long) ((tick * 60 * 1000) / (resolution * tempo.bpm));
	}

	static long getHeadMs(long tick, Midi midi) {
		long result = 0;
		int resolution = midi.resolution;
		for (int i = 0; i < midi.tempo.size(); i++) {
			Tempo tempo = midi.tempo.get(i);
			// 最後のテンポ区間
			if (i == midi.tempo.size() - 1) {
				result += getMs(tick - tempo.from, tempo, resolution);
				return result;
			}
			// 最初〜最後の１つ手前のテンポ区間
			// 通過済みテンポ区間の場合
			Tempo nextTempo = midi.tempo.get(i + 1);
			if (nextTempo.from < tick) {
				result += getMs(nextTempo.from - tempo.from, tempo, resolution);
				continue;
			} else {
				result += getMs(tick - tempo.from, tempo, resolution);
				return result;
			}
		}
		return result;
	}

	/**
	 * JSON化したMIDIファイルを取得する
	 * 
	 * @param path
	 *            MIDIファイルのパス
	 * @param readable
	 *            可読性が高い形式で出力するか
	 * @return JSON文字列
	 * @throws IOException 
	 */
	public static String getJSON(String path, boolean readable)
			throws IOException {
		MidiFile midiFile = load(path);
		return MidiFileUtil.convert2Json(midiFile, false);
	}

	/**
	 * MIDIオブジェクトを取得する
	 * 
	 * @param path
	 *            MIDIファイルのパス
	 * @return MIDIオブジェクト
	 * @throws IOException 
	 */
	public static Midi getMidi(String path) throws IOException {
		String json = getJSON(path, false);
		Midi midi = JSON.decode(json, Midi.class);
		return midi;
	}

	/**
	 * MIDIファイルを読み込む
	 * @param path MIDIファイルのパス
	 * @return MIDIファイル
	 * @throws IOException
	 */
	static MidiFile load(String path) throws IOException {
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		MidiFile midiFile = new MidiFile(is);
		return midiFile;
	}

}
