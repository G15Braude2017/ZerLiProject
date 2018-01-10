package StoreManager;

import java.util.ArrayList;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import CompanyManager.CompanyManagerReports;
import Reports.ComplaintReports;
import Reports.Reports;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class StoreManagerReports {
	private static int CurrentStoreID = -1;
	private static int reportAmount=0;
	private Reports report;
    @FXML
    private Label lblFillStatus;

    @FXML
    private JFXButton btn_send;

    @FXML
    private JFXComboBox<?> comboBox_Rtype1;

    @FXML
    private Label label_type1;

    @FXML
    private JFXComboBox<?> comboBox_Ryear1;

    @FXML
    private Label label_year1;

    @FXML
    private JFXComboBox<?> comboBox_Rquarter1;

    @FXML
    private Label label_qnum1;

    public void start() throws Exception {

 		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StoreManagerReports.fxml"));
 		Parent root1 = (Parent) fxmlLoader.load();
 		Stage stage = new Stage();
 		stage.setScene(new Scene(root1));
 		stage.setTitle("Store Manager Reports");
 		stage.show();

 		StoreManagerReports StoreManagerReportsHandle = fxmlLoader.getController();
 		Main.getStoreManagerMainControl().setShowStoreManagerReportsHandle(StoreManagerReportsHandle);
    }
 	    public void initialize() {
 	    	ObservableList RtypeList=FXCollections.observableArrayList("Income Report","Order Report","Satisfaction Report","Complaint Report");
 	       comboBox_Rtype1.setItems(RtypeList);
 	       ObservableList YearList=getYear(); 
 	       comboBox_Ryear1.setItems(YearList);
 	       
 	       ObservableList quarterNumList=FXCollections.observableArrayList(1,2,3,4);
 	       comboBox_Rquarter1.setItems(quarterNumList); 
 	}
    private ObservableList getYear()
    {
    	ObservableList yearList = FXCollections.observableArrayList();
    	int year = Calendar.getInstance().get(Calendar.YEAR);
    	for(int i=2000;i<=year;i++)
    	{
    		yearList.add(i);
    	}
    	
    	return yearList;
    }
/**
 * 
 * @param event
 */
    @FXML
    void click_StoreManagerReports_btnSend(ActionEvent event) {
 /*   	report=new Reports("10",this.comboBox_Ryear1.getValue().toString(),this.comboBox_Rquarter1.getValue().toString());
    	switch (this.comboBox_Rtype1.getValue().toString())
    	{
    	case "Income Report":
    		
    		break;
    	case "Order Report":
    		
    		break;
    	case "Satisfaction Report":
    		report=new ComplaintReport(report.getStoreID(),report.getQyear(),report.getQnum(),"1","12");
    		break;
    	case "Complaint Report":
    		
    		break;
    	default:
    		
    		break;
    	}*/
    }
	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getStoreManagerMainControl().getShowStoreManagerReportsHandle().lblFillStatus.setText(message);

				if (red_green)
					Main.getStoreManagerMainControl().getShowStoreManagerReportsHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getStoreManagerMainControl().getShowStoreManagerReportsHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}
}
