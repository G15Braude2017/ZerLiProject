package ShopWorker;

import java.util.ArrayList;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class FillCustomerAnswers {

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

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FillCustomerAnswers.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Fill customer answers");
		stage.show();

		FillCustomerAnswers FillCustomerAnswershandle = fxmlLoader.getController();
		Main.getShopWorkerMainControl().setFillCustomerAnswersHandle(FillCustomerAnswershandle);

		initializeFillCustomerAnswers();

	}

	public void initializeFillCustomerAnswers() {

		// disable all and then try to check if can be enabled
		setGUI_FillCustomerAnswers_Disable(true);

		// config all choise box with 1-10 answers
		setGUI_FillCustomerAnswers_ChoiseBoxCong();

		// TODO get StoreID into CurrentStoreID
		CurrentStoreID = 7777;

		PacketClass packet = new PacketClass( // , question1, question2, question3, question4, question5, question6
				Main.SELECTCommandStatement + "SurveyID" + Main.FROMCommmandStatement + "surveys_questions",
				Main.FillCustomerAnswersInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}

	}

	public void initializeGUI_GetSurveysID_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			if (CurrentStoreID == -1) {
				// Invalid ShopID, not changed
				updateStatusLabel("Invalid ShopID, log in again", true);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				String SurveyIDstr;
				int i;

				if (DataList == null) {
					updateStatusLabel("Survey list is empty", true);
				} else {

					setGUI_FillCustomerAnswers_Disable(false);

					for (i = 0; i < DataList.size(); i++) {
						addComboBoxItems(DataList.get(i).get(0));
					}
				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);

		}
	}

	public void click_FillCustomerAnswers_ComboBoxIDClient() {

		CurrentSurveyID = Integer.parseInt((String) Main.getShopWorkerMainControl().getFillCustomerAnswersHandle()
				.getCbChooseSurveyID().getValue());

		PacketClass packet = new PacketClass(Main.SELECTCommandStatement
				+ "question1, question2, question3, question4, question5, question6" + Main.FROMCommmandStatement
				+ "surveys_questions" + Main.WHERECommmandStatement + "SurveyID = " + (String) Main
						.getShopWorkerMainControl().getFillCustomerAnswersHandle().getCbChooseSurveyID().getValue(),
				Main.FillCustomerAnswersCheckComboBox, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
			setGUI_FillCustomerAnswers_Disable(true);
		}

	}

	public void click_FillCustomerAnswers_ComboBoxID_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {
			if (CurrentStoreID == -1) {
				// Invalid ShopID, not changed
				updateStatusLabel("Invalid ShopID, log in again", true);

			} else {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				String SurveyIDstr;

				if (DataList == null) {
					updateStatusLabel("Survey data incomplete", true);
				} else {

					try {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ1()
										.setText(DataList.get(0).get(0));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ2()
										.setText(DataList.get(0).get(1));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ3()
										.setText(DataList.get(0).get(2));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ4()
										.setText(DataList.get(0).get(3));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ5()
										.setText(DataList.get(0).get(4));
								Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ6()
										.setText(DataList.get(0).get(5));
							}
						});
					} catch (Exception e) {
						updateStatusLabel("Survey question data is invalid", true);
						CurrentSurveyID = -1;
					}
				}

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);
			CurrentSurveyID = -1;
		}
	}

	public void click_FillCustomerAnswers_SaveAnswersBtnClient() {

		if (CurrentSurveyID == -1 || CurrentStoreID == -1) {
			updateStatusLabel("Invalid SurveyID or StoreId", true);
		} else {
			if (a1 && a2 && a3 && a4 && a5 && a6) {
				PacketClass packet = new PacketClass(Main.INSERTCommmandStatement + "surveys_answers"
						+ Main.VALUESCommmandStatement + "(" + CurrentSurveyID + "," + CurrentStoreID + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ1().getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ2().getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ3().getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ4().getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ5().getValue() + ","
						+ Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ6().getValue() + ");",
						Main.FillCustomerAnswersAddSurveyAnswersBtn, Main.WRITE);

				try {
					Main.getClientConsolHandle().sendSqlQueryToServer(packet);
				} catch (Exception e) {
					updateStatusLabel("Client connection error", true);
				}
			} else
				updateStatusLabel("Not all questions answered", true);
		}
	}

	public void click_FillCustomerAnswers_SaveAnswersBtn_FromServer(PacketClass packet) {

		if (packet.getSuccessSql())
			updateStatusLabel("Save answers for " + CurrentSurveyID + " succeed", false);
		else
			updateStatusLabel("Save answers for " + CurrentSurveyID + " failed", true);
	}
	
	
	public void click_FillCustomerAnswers_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

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
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ1().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ2().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ3().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ4().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ5().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblQ6().setDisable(bool);

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ1().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ2().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ3().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ4().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ5().setDisable(bool);
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ6().setDisable(bool);

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getBtnAddAnswers().setDisable(bool);

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getCbChooseSurveyID().setDisable(bool);

			}
		});
	}

	private void addComboBoxItems(String surveyID) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getCbChooseSurveyID().getItems()
						.addAll(surveyID);

			}
		});
	}

	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblFillStatus().setText(message);

				if (red_green)
					Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblFillStatus()
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getLblFillStatus()
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}

	private void setGUI_FillCustomerAnswers_ChoiseBoxCong() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ1()
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ2()
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ3()
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ4()
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ5()
						.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
				Main.getShopWorkerMainControl().getFillCustomerAnswersHandle().getcbQ6()
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

	public Label getLblQ1() {
		return lblQ1;
	}

	public void setLblQ1(Label lblQ1) {
		this.lblQ1 = lblQ1;
	}

	public Label getLblQ2() {
		return lblQ2;
	}

	public void setLblQ2(Label lblQ2) {
		this.lblQ2 = lblQ2;
	}

	public Label getLblQ3() {
		return lblQ3;
	}

	public void setLblQ3(Label lblQ3) {
		this.lblQ3 = lblQ3;
	}

	public Label getLblQ4() {
		return lblQ4;
	}

	public void setLblQ4(Label lblQ4) {
		this.lblQ4 = lblQ4;
	}

	public Label getLblQ5() {
		return lblQ5;
	}

	public void setLblQ5(Label lblQ5) {
		this.lblQ5 = lblQ5;
	}

	public Label getLblQ6() {
		return lblQ6;
	}

	public void setLblQ6(Label lblQ6) {
		this.lblQ6 = lblQ6;
	}

	public ComboBox getcbQ1() {
		return cbQ1;
	}

	public void setcbQ1(ComboBox cbQ1) {
		this.cbQ1 = cbQ1;
	}

	public ComboBox getcbQ2() {
		return cbQ2;
	}

	public void setcbQ2(ComboBox cbQ2) {
		this.cbQ2 = cbQ2;
	}

	public ComboBox getcbQ3() {
		return cbQ3;
	}

	public void setcbQ3(ComboBox cbQ3) {
		this.cbQ3 = cbQ3;
	}

	public ComboBox getcbQ4() {
		return cbQ4;
	}

	public void setcbQ4(ComboBox cbQ4) {
		this.cbQ4 = cbQ4;
	}

	public ComboBox getcbQ5() {
		return cbQ5;
	}

	public void setcbQ5(ComboBox cbQ5) {
		this.cbQ5 = cbQ5;
	}

	public ComboBox getcbQ6() {
		return cbQ6;
	}

	public ComboBox getCbChooseSurveyID() {
		return cbChooseSurveyID;
	}

	public void setCbChooseSurveyID(ComboBox cbChooseSurveyID) {
		this.cbChooseSurveyID = cbChooseSurveyID;
	}

	public void setcbQ6(ComboBox cbQ6) {
		this.cbQ6 = cbQ6;
	}

	public Button getBtnAddAnswers() {
		return btnAddAnswers;
	}

	public void setBtnAddAnswers(Button btnAddAnswers) {
		this.btnAddAnswers = btnAddAnswers;
	}

	public Label getLblFillStatus() {
		return lblFillStatus;
	}

	public void setLblFillStatus(Label lblFillStatus) {
		this.lblFillStatus = lblFillStatus;
	}

}
