package SystemManager;


import Catalog.ViewCatalog;
import client.GuiExtensions;
import client.GuiIF;
import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class SystemManagerMain extends GuiExtensions{

	// Button
		@FXML
		private Button btnLogout;

		private static UpdateUserInformation UpdateUserInformationControl;

		public void start() throws Exception {

			Main.setSystemManagerMainControl(
					(SystemManagerMain) createAndDefinedFxmlWindow("SystemManagerMain.fxml", "System Manager panel"));

			UpdateUserInformationControl = new UpdateUserInformation();
		}

		public void click_UpdateUserInformation_btn(ActionEvent event) {

			((Node) event.getSource()).getScene().getWindow().hide();

			try {
				UpdateUserInformationControl.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void clickViewCatalog_SystemManagerMain_btn(ActionEvent event) {

			((Node) event.getSource()).getScene().getWindow().hide();

			try {
				(new ViewCatalog()).start(Main.getSystemManagerMainControl());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void clickLogoutButton() {

			logoutApplicationClient(btnLogout);
		}
		
		public static UpdateUserInformation getUpdateUserInformationControl() {
			return UpdateUserInformationControl;
		}

		public static void setUpdateUserInformationControl(UpdateUserInformation updateUserInformationControl) {
			UpdateUserInformationControl = updateUserInformationControl;
		}
}
