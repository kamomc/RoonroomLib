package jp.kamoc.roonroom.lib.midi;

import android.annotation.SuppressLint;
import jp.kamoc.roonroom.lib.controller.Controller;

/**
 * LED経過時間表示
 * 
 * @author kamoc
 * 
 */
public class Timer extends Thread {
	private static final long INTERVAL = 50;
	Controller controller;
	private int time = 0;
	private boolean loop = true;
	private long startAt;
	private int prev = 0;

	/**
	 * コンストラクタ
	 * 
	 * @param controller
	 */
	public Timer(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * タイマー表示を開始する
	 * @param startAt
	 */
	public void start(long startAt) {
		this.startAt = startAt;
		super.start();
	}

	@Override
	public void run() {
		while (loop) {
			long d = System.currentTimeMillis() - startAt;
			int t = (int) (d / 1000);
			if(t != prev){
				int min = t/60;
				int sec = t%60;
				time = min * 100 + sec;
				disp();
				
				prev = t;
			}
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 経過時間をLEDに表示する
	 */
	@SuppressLint("DefaultLocale")
	public void disp() {
		String str = String.format("%1$04d", time);
		controller.digitLedRaw(str);
	}
	
	/**
	 * カウントを終了する
	 */
	public void finish(){
		loop = false;
	}

}
