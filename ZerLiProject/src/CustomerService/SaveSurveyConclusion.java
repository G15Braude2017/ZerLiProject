package CustomerService;

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

public class SaveSurveyConclusion {

	// Labels
	@FXML
	private Label lblStatus;

	// ComboBox
	@FXML
	private ComboBox cbSurveyId;

	// TextArea
	@FXML
	private TextArea taConclusionText;

	// Button
	@FXML
	private Button btnApproveConclusion;

	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SaveSurveyConclusion.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Save conclusion");
		stage.show();

		SaveSurveyConclusion SaveSurveyConclusionControl = fxmlLoader.getController();
		Main.getCustomerServiceMainControl().setSaveSurveyConclusionControl(SaveSurveyConclusionControl);

		initializeSaveSurveyConclusion();

	}

	private void initializeSaveSurveyConclusion() {

		setGUI_SaveSurveyConclusion_Disable(true);

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + " DISTINCT " + "SurveyID" + Main.FROMCommmandStatement
						+ "surveys_questions" + Main.WHERECommmandStatement + "ConclusionText != 'NULL'",
				Main.SaveSurveyConclusionCheckComboBoxSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}

	}

	public void initializeGUI_SaveSurveyConclusion_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;
			int i;

			if (DataList == null) {
				updateStatusLabel("Survey list is empty", true);
			} else {

				setGUI_SaveSurveyConclusion_Disable(false);

				for (i = 0; i < DataList.size(); i++) {
					addComboBoxItems(DataList.get(i).get(0));
				}
			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);

		}
	}

	public void click_SaveSurveyConclusion_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			Main.getCustomerServiceMainControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void click_SaveSurveyConclusion_ComboBoxIDClient() {

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "ConclusionText" + Main.FROMCommmandStatement + "surveys_questions"
						+ Main.WHERECommmandStatement + "SurveyID = "
						+ Integer.parseInt((String) Main.getCustomerServiceMainControl()
								.getSaveSurveyConclusionControl().cbSurveyId.getValue()),
				Main.SaveSurveyConclusionGetConclusionText, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
			setGUI_SaveSurveyConclusion_Disable(true);
		}

	}

	public void click_SaveSurveyConclusion_ComboBoxIDServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		int i, j;

		if (packet.getSuccessSql()) {
			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;

			if (DataList == null) {
				updateStatusLabel("Survey conclusion is empty", true);
			} else {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update GUI
						Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().taConclusionText
								.setText(DataList.get(0).get(0));

					}
				});

			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true);
		}

	}

	public void click_SaveSurveyConclusion_ApproveConclusionClient() {

		if (Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().taConclusionText.getText().isEmpty())
			updateStatusLabel("Conclusion text is empty", true);
		else {
			PacketClass packet = new PacketClass(Main.UPDATECommandStatement + "surveys_questions"
					+ Main.SETCommandStatement + "ConclusionApproved = 1" + Main.WHERECommmandStatement + "SurveyID = '"
					+ Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().cbSurveyId.getValue()
					+ "' AND ConclusionText = '"
					+ Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().taConclusionText.getText()
					+ "'", Main.SaveSurveyConclusionApproveConclusionBtn, Main.WRITE);

			try {
				Main.getClientConsolHandle().sendSqlQueryToServer(packet);
			} catch (Exception e) {
				updateStatusLabel("Client connection error", true);
			}
		}
	}

	public void click_SaveSurveyConclusion_ApproveConclusionServer(PacketClass packet) {

		if (packet.getSuccessSql()) {

			updateStatusLabel("Survey conclusion approved", false);

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
				Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus.setText(message);

				if (red_green)
					Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}

	private void setGUI_SaveSurveyConclusion_Disable(boolean bool) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().cbSurveyId.setDisable(bool);
				Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().taConclusionText.setDisable(bool);
				Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().btnApproveConclusion
						.setDisable(bool);

			}
		});
	}

	private void addComboBoxItems(String surveyID) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().cbSurveyId.getItems()
						.addAll(surveyID);

			}
		});
	}

	
}
