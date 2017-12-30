package CustomerService;

import java.util.ArrayList;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class OpenNewComplaint {

	private static int CurrentWorkerID = -1;

	// ComboBox
	@FXML
	private ComboBox cbCustomerID;

	@FXML
	private ComboBox cbOrderID;

	// TextArea
	@FXML
	private TextArea taComplaintText;

	// Button
	@FXML
	private Button btnOpenNewComplaint;

	// Label
	@FXML
	private Label lblComplaintStatus;

	private EventHandler<ActionEvent> eh;

	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OpenNewComplaint.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Open complaint");
		stage.show();

		OpenNewComplaint OpenNewComplaintControl = fxmlLoader.getController();
		Main.getCustomerServiceMainControl().setOpenNewComplaintControl(OpenNewComplaintControl);

		initializeOpenNewComplaint();

	}

	public void initializeOpenNewComplaint() {

		setGUI_OpenNewComplaint_Disable(true);

		// TODO get WORKERID into CurrentWorkerID
		CurrentWorkerID = 7777;

		// TODO create table of customers
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "customer_id" + Main.FROMCommmandStatement + "customer",
				Main.OpenNewComplaintInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}
	}

	public void initializeGUI_SurveyIDCheck_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i;
		String SurveyIDstr;

		setGUI_OpenNewComplaint_Disable(true);

		if (packet.getSuccessSql()) {

			if (CurrentWorkerID == -1) {
				// Invalid workerID, not changed
				updateStatusLabel("Invalid WorkerID, log in again", true);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

				if (DataList == null) {
					updateStatusLabel("There is no customers", true);

				} else {

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbCustomerID
									.setDisable(false);
						}
					});

					for (i = 0; i < DataList.size(); i++) {
						SurveyIDstr = DataList.get(i).get(0);

						addCustomerComboBoxItems(SurveyIDstr);

					}
				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);

		}

	}

	public void click_OpenNewComplaint_ComboBoxCustomerIDClient() {

		// TODO change tables info
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "OrderID" + Main.FROMCommmandStatement + "orders"
						+ Main.WHERECommmandStatement + "CustomerID = "
						+ Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbCustomerID.getValue(),
				Main.OpenNewComplaintInitializeOrderID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}

	}

	public void click_OpenNewComplaint_ComboBoxCustomerIDServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i, j;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				eh = cbOrderID.getOnAction();
				cbOrderID.setOnAction(null);
				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.getItems().clear();
				cbOrderID.setOnAction(eh);
			}
		});

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;

			if (DataList == null) {
				updateStatusLabel("There is no orders for this customer", true);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.setDisable(true);
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText
								.setDisable(true);
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint
								.setDisable(true);
					}
				});

			} else {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.setDisable(false);
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText
								.setDisable(true);
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint
								.setDisable(true);
					}
				});

				updateStatusLabel("", true);

				for (i = 0; i < DataList.size(); i++) {
					SurveyIDstr = DataList.get(i).get(0);

					addOrderComboBoxItems(SurveyIDstr);

				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.setDisable(true);
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText.setDisable(true);
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint
							.setDisable(true);
				}
			});
		}

	}

	public void click_OpenNewComplaint_ComboBoxOrderIDClient() {

		// TODO change tables info
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "OrderID" + Main.FROMCommmandStatement + "complaints"
						+ Main.WHERECommmandStatement + "OrderID = "
						+ Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.getValue(),
				Main.OpenNewComplaintCheckComboBoxOrderID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}

	}

	public void click_OpenNewComplaint_ComboBoxOrderIDServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;

			if (DataList == null) {
				updateStatusLabel("", true);

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText
								.setDisable(false);
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint
								.setDisable(false);
					}
				});

			} else {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText
								.setDisable(true);
						Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint
								.setDisable(true);
					}
				});

				updateStatusLabel("There is already complaint for this order", true);

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText.setDisable(true);
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint
							.setDisable(true);
				}
			});
		}

	}

	public void click_OpenNewComplaint_OpenNewComplaintBtnClient() {

		if (Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbCustomerID.getValue() != null) {
			if (Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.getValue() != null) {
				if (Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText.getText()
						.isEmpty()) {
					updateStatusLabel("Complaint text is empty", true);

				} else {
					// TODO change tables info
					PacketClass packet = new PacketClass(
							Main.INSERTCommmandStatement + "complaints" + " VALUES (NULL , '"
									+ Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID
											.getValue()
									+ "','"
									+ Main.getCustomerServiceMainControl().getOpenNewComplaintControl().CurrentWorkerID
									+ "','"
									+ Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText
											.getText()
									+ "', 0 , -1 , NOW());",
							Main.OpenNewComplaintOpenComplaintBtn, Main.WRITE);

					try {
						Main.getClientConsolHandle().sendSqlQueryToServer(packet);
					} catch (Exception e) {
						updateStatusLabel("Client connection error", true);
					}
				}
			} else {
				updateStatusLabel("OrderID is empty", true);
			}
		} else {
			updateStatusLabel("CustomerID is empty", true);
		}
	}

	public void click_OpenNewComplaint_OpenNewComplaintBtnServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.setDisable(true);
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText.setDisable(true);
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint
							.setDisable(true);
				}
			});

			updateStatusLabel("New complaint opened", false);

		} else {
			// Sql command failed
			updateStatusLabel("Failed open new complaint", true);
		}
	}

	public void click_OpenNewComplaint_backBtn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		CurrentWorkerID = -1;

		try {
			Main.getCustomerServiceMainControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//////////////////////////////////
	// INTERNAL FUNCTIONS
	//////////////////////////////////

	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus.setText(message);

				if (red_green)
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}

	private void setGUI_OpenNewComplaint_Disable(boolean bool) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbCustomerID.setDisable(bool);
				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.setDisable(bool);
				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().taComplaintText.setDisable(bool);
				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().btnOpenNewComplaint.setDisable(bool);

			}
		});

	}

	private void addCustomerComboBoxItems(String surveyID) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbCustomerID.getItems()
						.addAll(surveyID);

			}
		});
	}

	private void addOrderComboBoxItems(String surveyID) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbOrderID.getItems().addAll(surveyID);

			}
		});
	}

}
