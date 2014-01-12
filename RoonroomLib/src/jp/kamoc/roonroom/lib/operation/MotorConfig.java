package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;

/**
 * モーターの設定クラス
 * @author kamoc
 *
 */
public class MotorConfig {
	private boolean mainBrush;
	private boolean sideBrush;
	private boolean vacuum;
	private RRL.MAIN_BRUSH_DIRECTION mainBrushDirection = RRL.MAIN_BRUSH_DIRECTION.DEFAULT;
	private RRL.SIDE_BRUSH_DIRECTION sideBrushDirection = RRL.SIDE_BRUSH_DIRECTION.CLOCKWISE;
	private boolean[] reserved = new boolean[] { false, false, false };

	/**
	 * コンストラクタ
	 */
	public MotorConfig() {
	}

	/**
	 * メインブラシの駆動状態を設定する
	 * @param mainBrush
	 */
	public void setMainBrush(boolean mainBrush) {
		this.mainBrush = mainBrush;
	}

	/**
	 * サイドブラシの駆動状態を設定する
	 * @param sideBrush
	 */
	public void setSideBrush(boolean sideBrush) {
		this.sideBrush = sideBrush;
	}

	/**
	 * 吸引状態を設定する
	 * @param vacuum
	 */
	public void setVacuum(boolean vacuum) {
		this.vacuum = vacuum;
	}

	/**
	 * メインブラシの回転方向を設定する
	 * @param mainBrushDirection
	 */
	public void setMainBrushDirection(RRL.MAIN_BRUSH_DIRECTION mainBrushDirection) {
		this.mainBrushDirection = mainBrushDirection;
	}

	/**
	 * サイドブラシの回転方向を設定する
	 * @param sideBrushDirection
	 */
	public void setSideBrushDirection(RRL.SIDE_BRUSH_DIRECTION sideBrushDirection) {
		this.sideBrushDirection = sideBrushDirection;
	}

	/**
	 * 未割り当てビットを設定する
	 * @param bit
	 * @param state
	 */
	public void setReserverd(RRL.MOTORS_RESERVED bit, boolean state) {
		switch (bit) {
		case BIT_5:
			reserved[0] = state;
			break;
		case BIT_6:
			reserved[1] = state;
			break;
		case BIT_7:
			reserved[2] = state;
			break;
		default:
			break;
		}
	}

	/**
	 * 命令値を取得する
	 * @return 命令値
	 */
	public int getValue() {
		int result = 0;
		if (sideBrush) {
			result = result | 1;
		}
		if (vacuum) {
			result = result | (1 << 1);
		}
		if (mainBrush) {
			result = result | (1 << 2);
		}
		if (sideBrushDirection.equals(RRL.SIDE_BRUSH_DIRECTION.CLOCKWISE)) {
			result = result | (1 << 3);
		}
		if (mainBrushDirection.equals(RRL.MAIN_BRUSH_DIRECTION.OPPOSITE)) {
			result = result | (1 << 4);
		}
		if (reserved[0]) {
			result = result | (1 << 5);
		}
		if (reserved[1]) {
			result = result | (1 << 6);
		}
		if (reserved[2]) {
			result = result | (1 << 7);
		}
		return result;
	}
}
