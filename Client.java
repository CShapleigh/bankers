public class Client extends Thread {
	private Banker banker;
	private int nUnits;
	private int nRequests;
	private long minSleepMillis;
	private long maxSleepMillis;

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
				this.banker.release(this.banker.remaining());  // release all units
			} else {
				this.banker.request(nUnits);  // request some units
			}

			// Sleep for random amount
			try {
				Thread.sleep((int)(Math.random() * maxSleepMillis + minSleepMillis));
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}

		this.banker.release(this.banker.remaining()); // release any units still allocated
		return;
	}
}
