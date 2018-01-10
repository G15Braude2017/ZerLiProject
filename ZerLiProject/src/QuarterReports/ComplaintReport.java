package QuarterReports;

import java.util.ArrayList;

import Reports.ComplaintReports;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class ComplaintReport {
	
	public static ArrayList<Integer>number;
    @FXML
    private Label lblFillStatus;
    @FXML
    private StackedBarChart ComplaintChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;
    public void start() throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/QuarterReports/ComplaintReport.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Complaint Report");
		stage.show();

		ComplaintReport ComplaintReportHandle = fxmlLoader.getController();
		Main.getCompanyManagerMainControl().getShowManagerReportsHandle().setComplaintReportHandle(ComplaintReportHandle);
		initialized();
    }
    /**
     * 
     */
    public void initialized()
    {
    	ComplaintReports data=new ComplaintReports();
    	data=Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getComplaintReports();
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
					updateStatusLabel("Couldn't get complaint report data", true);
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
						updateStatusLabel("complaint report data is invalid", true);
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to complaint_reports data", true);
		}
    	
    	
    }
	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getComplaintReportHandle().lblFillStatus.setText(message);

				if (red_green)
					Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getComplaintReportHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getComplaintReportHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}
    @FXML
    void click_StoreManagerReports_backBtn(ActionEvent event) {

    }
}
