package Customer;

import client.GuiExtensions;

import java.util.ArrayList;

import client.GuiExtensions;
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
import javafx.scene.control.TextArea;
import Customer.Order;

public class CreateOrder extends GuiExtensions {

	private static SelfDefinedItemInOrderManu SelfDefinedItemInOrderManuControl;
	// should be a control for the catalog aswell
	private static Order newOrder;
	private static String CurrentCustomerID;
    
	
	
	//to add shipping and pick on hide and will open tham by chose
	
	
	
	// ComboBox
	@FXML
	private ComboBox cbStore;
	@FXML
	private ComboBox cbSDelivery;

	// TextArea
	@FXML
	private TextArea taGreetingCardText;

	// Button
	@FXML
	private Button btnConfirm;
	@FXML
	private Button btnAddNewSelfDefinedItem;
	@FXML
	private Button btnAddCatalogItem;
	
	@FXML
	private Button btnBack; //needs to be added
	
	

	// lable
	@FXML
	private Label lblStore;
	@FXML
	private Label lblDelivery;

	private EventHandler<ActionEvent> eh;

	public void start() throws Exception {

		Main.getCustomerMainControl().setCreateNewOrderControl(
				(CreateOrder) createAndDefinedFxmlWindow("CustomerOrderManu.fxml", "Open new order"));

		initializeCreateOrder();

	}

	public void initializeCreateOrder() {

		// setGUI_CreateOrder_Disable(true);
		//create new order
		CurrentCustomerID = Main.getLoginLogicControl().getNewUser().getUserName();

	}

	public void click_CreateOrder_ComboBoxStoreIDClient() {

		// TODO change tables info
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "StoreID" + Main.FROMCommmandStatement + "store",
				Main.createOrderStoreIDCB, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true,
					Main.getCustomerMainControl().getCreateNewOrderControl().lblStore);
		}

	}

	public void click_CreateOrder_ComboBoxstoreNameServer() {
		// to do server

	}

	public void click_CreateOrder_AddNewSelfDefinedItem() {
	//add self def item	
	}
	
	
	public void click_CreateOrder_AddCatalogItem(){
	//add catalog item	
	}
	
	
	public void click_CreateOrder_ConfirmBTNClient(PacketClass packet) {
		// after payment that twill take from store balance and open a gui with payment amount left after taken from store balance
		// before info is sent to DB the system will check if customer has the balance
		// take new order created and pushes it to DB
		//Don't forget to put date of now in the order created (set)
		//create a shipping/pick up item and push to db
		//create a receipt and push to db
		//add separated func
		// Collection item and pickUp item get time and date in the interfac
	}

	public void click_CreateOrder_ConfirmBTNServer(PacketClass packet) {
		// check if item was created in db
		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {

			/*
			 * Platform.runLater(new Runnable() {
			 * 
			 * @Override public void run() {
			 * Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.
			 * setDisable(true);
			 * Main.getCustomerServiceMainControl().getOpenNewComplaintControl().
			 * taComplaintText.setDisable(true);
			 * Main.getCustomerServiceMainControl().getOpenNewComplaintControl().
			 * btnOpenNewComplaint .setDisable(true); } });
			 * 
			 * updateStatusLabel("New complaint opened", false
			 * ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().
			 * lblComplaintStatus);
			 * 
			 * } else { // Sql command failed updateStatusLabel("Failed open new complaint",
			 * true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().
			 * lblComplaintStatus); }
			 */
		}

	}

	public static SelfDefinedItemInOrderManu getSelfDefinedItemInOrderManuControl() {
		return SelfDefinedItemInOrderManuControl;
	}

	public static void setSelfDefinedItemInOrderManuControl(
			SelfDefinedItemInOrderManu selfDefinedItemInOrderManuControl) {
		SelfDefinedItemInOrderManuControl = selfDefinedItemInOrderManuControl;
	}

	public static Order getNewOrder() {
		return newOrder;
	}

	public static void setNewOrder(Order newOrder) {
		CreateOrder.newOrder = newOrder;
	}

	public static String getCurrentCustomerID() {
		return CurrentCustomerID;
	}

	public static void setCurrentCustomerID(String currentCustomerID) {
		CurrentCustomerID = currentCustomerID;
	}

	private void addOrderComboBoxItems() {

		/*
		 * Platform.runLater(new Runnable() {
		 * 
		 * @Override public void run() {
		 */
		Main.getCustomerMainControl().getCreateNewOrderControl().cbSDelivery.getItems().addAll("shipping", "Pickup");

		// }
		// });
	}

	private void addStoreNameComboBoxItems(String storeName) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getCustomerMainControl().getCreateNewOrderControl().cbStore.getItems().addAll(storeName);

			}
		});
	}

}
