package Costumer;


import client.Main.ItemType;
import client.Main.OrderType;

public class SelfDefinedItem extends OrderItem{

	private String color;
	private float MaxSelfDeifinedItemPrice;
	private float MinSelfDefinedItemPrice;
	
	
	
	public SelfDefinedItem(OrderType orderType, ItemType itemType, float itemPrice, int itemInOrderAmount, int itemID,int MinPrice , int MaxPrice , String color) {
		super(orderType, itemType, itemPrice, itemInOrderAmount, itemID);
		this.MinSelfDefinedItemPrice= MinPrice;
		this.color= color;
		this.MaxSelfDeifinedItemPrice= MaxPrice;
	}
	
	@Override 
	public float getPrice()
	{
		//get full single flower list from DB 
		//get  single ItemType price and add it  
		//add floweres untill higher than min price and lower than max 
		//create a boucute and return price of all flowers + itemType
		return 0;
	}
	
	@Override 
	public String toString() {
		//fun will create a string to be match DB chartes to be added to DB
		return "123";
	}
}
