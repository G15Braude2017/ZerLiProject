package CompanyManager;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import QuarterReports.*;

import Reports.*;
import client.GuiExtensions;
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


public class CompanyManagerReports {
	private static int windowsCounter=0;
	private static boolean twoReportsFlag;
	private static boolean companyManager=false;
	private static ComplaintReports complaintReport;//=new ComplaintReport();
	private static ComplaintReport complaintReportHandle;
	private static IncomeReports incomeReport;
	private static IncomeReport incomeReportHandle;
	private static OrderReports orderReport;
	private static OrderReport orderReportHandle;
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
		complaintReportHandle=new ComplaintReport();
		complaintReport=new ComplaintReports();
		incomeReportHandle=new IncomeReport();
		incomeReport=new IncomeReports();
		orderReportHandle=new OrderReport();
		orderReport=new OrderReports();
		companyManager=true;
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
			Main.SELECTCommandStatement + "storeID" + Main.FROMCommmandStatement + "stores",
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
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to store data", true);
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
    		twoReportsFlag=false;
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
        		twoReportsFlag=true;
    		}
    	}
    }

    public void click_CompanyManagerReports_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();
		
		try {
			Main.getCompanyManagerMainControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    @FXML
    void click_CompanyManagerReports_btnSend(ActionEvent event) throws Exception {
    	if(windowsCounter==0)
    	{
    	    	if(twoReportsFlag==true)
    	    	{
    	    		check2ReportType( event);
    	    		windowsCounter++;
    	    		disableReportsAmount(true);
    	    	}
    	    	check1ReportType(event);
    	    	windowsCounter++;
    	}
    	else
    		updateStatusLabel("Close opened reports", true);	
    }
    private void check1ReportType(ActionEvent event) throws Exception
    {
    	switch (this.comboBox_Rtype1.getValue().toString())
    	{
    	case "Income Report":
    		this.incomeReport.setStoreID(comboBox_StoreID1.getValue().toString());
    		this.incomeReport.setQyear(comboBox_Ryear1.getValue().toString());
    		this.incomeReport.setQnum(comboBox_Rquarter1.getValue().toString());
    		if(twoReportsFlag==false)
   		 ((Node)(event.getSource())).getScene().getWindow().hide();
   		 this.incomeReportHandle.start();
    		break;
    	case "Order Report":
    		this.orderReport.setStoreID(comboBox_StoreID1.getValue().toString());
    		this.orderReport.setQyear(comboBox_Ryear1.getValue().toString());
    		this.orderReport.setQnum(comboBox_Rquarter1.getValue().toString());
    		if(twoReportsFlag==false)
   		 ((Node)(event.getSource())).getScene().getWindow().hide();
   		 this.orderReportHandle.start();
    		break;
    	case "Satisfaction Report":
    		
    		break;
    	case "Complaint Report":
  
    		this.complaintReport.setStoreID(comboBox_StoreID1.getValue().toString());
    		this.complaintReport.setQyear(comboBox_Ryear1.getValue().toString());
    		this.complaintReport.setQnum(comboBox_Rquarter1.getValue().toString());	
    		if(twoReportsFlag==false)
    		 ((Node)(event.getSource())).getScene().getWindow().hide();
    		 this.complaintReportHandle.start();

    		break;
    	default:
    		
    		break;
    	}
    }
    private void check2ReportType(ActionEvent event) throws Exception
    {
    	switch (this.comboBox_Rtype2.getValue().toString())
    	{
    	case "Income Report":
    		this.incomeReport.setStoreID(comboBox_StoreID2.getValue().toString());
    		this.incomeReport.setQyear(comboBox_Ryear2.getValue().toString());
    		this.incomeReport.setQnum(comboBox_Rquarter2.getValue().toString());
   		// ((Node)(event.getSource())).getScene().getWindow().hide();
   		 this.incomeReportHandle.start();
    		break;
    	case "Order Report":
    		this.orderReport.setStoreID(comboBox_StoreID2.getValue().toString());
    		this.orderReport.setQyear(comboBox_Ryear2.getValue().toString());
    		this.orderReport.setQnum(comboBox_Rquarter2.getValue().toString());
   		// ((Node)(event.getSource())).getScene().getWindow().hide();
   		 this.orderReportHandle.start();
    		break;
    	case "Satisfaction Report":
    		
    		break;
    	case "Complaint Report":
  
    		this.complaintReport.setStoreID(comboBox_StoreID2.getValue().toString());
    		this.complaintReport.setQyear(comboBox_Ryear2.getValue().toString());
    		this.complaintReport.setQnum(comboBox_Rquarter2.getValue().toString());	
    		// ((Node)(event.getSource())).getScene().getWindow().hide();
    		 this.complaintReportHandle.start();

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

	/**complaintReportHandle geters, setters,complaintReports entity getter
	 * 
	 * @return
	 */
	public static ComplaintReport getComplaintReportHandle() {
		return complaintReportHandle;
	}


	public static void setComplaintReportHandle(ComplaintReport showComplaintReportsHandle) {
		CompanyManagerMain.getShowManagerReportsHandle().complaintReportHandle = showComplaintReportsHandle;
	}
	
	public static ComplaintReports getComplaintReports() {
		return complaintReport;
	}
	/**
	 * incomeReportHandle getter,setter,incomeReports entity getter
	 * @return
	 */
	public static IncomeReport getIncomeReportHandle() {
		return incomeReportHandle;
	}


	public static void setIncomeReportHandle(IncomeReport showIncomeReportsHandle) {
		CompanyManagerMain.getShowManagerReportsHandle().incomeReportHandle = showIncomeReportsHandle;
	}
	public static IncomeReports getIncomeReports() {
		return incomeReport;
	}
	/**
	 * orderReportHandle getter,setter,orderReports entity getter
	 */
	public static OrderReport getOrderReportHandle() {
		return orderReportHandle;
	}


	public static void setOrderReportHandle(OrderReport showOrderReportsHandle) {
		CompanyManagerMain.getShowManagerReportsHandle().orderReportHandle = showOrderReportsHandle;
	}
	public static OrderReports getOrderReports() {
		return orderReport;
	}
	public static boolean getcompanyManager()
	{
		return companyManager;
	}
	public static int getWindowsCounter()
	{
		return windowsCounter;
	}
	public static void decWindowsCounter()
	{
		windowsCounter--;
	}
	public static boolean getTwoReportsFlag()
	{
		return twoReportsFlag;
	}
	public void disableReportsAmount(boolean flag)
	{
		this.comboBox_amount1.setDisable(flag);
	}
}
