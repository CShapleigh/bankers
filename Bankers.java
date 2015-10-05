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
    registeredClaims = new ArratList<int[]>();
  }

  public synchronized void setClaim(int units) {
    String currentThread = Thread.currentThread.getName();
    if (threadHasClaim(currentThread) || notEnoughUnits(units)) {
      System.exit(1);
    }
    registerClaim(currentThread, units);
  }

  public synchronized boolean request(int units) {
    String currentThread = Thread.currentThread.getName();
    boolean requestExceedsClaim = units > remaining();
    if (noClaimOrNotEnoughUnits(currentThread, units) || requestExceedsClaim) {
      System.exit(1);
    }
    System.out.println("Thread " + currentThread + " requests " + units + ".");
    //allocate
    System.out.println("Thread " + currentThread + " has " + units + " allocated.");
  }

  public void release(int units) {
    if (noClaimOrNotEnoughUnits(currentThread, units)) {
      System.exit(1);
    }
    System.out.println("Thread " + currentThread + " releases " + units + ".");
  }

  public int allocated() {
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    return registeredClaims.get(threadNumber)[0];
  }

  public int remaining() {
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    return registeredClaims.get(threadNumber)[1] - allocated();
  }

  private boolean noClaimOrNotEnoughUnits(String currentThread, int units) {
    int threadNumber = registeredClaimNames.indexOf(currentThread);
    boolean noClaimRegistered = threadNumber == -1;
    boolean notEnoughUnits = units < 1;
    return noClaimRegistered || notEnoughUnits;
  }

  private void registerClaim(String currentThread, int units) {
      registeredClaims.add(currentThread);
      int[] claim = new int[2];
      claim[0] = 0;
      claim[1] = units;
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
      return true
    }
    return false;
  }

}
