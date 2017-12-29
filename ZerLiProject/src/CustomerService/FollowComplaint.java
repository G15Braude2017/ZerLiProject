package CustomerService;



import client.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class FollowComplaint {

	// Labels
	@FXML
	private Label lblTime;

	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FollowComplaint.fxml"));
		Parent root1;
		root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Follow complaint");
		stage.show();

		FollowComplaint FollowComplaintControl = fxmlLoader.getController();
		Main.getCustomerServiceMainControl().setFollowComplaintControl(FollowComplaintControl);

		// initializeSaveSurveyConclusion();
		
		UpdateTimerComplaint utc = new UpdateTimerComplaint();
		utc.start();

	}
	
	
	public void updateTimeComplaint(String mesg , boolean red_green) {
		
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
	
	

}
