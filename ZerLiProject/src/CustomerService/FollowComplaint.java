package CustomerService;

import java.util.ArrayList;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public class FollowComplaint extends GuiExtensions {

	private static String CurrentWorkerID = null;
	private static UpdateTimerComplaint UpdateTimerComplaintControl = null;
	private static boolean complaintAlreadyCompensated;

	// Labels
	@FXML
	private Label lblTime;

	@FXML
	private Label lblStatus;

	// ComboBox
	@FXML
	private ComboBox cbOrderID;

	@FXML
	private ComboBox cbComplaintStatus;

	// Text Fields
	@FXML
	private TextField tfCompensationAmount;

	// TextArea
	@FXML
	private TextArea taComplaintText;

	// Button
	@FXML
	private Button btnConfirmChanges;

	public void start() throws Exception {

		Main.getCustomerServiceMainControl().setFollowComplaintControl(
				(FollowComplaint) createAndDefinedFxmlWindow("FollowComplaint.fxml", "Follow complaint"));

		initializeFollowComplaint();

	}

	public void initializeFollowComplaint() {

		complaintAlreadyCompensated = false;

		setGUI_OpenNewComplaint_Disable(true);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getCustomerServiceMainControl().getFollowComplaintControl().cbOrderID.setDisable(true);
				Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus.getItems().addAll(0);
				Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus.getItems().addAll(1);
			}
		});

		
			CurrentWorkerID = Main.getLoginLogicControl().getNewUser().getUserName();

			PacketClass packet = new PacketClass(
					Main.SELECTCommandStatement + "OrderID" + Main.FROMCommmandStatement + "complaints"
							+ Main.WHERECommmandStatement + "WorkerID = '" + CurrentWorkerID + "';",
					Main.FollowComplaintInitializeOrderID, Main.READ);

			try {
				Main.getClientConsolHandle().sendSqlQueryToServer(packet);
			} catch (Exception e) {
				updateStatusLabel("Client connection error", true,
						Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);
			}
		
	}

	public void initializeGUI_OrderIDCheck_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i;
		String SurveyIDstr;

		if (packet.getSuccessSql()) {

			if (CurrentWorkerID == null) {
				// Invalid workerID, not changed
				updateStatusLabel("Invalid WorkerID, log in again", true,
						Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

				if (DataList == null) {
					updateStatusLabel("There is no complaints", true,
							Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);

				} else {

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Main.getCustomerServiceMainControl().getFollowComplaintControl().cbOrderID
									.setDisable(false);
						}
					});

					for (i = 0; i < DataList.size(); i++) {
						SurveyIDstr = DataList.get(i).get(0);

						addOrderComboBoxItems(SurveyIDstr);

					}
				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true,
					Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);

		}

	}

	public void click_FollowComplaint_ComboBoxOrderIDClient() {

		// Stop timer of present complaint if exist
		if (UpdateTimerComplaintControl != null) {
			try {
				UpdateTimerComplaintControl.setTimerFlag(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		complaintAlreadyCompensated = false;

		// TODO change tables info
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "State, AddingDate, CompansationAmount, ComplaintText"
						+ Main.FROMCommmandStatement + "complaints" + Main.WHERECommmandStatement + "OrderID = "
						+ Main.getCustomerServiceMainControl().getFollowComplaintControl().cbOrderID.getValue()
						+ " AND WorkerID = '" + CurrentWorkerID + "';",
				Main.FollowComplaintCheckComboBoxOrderID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true,
					Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);
		}

	}

	public void click_FollowComplaint_ComboBoxOrderIDServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;

			if (DataList == null) {
				updateStatusLabel("Invalid complaint data", true,
						Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);

				setGUI_OpenNewComplaint_Disable(true);

			} else {

				setGUI_OpenNewComplaint_Disable(false);

				// Complaint Status is closed (1)
				if (DataList.get(0).get(0).equals("1")) {

					updateTimeComplaint("Complaint closed", false);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							// Update GUI
							Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus
									.getSelectionModel().select("1");
						}
					});

				} else // Complaint Status is open (0)
				{

					UpdateTimerComplaint utc = new UpdateTimerComplaint(DataList.get(0).get(1));
					UpdateTimerComplaintControl = utc;
					utc.start();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							// Update GUI
							Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus
									.getSelectionModel().select("0");
						}
					});
				}

				// Compensation Amount
				if (!DataList.get(0).get(2).equals("-1")) {

					complaintAlreadyCompensated = true;

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							// Update GUI
							Main.getCustomerServiceMainControl().getFollowComplaintControl().tfCompensationAmount
									.setText(DataList.get(0).get(2));
							Main.getCustomerServiceMainControl().getFollowComplaintControl().tfCompensationAmount
									.setDisable(true);
						}
					});
				}

				// Complaint TEXT
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update GUI
						Main.getCustomerServiceMainControl().getFollowComplaintControl().taComplaintText
								.setText(DataList.get(0).get(3));
					}
				});

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true,
					Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);

			setGUI_OpenNewComplaint_Disable(true);
		}

	}

	public void click_FollowComplaint_ConfirmChangesBtnClient() {

		String sqlCommandStr = null;

		if (Main.getCustomerServiceMainControl().getFollowComplaintControl().cbOrderID.getValue() != null) {
			if (Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus.getValue() != null) {
				if (Main.getCustomerServiceMainControl().getFollowComplaintControl().taComplaintText.getText()
						.isEmpty()) {
					updateStatusLabel("Complaint text is empty", true,
							Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);

				} else {

					if (complaintAlreadyCompensated) {
						// TODO change tables info
						sqlCommandStr = Main.UPDATECommandStatement + "complaints" + Main.SETCommandStatement
								+ "State = "
								+ Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus
										.getValue()
								+ " , ComplaintText = '"
								+ Main.getCustomerServiceMainControl().getFollowComplaintControl().taComplaintText
										.getText()
								+ "' " + Main.WHERECommmandStatement + " OrderID = "
								+ Main.getCustomerServiceMainControl().getFollowComplaintControl().cbOrderID.getValue();
					} else {
						if (Main.getCustomerServiceMainControl().getFollowComplaintControl().tfCompensationAmount
								.getText().isEmpty())
							updateStatusLabel("Compensation amount is empty", true,
									Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);
						else {
							// TODO change tables info
							sqlCommandStr = Main.UPDATECommandStatement + "complaints" + Main.SETCommandStatement
									+ "State = "
									+ Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus
											.getValue()
									+ " , CompansationAmount = "
									+ Main.getCustomerServiceMainControl()
											.getFollowComplaintControl().tfCompensationAmount.getText()
									+ " , ComplaintText = '"
									+ Main.getCustomerServiceMainControl().getFollowComplaintControl().taComplaintText
											.getText()
									+ "' " + Main.WHERECommmandStatement + " OrderID = "
									+ Main.getCustomerServiceMainControl().getFollowComplaintControl().cbOrderID
											.getValue();
						}
					}

					if (sqlCommandStr != null) {
						PacketClass packet = new PacketClass(sqlCommandStr, Main.FollowComplaintConfirmComplaintBtn,
								Main.WRITE);

						try {
							Main.getClientConsolHandle().sendSqlQueryToServer(packet);
						} catch (Exception e) {
							updateStatusLabel("Client connection error", true,
									Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);
						}
					}
				}
			} else {
				updateStatusLabel("Complaint status is empty", true,
						Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);
			}
		} else {
			updateStatusLabel("OrderID is empty", true,
					Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);
		}
	}

	public void click_FollowComplaint_ConfirmChangesBtnServer(PacketClass packet) {

		if (packet.getSuccessSql()) {

			complaintAlreadyCompensated = false;

			setGUI_OpenNewComplaint_Disable(true);

			updateStatusLabel("Complaint updated", false,
					Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);

		} else {
			// Sql command failed
			updateStatusLabel("Failed update complaint", true,
					Main.getCustomerServiceMainControl().getFollowComplaintControl().lblStatus);
		}
	}

	public void updateTimeComplaint(String mesg, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				lblTime.setText(mesg);

				if (red_green)
					lblTime.setTextFill(Paint.valueOf(Main.RED));
				else
					lblTime.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}

	public void click_FollowComplaint_backBtn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		CurrentWorkerID = null;
		complaintAlreadyCompensated = false;

		// Close timer thread
		if (UpdateTimerComplaintControl != null) {
			try {
				UpdateTimerComplaintControl.setTimerFlag(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
				Main.getCustomerServiceMainControl().getFollowComplaintControl().cbComplaintStatus.setDisable(bool);
				Main.getCustomerServiceMainControl().getFollowComplaintControl().tfCompensationAmount.setDisable(bool);
				Main.getCustomerServiceMainControl().getFollowComplaintControl().taComplaintText.setDisable(bool);
				Main.getCustomerServiceMainControl().getFollowComplaintControl().btnConfirmChanges.setDisable(bool);

			}
		});

	}

	private void addOrderComboBoxItems(String surveyID) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getCustomerServiceMainControl().getFollowComplaintControl().cbOrderID.getItems().addAll(surveyID);

			}
		});
	}

}
