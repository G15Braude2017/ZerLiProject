package CompanyManager;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import ShopWorker.FillCustomerAnswers;
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
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class CompanyManagerReports {
	private static int CurrentStoreID = -1;
	//ObservableList<String> comboBoxList_amount1=FXCollections.observableArrayList("One Report","Two Reports");
    @FXML
    private Label lblFillStatus;
    @FXML
    private JFXButton btn_send;

    @FXML
    private JFXComboBox<?> comboBox_amount1;

    @FXML
    private JFXComboBox<?> comboBox_StoreID1;

    @FXML
    private JFXComboBox<?> comboBox_Rtype1;

    @FXML
    private JFXComboBox<?> comboBox_StoreID2;

    @FXML
    private JFXComboBox<?> comboBox_Rtype2;

    @FXML
    private JFXComboBox<?> comboBox_Ryear1;

    @FXML
    private JFXComboBox<?> comboBox_Rquarter1;

    @FXML
    private JFXComboBox<?> comboBox_Ryear2;
    @FXML
    private JFXComboBox<?> comboBox_Rquarter2;

    public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CompanyManagerReports.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Company Manager Reports");
		stage.show();

		FillCustomerAnswers FillCustomerAnswershandle = fxmlLoader.getController();
		//Main.getShopWorkerMainControl().setFillCustomerAnswersHandle(FillCustomerAnswershandle);

		initialize();

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
    void initialize()
    {
        ObservableList amountList=FXCollections.observableArrayList("OneReport","Two Reports");
        comboBox_amount1.setItems(amountList);
        ObservableList ReportTypetList=FXCollections.observableArrayList("Income Report","Order Report","Satisfaction Report","Complaint Report");
        comboBox_Rtype1.setItems(ReportTypetList);
        ObservableList YearList=getYear(); 
        comboBox_Ryear1.setItems(YearList);
        ObservableList quarterNumList=FXCollections.observableArrayList(1,2,3,4);
        comboBox_Rquarter1.setItems(quarterNumList); 

		PacketClass packet = new PacketClass( // , store id
				Main.SELECTCommandStatement + "storeID" + Main.FROMCommmandStatement + "store",
				Main.InitializeCompanyManagerStoreIDcomboBox, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true);
		}
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
								while(DataList.get(i).get(0)!=null)
								{
								storeIDList.add(DataList.get(i).get(0));
								}
								
								Main.getCompanyManagerMainControl().getShowManagerReportsHandle().comboBox_StoreID1.setItems(storeIDList);
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
    void click_CompanyManagerReports_backBtn(ActionEvent event) {

    }

    @FXML
    void click_CompanyManagerReports_btnSend(ActionEvent event) {

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
}
