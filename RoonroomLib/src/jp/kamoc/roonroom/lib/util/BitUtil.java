package jp.kamoc.roonroom.lib.util;

/**
 * ビットを取り扱うユーティリティ
 * @author kamoc
 *
 */
public class BitUtil {
	@SuppressWarnings("javadoc")
	public enum BIT {
		BIT_0, BIT_1, BIT_2, BIT_3, BIT_4, BIT_5, BIT_6, BIT_7;
		public int getInt() {
			switch (this) {
			case BIT_0:
				return 0;
			case BIT_1:
				return 1;
			case BIT_2:
				return 2;
			case BIT_3:
				return 3;
			case BIT_4:
				return 4;
			case BIT_5:
				return 5;
			case BIT_6:
				return 6;
			case BIT_7:
				return 7;
			}
			return 0;
		}
	}

	/**
	 * 指定したビットの値が1かどうかを取得する
	 * @param val 判定対象の値
	 * @param bit 位置
	 * @return 1かどうか
	 */
	public static boolean isTrue(int val, BIT bit) {
		int tmp = 0;
		tmp = val << (31-bit.getInt());
		tmp = tmp >>> 31;
		if(tmp == 1){
			return true;
		}
		return false;
	}
}
