package user;

import user.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.Main;
import clientServerCommon.PacketClass;
import ShopWorker.ShopWorkerMain;
import CustomerService.CustomerServiceMain;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class loginLogic 
{
	String userName, password;
	@FXML
	private Label lblUserNameNote;
	@FXML
	private Label lblPasswordNote;
	@FXML
	private Label lblConnectedNote;
	@FXML
	private Label lblUserName;
	@FXML
	private Label lblPassword;
	@FXML
	private Button btnLogin;
	@FXML
	private TextField txtFldUserName;
	@FXML
	private TextField txtFldPassword;
	
	int flag=1; //If all is ok, user logs in
	
	User NewUser;
	
	public void start() throws Exception 
	{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Login");
		stage.show();

		loginLogic loginLogicHandle = fxmlLoader.getController();
		Main.setLoginLogicControl(loginLogicHandle);
	}
	
	
	public void ClickLogin()
	{
		PacketClass packet = new PacketClass(Main.SELECTCommandStatement + "UserName, Password, Connected, Permission, StoreID " + Main.FROMCommmandStatement + "Users" + Main.WHERECommmandStatement + "userName=" + txtFldUserName + "password=" + txtFldPassword,
				Main.CheckIfUserExists, Main.READ);
		try 
		{
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			updateStatusLabel("ERROR!! Login failed",true,3);
		}
	}

	
	public void validationFromServer(PacketClass packet)
	{
		//Check if username and password exist in DB 
		
		if (packet.getSuccessSql())
		{
			ArrayList<ArrayList<String>> dataList;
			dataList = (ArrayList<ArrayList<String>>)packet.getResults();//
						
			if(dataList.get(0).get(2) == "true")
				NewUser = new User(dataList.get(0).get(0), dataList.get(0).get(1), true, Integer.parseInt(dataList.get(0).get(3)), dataList.get(0).get(4));
			else
				NewUser = new User(dataList.get(0).get(0), dataList.get(0).get(1), false, Integer.parseInt(dataList.get(0).get(3)), dataList.get(0).get(4));
			
			if(NewUser.getUserName() == null)
			{
				updateStatusLabel("Username does not exist",true,1);
				flag=0;
			}
			
			if(NewUser.getPassword() == null)
			{
				updateStatusLabel("wrong password",true,2);
				flag=0;
			}
			
			if(NewUser.getConnected() == false)
			{
				updateStatusLabel("User already logged in",true,3);
				flag=0;
			}
		
			if(flag == 1)
			{
				packet = new PacketClass(Main.UPDATECommandStatement + "Users" + Main.SETCommandStatement + "Connected= true" + Main.WHERECommmandStatement + "userName=" + NewUser.getUserName() + "password=" + NewUser.getPassword(),
					Main.UpdateStatusOfAnExistingUser, Main.READ);
			}
		}
	}
	
	public void UpdateStatusUserFromServer(PacketClass packet)
	{
		if (!(packet.getSuccessSql()))
		{
			updateStatusLabel("Login failed",true,3);
		}
	}
	
	private void updateStatusLabel(String message, boolean red_green, int flag) //1=userName, 2=password, 3=connected
	{
		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				// Update GUI
				if(flag==1)
				{
					Main.getLoginLogicControl().getLblUserNameNote().setText(message);
					Main.getLoginLogicControl().getLblUserNameNote().setTextFill(Paint.valueOf(Main.RED));
				}
				if(flag==2)
				{
					Main.getLoginLogicControl().getLblPasswordNote().setText(message);
					Main.getLoginLogicControl().getLblPasswordNote().setTextFill(Paint.valueOf(Main.RED));
				}
				if(flag==3)
				{
					Main.getLoginLogicControl().getLblConnectedNote().setText(message);
					Main.getLoginLogicControl().getLblConnectedNote().setTextFill(Paint.valueOf(Main.RED));
				}
			}
		});
	}
	
	public Label getLblUserNameNote() 
	{
		return lblUserNameNote;
	}
	
	public Label getLblPasswordNote() 
	{
		return lblPasswordNote;
	}
	
	public Label getLblConnectedNote() 
	{
		return lblConnectedNote;
	}
	
/*	
	private void OpenWindowByPermission() throws Exception
	{
		switch(NewUser.getPermission())
		{
		case ShopWorker:
		{
			ShopWorkerMain ShopWorker= new ShopWorkerMain();
			ShopWorker.start();
		}
		case Customer:
		{
			CustomerMain Customer= new CustomerMain();
			Customer.start();
		}
		
		case CustomerService:
		{
			CustomerServiceMain CustomerService= new CustomerServiceMain();
			CustomerService.start();
		}
		case Expert:
		{
			ExpertMain Expert= new ExpertMain();
			Expert.start();
		}
			
		case SystemManager: 
		{
			SystemManagerMain SystemManager= new SystemManagerMain();
			SystemManager.start();
		}
		case CompanyManager: 
		{
			CompanyManagerMain CompanyManager= new CompanyManagerMain();
			CompanyManager.start();
		}
		case StoreManager:
		{
			StoreManagerMain StoreManager= new StoreManagerMain();
			StoreManager.start();
		}
		case CompenyEmployee:
		{
			CompenyEmployeeMain CompenyEmployee= new CompenyEmployeeMain();
			CompenyEmployee.start();
		}
		}	
	}
	*/
}
