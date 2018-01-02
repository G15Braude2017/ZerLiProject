package Costumer;

public class CostumerOfStoreBalance {
	private int storeID;
	private int costumerID;
	private float storeBalanceMoney;
	
	public  CostumerOfStoreBalance (int store ,int costumer ,float balance) {
		this.storeID = store;
		this.costumerID = costumer;
		this.storeBalanceMoney =balance ;
		
	}
	
	 public int getStoreIDinBalance  () {
		 return storeID;
	 }
	 public int getCostumerIDinBalance () {
		 return costumerID;
	 }
	 public float getBalance () {
		 return storeBalanceMoney;
	 }
}
