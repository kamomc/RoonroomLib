package jp.kamoc.roonroom.lib.listener.stream;

import jp.kamoc.roonroom.lib.listener.sensor.SongPlayListener;

/**
 * Song Playing (for streaming)
 * @author kamoc
 *
 */
public abstract class SongPlayStreamListener extends SongPlayListener {
	boolean prevState = false;

	@Override
	public void onReceive(boolean play) {
		if(play == prevState){
			return;
		}
		prevState = play;
		if(play){
			onStart();
		}else{
			onFinish();
		}
	}

	/**
	 * 現在演奏中の楽曲の演奏が終了した
	 */
	public abstract void onFinish();

	/**
	 * 楽曲の演奏を開始した
	 */
	public abstract void onStart();
	

}
