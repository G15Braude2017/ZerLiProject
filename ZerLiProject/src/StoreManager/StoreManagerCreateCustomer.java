package StoreManager;

import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import client.Main;
import Customer.CustomerInterface;
import Customer.Customer;
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

public class StoreManagerCreateCustomer {
	private static int CurrentStoreID = -1;
	private static int reportAmount=0;
	private static Customer customer;
    @FXML
    private Label lblFillStatus;
    @FXML
    private JFXComboBox username;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField firstname;

    @FXML
    private JFXTextField lastname;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField creditcard;

    @FXML
    private JFXButton btnSend;
    
    public void start() throws Exception {

 		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StoreManagerCreateCostumer.fxml"));
 		Parent root1 = (Parent) fxmlLoader.load();
 		Stage stage = new Stage();
 		stage.setScene(new Scene(root1));
 		stage.setTitle("Store Manager Create Customer");
 		stage.show();

 		StoreManagerCreateCustomer StoreManagerCreateCustomerHandle = fxmlLoader.getController();
 		Main.getStoreManagerMainControl().setShowStoreManagerCreateCustomerHandle(StoreManagerCreateCustomerHandle);
    }
    
    public void initialize() {
    	customer=new Customer();
    	PacketClass packet = new PacketClass( // , username
    			Main.SELECTCommandStatement + "UserName" + Main.FROMCommmandStatement + "users",
    			Main.InitializeStoreManagerUsernamecomboBox, Main.READ);


    	try {
    		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
    	} catch (Exception e) {
    		updateStatusLabel("Client connection error", true);
    	}
        
    }
	public void FillStoreManagerUsername_ComboBoxUserName_FromServer(PacketClass packet) {

		ArrayList<ArrayList<String>> DataList;
		ObservableList usernameList=FXCollections.observableArrayList();
   
		if (packet.getSuccessSql()) {
		/*	if (CurrentStoreID == -1) {
				// Invalid ShopID, not changed
				updateStatusLabel("Invalid ShopID, log in again", true);*/

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();
				String SurveyIDstr;

				if (DataList == null) {
					updateStatusLabel("Couldn't get username data", true);
				} else {

					try {

						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								int i=0;
								for(i=0;i<DataList.size();i++)
								{
								usernameList.add(DataList.get(i).get(0));
								}
								
								Main.getStoreManagerMainControl().getShowStoreManagerCreateCustomerHandle().username.setItems(usernameList);
							}
						});
					} catch (Exception e) {
						updateStatusLabel("username data is invalid", true);
						CurrentStoreID = -1;
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to users data", true);
			CurrentStoreID = -1;
		}
	}
	private void updateStatusLabel(String message, boolean red_green) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				Main.getStoreManagerMainControl().getShowStoreManagerCreateCustomerHandle().lblFillStatus.setText(message);

				if (red_green)
					Main.getStoreManagerMainControl().getShowStoreManagerCreateCustomerHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.RED));
				else
					Main.getStoreManagerMainControl().getShowStoreManagerCreateCustomerHandle().lblFillStatus
							.setTextFill(Paint.valueOf(Main.GREEN));
			}
		});
	}
    @FXML
    void click_StoreManagerReports_backBtn(ActionEvent event) {

    }

    @FXML
    void click_companyManagerCreateCustomer_sendbtn(ActionEvent event) {
    	customer.setUsername(username.getValue().toString());
    	customer.setId(id.getText());
    	customer.setFirstname(firstname.getText());
    	customer.setLastname(lastname.getText());
    	customer.setEmail(email.getText());
    	customer.setAddress(address.getText());
    	customer.setCreditcard(creditcard.getText());
    	checkusername();

    }
     void checkusername() 
     {
     	PacketClass packet = new PacketClass( // , username
    			Main.SELECTCommandStatement + "username" + Main.FROMCommmandStatement + "customer"
     			+Main.WHERECommmandStatement+"username = "+"'"+customer.getUsername()+"'",
    			Main.CheckCustomerUsernameExist, Main.READ);


    	try {
    		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
    	} catch (Exception e) {
    		updateStatusLabel("Client connection error", true);
    	}
     }
   public  void CheckExistUsername_FromServer(PacketClass packet)
   {
		ArrayList<ArrayList<String>> DataList;
  
		if (packet.getSuccessSql()) {
				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

					try {

						Platform.runLater(new Runnable() {
							@Override
							public  void run() {
								if(DataList==null)
								{
						    		updateStatusLabel("customer created", false);
						    		CreateNewCostumer();
								}
								else 
								updateStatusLabel("customer already exist", true);
								
							}
						});
					} catch (Exception e) {
						updateStatusLabel("username data is invalid", true);
						CurrentStoreID = -1;
					}
				}

		 else {
			// Sql command failed
			updateStatusLabel("Failed connect to customer username data", true);
			CurrentStoreID = -1;
		}
   }
   void CreateNewCostumer()
   {
		PacketClass packet = new PacketClass(Main.INSERTCommmandStatement + "customer"
				+ Main.VALUESCommmandStatement + "(" +customer.getId()+","+"'"+customer.getUsername()+"'"
				+","+"'"+customer.getFirstname()+"'"+","+"'"+customer.getLastname()+"'"+","+"'"+customer.getEmail()+"'"+","+
				"'"+customer.getAddress()+"'"+","+1+","+"'"+customer.getCreditcard()+"'"+");"
    			,Main.CreateNewCustomer, Main.WRITE);


    	try {
    		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
    	} catch (Exception e) {
    		updateStatusLabel("Client connection error", true);
    	}
   }
	public void click_createCustomer_SendBtn_FromServer(PacketClass packet) {

		if (packet.getSuccessSql())
			updateStatusLabel("Save customer " + customer.getId() + " succeed", false);
		else
			updateStatusLabel("Save answers for " + customer.getId() + " failed", true);
	}
}
