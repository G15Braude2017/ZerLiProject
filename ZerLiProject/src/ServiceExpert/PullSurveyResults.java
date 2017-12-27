package ServiceExpert;

import java.util.ArrayList;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

public class PullSurveyResults {

	// Labels
	@FXML
	private Label lblStatus;

	@FXML
	private Label lblResult;

	@FXML
	private Label lblResultTxt;

	// ComboBox
	@FXML
	private ComboBox cbSurveyId;

	// TextArea
	@FXML
	private TextArea taConclusionText;

	// Button
	@FXML
	private Button btnAddConclusion;

	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PullSurveyResults.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Pull survey results");
		stage.show();

		PullSurveyResults PullSurveyResultshandle = fxmlLoader.getController();
		Main.getServiceExpertMainControl().setPullSurveyResultsControl(PullSurveyResultshandle);

		initializePullSurveyResults();

	}

	private void initializePullSurveyResults() {

		setGUI_PullSurveyResults_Disable(true);

		PacketClass packet = new PacketClass(Main.SELECTCommandStatement + " DISTINCT " + "SurveyID"
				+ Main.FROMCommmandStatement + "surveys_answers", Main.PullSurveyResultsInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}

	}

	public void initializeGUI_PullSurveyResults_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;
			int i;

			if (DataList == null) {
				updateStatusLabel("Survey list is empty", true);
			} else {

				setGUI_PullSurveyResults_Disable(false);

				for (i = 0; i < DataList.size(); i++) {
					addComboBoxItems(DataList.get(i).get(0));
				}
			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);

		}
	}

	public void click_PullSurveyResults_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			Main.getServiceExpertMainControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void click_PullSurveyResults_ComboBoxIDClient() {

		PacketClass packet = new PacketClass(Main.SELECTCommandStatement
				+ "answer1, answer2, answer3, answer4, answer5, answer6" + Main.FROMCommmandStatement
				+ "surveys_answers" + Main.WHERECommmandStatement + "SurveyID = "
				+ Integer.parseInt((String) Main.getServiceExpertMainControl().getPullSurveyResultsControl().cbSurveyId
						.getValue()),
				Main.PullSurveyResultsCheckComboBoxSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
			setGUI_PullSurveyResults_Disable(true);
		}

	}

	public void click_PullSurveyResults_ComboBoxIDServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0, c7 = 0, c8 = 0, c9 = 0, c10 = 0;
		int i, j;

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;

			if (DataList == null) {
				updateStatusLabel("Survey data is empty", true);
			} else {

				for (i = 0; i < DataList.size(); i++) {
					for (j = 0; j < 6; j++) {
						switch (DataList.get(i).get(j)) {
						case "1":
							c1++;
							break;
						case "2":
							c2++;
							break;
						case "3":
							c3++;
							break;
						case "4":
							c4++;
							break;
						case "5":
							c5++;
							break;
						case "6":
							c6++;
							break;
						case "7":
							c7++;
							break;
						case "8":
							c8++;
							break;
						case "9":
							c9++;
							break;
						case "10":
							c10++;
							break;
						}
					}
				}

				updateResultLabel(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);
		}

	}

	public void click_PullSurveyResults_AddConclusionBtn() {

		if (Main.getServiceExpertMainControl().getPullSurveyResultsControl().taConclusionText.getText().isEmpty())
			updateStatusLabel("Conclusion text is empty", true);
		else {
			PacketClass packet = new PacketClass(
					Main.UPDATECommandStatement + "surveys_questions" + Main.SETCommandStatement + "ConclusionText = '"
							+ Main.getServiceExpertMainControl().getPullSurveyResultsControl().taConclusionText
									.getText()
							+ "'" + Main.WHERECommmandStatement + "SurveyID = "
							+ Main.getServiceExpertMainControl().getPullSurveyResultsControl().cbSurveyId.getValue(),
					Main.PullSurveyResultsAddConclusionBtn, Main.WRITE);

			try {
				Main.getClientConsolHandle().sendSqlQueryToServer(packet);
			} catch (Exception e) {
				updateStatusLabel("Client connection error", true);
			}
		}
	}

	public void click_PullSurveyResults_AddConclusionServer(PacketClass packet) {

		if (packet.getSuccessSql()) {

			updateStatusLabel("Survey conclusion added", false);

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);
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
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblStatus.setText(message);

				if (red_green)
					Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblStatus
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblStatus
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}

	private void setGUI_PullSurveyResults_Disable(boolean bool) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().cbSurveyId.setDisable(bool);
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblResult.setDisable(bool);
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblResultTxt.setDisable(bool);
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().taConclusionText.setDisable(bool);
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().btnAddConclusion.setDisable(bool);

			}
		});
	}

	private void addComboBoxItems(String surveyID) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getServiceExpertMainControl().getPullSurveyResultsControl().cbSurveyId.getItems().addAll(surveyID);

			}
		});
	}

	private void updateResultLabel(int c1, int c2, int c3, int c4, int c5, int c6, int c7, int c8, int c9, int c10) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblResultTxt.setText("Ans1: " + c1
						+ "   Ans2: " + c2 + "   Ans3: " + c3 + "   Ans4: " + c4 + "   Ans5: " + c5 + "   Ans6: " + c6
						+ "   Ans7: " + c7 + "   Ans8: " + c8 + "   Ans9: " + c9 + "   Ans10: " + c10);
			}
		});
	}

}
