package Costumer;

import java.sql.Date;

import client.Main.ReceiptStatus;

public class Receipt {

	private int storeID;
	private int orderID;
	private int costumerID;
	private float ReceiptPrice ;
	private ReceiptStatus status;
	private Date ReceiptDate; //get from order 
	
	
	public Receipt (Date date ,int store, int order , int costumer , float price) {
		this.storeID = store ;
		this.orderID = order;
		this.costumerID= costumer ;
		this.ReceiptPrice=price;
		this.status=ReceiptStatus.active;
		this.ReceiptDate=date ;
	}
	
	
	
	public String toString(){
		//will return a string that suted to the DB
		return "123";
	}
	// add 
	
}
