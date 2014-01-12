package jp.kamoc.roonroom.lib.command;

/**
 * シリアル通信が成功／失敗した場合に呼ばれるリスナのインタフェース
 * @author kamoc
 *
 */
public interface SerialSequenceListener {
	/**
	 * シリアル通信が成功した場合に呼ばれる
	 */
	abstract void onSuccess();
	/**
	 * シリアル通信が失敗した場合に呼ばれる
	 * @param e 発生した例外
	 */
	abstract void onFailure(Exception e);
}
