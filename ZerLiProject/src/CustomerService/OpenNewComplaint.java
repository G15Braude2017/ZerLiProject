package CustomerService;

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

public class OpenNewComplaint extends GuiExtensions{

	private static String CurrentWorkerID = null;

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

		Main.getCustomerServiceMainControl().setOpenNewComplaintControl((OpenNewComplaint) createAndDefinedFxmlWindow("OpenNewComplaint.fxml", "Open complaint" ));

		initializeOpenNewComplaint();

	}

	public void initializeOpenNewComplaint() {

		setGUI_OpenNewComplaint_Disable(true);

	
		CurrentWorkerID = Main.getLoginLogicControl().getNewUser().getUserName();

		// TODO create table of customers
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "UserName" + Main.FROMCommmandStatement + "users" 
				+ Main.WHERECommmandStatement + "Permission = 2;",
				Main.OpenNewComplaintInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);
		}
	}

	public void initializeGUI_SurveyIDCheck_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i;
		String SurveyIDstr;

		setGUI_OpenNewComplaint_Disable(true);

		if (packet.getSuccessSql()) {

			if (CurrentWorkerID == null) {
				// Invalid workerID, not changed
				updateStatusLabel("Invalid WorkerID, log in again", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

				if (DataList == null) {
					updateStatusLabel("There is no customers", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

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
			updateStatusLabel("Failed connect to surveys data", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

		}

	}

	public void click_OpenNewComplaint_ComboBoxCustomerIDClient() {

		// TODO change tables info
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "OrderID" + Main.FROMCommmandStatement + "orders"
						+ Main.WHERECommmandStatement + "CustomerID = '"
						+ Main.getCustomerServiceMainControl().getOpenNewComplaintControl().cbCustomerID.getValue() + "';",
				Main.OpenNewComplaintInitializeOrderID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);
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
				updateStatusLabel("There is no orders for this customer", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

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

				updateStatusLabel("", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

				for (i = 0; i < DataList.size(); i++) {
					SurveyIDstr = DataList.get(i).get(0);

					addOrderComboBoxItems(SurveyIDstr);

				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

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
			updateStatusLabel("Client connection error", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);
		}

	}

	public void click_OpenNewComplaint_ComboBoxOrderIDServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;

			if (DataList == null) {
				updateStatusLabel("", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

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

				updateStatusLabel("There is already complaint for this order", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

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
					updateStatusLabel("Complaint text is empty", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

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
						updateStatusLabel("Client connection error", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);
					}
				}
			} else {
				updateStatusLabel("OrderID is empty", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);
			}
		} else {
			updateStatusLabel("CustomerID is empty", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);
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

			updateStatusLabel("New complaint opened", false ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);

		} else {
			// Sql command failed
			updateStatusLabel("Failed open new complaint", true ,Main.getCustomerServiceMainControl().getOpenNewComplaintControl().lblComplaintStatus);
		}
	}

	public void click_OpenNewComplaint_backBtn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		CurrentWorkerID = null;

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
