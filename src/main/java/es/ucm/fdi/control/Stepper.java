package es.ucm.fdi.control;

public class Stepper {
	private Runnable before;
	private Runnable during;
	private Runnable after;
	
	private boolean stopRequested = false;
	private int steps;
	
	public Stepper(Runnable before, Runnable during, Runnable after) {
		this.before = before;
		this.during = during;
		this.after = after;
	}
	
	public Thread start(int steps, int delay) {
		this.steps = steps;
		this.stopRequested = false;
		
		Thread t = new Thread(() -> {
			try {
				before.run();
				while(!stopRequested  &&  Stepper.this.steps > 0) {
					during.run();
					try {
						Thread.sleep(delay);
					} catch(InterruptedException ie) {
						//ignore and continue
					}
					Stepper.this.steps--;
				}
			} finally {
				after.run();
			}
		});
		t.start();
		return t;
	}
	
	public void stop() {
		stopRequested = true;
	}

}
