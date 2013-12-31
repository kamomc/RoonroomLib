package jp.kamoc.roonroom.lib.operation;

import java.util.ArrayList;
import java.util.List;

public class Song {
	private static final int MAX_LENGTH = 16;
	private static final int MAX_PITCH = 127;
	private static final int MIN_PITCH = 31;
	private static final int MAX_DURATION = 255;
	private static final int MIN_DURATION = 0;
	private List<Note> list = new ArrayList<Song.Note>();

	private class Note {
		public int pitch;
		public int duration;

		public Note(int pitch, int duration) {
			this.pitch = pitch;
			this.duration = duration;
		}
	}

	public Song() {
	}

	public Song putNote(int pitch, int duration) {
		if (list.size() > MAX_LENGTH) {
			return this;
		}
		int p = pitch;
		int d = duration;
		if (pitch > MAX_PITCH) {
			p = MAX_PITCH;
		}
		if (pitch < MIN_PITCH) {
			p = MIN_PITCH;
		}
		if (duration > MAX_DURATION) {
			d = MAX_DURATION;
		}
		if (duration < MIN_DURATION) {
			d = MIN_DURATION;
		}
		list.add(new Note(p, d));
		return this;
	}
	
	public int length(){
		return list.size();
	}
	
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
}
