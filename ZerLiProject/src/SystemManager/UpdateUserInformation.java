package SystemManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import ShopWorker.FillCustomerAnswers;
import ShopWorker.ShopWorkerMain;
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

public class UpdateUserInformation extends GuiExtensions {

	// Labels
	@FXML
	private Label lblUpdateUserInformationStatus;

	// Text fields
	@FXML
	private TextField tfUserName;

	@FXML
	private TextField tfPassword;

	@FXML
	private TextField tfPaymentBalance;

	// Combo Box
	@FXML
	private ComboBox cbUserName;

	@FXML
	private ComboBox cbPermission;

	@FXML
	private ComboBox cbStoreConnection;

	@FXML
	private ComboBox cbPaymentStatus;

	// Buttons
	@FXML
	private Button btnConfirmChanges;

	public void start() throws Exception {

		Main.getSystemManagerMainControl().setUpdateUserInformationControl(
				(UpdateUserInformation) createAndDefinedFxmlWindow("UpdateUserInformation.fxml",
						"Update Login information"));

		initialize_UpdateUserInformation_client();
	}

	public void initialize_UpdateUserInformation_client() {

		// disable all and then check if can be enabled
		disableGui(true, Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfUserName,
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPassword,
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPaymentBalance,
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission,
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbStoreConnection,
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus,
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().btnConfirmChanges,
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbUserName);

		// configuration of all combo box
		for (Main.Permission per : Main.Permission.values())
			addItemsToComboBox(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission,
					per.toString());

		addItemsToComboBox(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus,
				"Active", "Suspended");

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "UserName" + Main.FROMCommmandStatement + "users",
				Main.SystemManagerInitializeUserID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);
		}

		packet = new PacketClass(Main.SELECTCommandStatement + "storeID" + Main.FROMCommmandStatement + "stores",
				Main.SystemManagerInitializeStoreID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);
		}

	}

	public void initialize_UpdateUserInformation_server(PacketClass packet) {
		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			DataList = (ArrayList<ArrayList<String>>) packet.getResults();

			if (DataList == null) {
				updateStatusLabel("Users list is empty", true, Main.getSystemManagerMainControl()
						.getUpdateUserInformationControl().lblUpdateUserInformationStatus);
			} else {

				disableGui(false, Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbUserName);

				for (int i = 0; i < DataList.size(); i++) {
					addItemsToComboBox(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbUserName,
							DataList.get(i).get(0));
				}
			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to users data", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);

		}
	}

	public void addStores_UpdateUserInformation_server(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			int i;

			if (DataList == null) {
				updateStatusLabel("Stores list is empty", true, Main.getSystemManagerMainControl()
						.getUpdateUserInformationControl().lblUpdateUserInformationStatus);
			} else {

				for (i = 0; i < DataList.size(); i++) {
					addItemsToComboBox(
							Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbStoreConnection,
							DataList.get(i).get(0));
				}
			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to users data", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);

		}

	}

	public void click_UpdateUserInformation_ComboBoxIDClient() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getSelectionModel()
						.clearSelection();
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbStoreConnection
						.getSelectionModel().clearSelection();
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus.getSelectionModel()
						.clearSelection();
				Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPaymentBalance.setText("");
			}
		});

		PacketClass packet = new PacketClass(Main.SELECTCommandStatement + "UserName,Password,Permission"
				+ Main.FROMCommmandStatement + "users" + Main.WHERECommmandStatement + "UserName = '"
				+ Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbUserName.getValue() + "'",
				Main.SystemManagerClickUserID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			disableGui(true, Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfUserName,
					Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPassword,
					Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPaymentBalance,
					Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission,
					Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbStoreConnection,
					Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus,
					Main.getSystemManagerMainControl().getUpdateUserInformationControl().btnConfirmChanges,
					Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbUserName);
			updateStatusLabel("Client connection error", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);
		}

	}

	public void click_UpdateUserInformation_ComboBoxIDServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			DataList = (ArrayList<ArrayList<String>>) packet.getResults();

			if (DataList == null) {
				updateStatusLabel("Failed found Login", true, Main.getSystemManagerMainControl()
						.getUpdateUserInformationControl().lblUpdateUserInformationStatus);
			} else {

				disableGui(false, Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfUserName,
						Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPassword,
						Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission,
						Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbStoreConnection,
						Main.getSystemManagerMainControl().getUpdateUserInformationControl().btnConfirmChanges);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfUserName
								.setText(DataList.get(0).get(0));
						Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPassword
								.setText(DataList.get(0).get(1));
					}
				});

				if (DataList.get(0).get(2).equals("1"))
					disableGui(false,
							Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus,
							Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPaymentBalance);
				else
					disableGui(true,
							Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus,
							Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPaymentBalance);

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to users data", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);

		}

	}

	public void click_UpdateUserInformation_ConfirmChangesClient() {

		String sqlCommandInsert = Main.INSERTCommmandStatement + "users" + "(";
		String sqlCommandValues = Main.VALUESCommmandStatement + "(";
		boolean changesSelected = false;

		if (!Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfUserName.getText().isEmpty()) {
			changesSelected = true;
			sqlCommandInsert += "UserName";
			sqlCommandValues += "'" + Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfUserName.getText() + "'";
		}

		if (!Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPassword.getText().isEmpty()) {
			if(changesSelected)
			{
				sqlCommandInsert += ",";
				sqlCommandValues += ",";
			}
			else
				changesSelected = true;
			
			sqlCommandInsert += "Password";
			sqlCommandValues += "'" + Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPassword.getText() + "'";
		}

		if (Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getUserData() != null) {
			if(changesSelected)
			{
				sqlCommandInsert += ",";
				sqlCommandValues += ",";
			}
			else
				changesSelected = true;
			
			sqlCommandInsert += "Permission";
			
			if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("Login"))
				sqlCommandValues += "0";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("ShopWorker"))
				sqlCommandValues += "1";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("Customer"))
				sqlCommandValues += "2";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("Expert"))
				sqlCommandValues += "3";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("CustomerService"))
				sqlCommandValues += "4";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("SystemManager"))
				sqlCommandValues += "5";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("CompanyManager"))
				sqlCommandValues += "6";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("StoreManager"))
				sqlCommandValues += "7";
			else if(Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPermission.getValue().equals("CompanyEmployee"))
				sqlCommandValues += "8";

		}

		if (Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbStoreConnection
				.getUserData() != null) {
			if(changesSelected)
			{
				sqlCommandInsert += ",";
				sqlCommandValues += ",";
			}
			else
				changesSelected = true;
			
			sqlCommandInsert += "StoreID";
			sqlCommandValues += Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbStoreConnection.getValue();

		}

		if (Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus.isDisabled()) {
	
			
			// TODO payment tables and column info missing
			
			if (Main.getSystemManagerMainControl().getUpdateUserInformationControl().cbPaymentStatus
					.getUserData() != null) {
				if(changesSelected)
				{
					sqlCommandInsert += ",";
					sqlCommandValues += ",";
				}
				else
					changesSelected = true;

			}

			if (!Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPaymentBalance.getText()
					.isEmpty()) {
				if(changesSelected)
				{
					sqlCommandInsert += ",";
					sqlCommandValues += ",";
				}
				else
					changesSelected = true;
				
				sqlCommandInsert += "";
				sqlCommandValues += "'" + Main.getSystemManagerMainControl().getUpdateUserInformationControl().tfPaymentBalance.getText() + "'";

			}

		}

		if (changesSelected) {
			sqlCommandInsert += ")";
			sqlCommandValues += ");";
			
			PacketClass packet = new PacketClass(sqlCommandInsert + sqlCommandValues,
					Main.SystemManagerClickConfirmChanges, Main.WRITE);

			try {
				Main.getClientConsolHandle().sendSqlQueryToServer(packet);
			} catch (Exception e) {
				updateStatusLabel("Client connection error", true, Main.getSystemManagerMainControl()
						.getUpdateUserInformationControl().lblUpdateUserInformationStatus);
			}
		} else
			updateStatusLabel("No changes inserted", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);

	}
	
	
	public void click_UpdateUserInformation_ConfirmChangesServer(PacketClass packet) {

		if (packet.getSuccessSql()) {

			updateStatusLabel("User details updated", false, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);

		} else {
			// Sql command failed
			updateStatusLabel("User details not updated", true, Main.getSystemManagerMainControl()
					.getUpdateUserInformationControl().lblUpdateUserInformationStatus);

		}
		
	}
	
	public void click_UpdateUserInformation_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();
		
		try {
			Main.getSystemManagerMainControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
