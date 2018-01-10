package QuarterReports;

import java.util.ArrayList;

import Reports.OrderReports;
import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class OrderReport extends GuiExtensions{
	public static OrderReports data;
    @FXML
    private Label lblFillStatus;

    @FXML
    private TableView<OrderReports> ordertable;

    @FXML
    private TableColumn<OrderReports, String> productID;

    @FXML
    private TableColumn<OrderReports, String> name;

    @FXML
    private TableColumn<OrderReports, Integer> amount;

    @FXML
    private Label storeID;

    @FXML
    private Label qyear;

    @FXML
    private Label qnum;

    public void start() throws Exception {
    	Main.getCompanyManagerMainControl().getShowManagerReportsHandle().setOrderReportHandle((OrderReport) createAndDefinedFxmlWindow("/QuarterReports/OrderReport.fxml","Order Report"));
    	data=new OrderReports();
    	initialized();
    }
    
    public void initialized()
    {
  	  data=Main.getCompanyManagerMainControl().getShowManagerReportsHandle().getOrderReports();

  	  getOrderReport(data.getStoreID(),data.getQyear(),data.getQnum());
    }
    public static void getOrderReport(String store,String year,String qnum)
  	{
  	PacketClass packet = new PacketClass( // , ComplaintReportData
  			Main.SELECTCommandStatement + "productNum,productName,orderAmount" + Main.FROMCommmandStatement + "order_report"
  			+Main.WHERECommmandStatement+"storeID="+store+" AND "+"quarterYear="+year+" AND "+"quarterNum="+qnum,
  			Main.GetOrderReportData, Main.READ);


  	try {
  		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
  	} catch (Exception e) {
  		//updateStatusLabel("Client connection error", true);
  		System.out.println("Client connection error");
  	}
  	}
    
    public void setOrderReport_FromServer(PacketClass packet) {
    	ArrayList<ArrayList<String>> DataList;
  		if (packet.getSuccessSql()) {
  				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

  				if (DataList == null) {
  					updateStatusLabel("Couldn't get order report data", true,lblFillStatus);
  				} else {

  					try {
  						Platform.runLater(new Runnable() {
  							@Override
  							public void run() {
  								ObservableList<OrderReports> list=FXCollections.observableArrayList();
  								for(int i=0;i<DataList.size();i++)
  								{
  									OrderReports data=new OrderReports();
  								data.setProductID(DataList.get(i).get(0));
  								data.setPname(DataList.get(i).get(1));
  								data.setOrder(Integer.parseInt(DataList.get(i).get(2)));
  								list.add(data);
  								
  								}
  									
  									ordertable.setItems(list);   
  									productID.setCellValueFactory(new PropertyValueFactory<OrderReports,String>("productID"));
  									 name.setCellValueFactory(new PropertyValueFactory<OrderReports,String>("pname"));
  									 amount.setCellValueFactory(new PropertyValueFactory<OrderReports,Integer>("order"));
  								  	 storeID.setText(data.getStoreID());
  								  	  qyear.setText(data.getQyear());
  								  	  qnum.setText(data.getQnum());
  							}
  						});
  					} catch (Exception e) {
  						updateStatusLabel("order report data is invalid", true,lblFillStatus);
  					}
  				}

  		} else {
  			// Sql command failed
  			updateStatusLabel("Failed connect to order_reports data", true,lblFillStatus);
  		}
    	
    	
    }
}
