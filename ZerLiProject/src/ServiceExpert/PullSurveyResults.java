package ServiceExpert;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class PullSurveyResults {
	
	
	// Labels
	@FXML
	private Label lblStatus;
	
	@FXML
	private Label lblResult;
	
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
		
		
		PacketClass packet = new PacketClass( // , question1, question2, question3, question4, question5, question6
				Main.SELECTCommandStatement + "SurveyID" + Main.FROMCommmandStatement + "surveys_questions",
				Main.FillCustomerAnswersInitializeSurveyID, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}
		
	}
	
	
	
	
	
	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblStatus.setText(message);

				if (red_green)
					Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblStatus.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getServiceExpertMainControl().getPullSurveyResultsControl().lblStatus.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}
	
	

}
