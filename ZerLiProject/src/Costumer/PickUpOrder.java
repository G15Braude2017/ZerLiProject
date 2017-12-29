package Costumer;

import java.sql.Date;

public class PickUpOrder extends CollectionItem {

	private int pickUpID;

	public PickUpOrder(Date date, int order, int LastPickUpID) {
		super(date, order);
		this.pickUpID = LastPickUpID + 1;
	}

	@Override
	public String toString() {
		return "123";
	}
}
