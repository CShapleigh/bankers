import java.util.ArrayList;

public class Banker {
  private int numberOfUnitsOnHand;  // total number of units Bank has to loan
  private int totalUnits;

  private ArrayList<String> registeredClaimNames;
  private ArrayList<int[]> registeredClaims;

  public Banker(int nUnits) {
    this.numberOfUnitsOnHand = nUnits;
    this.totalUnits = nUnits;
    registeredClaimNames = new ArrayList<String>();
    registeredClaims = new ArrayList<int[]>();
    System.out.println("Banker created with " + this.totalUnits + " total units.");
  }

  // Attempts to register claim for up to nUnits of resource
  public synchronized void setClaim(int nUnits) {


    String currentThread = Thread.currentThread().getName();

    if (threadHasClaim(currentThread)) {
      System.out.println(currentThread + " already has registered claim!");
    } else if (nUnits <= 0) {
      System.out.println(currentThread + " isn't requesting a positive amount...");
    } else if (notEnoughUnits(nUnits)) {
      System.out.println(nUnits + " is greater than the total Banks's " + this.numberOfUnitsOnHand);
    } else {
      registerClaim(currentThread, nUnits);
      return;
    }

    System.exit(1);
  }

  //needs refactor, very ugly!!!
  public synchronized boolean request(int units) {
    String currentThread = Thread.currentThread().getName();
    boolean requestExceedsClaim = units > remaining();
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    if (noClaimOrNotEnoughUnits(currentThread, units) || requestExceedsClaim) {

      System.exit(1);
    }
    System.out.println(currentThread + " requests " + units + ".");
    while (true) {
      if (this.numberOfUnitsOnHand > units && isStateSafe(currentThread, units)) {
            allocate(currentThread, units);
            return true;
      }
      System.out.println(currentThread + " waits");
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("Error");
			}
			System.out.println(currentThread + " awakened");
    }
  }

  public synchronized void release(int units) {
    // Client is returning its loan (resources) to the bank.
    String currentThread = Thread.currentThread().getName();
    if (noClaimOrNotEnoughUnits(currentThread, units)) {
      System.exit(1);
    }
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    registeredClaims.get(threadNumber)[0] -= units;
    this.numberOfUnitsOnHand += units;
    System.out.println("Thread " + currentThread + " releases " + units + ".");
    notifyAll();
  }

  public int allocated() {
    String currentThread = Thread.currentThread().getName();
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    return registeredClaims.get(threadNumber)[0];
  }

  public int remaining() {
    String currentThread = Thread.currentThread().getName();
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    return registeredClaims.get(threadNumber)[1] - allocated();
  }

  //I kinda got lazy here...sorry
  private boolean isStateSafe(String currentThread, int units) {
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    int desiredUnits = getDesiredUnits(threadNumber);
    if (desiredUnits == units) {
      return true;
    }
    int resourcesLeft = this.numberOfUnitsOnHand - units;
    for (int[] claim : registeredClaims) {
      if (claim[1] - claim[0] == 0) {
        resourcesLeft += claim[1];
      }
    }
    desiredUnits = getDesiredUnits(threadNumber);
    if(desiredUnits <= resourcesLeft + units) {
      return true;
    }
    return false;
  }

  private int getDesiredUnits(int threadNumber) {
    int desiredUnits = registeredClaims.get(threadNumber)[1]
        - registeredClaims.get(threadNumber)[0];
    return desiredUnits;
  }

  private void allocate(String currentThread, int units) {

    // Get the thread and assign it the requested units.
    // Also decrement numberOfUnitsOnHand
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    this.numberOfUnitsOnHand -= units;
    registeredClaims.get(threadNumber)[0] += units;

    System.out.println("Ding! Bank allocates " + units + " units to Client " + threadNumber);
    System.out.println("Bank has " + this.numberOfUnitsOnHand + " units on hand.");
  }

  private boolean noClaimOrNotEnoughUnits(String currentThread, int units) {
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    int threadSetClaim = registeredClaims.get(threadNumber)[1];
    boolean isClaimRegistered = (threadNumber != -1);
    if (!isClaimRegistered) {
      System.out.println(currentThread + " didn't register a claim");
    } else if (units < 1) {
      System.out.println(currentThread + "'s request of " + units + " units isn't enough.");
    } else if (units > threadSetClaim) {
      System.out.println(currentThread + " is requesting more than total claim of " + threadSetClaim);
    } else {
      return false;
    }
    return true;
  }

  private void registerClaim(String currentThread, int units) {
      registeredClaimNames.add(currentThread);
      int claim[] = {0, units};
      registeredClaims.add(claim);
      System.out.println(currentThread + " registered claim for " + units + ".");
  }

  private boolean threadHasClaim(String currentThread) {
    for (String registeredClaim : registeredClaimNames) {
      if (registeredClaim.equals(currentThread)) {
        return true;
      }
    }
    return false;
  }

  private boolean notEnoughUnits(int units) {
    return (units > this.totalUnits);
  }
}
