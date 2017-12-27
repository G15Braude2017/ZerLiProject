package CustomerService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class CreateSurvey {

	private static int CurrentSurveyID;
	private static int CurrentWorkerID = -1;

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

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateSurvey.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Create survey");
		stage.show();

		CreateSurvey CreateSurveyhandle = fxmlLoader.getController();
		Main.getCustomerServiceMainControl().setCreateSurveyControl(CreateSurveyhandle);
		
		initializeCreateSurvey();

	}

	
	public void initializeCreateSurvey() {

		// disable all and then try to check if can be enabled
		setGUI_CreateSurvey_Disable(true);

		// TODO get WORKERID into CurrentWorkerID
		CurrentWorkerID = 7777;

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "SurveyID" + Main.FROMCommmandStatement + "surveys_questions",
				Main.CreateSurveyInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
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

					updateStatusLabel("One or more of the questions text is empty", true);
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
						updateStatusLabel("Client connection error", true);
					}
				}

			}
		});
	}

	public void initializeGUI_SurveyIDCheck_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			if (CurrentWorkerID == -1) {
				// Invalid workerID, not changed 
				updateStatusLabel("Invalid WorkerID, log in again", true);

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
						updateStatusLabel("Invalid data format in DataBase", true);
					}
				}

				updateIDsLabels();
			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);

		}

	}

	public void addSurveyClicked_handleFromServer(PacketClass packet) {

		if (packet.getSuccessSql()) {
			updateStatusLabel("Survey " + CurrentSurveyID + " added successfuly", false);
			CurrentSurveyID++;

			updateIDsLabels();
		} else {
			updateStatusLabel("Survey " + CurrentSurveyID + " not added", true);
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

	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getLblSurveyStatus().setText(message);

				if (red_green)
					Main.getCustomerServiceMainControl().getCreateSurveyControl().getLblSurveyStatus().setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getCustomerServiceMainControl().getCreateSurveyControl().getLblSurveyStatus().setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}

	private void setGUI_CreateSurvey_Disable(boolean bool) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getTxtFldQuestion1().setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getTxtFldQuestion2().setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getTxtFldQuestion3().setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getTxtFldQuestion4().setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getTxtFldQuestion5().setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getTxtFldQuestion6().setDisable(bool);

				Main.getCustomerServiceMainControl().getCreateSurveyControl().getLblCurrentSurveyID().setDisable(bool);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getLblCurrentWorkerID().setDisable(bool);

				Main.getCustomerServiceMainControl().getCreateSurveyControl().getBtnAddSurvey().setDisable(bool);

			}
		});

	}

	private void updateIDsLabels() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getLblCurrentSurveyID().setText("SurveyID: " + CurrentSurveyID);
				Main.getCustomerServiceMainControl().getCreateSurveyControl().getLblCurrentWorkerID().setText("WorkerID: " + CurrentWorkerID);

			}
		});
	}

	public TextField getTxtFldQuestion1() {
		return txtFldQuestion1;
	}

	public Label getLblSurveyStatus() {
		return lblSurveyStatus;
	}

	public Label getLblCurrentSurveyID() {
		return lblCurrentSurveyID;
	}

	public Label getLblCurrentWorkerID() {
		return lblCurrentWorkerID;
	}

	public TextField getTxtFldQuestion2() {
		return txtFldQuestion2;
	}

	public TextField getTxtFldQuestion3() {
		return txtFldQuestion3;
	}

	public TextField getTxtFldQuestion4() {
		return txtFldQuestion4;
	}

	public TextField getTxtFldQuestion5() {
		return txtFldQuestion5;
	}

	public TextField getTxtFldQuestion6() {
		return txtFldQuestion6;
	}

	public Button getBtnAddSurvey() {
		return btnAddSurvey;
	}

	

}
