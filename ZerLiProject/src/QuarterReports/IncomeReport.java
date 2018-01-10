package QuarterReports;

import java.util.ArrayList;

import Reports.IncomeReports;
import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IncomeReport extends GuiExtensions{
public static IncomeReports data;
    @FXML
    private Label lblFillStatus;

    @FXML
    private TableView<IncomeReports> incometable;

    @FXML
    private TableColumn<IncomeReports, String> storeID;

    @FXML
    private TableColumn<IncomeReports, String> year;

    @FXML
    private TableColumn<IncomeReports, String> qnum;

    @FXML
    private TableColumn<IncomeReports, Integer> income;
    
    public void start() throws Exception {
    	Main.getCompanyManagerMainControl().getShowManagerReportsHandle().setIncomeReportHandle((IncomeReport) createAndDefinedFxmlWindow("/QuarterReports/IncomeReport.fxml","Income Report"));
    	data=new IncomeReports();
    	initialized();
    }
   
 
  public void initialized()
  {
	  data=Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getIncomeReports();
	  getIncomeReport(data.getStoreID(),data.getQyear(),data.getQnum());
  }
  public static void getIncomeReport(String store,String year,String qnum)
	{
	PacketClass packet = new PacketClass( // , ComplaintReportData
			Main.SELECTCommandStatement + "incomeNum" + Main.FROMCommmandStatement + "income_report"
			+Main.WHERECommmandStatement+"storeID="+store+" AND "+"quarterYear="+year+" AND "+"quarterNum="+qnum,
			Main.GetIncomeReportData, Main.READ);


	try {
		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
	} catch (Exception e) {
		//updateStatusLabel("Client connection error", true);
		System.out.println("Client connection error");
	}
	}
  
  public void setIncomeReport_FromServer(PacketClass packet) {
  	ArrayList<ArrayList<String>> DataList;
		if (packet.getSuccessSql()) {
				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

				if (DataList == null) {
					updateStatusLabel("Couldn't get income report data", true,lblFillStatus);
				} else {

					try {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
										data.setIncome(Integer.parseInt(DataList.get(0).get(0)));	
										ObservableList<IncomeReports> list =FXCollections.observableArrayList(data);
										incometable.setItems(list);
									   
									   storeID.setCellValueFactory(new PropertyValueFactory<IncomeReports,String>("storeID"));
									   year.setCellValueFactory(new PropertyValueFactory<IncomeReports,String>("qyear"));
									   qnum.setCellValueFactory(new PropertyValueFactory<IncomeReports,String>("qnum"));
									   income.setCellValueFactory(new PropertyValueFactory<IncomeReports,Integer>("income"));
								
								
							}
						});
					} catch (Exception e) {
						updateStatusLabel("income report data is invalid", true,lblFillStatus);
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to income_reports data", true,lblFillStatus);
		}
  	
  	
  }
  
  
  
  
}
