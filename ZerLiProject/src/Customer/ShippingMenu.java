package Customer;

import client.GuiExtensions;

import java.awt.TextField;
import java.util.ArrayList;


import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;



public class ShippingMenu extends GuiExtensions {

	// Text Field
	@FXML
	private TextField tfShippingAddress;
	@FXML
	private TextField tfShippingPhoneNumber;
	@FXML
	private TextField tfShippingReceiverName;

	// Button
	@FXML
	private Button btnConfirm;
	
	private EventHandler<ActionEvent> eh;
	
	public void start() throws Exception {

		//Main.getCustomerMainControl().setOpenNewComplaintControl((OpenNewComplaint) createAndDefinedFxmlWindow("OpenNewComplaint.fxml", "Open complaint" ));

		 //initialize();

	}
	

}
