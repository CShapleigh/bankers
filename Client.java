public class Client extends Thread {
	private Banker banker;
	private int nUnits;
	private int nRequests;
	private long minSleepMillis;
	private long maxSleepMillis;

	// Client has a max "claim" - the total amount that he can
	// possibly take from the bank.
	public Client(String name, Banker banker, int nUnits, int nRequests, long minSleepMillis, long maxSleepMillis) {
			super(name);
			this.banker = banker;
			this.nUnits = nUnits;
			this.nRequests = nRequests;
			this.minSleepMillis = minSleepMillis;
			this.maxSleepMillis = maxSleepMillis;
	}

	public void run() {
		this.banker.setClaim(nUnits);  // register claim for up to nUnits

		for (int i = 0; i < this.nRequests; ++i) {
			if (this.banker.remaining() == 0) {
				this.banker.release(this.nUnits);  // release all units
			} else {
				this.banker.request((int)(Math.random() * this.nUnits + 1));  // request some units
			}

			// Sleep for random amount
			try {
				Thread.sleep((int)(Math.random() * maxSleepMillis + minSleepMillis));
				System.out.println(Thread.currentThread().getName() + " waits...");
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}

		this.banker.release(this.nUnits); // release any units still allocated
		return;
	}
}
