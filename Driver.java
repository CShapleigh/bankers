public class Driver  {
	
	//Number of resources available for the banker
	final private static int RESOURCE_NUM = 6;
	//Number of units for each client
	final private static int UNIT_NUM = 5;
	//Number of times each client requests
	final private static int REQUESTS = 5;
	//Minimum sleep time
	final private static int MIN_SLEEP = 100;
	//Maximum sleep time
	final private static int MAX_SLEEP = 1000;
	//Number of Clients
	final private static int CLIENT_NUM = 4;
	
	public static void main(String[] args){
		Banker banker = new Banker(RESOURCE_NUM);
		Client[] clients = new Client[CLIENT_NUM];
		for(int i = 0; i<CLIENT_NUM; i++){
			clients[i] = new Client("Client " + i, banker, UNIT_NUM, REQUESTS, MIN_SLEEP, MAX_SLEEP);
		}
		for(int i=0; i<CLIENT_NUM; i++){
			clients[i].start();
		}
		
		try{
			for(int i=0; i<CLIENT_NUM;i++){
				clients[i].join();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("All Finished");
	}
	
}
