package Costumer;

import java.sql.Date;

public class CollectionItem {

	private int orderID;
	private Date dateToBeDelivierdOrReadyToPickUp; 
	
	
	public CollectionItem(Date dateToBeDelivierdOrReadyToPickUp,int order){
		this.dateToBeDelivierdOrReadyToPickUp=dateToBeDelivierdOrReadyToPickUp;
		this.orderID=order;
	}
	
	@Override
	public String toString() {
		return "123";
	}
}
