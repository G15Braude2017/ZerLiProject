package QuarterReports;

import java.util.ArrayList;

import Reports.ComplaintReports;
import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class ComplaintReport extends GuiExtensions{
	public static boolean actorflag;
	public static ComplaintReports data;
    @FXML
    private Label lblFillStatus;
    @FXML
    private StackedBarChart ComplaintChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
    public void start() throws Exception {
    	if (Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getcompanyManager()==true)
    	{
    		actorflag=true;
		Main.getCompanyManagerMainControl().getShowManagerReportsHandle().setComplaintReportHandle((ComplaintReport) createAndDefinedFxmlWindow("/QuarterReports/ComplaintReport.fxml","Complaint Report"));
    	}
    	else
    	{
    		Main.getStoreManagerMainControl().getShowStoreManagerReportsHandle().setComplaintReportHandle((ComplaintReport) createAndDefinedFxmlWindow("/QuarterReports/ComplaintReport.fxml","Complaint Report"));
    	}
		data=new ComplaintReports();
		initialized();
    }
    /**
     * 
     */
    public void initialized()
    {
    	if (actorflag==true)
    	data=Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getComplaintReports();
    	else
    		data=Main.getStoreManagerMainControl().getShowStoreManagerReportsHandle().getComplaintReports();	
    	getComplaintReport(data.getStoreID(),data.getQyear(),data.getQnum()); 
    
    }
    /**
     * 
     * @param store
     * @param year
     * @param qnum
     */
	public static void getComplaintReport(String store,String year,String qnum)
	{
	PacketClass packet = new PacketClass( // , ComplaintReportData
			Main.SELECTCommandStatement + "monthNum,complaintNum" + Main.FROMCommmandStatement + "complaint_report"
			+Main.WHERECommmandStatement+"storeID="+store+" AND "+"quarterYear="+year+" AND "+"quarterNum="+qnum,
			Main.GetComplaintReportData, Main.READ);


	try {
		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
	} catch (Exception e) {
		//updateStatusLabel("Client connection error", true);
		System.out.println("Client connection error");
	}
	}
	
	/**
	 * 
	 * @param packet
	 */
    public void setComplaintReport_FromServer(PacketClass packet) {
    	ArrayList<ArrayList<String>> DataList;
		if (packet.getSuccessSql()) {
				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

				if (DataList == null) {
					updateStatusLabel("Couldn't get complaint report data", true,lblFillStatus);
				} else {

					try {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								int i=0;
								for(i=0;i<DataList.size();i++)
								{
									XYChart.Series set=new XYChart.Series<>();
									set.getData().add(new XYChart.Data("Month: "+DataList.get(i).get(0),Integer.parseInt(DataList.get(i).get(1))));
									set.setName("Month: "+(i+1));
									ComplaintChart.getData().add(set);
									ComplaintChart.setCategoryGap(100);
									
								}
								
							}
						});
					} catch (Exception e) {
						updateStatusLabel("complaint report data is invalid", true,lblFillStatus);
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to complaint_reports data", true,lblFillStatus);
		}
    	
    	
    }

    @FXML
    void click_backBtn(ActionEvent event) throws Exception {
  		 ((Node)(event.getSource())).getScene().getWindow().hide();
  		if(actorflag==true)
  		{
  			Main.getCompanyManagerMainControl().getShowManagerReportsHandle().decWindowsCounter();
  			if(Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getTwoReportsFlag()==false)
  				Main.getCompanyManagerMainControl().getShowManagerReportsHandle().start();
  			else
  				if(Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getWindowsCounter()==0)
  					Main.getCompanyManagerMainControl().getShowManagerReportsHandle().disableReportsAmount(false);
  		}
  		else
  		Main.getStoreManagerMainControl().getShowStoreManagerReportsHandle().start();
    }
}
