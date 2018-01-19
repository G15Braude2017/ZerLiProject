package CompanyManager;

import com.jfoenix.controls.JFXButton;

import Catalog.ViewCatalog;
import client.GuiExtensions;
import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CompanyManagerMain extends GuiExtensions{
    
	@FXML
	private Button btnLogout;
	
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

    public void clickViewCatalog_CompanyManagerMain_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			(new ViewCatalog()).start(Main.getCompanyManagerMainControl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void clickLogoutButton() {

		logoutApplicationClient(btnLogout);
	}

	public static CompanyManagerReports getShowManagerReportsHandle() {
		return showManagerReportsHandle;
	}


	public static void setShowManagerReportsHandle(CompanyManagerReports showManagerReportsHandle) {
		CompanyManagerMain.showManagerReportsHandle = showManagerReportsHandle;
	}

}
