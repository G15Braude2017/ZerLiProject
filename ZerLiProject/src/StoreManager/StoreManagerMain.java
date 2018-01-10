package StoreManager;

import com.jfoenix.controls.JFXButton;


import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StoreManagerMain {
    @FXML
    private JFXButton btn_createCustomer;
    @FXML
    private JFXButton btn_showReports;
    private static StoreManagerReports showStoreManagerReportsHandle;
    private static StoreManagerCreateCustomer storeManagerCreateCustomerHandle;
	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StoreManagerMain.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Store Manager panel");
		stage.show();

		Main.setStoreManagerMainControl(fxmlLoader.getController());
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
    @FXML
    void click_StoreManagerReports_backBtn(ActionEvent event) {

    }

	public static StoreManagerReports getShowStoreManagerReportsHandle() {
		return showStoreManagerReportsHandle;
	}


	public static void setShowStoreManagerReportsHandle(StoreManagerReports showStoreManagerReportsHandle) {
		StoreManagerMain.showStoreManagerReportsHandle = showStoreManagerReportsHandle;
	}
	/**
	 * 
	 * @return
	 */
	public static StoreManagerCreateCustomer getShowStoreManagerCreateCustomerHandle() {
		return storeManagerCreateCustomerHandle;
	}


	public static void setShowStoreManagerCreateCustomerHandle(StoreManagerCreateCustomer storeManagerCreateCustomerHandle) {
		StoreManagerMain.storeManagerCreateCustomerHandle = storeManagerCreateCustomerHandle;
	}

}
