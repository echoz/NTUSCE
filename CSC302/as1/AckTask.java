import java.util.*;

class AckTask extends TimerTask {
	private SWE swe = null;
	
	public AckTask(SWE swe) {
		this.swe = swe;
	}
	
	public void run() {
		swe.generate_acktimeout_event();
	}
}