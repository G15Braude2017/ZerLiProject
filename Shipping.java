package Costumer;

import java.sql.Date;

public class Shipping extends CollectionItem{
	
	
	private String CostumerPhoneNumber;
	private String addressTobeSentTo;
	private String NameOfReceiver;
	private int ShippingID;
	
	
	
	public Shipping (Date date, int order,String PhoneNumber, String address , String name ,int LastShippingID) {
		super(date,order);
		
		this.addressTobeSentTo = address;
		
		this.CostumerPhoneNumber= PhoneNumber;
		this.NameOfReceiver=name;
		this.ShippingID=LastShippingID+1;
	}
	
	public String toString(){
		//will return a string that suted to the DB
		return "123";
	}
	
	
}
