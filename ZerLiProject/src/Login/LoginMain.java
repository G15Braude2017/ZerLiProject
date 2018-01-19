package Login;

import java.util.ArrayList;
import java.util.regex.Pattern;

import CompanyManager.CompanyManagerMain;
import CompanyWorker.CompanyWorkerMain;
import client.GuiExtensions;
import client.Main;
import clientServerCommon.PacketClass;
import ShopWorker.ShopWorkerMain;
import StoreManager.StoreManagerMain;
import SystemManager.SystemManagerMain;
import CustomerService.CustomerServiceMain;
import Login.UserEntity;
import ServiceExpert.ServiceExpertMain;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginMain extends GuiExtensions {

	private String userName, password;

	private UserEntity NewUser;

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

		Main.setLoginLogicControl((LoginMain) createAndDefinedFxmlWindow("LoginMain.fxml", "Login"));

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

			updateStatusLabel("Failed establish client", true, Main.getLoginLogicControl().lblNote);
		}
	}

	public void clickLoginButton(ActionEvent event) {

		Pattern patternPassword = Pattern.compile("^[a-zA-Z0-9_-]{4,20}$");
		Pattern patternUserName = Pattern.compile("^[a-zA-Z0-9]{4,20}$");

		if (txtFldUserName.getText().isEmpty() || txtFldPassword.getText().isEmpty()) {
			updateStatusLabel("Username or password is blank", true, Main.getLoginLogicControl().lblNote);
		} else {

			if (!patternUserName.matcher(txtFldUserName.getText()).matches()
					|| !patternPassword.matcher(txtFldPassword.getText()).matches()) {
				updateStatusLabel("Invalid chars in username or password", true, Main.getLoginLogicControl().lblNote);
			} else {

				PacketClass packet = new PacketClass(
						Main.SELECTCommandStatement + "UserName, Password, Permission, Connected, StoreID"
								+ Main.FROMCommmandStatement + "Users" + Main.WHERECommmandStatement + "userName='"
								+ txtFldUserName.getText() + "' AND password='" + txtFldPassword.getText() + "';",
						Main.LoginCheckIfUserExists, Main.READ);
				try {
					Main.getClientConsolHandle().sendSqlQueryToServer(packet);
				} catch (Exception e) {
					// System.out.println(e.toString());
					updateStatusLabel("Failed connect to server", true, Main.getLoginLogicControl().lblNote);
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

					if (Integer.parseInt(dataList.get(0).get(2)) <= 8) {
						if (Integer.parseInt(dataList.get(0).get(3)) == 1)
							updateStatusLabel("User already connected", true, Main.getLoginLogicControl().lblNote);
						else {
							NewUser = new UserEntity(dataList.get(0).get(0), dataList.get(0).get(1), true,
									Integer.parseInt(dataList.get(0).get(2)), dataList.get(0).get(4));

							packet = new PacketClass(Main.UPDATECommandStatement + "Users" + Main.SETCommandStatement
									+ "Connected= 1" + Main.WHERECommmandStatement + "userName='"
									+ getNewUser().getUserName() + "';", Main.LoginUpdateStatusOfAnExistingUser,
									Main.WRITE);

							try {
								Main.getClientConsolHandle().sendSqlQueryToServer(packet);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					} else
						updateStatusLabel("Problem in data base", true, Main.getLoginLogicControl().lblNote);
				} else
					updateStatusLabel("Invalid Login data", true, Main.getLoginLogicControl().lblNote);

			} else
				updateStatusLabel("User not exist or password incorrect", true, Main.getLoginLogicControl().lblNote);

		}
	}

	public void UpdateStatusUserFromServer(PacketClass packet) {
		if (packet.getSuccessSql()) {
			OpenWindowByPermission();
		} else {
			updateStatusLabel("Unable update Login data", true, Main.getLoginLogicControl().lblNote);
		}
	}

	private void OpenWindowByPermission() {

		Stage stage = (Stage) btnLogin.getScene().getWindow();

		try {
			switch (getNewUser().getPermission()) {
			case user: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new UserMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			break;
			
			case ShopWorker: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new ShopWorkerMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				break;
			}

			
			case Expert: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new ServiceExpertMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			break;

			case CustomerService: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new CustomerServiceMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				break;
			}
			
			
			
			
			case SystemManager: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new SystemManagerMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			break;
			
			
			case CompanyManager: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new CompanyManagerMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			break;
			
			case StoreManager: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new StoreManagerMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			break;
			
			case CompanyEmployee: {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stage.hide();

						try {
							(new CompanyWorkerMain()).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			break;
			
			
			
			
			
			
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public UserEntity getNewUser() {
		return NewUser;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		
	}

}