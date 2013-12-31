package jp.kamoc.roonroom.lib.command;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jp.kamoc.roonroom.lib.serial.SerialAdapter;
import jp.kamoc.roonroom.lib.serial.SerialConnectionException;

public class CommandSender extends Thread {
	private BlockingQueue<SerialSequence> commandQueue = new LinkedBlockingQueue<SerialSequence>();
	private SerialAdapter serialAdapter;
	private int interval = 30;
	private final int MIN_INTERVAL = 15;
	private final int DEFAULT_TIMEOUT = 1000;
	private int timeout = DEFAULT_TIMEOUT;
	private long sendStartTime = 0;

	public CommandSender(SerialAdapter serialAdapter) {
		this.serialAdapter = serialAdapter;
		start();
	}

	public void send(SerialSequence serialSequence) {
		commandQueue.add(serialSequence);
	}

	public void setInterval(int interval) {
		if (interval < MIN_INTERVAL) {
			return;
		}
		this.interval = interval;
	}

	@Override
	public void run() {
		while (true) {
			if (commandQueue.size() != 0) {
				SerialSequence serialSequence = commandQueue.remove();
				if(sendSerialSequence(serialSequence)){
					serialSequence.onSuccess();
				}else{
					serialSequence.onFailure(new CommandSendTimeoutException());
				}
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				;
			}
		}
	}

	private boolean sendSerialSequence(SerialSequence serialSequence) {
		System.out.println(serialSequence);
		sendStartTime = System.currentTimeMillis();
		for (Integer command : serialSequence) {
			if (sendCommand(command)) {
				continue;
			}
			return false;
		}
		return true;
	}

	private boolean sendCommand(Integer command) {
		try {
			serialAdapter.send(command);
		} catch (SerialConnectionException e) {
			if (System.currentTimeMillis() - sendStartTime > timeout) {
				return false;
			}
			return sendCommand(command);
		}
		return true;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
