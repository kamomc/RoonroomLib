package jp.kamoc.roonroom.lib.operation;

import jp.kamoc.roonroom.lib.constants.RRL;

public class LEDConfig {
	private static final int MAX_COLOR = 255;
	private static final int MIN_COLOR = 0;
	private static final int MAX_INTENSITY = 255;
	private static final int MIN_INTENSITY = 0;
	private boolean checkRobot;
	private boolean dock;
	private boolean spot;
	private boolean debris;
	private boolean[] reserved;
	private int color;
	private int intensity;
	
	public LEDConfig() {
		reserved = new boolean[]{ false, false, false, false };
	}
	
	private void adjust(){
		if(color > MAX_COLOR){
			color = MAX_COLOR;
		}
		if(color < MIN_COLOR){
			color = MIN_COLOR;
		}
		if(intensity > MAX_INTENSITY){
			intensity = MAX_INTENSITY;
		}
		if(intensity < MIN_INTENSITY){
			intensity = MIN_INTENSITY;
		}
	}
	
	public void setCheckRobot(boolean checkRobot) {
		this.checkRobot = checkRobot;
	}

	public void setDock(boolean dock) {
		this.dock = dock;
	}

	public void setSpot(boolean spot) {
		this.spot = spot;
	}

	public void setDebris(boolean debris) {
		this.debris = debris;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public void setReserved(RRL.LED_RESERVED bit, boolean state){
		switch (bit) {
		case BIT_4:
			reserved[0] = state;
			break;
		case BIT_5:
			reserved[1] = state;
			break;
		case BIT_6:
			reserved[2] = state;
			break;
		case BIT_7:
			reserved[3] = state;
			break;
		default:
			break;
		}
	}

	public int getValue() {
		adjust();
		int result = 0;
		if(debris){
			result = result | 1;
		}
		if(spot){
			result = result | (1<<1);
		}
		if(dock){
			result = result | (1<<2);
		}
		if(checkRobot){
			result = result | (1<<3);
		}
		if(reserved[0]){
			result = result | (1<<4);
		}
		if(reserved[1]){
			result = result | (1<<5);
		}
		if(reserved[2]){
			result = result | (1<<6);
		}
		if(reserved[3]){
			result = result | (1<<7);
		}
		return result;
	}
	
	public int getColor() {
		return color;
	}
	public int getIntensity() {
		return intensity;
	}
	
	
}
