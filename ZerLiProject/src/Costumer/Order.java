package Costumer;

import java.util.*;
import client.Main;
import client.Main.GreetingCardIs;
import Costumer.OrderItem;

public class Order {

	private Date CreationDate;
	private GreetingCardIs greetingCardFlag;
	private String GreetingCardText;
	private int CostumerID;
	private int orderNumber; // will get last order number from DB first
	private int storeID;
	private ArrayList<OrderItem> itemList;
	
	
	
	public Order(GreetingCardIs greetingCardFlag, String GreetingCardText, int CostumerID, int LastOrderNumber,int StoreID) {
		this.CreationDate = new Date();
		this.orderNumber = LastOrderNumber + 1;
		this.greetingCardFlag = greetingCardFlag;
		this.GreetingCardText = GreetingCardText;
		this.CostumerID = CostumerID;
		this.storeID=storeID;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public Date getTimeAndDate() {
		return CreationDate;
	}

	public GreetingCardIs getGreetingCardFlag() {
		return greetingCardFlag;
	}

	public int GetOrderCostumerID() {
		return CostumerID;
	}

	public String GetGreetingCardText() {
		return GreetingCardText;
	}

	public float GetOrderFinelPrice() {
		float price=0;
		for (Iterator<OrderItem> i = itemList.iterator(); i.hasNext();) {
			OrderItem item = i.next();
		price =price+ item.getPrice();
		}
		return price;
	}
	
	public int getStoreID()
	{
		return storeID;
	}

	@Override
	public String toString() {
		String str, strOut;
		str = String.valueOf(orderNumber) + " ";
		str = str + CreationDate.toString() + " ";
		str = str + String.valueOf(CostumerID) + " ";
		for (Iterator<OrderItem> i = itemList.iterator(); i.hasNext();) {
			OrderItem item = i.next();
			str = str + item.toString() + ",";
		}
		strOut = str.substring(0, str.length() - 1);
		str = strOut;
		if (greetingCardFlag == GreetingCardIs.yes)
			str = str + GreetingCardText;
		return str;
	}

}
