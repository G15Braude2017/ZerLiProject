package Customer;

import client.Main.IsOnSale;
import client.Main.ItemType;
import client.Main.OrderType;

public class CatalogItem extends OrderItem {

	private int catalogItemID;
	private IsOnSale catalogitemSaleFlag;
	private float SaleRate;

	public CatalogItem(OrderType orderType, ItemType itemType, float itemPrice, int itemInOrderAmount,
			int catalogItemID, IsOnSale catalogitemSaleFlag, float SalesRate, int itemID) {
		super(orderType, itemType, itemPrice, itemInOrderAmount, itemID);
		this.catalogItemID = catalogItemID;
		this.catalogitemSaleFlag = catalogitemSaleFlag;
		this.SaleRate = SaleRate;

	}
	@Override 
	public float getPrice()
	{
		if (catalogitemSaleFlag== IsOnSale.OnSale) {
			return itemPrice-itemPrice*SaleRate;
		}
		else return itemPrice;
	}
	
	
	
	
	@Override
	public String toString() {
		return "123";
	}
	
}
