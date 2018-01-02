package Costumer;

import client.Main;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CostumerInterface {

	// refund values
	public static final float NO_REFUND_last_hour = 0;
	public static final float FULL_REFUND_before_3_hour_mark = 1;
	public static final float HALF_REFUND_betwin_3_and_1_hour = (float) 0.5;
	public static final float DATE_ERROR = -1;
	public static final float three_hours =180;
	public static final float one_hour=60;
	
	// function that takes on click the color (combo box of the enum in main) ,
	// price range , and item type
	// and creates am item (with simple alg that takes the flower types from DB )

	// when creating an order self defined item is added to itemDB (after created
	// with flowers +type)
	// when creating a catalog item it just retrieve the item

	// when creating a price it can be worked from the array list of the order ;

	// Collection item and pickUp item get time and date in the interface

	// first costumer data is sent from db by entering this window
	// after the costumer confirm order it will be created
	// after order creation pickup/shipping is created
	// before info is sent to DB the system will check if costumer has the balance needed to pay for the order if not he will be notified
	// if costumer can pay and all sent to dp
	// after pickup/shipping creation receipt is created from the objected that are
	// and pushed to db

	// in "cancel order menu" the costumer will be shown the receipt id and date of
	// the order (in the combo box)
	public void cancelOrder(int costumerID) {
		// will pull all orders from the specific costumer (given an id)
		// Display all orders in combo box 
		// When the costumer press confirm the status of the receipt will change in db
		// after change in db the system will set refund amount and set to store balance in costumer with it
	}

	public float refundAmountCalculatin(float price, Date dateAndTimeOfOrderdeliveryOrPickUp) {
		java.util.Date TimeAndDateOfNow = new java.util.Date();
		float RefundMultiplyer = NO_REFUND_last_hour;
		if (dateAndTimeOfOrderdeliveryOrPickUp.after(TimeAndDateOfNow)) {
			long diffrenceInTime = getDateDiff(dateAndTimeOfOrderdeliveryOrPickUp, TimeAndDateOfNow, TimeUnit.MINUTES);
			if (diffrenceInTime >= three_hours)
				RefundMultiplyer = FULL_REFUND_before_3_hour_mark;
			else if (diffrenceInTime >= one_hour)
				RefundMultiplyer = HALF_REFUND_betwin_3_and_1_hour;
			return price * RefundMultiplyer;
		}
		  return DATE_ERROR;
	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

}
