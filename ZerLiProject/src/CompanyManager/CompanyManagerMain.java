package CompanyManager;

import com.jfoenix.controls.JFXButton;
import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CompanyManagerMain {
    @FXML
    private JFXButton btn_showReports;
    
	private static CompanyManagerReports showManagerReportsHandle;
	
	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CompanyManagerMain.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Company Manager panel");
		stage.show();

		Main.setCompanyManagerMainControl(fxmlLoader.getController());
		showManagerReportsHandle = new CompanyManagerReports();
	}

    @FXML
    void click_CompanyManagerMain_btnShowReports(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		
		try {
			showManagerReportsHandle.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void click_CompanyManagerReports_backBtn(ActionEvent event) {

    }

	public static CompanyManagerReports getShowManagerReportsHandle() {
		return showManagerReportsHandle;
	}


	public static void setShowManagerReportsHandle(CompanyManagerReports showManagerReportsHandle) {
		CompanyManagerMain.showManagerReportsHandle = showManagerReportsHandle;
	}

}
