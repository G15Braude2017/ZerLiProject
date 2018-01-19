package StoreManager;

import com.jfoenix.controls.JFXButton;

import Catalog.ViewCatalog;
import SystemManager.SystemManagerMain;
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

public class StoreManagerMain extends GuiExtensions{
    
	@FXML
	private Button btnLogout;
	
	@FXML
    private JFXButton btn_createCustomer;
    @FXML
    private JFXButton btn_showReports;
    private static StoreManagerReports showStoreManagerReportsHandle;
    private static StoreManagerCreateCustomer storeManagerCreateCustomerHandle;
	public void start() throws Exception {

		Main.setStoreManagerMainControl(
				(StoreManagerMain)createAndDefinedFxmlWindow("StoreManagerMain.fxml", "Store Manager panel"));
		
		showStoreManagerReportsHandle = new StoreManagerReports();
		storeManagerCreateCustomerHandle=new StoreManagerCreateCustomer();
	}
    @FXML
    void click_StoreManagerMain_btnShowReports(ActionEvent event) {
	((Node) event.getSource()).getScene().getWindow().hide();
		
		try {
			showStoreManagerReportsHandle.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    void click_StoreManagerMain_btnCreateCustomer(ActionEvent event) {
	((Node) event.getSource()).getScene().getWindow().hide();
		
		try {
			storeManagerCreateCustomerHandle.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void clickViewCatalog_StoreManagerMain_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			(new ViewCatalog()).start(Main.getStoreManagerMainControl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void clickLogoutButton() {

		logoutApplicationClient(btnLogout);
	}

	public static StoreManagerReports getShowStoreManagerReportsHandle() {
		return showStoreManagerReportsHandle;
	}


	public static void setShowStoreManagerReportsHandle(StoreManagerReports showStoreManagerReportsHandle) {
		StoreManagerMain.showStoreManagerReportsHandle = showStoreManagerReportsHandle;
	}

	public static StoreManagerCreateCustomer getShowStoreManagerCreateCustomerHandle() {
		return storeManagerCreateCustomerHandle;
	}


	public static void setShowStoreManagerCreateCustomerHandle(StoreManagerCreateCustomer storeManagerCreateCustomerHandle) {
		StoreManagerMain.storeManagerCreateCustomerHandle = storeManagerCreateCustomerHandle;
	}

}
