package jp.kamoc.roonroom.lib.midi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.arnx.jsonic.JSON;
import jp.kamoc.roonroom.lib.midi.meta.Midi;
import jp.kamoc.roonroom.lib.midi.meta.Note;
import jp.kamoc.roonroom.lib.midi.meta.Track;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;

/**
 * MIDIをJSONに変換するクラス
 * 
 * @author kamoc
 */
public class MidiFileUtil {
	static final int IGNORE_REST = 0;

	/**
	 * コンストラクタ
	 */
	private MidiFileUtil() {
	}

	/**
	 * MIDIファイルをJSON文字列に変換する
	 * 
	 * @param midiFile
	 *            MIDIファイル
	 * 
	 * @param readable
	 *            可読性が高い形式で出力するか
	 * @return JSON文字列
	 */
	public static String convert2Json(MidiFile midiFile, boolean readable) {
		Midi midi = convert2Midi(midiFile);
		String json = JSON.encode(midi, readable);
		return json;
	}

	/**
	 * MIDIファイルをMIDIオブジェクトにコンバートする
	 * @param midiFile
	 * @return MIDIオブジェクト
	 */
	public static Midi convert2Midi(MidiFile midiFile) {
		Midi midi = new Midi();
		midi.resolution = midiFile.getResolution();
		midi.length = midiFile.getLengthInTicks();

		List<MidiTrack> tracks = midiFile.getTracks();
		for (MidiTrack track : tracks) {
			convertTrack(midi, track);
		}
		return midi;
	}

	private static void convertTrack(Midi midi, MidiTrack track) {
		Track convTrack = new Track();
		List<Note> processingNotes = new ArrayList<Note>();

		Set<MidiEvent> events = track.getEvents();
		for (MidiEvent event : events) {
			if (event instanceof Tempo) {
				Tempo tempo = (Tempo) event;
				jp.kamoc.roonroom.lib.midi.meta.Tempo convTempo = new jp.kamoc.roonroom.lib.midi.meta.Tempo();
				convTempo.bpm = tempo.getBpm();
				convTempo.mpqn = tempo.getMpqn();
				convTempo.from = tempo.getTick();
				midi.tempo.add(convTempo);
				continue;
			}
			if (event instanceof NoteOn) {
				NoteOn noteOn = (NoteOn) event;
				Note pNote = popProcessingNote(processingNotes, noteOn);
				if (pNote == null) {
					Note note = new Note();
					note.on = noteOn.getTick();
					note.off = 0;
					note.pitch = noteOn.getNoteValue();
					processingNotes.add(note);
				} else {
					convTrack.note.add(pNote);
				}
				continue;
			}
			if (event instanceof NoteOff) {
				NoteOff noteOff = (NoteOff) event;
				Note pNote = popProcessingNote(processingNotes, noteOff);
				if (pNote == null) {
					;
				} else {
					convTrack.note.add(pNote);
				}
				continue;
			}
		}
		if (convTrack.note.size() == 0) {
			return;
		}
		List<Track> separatedTracks = separateTrack(convTrack);
		for (Track separatedTrack : separatedTracks) {
			midi.track.add(separatedTrack);
		}
	}

	/**
	 * 次に処理対象とする音符を取り出す
	 * 
	 * @param processingNotes
	 * @param noteOn
	 * @return
	 */
	private static Note popProcessingNote(List<Note> processingNotes, NoteOn noteOn) {
		long tick = noteOn.getTick();
		int pitch = noteOn.getNoteValue();
		int velocity = noteOn.getVelocity();
		for (Note note : processingNotes) {
			if (note.pitch != pitch || velocity != 0) {
				continue;
			}
			note.off = tick;
			processingNotes.remove(note);
			return note;
		}
		return null;
	}

	/**
	 * 次に処理対象とする音符を取り出す
	 * 
	 * @param processingNotes
	 * @param noteOff
	 * @return
	 */
	private static Note popProcessingNote(List<Note> processingNotes, NoteOff noteOff) {
		long tick = noteOff.getTick();
		int pitch = noteOff.getNoteValue();
		int velocity = noteOff.getVelocity();
		for (Note note : processingNotes) {
			if (note.pitch != pitch || velocity != 0) {
				continue;
			}
			note.off = tick;
			processingNotes.remove(note);
			return note;
		}
		return null;
	}

	private static List<Track> separateTrack(Track track) {
		List<Track> trackList = new ArrayList<Track>();
		while (track.note.size() != 0) {
			Track newTrack = new Track();
			Note note = null;
			Note currentNote = track.note.remove(0);
			newTrack.note.add(currentNote);
			while ((note = getNextNote(track.note, currentNote)) != null) {
				currentNote = note;
				newTrack.note.add(note);
			}
			trackList.add(newTrack);
		}

		return trackList;
	}

	private static Note getNextNote(List<Note> noteList, Note currentNote) {
		for (Note note : noteList) {
			if ((note.on - currentNote.off) < 0) {
				continue;
			}
			noteList.remove(note);
			return note;
		}
		return null;
	}
}
