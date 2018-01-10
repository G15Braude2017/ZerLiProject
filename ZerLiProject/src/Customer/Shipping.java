package Customer;

import java.sql.Date;

public class Shipping extends CollectionItem{
	
	
	private String CustomerPhoneNumber;
	private String addressTobeSentTo;
	private String NameOfReceiver;
	private int ShippingID;
	
	
	
	public Shipping (Date dateToBeDeliverd, int order,String PhoneNumber, String address , String name ,int LastShippingID) {
		super(dateToBeDeliverd,order);
		
		this.addressTobeSentTo = address;
		
		this.CustomerPhoneNumber= PhoneNumber;
		this.NameOfReceiver=name;
		this.ShippingID=LastShippingID+1;
	}
	
	public String toString(){
		//will return a string that suted to the DB
		return "123";
	}
	
	
}
