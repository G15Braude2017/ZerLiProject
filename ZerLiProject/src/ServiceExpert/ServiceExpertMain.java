package ServiceExpert;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServiceExpertMain {

private static PullSurveyResults PullSurveyResultsControl;
	
	
	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServiceExpertMain.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Service expert panel");
		stage.show();

		Main.setServiceExpertMainControl(fxmlLoader.getController());
		PullSurveyResultsControl = new PullSurveyResults();
	}


	public void click_ServiceExpertMain_btn(ActionEvent event) {
		
		((Node) event.getSource()).getScene().getWindow().hide();
		
		try {
			PullSurveyResultsControl.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static PullSurveyResults getPullSurveyResultsControl() {
		return PullSurveyResultsControl;
	}


	public static void setPullSurveyResultsControl(PullSurveyResults pullSurveyResultsControl) {
		PullSurveyResultsControl = pullSurveyResultsControl;
	}
}
