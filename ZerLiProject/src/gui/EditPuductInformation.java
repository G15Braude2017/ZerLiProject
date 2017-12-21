package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientConsole;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class EditPuductInformation implements Initializable {

	private static ClientConsole clientConsolHandle;

	private static String currentPID = null;
	private static String currentPName = null;
	private static String currentPType = null;

	// Buttons
	@FXML
	private Button btnSearchPID;
	@FXML
	private Button btnChangeName;

	// Labels
	@FXML
	private Label lblProductIdResult;
	@FXML
	private Label lblProductIdStatus;
	@FXML
	private Label lblProductNameResult;
	@FXML
	private Label lblProductTypeResult;

	// Text fields
	@FXML
	private TextField txtFldProductIdSearch;
	@FXML
	private TextField txtFldProductChangeName;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Update GUI
		btnSearchPID.setDisable(true);
		btnChangeName.setDisable(true);
		txtFldProductIdSearch.setDisable(true);
		txtFldProductChangeName.setDisable(true);

		initializeClient();

	}

	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("DesignScene.fxml"));
		Parent root = (Parent) loader.load();

		Main.setFrameHandle((EditPuductInformation) (loader.getController()));

		Scene scene = new Scene(root);
		primaryStage.setTitle("ZerLi");
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	private void initializeClient() {

		String host = "";
		String[] arguments = Main.getArguments();
		int port; // The port number

		try {
			host = arguments[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
		
		
		try {
			port = Integer.parseInt(arguments[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			port = Main.DEFAULT_PORT;
		}

		try {

			clientConsolHandle = new ClientConsole(host, port);

			// Successfully connect to server
			// Update GUI
			this.UpdateGuiStatusLabel("Connected " + host, "green");

			txtFldProductIdSearch.setDisable(false);
			btnSearchPID.setDisable(false);

		} catch (IOException ex) {
			System.out.println("Error: Can't setup connection! Check host and port.");

			// Update GUI
			this.UpdateGuiStatusLabel("Failed connect to the server", "red");
		}

	}

	public void searchButtonPressed() {

		String strProductID = txtFldProductIdSearch.getText();

		if (!strProductID.isEmpty() && !strProductID.matches(".*[a-z].*")) {

			PacketClass packet = new PacketClass(
					Main.SELECTCommandStatement + "ProductID,ProductName,ProductType" + Main.FROMCommmandStatement + "Products"
							+ Main.WHERECommmandStatement + "ProductID=" + strProductID,
					Main.EditProductInformationSearchBtn, Main.READ);

			clientConsolHandle.sendSqlQueryToServer(packet);
		} else {
			System.out.println("Error: Invalid product id.");

			// Update GUI
			this.UpdateGuiStatusLabel("Invalid product id", "red");
			txtFldProductChangeName.setDisable(true);
			btnChangeName.setDisable(true);
			UpdateGuiSearchProductLables(null, null, null);
		}

	}

	public void UpdateGuiStatusLabel(String msg, String color) {
		// Update GUI
		lblProductIdStatus.setText(msg);
		lblProductIdStatus.setTextFill(Paint.valueOf(color));
	}

	public void UpdateGuiSearchProductLables(String ProductId, String ProductName, String ProductType) {

		if (ProductId == null) {
			String nullStr = "---";
			
			currentPID = null;
			currentPName = null;
			currentPType = null;
			
			lblProductIdResult.setText("ProductId: " + nullStr);
			lblProductNameResult.setText("ProductName: " + nullStr);
			lblProductTypeResult.setText("Product Type: " + nullStr);
		} else {

			currentPID = ProductId;
			currentPName = ProductName;
			currentPType = ProductType;

			lblProductIdResult.setText("ProductId: " + ProductId);
			lblProductNameResult.setText("ProductName: " + ProductName);
			lblProductTypeResult.setText("Product Type: " + ProductType);
		}

	}

	public void SearchLblAndBtnEnable(boolean disable) {
		btnChangeName.setDisable(disable);
		txtFldProductChangeName.setDisable(disable);
	}

	public void ChangeBtnPressed() {

		String strProductName = txtFldProductChangeName.getText();

		if (currentPID != null && currentPName != null && currentPType != null) {
			if (!strProductName.isEmpty()) {

				PacketClass packet = new PacketClass(
						Main.UPDATECommandStatement + "Products" + Main.SETCommandStatement + "ProductName ='" + strProductName
								+ "'" + Main.WHERECommmandStatement + "ProductID=" + currentPID + ";",
						Main.EditProductInformationChangeBtn, Main.READ);

				clientConsolHandle.sendSqlQueryToServer(packet);
			} else {
				System.out.println("Error: Invalid product Name.");
				// Update GUI
				this.UpdateGuiStatusLabel("Product name empty", "red");
			}
		} else
			System.out.println("Product not found/selected");
	}

	public void EditProductInformation_SearchBtn_HandleMessageFromServer(PacketClass packet) {
		if (packet.getResults() != null) {

			ArrayList<ArrayList<String>> DataObject = (ArrayList<ArrayList<String>>) packet.getResults();

			try {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update GUI
						UpdateGuiSearchProductLables(DataObject.get(0).get(0), DataObject.get(0).get(1),
								DataObject.get(0).get(2));
						UpdateGuiStatusLabel(DataObject.get(0).get(0) + " founded", "green"); // show id
						SearchLblAndBtnEnable(false);
					}
				});
			} catch (Exception ex) {
				System.out.println("Error updating gui");
			}

		} else {
			System.out.println("Data recieve is empty");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					UpdateGuiSearchProductLables(null, null, null);
					UpdateGuiStatusLabel("Product id not exist", "red");
					SearchLblAndBtnEnable(true);
				}
			});
		}
	}

	public void EditProductInformation_ChangeBtn_HandleMessageFromServer(PacketClass packet) {

		if (packet.getSuccessSql()) {

			searchButtonPressed();
			System.out.println(currentPID + " changed");
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Update GUI
					txtFldProductChangeName.setText("");
				}
			});

		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Update GUI
					System.out.println("Error occured changing " + currentPID);
					UpdateGuiStatusLabel(currentPID + " not changed", "red"); // show id
				}
			});
		}

	}

}
