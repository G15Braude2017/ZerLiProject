package Customer;

public class CustomerOfStoreBalance {
	private int storeID;
	private int customerID;
	private float storeBalanceMoney;
	
	public  CustomerOfStoreBalance (int store ,int customer ,float balance) {
		this.storeID = store;
		this.customerID = customer;
		this.storeBalanceMoney =balance ;
		
	}
	
	 public int getStoreIDinBalance  () {
		 return storeID;
	 }
	 public int getCustomerIDinBalance () {
		 return customerID;
	 }
	 public float getBalance () {
		 return storeBalanceMoney;
	 }
}
