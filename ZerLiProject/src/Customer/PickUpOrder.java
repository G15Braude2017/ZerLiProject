package Customer;

import java.sql.Date;

public class PickUpOrder extends CollectionItem {

	private int pickUpID;

	public PickUpOrder(Date dateTobeRdyForPickUp, int order, int LastPickUpID) {
		super(dateTobeRdyForPickUp, order);
		this.pickUpID = LastPickUpID + 1;
	}

	@Override
	public String toString() {
		return "123";
	}
}
