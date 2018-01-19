package StoreManager;

import java.util.ArrayList;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import CompanyManager.CompanyManagerMain;
import CompanyManager.CompanyManagerReports;
import QuarterReports.ComplaintReport;
import QuarterReports.IncomeReport;
import QuarterReports.OrderReport;
import Reports.ComplaintReports;
import Reports.IncomeReports;
import Reports.OrderReports;
import Reports.Reports;
import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class StoreManagerReports extends GuiExtensions{
	private static ComplaintReports complaintReport;//=new ComplaintReport();
	private static ComplaintReport complaintReportHandle;
	private static IncomeReports incomeReport;
	private static IncomeReport incomeReportHandle;
	private static OrderReports orderReport;
	private static OrderReport orderReportHandle;
	private static boolean storeManager=false;
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

    	Main.getStoreManagerMainControl().setShowStoreManagerReportsHandle((StoreManagerReports) createAndDefinedFxmlWindow("StoreManagerReports.fxml","Store Manager Reports"));
		complaintReportHandle=new ComplaintReport();
		complaintReport=new ComplaintReports();
		incomeReportHandle=new IncomeReport();
		incomeReport=new IncomeReports();
		orderReportHandle=new OrderReport();
		orderReport=new OrderReports();
		storeManager=true;
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
 * @throws Exception 
 */
 
    @FXML
    void click_StoreManagerReports_btnSend(ActionEvent event) throws Exception {
    	switch (this.comboBox_Rtype1.getValue().toString())
    	{
    	case "Income Report":
    		this.incomeReport.setStoreID("1");
    		this.incomeReport.setQyear(comboBox_Ryear1.getValue().toString());
    		this.incomeReport.setQnum(comboBox_Rquarter1.getValue().toString());
   		 ((Node)(event.getSource())).getScene().getWindow().hide();
   		 this.incomeReportHandle.start();
    		break;
    	case "Order Report":
    		this.orderReport.setStoreID("1");
    		this.orderReport.setQyear(comboBox_Ryear1.getValue().toString());
    		this.orderReport.setQnum(comboBox_Rquarter1.getValue().toString());
   		 ((Node)(event.getSource())).getScene().getWindow().hide();
   		 this.orderReportHandle.start();
    		break;
    	case "Satisfaction Report":
    		
    		break;
    	case "Complaint Report":
  
    		this.complaintReport.setStoreID("1");
    		this.complaintReport.setQyear(comboBox_Ryear1.getValue().toString());
    		this.complaintReport.setQnum(comboBox_Rquarter1.getValue().toString());	
    		 ((Node)(event.getSource())).getScene().getWindow().hide();
    		 this.complaintReportHandle.start();

    		break;
    	default:
    		
    		break;
    	}
    }
    @FXML
    void click_StoreManagerReports_backBtn(ActionEvent event) throws Exception {
    	 ((Node)(event.getSource())).getScene().getWindow().hide();
    	 Main.getStoreManagerMainControl().start();
    }
	
	/**complaintReportHandle geters, setters,complaintReports entity getter
	 * 
	 * @return
	 */
	public static ComplaintReport getComplaintReportHandle() {
		return complaintReportHandle;
	}


	public static void setComplaintReportHandle(ComplaintReport showComplaintReportsHandle) {
		StoreManagerMain.getShowStoreManagerReportsHandle().complaintReportHandle = showComplaintReportsHandle;
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
		StoreManagerMain.getShowStoreManagerReportsHandle().incomeReportHandle = showIncomeReportsHandle;
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
		StoreManagerMain.getShowStoreManagerReportsHandle().orderReportHandle = showOrderReportsHandle;
	}
	public static OrderReports getOrderReports() {
		return orderReport;
	}
	public static boolean getcompanyManager()
	{
		return storeManager;
	}
}
