package world.time;

public class WorldClock {
	
	public static final int MAX_DAYTIME = 2400;
	
	private long totalTime;
	private int daytime;
	
	public void reset() {
		daytime = 0;
	}
	
	public void advance() {
		if(daytime <= MAX_DAYTIME) {
			daytime++;
		}else {
			daytime = 0;
		}
		totalTime++;
	}
	
	public int daytime() {
		return daytime;
	}
	
	public void daytime(int daytime) {
		this.daytime = daytime;
	}
	
	public long totalTime() {
		return totalTime;
	}

}
