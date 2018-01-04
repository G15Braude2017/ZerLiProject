package user;

import user.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import ShopWorker.ShopWorkerMain;
import CustomerService.CustomerServiceMain;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

public class loginLogic extends GuiExtensions {
	
	private String userName, password;

	private User NewUser;
	
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

	public void start(boolean connFlag) throws Exception {
		
		Main.setLoginLogicControl((loginLogic) createAndDefinedFxmlWindow("Login.fxml", "Login" ));

		if (!connFlag) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Update GUI
					Main.getLoginLogicControl().txtFldUserName.setDisable(true);
					Main.getLoginLogicControl().txtFldPassword.setDisable(true);
					Main.getLoginLogicControl().btnLogin.setDisable(true);
				}
			});

			updateStatusLabel("Failed establish client", true,Main.getLoginLogicControl().lblNote);
		}
	}

	public void ClickLogin(ActionEvent event) {

		String patternPassword = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";
		String patternUserName = "[a-zA-Z]{4,}";

		if (txtFldUserName.getText().isEmpty() || txtFldPassword.getText().isEmpty()) {
			updateStatusLabel("Username or password is blank", true,Main.getLoginLogicControl().lblNote);
		} else {

			if (txtFldUserName.getText().matches(patternUserName)
					|| txtFldPassword.getText().matches(patternPassword)) {
				updateStatusLabel("Invalid chars in username or password", true,Main.getLoginLogicControl().lblNote);
			} else {

				PacketClass packet = new PacketClass(
						Main.SELECTCommandStatement + "UserName, Password, Promission, Connected, StoreID"
								+ Main.FROMCommmandStatement + "Users" + Main.WHERECommmandStatement + "userName='"
								+ txtFldUserName.getText() + "' AND password='" + txtFldPassword.getText() + "';",
						Main.CheckIfUserExists, Main.READ);
				try {
					Main.getClientConsolHandle().sendSqlQueryToServer(packet);
				} catch (Exception e) {
					// System.out.println(e.toString());
					updateStatusLabel("Failed connect to server", true,Main.getLoginLogicControl().lblNote);
				}
			}
		}
	}

	public void validationFromServer(PacketClass packet) {
		// Check if username and password exist in DB

		if (packet.getSuccessSql()) {
			ArrayList<ArrayList<String>> dataList;
			dataList = (ArrayList<ArrayList<String>>) packet.getResults();//

			if (dataList != null) {

				if (dataList.get(0).get(0) != null && dataList.get(0).get(1) != null && dataList.get(0).get(2) != null
						&& dataList.get(0).get(3) != null) {

					if (Integer.parseInt(dataList.get(0).get(2)) != 0 && dataList.get(0).get(4) != null) {
						if (Integer.parseInt(dataList.get(0).get(3)) == 1)
							updateStatusLabel("User already connected", true,Main.getLoginLogicControl().lblNote);
						else {
							setNewUser(new User(dataList.get(0).get(0), dataList.get(0).get(1), true,
									Integer.parseInt(dataList.get(0).get(2)), Integer.parseInt(dataList.get(0).get(4))));

							packet = new PacketClass(
									Main.UPDATECommandStatement + "Users" + Main.SETCommandStatement + "Connected= 1"
											+ Main.WHERECommmandStatement + "userName='" + getNewUser().getUserName()
											+ "' AND password='" + getNewUser().getPassword() + "';",
									Main.UpdateStatusOfAnExistingUser, Main.WRITE);

							try {
								Main.getClientConsolHandle().sendSqlQueryToServer(packet);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					} else
						updateStatusLabel("User not connected to store", true , Main.getLoginLogicControl().lblNote);
				} else
					updateStatusLabel("Invalid user data", true ,Main.getLoginLogicControl().lblNote);

			} else
				updateStatusLabel("User not exist or password incorrect", true ,Main.getLoginLogicControl().lblNote);

		}
	}

	public void UpdateStatusUserFromServer(PacketClass packet) {
		if (packet.getSuccessSql()) {

			OpenWindowByPermission();
		} else {
			updateStatusLabel("Unable update user data", true, Main.getLoginLogicControl().lblNote);
		}
	}

	private void OpenWindowByPermission() {

		Stage stage = (Stage) btnLogin.getScene().getWindow();

		try {
			switch (getNewUser().getPermission()) {
			case ShopWorker: {
				stage.hide();

				ShopWorkerMain ShopWorker = new ShopWorkerMain();
				ShopWorker.start();
			}

			/*
			 * case Customer: { CustomerMain Customer= new CustomerMain(); Customer.start();
			 * }
			 */

			case CustomerService: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						CustomerServiceMain CustomerService = new CustomerServiceMain();
						try {
							CustomerService.start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			/*
			 * case Expert: { ExpertMain Expert= new ExpertMain(); Expert.start(); }
			 * 
			 * case SystemManager: { SystemManagerMain SystemManager= new
			 * SystemManagerMain(); SystemManager.start(); } case CompanyManager: {
			 * CompanyManagerMain CompanyManager= new CompanyManagerMain();
			 * CompanyManager.start(); } case StoreManager: { StoreManagerMain StoreManager=
			 * new StoreManagerMain(); StoreManager.start(); } case CompenyEmployee: {
			 * CompenyEmployeeMain CompenyEmployee= new CompenyEmployeeMain();
			 * CompenyEmployee.start(); }
			 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User getNewUser() {
		return NewUser;
	}

	public void setNewUser(User newUser) {
		NewUser = newUser;
	}

}