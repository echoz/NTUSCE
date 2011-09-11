import java.util.*;

class TimeoutTask extends TimerTask {
	private SWE swe = null;
	private int seq = 0;
	
	public TimeoutTask(SWE sw, int seq) {
		this.swe = sw;
		this.seq = seq;
	}
	
	public void run() {
		swe.generate_timeout_event(seq);
	}
}