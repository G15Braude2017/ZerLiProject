package Customer;




import Customer.CreateOrder;
import client.GuiExtensions;
import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import user.loginLogic;

public class CustomerInterface extends GuiExtensions {

	// Button
	@FXML
	private Button btnLogout;
	@FXML
	private Button btnCreateNewOrder;
	@FXML
	private Button btnCancelOrder;
	
	
	
	
	private static CreateOrder CreateNewOrderControl;
	private static CancelOrder CancelOrderControl;

	// refund values
	

	public void start() throws Exception {

		Main.setCustomerMainControl(
				(CustomerInterface) createAndDefinedFxmlWindow("CustomerMain.fxml", "Customer panel"));

		CreateNewOrderControl = new CreateOrder();
		CancelOrderControl = new CancelOrder();

	}

	public void click_CreateNewOrder_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			CreateNewOrderControl.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void click_CancelOrder_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			CancelOrderControl.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clickLogoutButton() {

		logoutApplicationClient(btnLogout);
	}

	public static CreateOrder getCreateNewOrderControl() {
		return CreateNewOrderControl;
	}

	public static void setCreateNewOrderControl(CreateOrder createNewOrderControl) {
		CreateNewOrderControl = createNewOrderControl;
	}

	public static CancelOrder CancelOrderControl() {
		return CancelOrderControl;
	}

	public static void setCancelOrderControl(CancelOrder cancelOrderControl) {
		CancelOrderControl = cancelOrderControl;
	}


	

}
