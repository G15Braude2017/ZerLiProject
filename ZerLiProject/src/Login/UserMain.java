package Login;

import Catalog.ViewCatalog;
import client.GuiExtensions;
import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class UserMain extends GuiExtensions {

	// Button
	@FXML
	private Button btnLogout;

	public void start() throws Exception {

		Main.setUserMainControl((UserMain) createAndDefinedFxmlWindow("UserMain.fxml", "User panel"));

	}
	
	public void clickViewCatalog_UserMain_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			(new ViewCatalog()).start(Main.getUserMainControl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clickLogoutButton() {

		logoutApplicationClient(btnLogout);
	}

}