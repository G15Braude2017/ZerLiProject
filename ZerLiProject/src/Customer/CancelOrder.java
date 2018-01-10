package Customer;

import client.GuiExtensions;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class CancelOrder extends GuiExtensions {

	public static final float NO_REFUND_last_hour = 0;
	public static final float FULL_REFUND_before_3_hour_mark = 1;
	public static final float HALF_REFUND_betwin_3_and_1_hour = (float) 0.5;
	public static final float DATE_ERROR = -1;
	public static final float three_hours = 180;
	public static final float one_hour = 60;
	
	//Laybel
	
	
	// ComboBox
	@FXML
	private ComboBox cbOrder;

	// Button
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnBack; //needs to be added
	
	

	// Label
	@FXML
	private Label lblCustomerOrder;

	private EventHandler<ActionEvent> eh;

	public void start() throws Exception {

		Main.getCustomerMainControl()
				.setCancelOrderControl((CancelOrder) createAndDefinedFxmlWindow("CancelOrder.fxml", "Cancel Order"));

		initializeCancelOrder();

	}

	public void initializeCancelOrder() {

	}

	public void cancelOrder(int customerID) {
		// will pull all orders from the specific customer (given an id)
		// Display all orders in combo box
		// When the customer press confirm the status of the receipt will change in db
		// after change in db the system will set refund amount and set to store balance
		// in customer with it
	}

	// cancel order func
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

	public void Click_CacelOrder_OrderComboBoxClient() {

	}

	public void Click_CacelOrder_OrderComboBoxServer() {

	}

	public void Click_CacelOrder_cancelOrderBTN() {
		
	}
	
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

}
