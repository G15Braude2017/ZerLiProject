package ShopWorker;

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

public class ShopWorkerMain extends GuiExtensions {

	// Button
	@FXML
	private Button btnLogout;

	private static FillCustomerAnswers fillCustomerAnswersHandle;

	public void start() throws Exception {

		Main.setShopWorkerMainControl(
				(ShopWorkerMain) createAndDefinedFxmlWindow("ShopWorkerMain.fxml", "Shop worker panel"));

		fillCustomerAnswersHandle = new FillCustomerAnswers();
	}

	public void click_FillCustomerAnswers_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			fillCustomerAnswersHandle.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clickLogoutButton() {

		logoutApplicationClient(btnLogout);
	}

	public static FillCustomerAnswers getFillCustomerAnswersHandle() {
		return fillCustomerAnswersHandle;
	}

	public static void setFillCustomerAnswersHandle(FillCustomerAnswers fillCustomerAnswersHandle) {
		ShopWorkerMain.fillCustomerAnswersHandle = fillCustomerAnswersHandle;
	}

}
