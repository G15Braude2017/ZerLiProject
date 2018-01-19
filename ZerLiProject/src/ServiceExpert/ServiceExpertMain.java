package ServiceExpert;

import Catalog.ViewCatalog;
import client.GuiExtensions;
import client.GuiIF;
import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class ServiceExpertMain extends GuiExtensions{

private static PullSurveyResults PullSurveyResultsControl;
	
	//Button
	@FXML
	private Button btnLogout;
	
	public void start() throws Exception {

		Main.setServiceExpertMainControl((ServiceExpertMain) createAndDefinedFxmlWindow("ServiceExpertMain.fxml", "Service expert panel"));
		
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
	
	public void clickViewCatalog_ServiceExpertMain_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			(new ViewCatalog()).start(Main.getServiceExpertMainControl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clickLogoutButton() {
		
		logoutApplicationClient(btnLogout);
	}



	public static PullSurveyResults getPullSurveyResultsControl() {
		return PullSurveyResultsControl;
	}


	public static void setPullSurveyResultsControl(PullSurveyResults pullSurveyResultsControl) {
		PullSurveyResultsControl = pullSurveyResultsControl;
	}
}
