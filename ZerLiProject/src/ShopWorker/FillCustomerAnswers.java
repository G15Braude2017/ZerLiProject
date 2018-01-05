package ShopWorker;

import java.util.ArrayList;
import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class FillCustomerAnswers extends GuiExtensions{

	private static int CurrentSurveyID = -1;
	private static int CurrentStoreID = -1;

	// flags for unchecked answers
	private static boolean a1 = false, a2 = false, a3 = false, a4 = false, a5 = false, a6 = false;

	// Labels
	@FXML
	private Label lblQ1;
	@FXML
	private Label lblQ2;
	@FXML
	private Label lblQ3;
	@FXML
	private Label lblQ4;
	@FXML
	private Label lblQ5;
	@FXML
	private Label lblQ6;
	@FXML
	private Label lblFillStatus;

	// ComboBox
	@FXML
	private ComboBox cbQ1;
	@FXML
	private ComboBox cbQ2;
	@FXML
	private ComboBox cbQ3;
	@FXML
	private ComboBox cbQ4;
	@FXML
	private ComboBox cbQ5;
	@FXML
	private ComboBox cbQ6;

	// Buttons
	@FXML
	private Button btnAddAnswers;

	// ComboBox
	@FXML
	private ComboBox cbChooseSurveyID;

	public void start() throws Exception {

		Main.getShopWorkerMainControl().setFillCustomerAnswersHandle((FillCustomerAnswers) createAndDefinedFxmlWindow("FillCustomerAnswers.fxml", "Fill customer answers"));

		initializeFillCustomerAnswers();

	}

	public void initializeFillCustomerAnswers() {

		// disable all and then try to check if can be enabled
		setGUI_FillCustomerAnswers_Disable(true);

		// config all choise box with 1-10 answers
		setGUI_FillCustomerAnswers_ChoiseBoxCong();

		if(Main.getLoginLogicControl().getNewUser().getStoreID() != -1)
		{
		CurrentStoreID = Main.getLoginLogicControl().getNewUser().getStoreID();

		PacketClass packet = new PacketClass( // , question1, question2, question3, question4, question5, question6
				Main.SELECTCommandStatement + "SurveyID" + Main.FROMCommmandStatement + "surveys_questions",
				Main.FillCustomerAnswersInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true , Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
		}
		}else
			updateStatusLabel("Worker not connected to valid store", true , Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);

	}

	public void initializeGUI_GetSurveysID_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			if (CurrentStoreID == -1) {
				// Invalid ShopID, not changed
				updateStatusLabel("Invalid ShopID, log in again", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				String SurveyIDstr;
				int i;

				if (DataList == null) {
					updateStatusLabel("Survey list is empty", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
				} else {

					setGUI_FillCustomerAnswers_Disable(false);

					for (i = 0; i < DataList.size(); i++) {
						addComboBoxItems(DataList.get(i).get(0));
					}
				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);

		}
	}

	public void click_FillCustomerAnswers_ComboBoxIDClient() {

		CurrentSurveyID = Integer.parseInt((String) Main.getShopWorkerMainControl().getFillCustomerAnswersHandle()
				.cbChooseSurveyID.getValue());

		PacketClass packet = new PacketClass(Main.SELECTCommandStatement
				+ "question1, question2, question3, question4, question5, question6" + Main.FROMCommmandStatement
				+ "surveys_questions" + Main.WHERECommmandStatement + "SurveyID = " + (String) Main
						.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbChooseSurveyID.getValue(),
				Main.FillCustomerAnswersCheckComboBox, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
			setGUI_FillCustomerAnswers_Disable(true);
		}

	}

	public void click_FillCustomerAnswers_ComboBoxID_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {
			if (CurrentStoreID == -1) {
				// Invalid ShopID, not changed
				updateStatusLabel("Invalid ShopID, log in again", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				String SurveyIDstr;

				if (DataList == null) {
					updateStatusLabel("Survey data incomplete", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
				} else {

					try {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ1
										.setText(DataList.get(0).get(0));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ2
										.setText(DataList.get(0).get(1));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ3
										.setText(DataList.get(0).get(2));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ4
										.setText(DataList.get(0).get(3));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ5
										.setText(DataList.get(0).get(4));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ6
										.setText(DataList.get(0).get(5));
							}
						});
					} catch (Exception e) {
						updateStatusLabel("Survey question data is invalid", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
						CurrentSurveyID = -1;
					}
				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
			CurrentSurveyID = -1;
		}
	}

	public void click_FillCustomerAnswers_SaveAnswersBtnClient() {

		if (CurrentSurveyID == -1 || CurrentStoreID == -1) {
			updateStatusLabel("Invalid SurveyID or StoreId", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
		} else {
			if (a1 && a2 && a3 && a4 && a5 && a6) {
				PacketClass packet = new PacketClass(Main.INSERTCommmandStatement + "surveys_answers"
						+ Main.VALUESCommmandStatement + "(" + CurrentSurveyID + "," + CurrentStoreID + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ1.getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ2.getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ3.getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ4.getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ5.getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ6.getValue() + ", CURDATE());",
						Main.FillCustomerAnswersAddSurveyAnswersBtn, Main.WRITE);

				try {
					Main.getClientConsolHandle().sendSqlQueryToServer(packet);
				} catch (Exception e) {
					updateStatusLabel("Client connection error", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
				}
			} else
				updateStatusLabel("Not all questions answered", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
		}
	}

	public void click_FillCustomerAnswers_SaveAnswersBtn_FromServer(PacketClass packet) {

		if (packet.getSuccessSql())
			updateStatusLabel("Save answers for " + CurrentSurveyID + " succeed", false, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
		else
			updateStatusLabel("Save answers for " + CurrentSurveyID + " failed", true, Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblFillStatus);
	}
	
	
	public void click_FillCustomerAnswers_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		CurrentSurveyID = -1;
		CurrentStoreID = -1;
		a1 = false; 
		a2 = false; 
		a3 = false; 
		a4 = false; 
		a5 = false; 
		a6 = false;
		
		try {
			Main.getShopWorkerMainControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	//////////////////////////////////
	// INTERNAL FUNCTIONS
	//////////////////////////////////

	private void setGUI_FillCustomerAnswers_Disable(boolean bool) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ1.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ2.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ3.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ4.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ5.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().lblQ6.setDisable(bool);

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ1.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ2.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ3.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ4.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ5.setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ6.setDisable(bool);

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().btnAddAnswers.setDisable(bool);

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbChooseSurveyID.setDisable(bool);

			}
		});
	}

	private void addComboBoxItems(String surveyID) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbChooseSurveyID.getItems()
						.addAll(surveyID);

			}
		});
	}

	private void setGUI_FillCustomerAnswers_ChoiseBoxCong() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ1
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ2
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ3
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ4
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ5
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().cbQ6
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
			}
		});
	}

	public void ComboBox1() {
		a1 = true;
	}

	public void ComboBox2() {
		a2 = true;
	}

	public void ComboBox3() {
		a3 = true;
	}

	public void ComboBox4() {
		a4 = true;
	}

	public void ComboBox5() {
		a5 = true;
	}
	
	public void ComboBox6() {
		a6 = true;
	}

}
