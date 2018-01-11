package Catalog;


public class catalog 
{
	private String ItemImage;
	private String ItemName;
	private String ItemID;
	private String Type;
	private String Price;
	
	public catalog ( String image ,String itemName, String itemID, String type, String price)
	{
		ItemImage = image;
        ItemName = itemName;
		ItemID = itemID;
		Type = type;
		Price = price;
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

	public String getItemID() {
		return ItemID;
	}

	public void setItemID(String itemID) {
		ItemID = itemID;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	
	
}
