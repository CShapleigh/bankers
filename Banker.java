import java.util.ArrayList;

public class Banker {

  private int numberOfUnits;
  private int totalUnits;

  //These should really be an object but NOOO apparently I have to submit 3 files
  //...whatever, this is super shitty.
  private ArrayList<String> registeredClaimNames;
  private ArrayList<int[]> registeredClaims;

  public Banker(int numberOfUnits) {
    numberOfUnits = numberOfUnits;
    registeredClaimNames = new ArrayList<String>();
    registeredClaims = new ArrayList<int[]>();
  }

  public synchronized void setClaim(int units) {
    String currentThread = Thread.currentThread().getName();
    if (threadHasClaim(currentThread) || notEnoughUnits(units)) {
      System.exit(1);
    }
    registerClaim(currentThread, units);
  }

  //needs refactor, very ugly!!!
  public synchronized boolean request(int units) {
    String currentThread = Thread.currentThread().getName();
    boolean requestExceedsClaim = units > remaining();
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    if (noClaimOrNotEnoughUnits(currentThread, units) || requestExceedsClaim) {
      System.exit(1);
    }
    System.out.println("Thread " + currentThread + " requests " + units + ".");
    while (true) {
      if (totalUnits > units && isStateSafe(currentThread, units)) {
            allocate(currentThread, units);
      }
    }
  }

  public synchronized void release(int units) {
    String currentThread = Thread.currentThread().getName();
    if (noClaimOrNotEnoughUnits(currentThread, units)) {
      System.exit(1);
    }
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    registeredClaims.get(threadNumber)[0] -= units;
    totalUnits += units;
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
    int resourcesLeft = totalUnits - units;
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
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    totalUnits -= units;
    registeredClaims.get(threadNumber)[0] += units;
    System.out.println("Thread " + currentThread + " has " + units + " allocated.");
  }

  private boolean noClaimOrNotEnoughUnits(String currentThread, int units) {
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    boolean noClaimRegistered = threadNumber == -1;
    boolean notEnoughUnits = units < 1;
    return noClaimRegistered || notEnoughUnits;
  }

  private void registerClaim(String currentThread, int units) {
      registeredClaimNames.add(currentThread);
      int[] claim = new int[2];
      claim[0] = 0;
      claim[1] = units;
      registeredClaims.add(claim);
      System.out.println("Thread " + currentThread + " sets a claim for " + units+ ".");
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
    if (units > totalUnits) {
      return true;
    }
    return false;
  }

}
