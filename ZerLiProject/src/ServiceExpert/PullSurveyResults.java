package ServiceExpert;

import java.util.ArrayList;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
	
	@FXML
	private ComboBox cbSurveyId;
	
	@FXML
	private TextArea taConclusionText;
	
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

				Main.getServiceExpertMainControl().getPullSurveyResultsControl().cbSurveyId.getItems()
						.addAll(surveyID);

			}
		});
	}

}
