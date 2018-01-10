package Customer;

import client.Main.OrderType;
import client.Main.ItemType;

public class OrderItem {

	private OrderType orderType;
	private ItemType itemType;
	protected float itemPrice;
	private int itemInOrderAmount;
	private int itemID;
	
	
	
	public OrderItem(OrderType orderType, ItemType itemType, float itemPrice, int itemInOrderAmount, int itemID) {
		this.itemInOrderAmount = itemInOrderAmount;
		this.orderType = orderType;
		this.itemType = itemType;
		this.itemInOrderAmount = itemInOrderAmount;
		this.itemID = itemID;
	}

	
	@Override
	public String toString() {
		return "123";
	}


	public float getPrice() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
}
