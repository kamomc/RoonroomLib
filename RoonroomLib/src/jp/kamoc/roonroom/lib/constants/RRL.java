package jp.kamoc.roonroom.lib.constants;

public final class RRL {

	public static enum LED_RESERVED {
		BIT_4, BIT_5, BIT_6, BIT_7
	}

	public static enum WEEKDAY_LED_RESERVED {
		BIT_7,
	}

	public static enum SCHEDULING_LED_RESERVED {
		BIT_5, BIT_6, BIT_7,
	}

	public static enum MOTORS_RESERVED {
		BIT_5, BIT_6, BIT_7
	}

	public static enum DIGIT_LED {
		RAW_0, RAW_1, RAW_2, RAW_3
	}

	public static enum DAY {
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
		public int getCode() {
			switch (this) {
			case SUNDAY:
				return 0;
			case MONDAY:
				return 1;
			case TUESDAY:
				return 2;
			case WEDNESDAY:
				return 3;
			case THURSDAY:
				return 4;
			case FRIDAY:
				return 5;
			case SATURDAY:
				return 6;
			default:
				break;
			}
			return 0;
		}
	}

	public static enum DIGIT_LED_BIT {
		A, B, C, D, E, F, G;
		public int getCode() {
			switch (this) {
			case A:
				return 0;
			case B:
				return 1;
			case C:
				return 2;
			case D:
				return 3;
			case E:
				return 4;
			case F:
				return 5;
			case G:
				return 6;
			default:
				break;
			}
			return 0;
		}
	}

	public static enum MAIN_BRUSH_DIRECTION {
		DEFAULT, OPPOSITE
	}

	public static enum SIDE_BRUSH_DIRECTION {
		COUNTERCLOCKWISE, CLOCKWISE
	}

	public static enum OPERATIONG_MODE {
		OFF, PASSIVE, SAFE, FULL;
		public static OPERATIONG_MODE getMode(int val) {
			if (val == 0) {
				return OFF;
			}
			if (val == 1) {
				return PASSIVE;
			}
			if (val == 2) {
				return SAFE;
			}
			if (val == 3) {
				return FULL;
			}
			return OFF;
		}
	}

	public static enum CLEANING_MODE {
		DEFAULT, MAX, SPOT, DOCK,
	}

	public static enum BUTTON {
		CLEAN, SPOT, DOCK, MINUTE, HOUR, DAY, SCHEDULE, CLOCK;
		public int getCode() {
			switch (this) {
			case CLEAN:
				return 0;
			case SPOT:
				return 1;
			case DOCK:
				return 2;
			case MINUTE:
				return 3;
			case HOUR:
				return 4;
			case DAY:
				return 5;
			case SCHEDULE:
				return 6;
			case CLOCK:
				return 7;
			default:
				break;
			}
			return 0;
		}
	}

	public static enum SONG {
		NUMBER_0, NUMBER_1, NUMBER_2, NUMBER_3, NUMBER_4;
		public int getCode() {
			switch (this) {
			case NUMBER_0:
				return 0;
			case NUMBER_1:
				return 1;
			case NUMBER_2:
				return 2;
			case NUMBER_3:
				return 3;
			case NUMBER_4:
				return 4;
			default:
				break;
			}
			return 0;
		}
	}

	public static enum CHARGING_STATE {
		NOT_CHARGING, RECONDITIONING_CHARGING, FULL_CHARGING, TRICKLE_CHARGING, WAITING, CHARGING_FAULT_CONDITION;
		public static CHARGING_STATE getState(int val) {
			if (val == 0) {
				return NOT_CHARGING;
			}
			if (val == 1) {
				return RECONDITIONING_CHARGING;
			}
			if (val == 2) {
				return FULL_CHARGING;
			}
			if (val == 3) {
				return TRICKLE_CHARGING;
			}
			if (val == 4) {
				return WAITING;
			}
			if (val == 5) {
				return CHARGING_FAULT_CONDITION;
			}
			return NOT_CHARGING;
		}
	}
}
