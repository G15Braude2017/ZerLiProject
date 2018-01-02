package CompanyManager;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import QuarterReports.ReportsData;
import Reports.ComplaintReport;
import Reports.Reports;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class CompanyManagerReports {
	private static int CurrentStoreID = -1;
	private static int reportAmount=0;
	private static ComplaintReport complaintReport;//=new ComplaintReport();
  //  @FXML
  //  private JFXButton back_btn;
    @FXML
    private Label lblFillStatus;
    @FXML
    private JFXButton btn_send;

    @FXML
    private JFXComboBox comboBox_amount1;

    @FXML
    private JFXComboBox comboBox_StoreID1;

    @FXML
    private JFXComboBox comboBox_Rtype1;

    @FXML
    private JFXComboBox comboBox_StoreID2;

    @FXML
    private JFXComboBox comboBox_Rtype2;

    @FXML
   private JFXComboBox comboBox_Ryear1;

    @FXML
    private JFXComboBox comboBox_Rquarter1;

    @FXML
    private JFXComboBox comboBox_Ryear2;
    @FXML
    private JFXComboBox comboBox_Rquarter2;
    @FXML
    private Label label_id1;
    @FXML
    private Label label_id2;
    @FXML
    private Label label_type1;
    @FXML
    private Label label_type2;
    @FXML
    private Label label_year1;
    @FXML
    private Label label_year2;
    @FXML
    private Label label_qnum1;
    @FXML
    private Label label_qnum2;
    
    public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CompanyManagerReports.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Company Manager Reports");
		stage.show();

		CompanyManagerReports CompanyManagerReportsHandle = fxmlLoader.getController();
		Main.getCompanyManagerMainControl().setShowManagerReportsHandle(CompanyManagerReportsHandle);

		//initialize();

	}
  
    public void initialize() {
    comboBox_amount1.setItems(FXCollections.observableArrayList("One report","Two reports"));
    comboBox_Rtype1.setItems(FXCollections.observableArrayList("Income Report","Order Report","Satisfaction Report","Complaint Report"));
    comboBox_Rtype2.setItems(FXCollections.observableArrayList("Income Report","Order Report","Satisfaction Report","Complaint Report"));
    ObservableList YearList=getYear(); 
    comboBox_Ryear1.setItems(YearList);
    comboBox_Ryear2.setItems(YearList);
    ObservableList quarterNumList=FXCollections.observableArrayList(1,2,3,4);
    comboBox_Rquarter1.setItems(quarterNumList); 
    comboBox_Rquarter2.setItems(quarterNumList); 
	PacketClass packet = new PacketClass( // , store id
			Main.SELECTCommandStatement + "storeID" + Main.FROMCommmandStatement + "store",
			Main.InitializeCompanyManagerStoreIDcomboBox, Main.READ);


	try {
		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
	} catch (Exception e) {
		updateStatusLabel("Client connection error", true);
	}
    
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

	public void FillCompanyManagerStoreID_ComboBoxStoreID_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		ObservableList storeIDList=FXCollections.observableArrayList();
   
		if (packet.getSuccessSql()) {
		/*	if (CurrentStoreID == -1) {
				// Invalid ShopID, not changed
				updateStatusLabel("Invalid ShopID, log in again", true);*/

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				String SurveyIDstr;

				if (DataList == null) {
					updateStatusLabel("Couldn't get store data", true);
				} else {

					try {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								int i=0;
								for(i=0;i<DataList.size();i++)
								{
								storeIDList.add(DataList.get(i).get(0));
								}
								
								Main.getCompanyManagerMainControl().getShowManagerReportsHandle().comboBox_StoreID1.setItems(storeIDList);
								Main.getCompanyManagerMainControl().getShowManagerReportsHandle().comboBox_StoreID2.setItems(storeIDList);
							}
						});
					} catch (Exception e) {
						updateStatusLabel("store id data is invalid", true);
						CurrentStoreID = -1;
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to store data", true);
			CurrentStoreID = -1;
		}
	}
    @FXML
    void click_CompanyManager_comboBoxReportAmount(ActionEvent event) {

    	if(comboBox_amount1.getValue().toString().equals("One report"))
    	{
    		label_id2.setDisable(true);
    		comboBox_StoreID2.setDisable(true);
    		label_type2.setDisable(true);
    		comboBox_Rtype2.setDisable(true);
    		label_year2.setDisable(true);
    		comboBox_Ryear2.setDisable(true);
    		label_qnum2.setDisable(true);
    		comboBox_Rquarter2.setDisable(true);
    		label_id1.setDisable(false);
    		comboBox_StoreID1.setDisable(false);
    		label_type1.setDisable(false);
    		comboBox_Rtype1.setDisable(false);
    		label_year1.setDisable(false);
    		comboBox_Ryear1.setDisable(false);
    		label_qnum1.setDisable(false);
    		comboBox_Rquarter1.setDisable(false);
    	}
    	else
    	{
    		if(comboBox_amount1.getValue().toString().equals("Two reports"))
    		{
        		label_id1.setDisable(false);
        		comboBox_StoreID1.setDisable(false);
        		label_type1.setDisable(false);
        		comboBox_Rtype1.setDisable(false);
        		label_year1.setDisable(false);
        		comboBox_Ryear1.setDisable(false);
        		label_qnum1.setDisable(false);
        		comboBox_Rquarter1.setDisable(false);
        		label_id2.setDisable(false);
        		comboBox_StoreID2.setDisable(false);
        		label_type2.setDisable(false);
        		comboBox_Rtype2.setDisable(false);
        		label_year2.setDisable(false);
        		comboBox_Ryear2.setDisable(false);
        		label_qnum2.setDisable(false);
        		comboBox_Rquarter2.setDisable(false);
    		}
    	}
    }
    public void setComplaintReport_FromServer(PacketClass packet) {
    	ArrayList<ArrayList<String>> DataList;
 
		if (packet.getSuccessSql()) {
				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				if (DataList == null) {
					updateStatusLabel("Couldn't get complaint report data", true);
				} else {

					try {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								int i=0;
								for(i=0;i<DataList.size();i++)
								{
									complaintReport.setMonthNum(DataList.get(i).get(0));
									complaintReport.setComplaintAmount(DataList.get(i).get(1));
								}
								
							}
						});
					} catch (Exception e) {
						updateStatusLabel("complaint report data is invalid", true);
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to complaint_reports data", true);
		}
    	
    	
    }
   // @FXML
   // void click_CompanyManagerReports_backBtn2(ActionEvent event) {

   // }

    @FXML
    void click_CompanyManagerReports_btnSend(ActionEvent event) {
    	    	complaintReport=new ComplaintReport();
    	switch (this.comboBox_Rtype1.getValue().toString())
    	{
    	case "Income Report":
    		
    		break;
    	case "Order Report":
    		
    		break;
    	case "Satisfaction Report":
    		
    		break;
    	case "Complaint Report":
    		this.complaintReport.setStoreID(comboBox_StoreID1.getValue().toString());
    		this.complaintReport.setQyear(comboBox_Ryear1.getValue().toString());
    		this.complaintReport.setQnum(comboBox_Rquarter1.getValue().toString());
    		ReportsData.getComplaintReport(this.complaintReport.getStoreID(),this.complaintReport.getQyear(),this.complaintReport.getQnum());
    		OpenNewWindow();
    		 ((Node)(event.getSource())).getScene().getWindow().hide();
    		break;
    	default:
    		
    		break;
    	}
    }
	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getCompanyManagerMainControl().getShowManagerReportsHandle().lblFillStatus.setText(message);

				if (red_green)
					Main.getCompanyManagerMainControl().getShowManagerReportsHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getCompanyManagerMainControl().getShowManagerReportsHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}
	private void OpenNewWindow()
	{
        try {
    		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ZerLiProject/src/QuarterReports/ComplaintReport.fxml"));
    		Parent root1 = (Parent) fxmlLoader.load();
    		Stage stage = new Stage();
    		stage.setScene(new Scene(root1));
    		stage.setTitle("Complaint Report");
    		stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    
	}
}
