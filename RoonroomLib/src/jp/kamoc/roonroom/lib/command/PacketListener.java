package jp.kamoc.roonroom.lib.command;

import jp.kamoc.roonroom.lib.serial.SerialAdapter;
import jp.kamoc.roonroom.lib.serial.SerialConnectionException;

public class PacketListener extends Thread {
	private SerialAdapter serialAdapter;
	private int interval = 1000;
	
	public PacketListener(SerialAdapter serialAdapter) {
		this.serialAdapter = serialAdapter;
		start();
	}
	
	@Override
	public void run() {
		System.out.println("run");
		while(true){
			try {
				int receive = serialAdapter.receive();
				System.out.println("ReceivedVal="+receive);
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
				;
			} catch (SerialConnectionException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				System.out.println("error");
			}
		}
	}
}
