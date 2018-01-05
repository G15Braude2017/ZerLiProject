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

public class SaveSurveyConclusion extends GuiExtensions{

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

		Main.getCustomerServiceMainControl().setSaveSurveyConclusionControl((SaveSurveyConclusion) createAndDefinedFxmlWindow("SaveSurveyConclusion.fxml", "Save conclusion" ));

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
			updateStatusLabel("Client connection error", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
		}

	}

	public void initializeGUI_SaveSurveyConclusion_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;

		if (packet.getSuccessSql()) {

			DataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String SurveyIDstr;
			int i;

			if (DataList == null) {
				updateStatusLabel("Survey list is empty", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
			} else {

				setGUI_SaveSurveyConclusion_Disable(false);

				for (i = 0; i < DataList.size(); i++) {
					addComboBoxItems(DataList.get(i).get(0));
				}
			}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);

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
			updateStatusLabel("Client connection error", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
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
				updateStatusLabel("Survey conclusion is empty", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
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
			updateStatusLabel("Failed connect to surveys data", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
		}

	}

	public void click_SaveSurveyConclusion_ApproveConclusionClient() {

		if (Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().taConclusionText.getText().isEmpty())
			updateStatusLabel("Conclusion text is empty", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
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
				updateStatusLabel("Client connection error", true, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
			}
		}
	}

	public void click_SaveSurveyConclusion_ApproveConclusionServer(PacketClass packet) {

		if (packet.getSuccessSql()) {

			updateStatusLabel("Survey conclusion approved", false, Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to surveys data", true , Main.getCustomerServiceMainControl().getSaveSurveyConclusionControl().lblStatus);
		}
	}

	//////////////////////////////////
	// INTERNAL FUNCTIONS
	//////////////////////////////////

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
