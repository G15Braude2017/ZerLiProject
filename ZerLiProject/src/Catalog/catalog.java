package Catalog;

import client.Main;
import client.Main.ItemType;

public class catalog 
{
	private String ItemImage;
	private String ItemName;
	private int ItemID;
	private ItemType Type;
	private float Price;
	private int Sale;
	
	public catalog ( String image ,String itemName, int type, float price, int itemID, int sale)
	{
		ItemImage = image;
        ItemName = itemName;
		ItemID = itemID;
		Type = Main.ItemType.values()[type];
		Price = price;
		Sale = sale;
	}

	public String getItemImage() {
		return ItemImage;
	}

	public void setItemImage(String itemImage) {
		ItemImage = itemImage;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	public int getItemID() {
		return ItemID;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	public ItemType getType() {
		return Type;
	}

	public void setType(ItemType type) {
		Type = type;
	}

	public float getPrice() {
		return Price;
	}

	public void setPrice(float price) {
		Price = price;
	}

	public int getSale() 
	{
		return Sale;
	}

	public void setSale(int sale) 
	{
		Sale = sale;
	}
	
	
}
