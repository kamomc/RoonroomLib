package jp.kamoc.roonroom.lib.command;

public interface SerialSequenceListener {
	abstract void onSuccess();
	abstract void onFailure(Exception e);
}
