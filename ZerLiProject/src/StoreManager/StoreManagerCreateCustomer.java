package StoreManager;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import client.GuiExtensions;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class StoreManagerCreateCustomer extends GuiExtensions{
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

 		Main.getStoreManagerMainControl().setShowStoreManagerCreateCustomerHandle((StoreManagerCreateCustomer) createAndDefinedFxmlWindow("StoreManagerCreateCostumer.fxml","Store Manager Create Customer"));
    }
    
    public void initialize() {
    	customer=new Customer();
    	PacketClass packet = new PacketClass( // , username
    			Main.SELECTCommandStatement + "UserName" + Main.FROMCommmandStatement + "users",
    			Main.InitializeStoreManagerUsernamecomboBox, Main.READ);


    	try {
    		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
    	} catch (Exception e) {
    		updateStatusLabel("Client connection error", true,lblFillStatus);
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
					updateStatusLabel("Couldn't get username data", true,lblFillStatus);
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
						updateStatusLabel("username data is invalid", true,lblFillStatus);
						CurrentStoreID = -1;
					}
				}

		} else {
			// Sql command failed
			updateStatusLabel("Failed connect to users data", true,lblFillStatus);
			CurrentStoreID = -1;
		}
	}

    @FXML
    void click_StoreManagerReports_backBtn(ActionEvent event) throws Exception {
     	 ((Node)(event.getSource())).getScene().getWindow().hide();
    	 Main.getStoreManagerMainControl().start();
    }

    @FXML
    void click_companyManagerCreateCustomer_sendbtn(ActionEvent event) {
    	customer.setUsername(username.getValue().toString());
    	customer.setId(id.getText());
    	if (customer.getId().length() != 9) {
    		updateStatusLabel("ID must be only 9 numbers ", true,lblFillStatus);
    		return;
    	}
    	customer.setFirstname(firstname.getText());
    	if(customer.getFirstname() == null && customer.getFirstname().trim().isEmpty())
    	{
    		updateStatusLabel("First name is empty ", true,lblFillStatus);
    		return;	
    	}
    	customer.setLastname(lastname.getText());
    	if(customer.getLastname() == null && customer.getLastname().trim().isEmpty())
    	{
    		updateStatusLabel("Last name is empty ", true,lblFillStatus);
    		return;	
    	}
    	customer.setEmail(email.getText());
    	if(customer.getEmail() == null && customer.getEmail().trim().isEmpty())
    	{
    		updateStatusLabel("Email is empty ", true,lblFillStatus);
    		return;	
    	}
    	customer.setAddress(address.getText());
    	if(customer.getAddress() == null && customer.getAddress().trim().isEmpty())
    	{
    		updateStatusLabel("Address is empty ", true,lblFillStatus);
    		return;	
    	}
    	customer.setCreditcard(creditcard.getText());
    	if(customer.getCreditcard() == null && customer.getCreditcard().trim().isEmpty()&&customer.getCreditcard().matches("[0-9]+"))
    	{
    		updateStatusLabel("credit card is empty ", true,lblFillStatus);
    		return;	
    	}
    	checkusername();

    }
     void checkusername() 
     {
     	PacketClass packet = new PacketClass( // , username
    			Main.SELECTCommandStatement + "username,storeID" + Main.FROMCommmandStatement + "customer"
     			+Main.WHERECommmandStatement+"username = "+"'"+customer.getUsername()+"'"+"AND "+
    			"storeID = "+"'"+12/*Main.getLoginLogicControl().getNewUser().getStoreID()*/+"'",
    			Main.CheckCustomerUsernameExist, Main.READ);


    	try {
    		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
    	} catch (Exception e) {
    		updateStatusLabel("Client connection error", true,lblFillStatus);
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
						    		updateStatusLabel("costumer create start", false,lblFillStatus);
						    		CreateNewCostumer();
								}
								else 
								updateStatusLabel("costumer already exist", true,lblFillStatus);
								
							}
						});
					} catch (Exception e) {
						updateStatusLabel("username data is invalid", true,lblFillStatus);
						CurrentStoreID = -1;
					}
				}

		 else {
			// Sql command failed
			updateStatusLabel("Failed connect to costumer username data", true,lblFillStatus);
			CurrentStoreID = -1;
		}
   }
   void CreateNewCostumer()
   {
		PacketClass packet = new PacketClass(Main.INSERTCommmandStatement + "customer"
				+ Main.VALUESCommmandStatement + "(" +customer.getId()+","+12/*Main.getLoginLogicControl().getNewUser().getStoreID()*/+","+"'"+customer.getUsername()+"'"
				+","+"'"+customer.getFirstname()+"'"+","+"'"+customer.getLastname()+"'"+","+"'"+customer.getEmail()+"'"+","+
				"'"+customer.getAddress()+"'"+","+1+","+"'"+customer.getCreditcard()+"'"+");"
    			,Main.CreateNewCustomer, Main.WRITE);


    	try {
    		Main.getClientConsolHandle().sendSqlQueryToServer(packet);
    	} catch (Exception e) {
    		updateStatusLabel("Client connection error", true,lblFillStatus);
    	}
   }
	public void click_createCustomer_SendBtn_FromServer(PacketClass packet) {

		if (packet.getSuccessSql())
			updateStatusLabel("Save costumer " + customer.getId() + " succeed", false,lblFillStatus);
		else
			updateStatusLabel("Save answers for " + customer.getId() + " failed", true,lblFillStatus);
	}
}
