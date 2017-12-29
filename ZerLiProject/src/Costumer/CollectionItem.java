package Costumer;

import java.sql.Date;

public class CollectionItem {

	private int orderID;
	private Date date;
	
	
	public CollectionItem(Date date,int order){
		this.date=date;
		this.orderID=order;
	}
	
	@Override
	public String toString() {
		return "123";
	}
}
