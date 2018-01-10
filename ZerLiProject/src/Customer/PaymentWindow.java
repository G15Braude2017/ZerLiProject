package Customer;

import client.GuiExtensions;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class PaymentWindow extends GuiExtensions {

	// Button
	@FXML
	private Button btnBack; // needs to be added
	@FXML
	private Button btnPay;

	// Label
	@FXML
	private Label lblTotalOrder;
	@FXML
	private Label lblTotal;
	@FXML
	private Label lblStoreBalance;
	@FXML
	private Label lblBalance;
	@FXML
	private Label lblLeftToPay;
	@FXML
	private Label lblPaymentAmountleft;

	
	public void start() throws Exception {
		
		//disable unused lables
		initializePaymentWindow();
		
		
	}
	public void initializePaymentWindow(){
		
		//get order price from the OrderCreated in "createOrder"
		//get Balance from DB and put it in "balance label" 
		//if there is more than 0 money display windows of balance
		//Coagulate amount to pay after deduction if there is any
	}
	
	public void Click_PaymentWindow_PayBTN() {
		//will push receipt to db 
	}
	
	public void Click_PaymentWindow_BackBTN() {
		
	}
	
	
}
