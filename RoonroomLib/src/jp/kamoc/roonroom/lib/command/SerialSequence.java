package jp.kamoc.roonroom.lib.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SerialSequence implements Iterable<Integer> {
	private List<Integer> serialSequence = new ArrayList<Integer>();
	private List<SerialSequenceListener> listenerList =
			new ArrayList<SerialSequenceListener>();
	
	public SerialSequence(int... sequence) {
		for(int command : sequence){
			this.serialSequence.add(command);
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return serialSequence.iterator();
	}
	
	public void addSerialSequenceListener(SerialSequenceListener listener){
		listenerList.add(listener);
	}
	
	public void removeSerialSequenceListener(SerialSequenceListener listener){
		listenerList.remove(listener);
	}
	
	public void onSuccess() {
		for(SerialSequenceListener listener : listenerList){
			listener.onSuccess();
		}
	}
	
	public void onFailure(Exception e) {
		for(SerialSequenceListener listener : listenerList){
			listener.onFailure(e);
		}
	}

	@Override
	public String toString() {
		return "SerialSequence" + serialSequence;
	}
}
