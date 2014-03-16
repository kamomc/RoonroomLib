package jp.kamoc.roonroom.lib.midi.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * MIDI
 * @author kamoc
 *
 */
public class Midi {
	/**
	 * Track
	 */
	public List<Track> track = new ArrayList<Track>();
	
	/**
	 * Resolution
	 */
	public int resolution;
	
	
	/**
	 * Length in ticks
	 */
	public long length;
	
	/**
	 * Tempo
	 */
	public List<Tempo> tempo = new ArrayList<Tempo>();
}
