package Customer;

import java.util.ArrayList;

import Customer.SelfDefinedItem;
import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class SelfDefinedItemInOrderManu extends GuiExtensions {

	private static boolean selfDefindItemReady;

	// Labels
	@FXML
	private Label lblBouquet_Type;
	@FXML
	private Label lblPrice_range;
	@FXML
	private Label lblDominanting_color;

	// Text fields
	@FXML
	private TextField txtFldMinPrice;
	@FXML
	private TextField txtFldMaxPrice;
	@FXML
	private TextField txtFldSelfDefItemAmount;//needs to be added in fx

	// Buttons
	@FXML
	private Button Back;
	@FXML
	private Button Add_to_Order;

	// combobox
	@FXML
	private ComboBox bouquet;
	@FXML
	private ComboBox flower_Color;

	public void start() throws Exception {

		Main.getCustomerMainControl().getCreateNewOrderControl().setSelfDefinedItemInOrderManuControl(
				(SelfDefinedItemInOrderManu) createAndDefinedFxmlWindow("SelfDefinedItemInOrderManu.fxml",
						"Add a self defined item"));

		initializeSelfDefinedItem();
	}

	public void initializeSelfDefinedItem() {

		setGUI_OpenSelfDefinedItem_Disable(true);

		// platform somthing run later ?

		selfDefindItemReady = false;

	}

	public void initializeGUI_flowerColors_FromServer(PacketClass packet) {
		ArrayList<ArrayList<String>> DataList;
		int i;
		String flowerColorStr;
		if (packet.getSuccessSql()) {

			DataList = (ArrayList<ArrayList<String>>) packet.getResults();

			if (DataList == null) {
				updateStatusLabel("There are no colors", true, Main.getCustomerMainControl().getCreateNewOrderControl()
						.getSelfDefinedItemInOrderManuControl().lblDominanting_color);

			} else {

				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						Main.getCustomerMainControl().getCreateNewOrderControl()
								.getSelfDefinedItemInOrderManuControl().flower_Color.setDisable(false);
					}
				});

				for (i = 0; i < DataList.size(); i++) {
					flowerColorStr = DataList.get(i).get(0);

					addCustomerComboBoxFlowerColor(flowerColorStr);
				}

			}
		}

	}

	public void click_SelfDefindItem_ComboBoxFlowerColorClient() {
		PacketClass colors = new PacketClass(Main.SELECTCommandStatement + Main.DISTINCTCommandStatement + "flowerColor"
				+ Main.FROMCommmandStatement + "flowers", Main.SelfdefindItemFlowers, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(colors);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true, Main.getCustomerMainControl().getCreateNewOrderControl()
					.getSelfDefinedItemInOrderManuControl().lblDominanting_color);
		}

	}

	public void click_SelfDefindItem_ComboBoxFlowerColorServer(PacketClass packet) {
		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String FlowerColorStr;
			if (DataList == null) {
				updateStatusLabel("There are no colors to choose from", true, Main.getCustomerMainControl()
						.getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().lblDominanting_color);
			}
			else {
				updateStatusLabel("", true , Main.getCustomerMainControl()
						.getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().lblDominanting_color);
				addCustomerComboBoxFlowerColor("None");// add anoption to see the combo box but not pick any thing 
				for (i = 0; i < DataList.size(); i++) {
					FlowerColorStr = DataList.get(i).get(0);

					addCustomerComboBoxFlowerColor(FlowerColorStr);

			}
			
			}}
		 else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true, Main.getCustomerMainControl()
					.getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().lblDominanting_color);
		}

	}

	public void click_SelfDefindItem_ComboBoxBouqeutClient() {

		PacketClass bouquetfromDB = new PacketClass(
				Main.SELECTCommandStatement + " bouquteName" + Main.FROMCommmandStatement + "bouqutettype",
				Main.SelfdefindItemBouqut, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(bouquetfromDB);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true, Main.getCustomerMainControl().getCreateNewOrderControl()
					.getSelfDefinedItemInOrderManuControl().lblBouquet_Type);
		}
	}

	public void click_SelfDefindItem_ComboBoxBouqeutServer(PacketClass packet) {
		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String BouquetTypeStr;
			if (DataList == null) {
				updateStatusLabel("There are no bouquet types to choose from", true, Main.getCustomerMainControl()
						.getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().lblBouquet_Type);
			} 
			else {
				updateStatusLabel("", true , Main.getCustomerMainControl()
						.getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().lblBouquet_Type);
				
				
				for (i = 0; i < DataList.size(); i++) {
					BouquetTypeStr = DataList.get(i).get(0);

					addCustomerComboBoxFlowerColor(BouquetTypeStr);

			}
			
			}}
			
		 else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true, Main.getCustomerMainControl()
					.getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().lblBouquet_Type);
		} 
	}

	public void click_selfDefinedItemInOrder_AddToOrder() {
		float price;
		SelfDefinedItem selfDefItem;//create new item in  

		// will take bouquet price from DB
		// add it to price
		// will take all flowers from color that is spacfied if there is one
		// if there is non will take first flower in db
		// will create selfdefineditem class
		// will add all data to class
		// will send selfdefineditem data to DB
	}

	public void click_SelfDefinedItemInOrder_backBtn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			Main.getCustomerMainControl().getCreateNewOrderControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//////////////////////////////////
	// INTERNAL FUNCTIONS
	//////////////////////////////////
	private void setGUI_OpenSelfDefinedItem_Disable(boolean bool)// cancel unpremmsion window in window
	{

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getCustomerMainControl().getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().bouquet
						.setDisable(bool);
				Main.getCustomerMainControl().getCreateNewOrderControl()
						.getSelfDefinedItemInOrderManuControl().flower_Color.setDisable(bool);
				Main.getCustomerMainControl().getCreateNewOrderControl()
						.getSelfDefinedItemInOrderManuControl().txtFldMaxPrice.setDisable(bool);
				Main.getCustomerMainControl().getCreateNewOrderControl()
						.getSelfDefinedItemInOrderManuControl().txtFldMinPrice.setDisable(bool);
				Main.getCustomerMainControl().getCreateNewOrderControl()
						.getSelfDefinedItemInOrderManuControl().Add_to_Order.setDisable(bool);
			}
		});

	}

	private void addCustomerComboBoxBouquet(String bouquet) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getCustomerMainControl().getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().bouquet
						.getItems().addAll(bouquet);

			}
		});
	}

	private void addCustomerComboBoxFlowerColor(String color) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getCustomerMainControl().getCreateNewOrderControl().getSelfDefinedItemInOrderManuControl().bouquet
						.getItems().addAll(color);

			}
		});
	}

}
