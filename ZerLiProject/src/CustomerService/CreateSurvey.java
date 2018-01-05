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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateSurvey extends GuiExtensions{

	private static int CurrentSurveyID;
	private static String CurrentWorkerID = null;

	// Labels
	@FXML
	private Label lblSurveyStatus;
	@FXML
	private Label lblCurrentSurveyID;
	@FXML
	private Label lblCurrentWorkerID;

	// Text fields
	@FXML
	private TextField txtFldQuestion1;
	@FXML
	private TextField txtFldQuestion2;
	@FXML
	private TextField txtFldQuestion3;
	@FXML
	private TextField txtFldQuestion4;
	@FXML
	private TextField txtFldQuestion5;
	@FXML
	private TextField txtFldQuestion6;

	// Buttons
	@FXML
	private Button btnAddSurvey;

	public void start() throws Exception {

		Main.getCustomerServiceMainControl().setCreateSurveyControl((CreateSurvey) createAndDefinedFxmlWindow("CreateSurvey.fxml", "Create survey" ));
		
		initializeCreateSurvey();

	}

	
	public void initializeCreateSurvey() {

		// disable all and then try to check if can be enabled
		setGUI_CreateSurvey_Disable(true);

		
		CurrentWorkerID = Main.getLoginLogicControl().getNewUser().getUserName();

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "SurveyID" + Main.FROMCommmandStatement + "surveys_questions",
				Main.CreateSurveyInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true, Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);
		}
		
	}

	public void addSurveyClicked() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String q1 = txtFldQuestion1.getText();
				String q2 = txtFldQuestion2.getText();
				String q3 = txtFldQuestion3.getText();
				String q4 = txtFldQuestion4.getText();
				String q5 = txtFldQuestion5.getText();
				String q6 = txtFldQuestion6.getText();

				if (q1.isEmpty() || q2.isEmpty() || q3.isEmpty() || q4.isEmpty() || q5.isEmpty() || q6.isEmpty()) {

					updateStatusLabel("One or more of the questions text is empty", true, Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);
				} else {

					PacketClass packet = new PacketClass(Main.INSERTCommmandStatement
							+ "surveys_questions (SurveyID, WorkerID, question1, question2, question3, question4, question5, question6, ConclusionText, ConclusionApproved) "
							+ Main.VALUESCommmandStatement + "('" + CurrentSurveyID + "','" + CurrentWorkerID + "','"
							+ q1 + "','" + q2 + "','" + q3 + "','" + q4 + "','" + q5 + "','" + q6 + "','" + "NULL"
							+ "'," + "0" + ");", Main.CreateSurveyAddSurveyBtn, Main.WRITE);

					try {
						Main.getClientConsolHandle().sendSqlQueryToServer(packet);
					} catch (Exception e) {
						System.out.println(e.toString());
						updateStatusLabel("Client connection error", true, Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);
					}
				}

			}
		});
	}

	public void initializeGUI_SurveyIDCheck_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			if (CurrentWorkerID == null) {
				// Invalid workerID, not changed 
				updateStatusLabel("Invalid WorkerID, log in again", true, Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				String SurveyIDstr;

				if (DataList == null) {
					CurrentSurveyID = 0; // first survey

					setGUI_CreateSurvey_Disable(false);
				} else {
					SurveyIDstr = DataList.get(DataList.size() - 1).get(0);

					try {
						CurrentSurveyID = Integer.parseInt(SurveyIDstr);
						CurrentSurveyID++;
						setGUI_CreateSurvey_Disable(false);
					} catch (Exception e) {
						updateStatusLabel("Invalid data format in DataBase", true, Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);
					}
				}

				updateIDsLabels();
			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true, Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);

		}

	}

	public void addSurveyClicked_handleFromServer(PacketClass packet) {

		if (packet.getSuccessSql()) {
			updateStatusLabel("Survey " + CurrentSurveyID + " added successfuly", false, Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);
			CurrentSurveyID++;

			updateIDsLabels();
		} else {
			updateStatusLabel("Survey " + CurrentSurveyID + " not added", true , Main.getCustomerServiceMainControl().getCreateSurveyControl().lblSurveyStatus);
		}

	}

	public void click_CreateSurvey_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

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

	private void setGUI_CreateSurvey_Disable(boolean bool) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getCustomerServiceMainControl().getCreateSurveyControl().txtFldQuestion1.setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().txtFldQuestion2.setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().txtFldQuestion3.setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().txtFldQuestion4.setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().txtFldQuestion5.setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().txtFldQuestion6.setDisable(bool);

				Main.getCustomerServiceMainControl().getCreateSurveyControl().lblCurrentSurveyID.setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().lblCurrentWorkerID.setDisable(bool);

				Main.getCustomerServiceMainControl().getCreateSurveyControl().btnAddSurvey.setDisable(bool);

			}
		});

	}

	private void updateIDsLabels() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getCustomerServiceMainControl().getCreateSurveyControl().lblCurrentSurveyID.setText("SurveyID: " + CurrentSurveyID);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().lblCurrentWorkerID.setText("WorkerID: " + CurrentWorkerID);

			}
		});
	}
	

}
