public class Client extends Thread {
	Banker banker;
	int nUnits;
	int nRequests;
	long minSleepMillis;
	long maxSleepMillis;

	public Client(String name, Banker banker, int nUnits, int nRequests, long minSleepMillis, long maxSleepMillis) {
			super(name);
			this.banker = banker;
			this.nUnits = nUnits;
			this.nRequests = nRequests;
			this.minSleepMillis = minSleepMillis;
			this.maxSleepMillis = maxSleepMillis;
	}

	public void run() {
		// TODO: Register a claim for up to nUnits of resource with the banker.

		for (int i = 0; i < this.nRequests; ++i) {
			// TODO: request or release resources by invoking methods in banker
		}

		if (this.banker.remaining() == 0) {
			// TODO: release all units
		} else {
			// TODO: request units
		}
		
		//TODO: At the end of each loop, use Thread.sleep(millis) to sleep a random number of milliseconds from minSleepMillis to maxSleepMillis. This will introduce another dose of non-determinism into your program.
		
		// TODO: after loop, release any units still allocated and return
		return;
	}
}
