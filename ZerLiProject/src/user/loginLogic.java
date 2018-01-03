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
	private Label lblNote;
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
	
	private User NewUser;
	
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
		if(txtFldUserName.getText().isEmpty() || txtFldPassword.getText().isEmpty())
		{
			updateStatusLabel("User details were not entered correctly",true);
		}
		
		PacketClass packet = new PacketClass(Main.SELECTCommandStatement + "UserName, Password, Promission, Connected, StoreID" + Main.FROMCommmandStatement + "Users" + Main.WHERECommmandStatement + "userName='" + txtFldUserName.getText() + "' AND password='" + txtFldPassword.getText()+"';",
				Main.CheckIfUserExists, Main.READ);
		try 
		{
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			updateStatusLabel("ERROR!! Login failed",true);
		}
	}

	
	public void validationFromServer(PacketClass packet)
	{
		//Check if username and password exist in DB 
		int flag =1;
		String patternPassword = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";
		String patternUserName = "[a-zA-Z]{4,}";
		if (packet.getSuccessSql())
		{
			ArrayList<ArrayList<String>> dataList;
			dataList = (ArrayList<ArrayList<String>>)packet.getResults();//
						
			if(Integer.parseInt(dataList.get(0).get(2)) == 1)
				NewUser = new User(dataList.get(0).get(0), dataList.get(0).get(1), true, Integer.parseInt(dataList.get(0).get(3)), Integer.parseInt(dataList.get(0).get(4)));
			else
				if(Integer.parseInt(dataList.get(0).get(2)) == 0)
					NewUser = new User(dataList.get(0).get(0), dataList.get(0).get(1), false, Integer.parseInt(dataList.get(0).get(3)), Integer.parseInt(dataList.get(0).get(4)));
						
			if(NewUser.getUserName() == null)
			{
				updateStatusLabel("Username does not exist",true);
				flag=0;
			}
			
			if(NewUser.getPassword() == null)
			{
				updateStatusLabel("wrong password",true);
				flag=0;
			}
			
			if(NewUser.getConnected() == false)
			{
				updateStatusLabel("User already logged in",true);
				flag=0;
			}
			
			if(!(NewUser.getUserName().matches(patternUserName)))
			{
				updateStatusLabel("Invalid Username",true);
				flag=0;
			}
			
			if(!(NewUser.getPassword().matches(patternPassword)))
			{
				updateStatusLabel("Invalid Password",true);
				flag=0;
			}
			
		
			if(flag == 1)
			{
				packet = new PacketClass(Main.UPDATECommandStatement + "Users" + Main.SETCommandStatement + "Connected= true" + Main.WHERECommmandStatement + "userName=" + NewUser.getUserName() + "password=" + NewUser.getPassword(),
					Main.UpdateStatusOfAnExistingUser, Main.READ);
				
				try 
				{
					OpenWindowByPermission();
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void UpdateStatusUserFromServer(PacketClass packet)
	{
		if (!(packet.getSuccessSql()))
		{
			updateStatusLabel("Login failed",true);
		}
	}
	
	private void updateStatusLabel(String message, boolean red_green)
	{
		Platform.runLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				// Update GUI
				Main.getLoginLogicControl().getLblNote().setText(message);
				Main.getLoginLogicControl().getLblNote().setTextFill(Paint.valueOf(Main.RED));
				
			}
		});
	}
	
	public Label getLblNote() 
	{
		return lblNote;
	}
	
	
	private void OpenWindowByPermission() throws Exception
	{
		switch(NewUser.getPermission())
		{
		case ShopWorker:
		{
			ShopWorkerMain ShopWorker= new ShopWorkerMain();
			ShopWorker.start();
		}
		/*
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
		*/
		}	
	}	
}