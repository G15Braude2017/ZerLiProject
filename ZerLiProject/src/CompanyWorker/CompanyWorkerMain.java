package CompanyWorker;

import Catalog.ViewCatalog;
import client.GuiExtensions;
import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class CompanyWorkerMain extends GuiExtensions{

	// Button
			@FXML
			private Button btnLogout;

			private static EditCatalog EditCatalogControl;

			public void start() throws Exception {

				Main.setCompanyWorkerMainControl(
						(CompanyWorkerMain) createAndDefinedFxmlWindow("CompanyWorkerMain.fxml", "Company worker panel"));

				EditCatalogControl = new EditCatalog();
			}

			public void click_EditCatalog_btn(ActionEvent event) {

				((Node) event.getSource()).getScene().getWindow().hide();

				try {
					EditCatalogControl.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			public void clickViewCatalog_CompanyWorkerMain_btn(ActionEvent event) {

				((Node) event.getSource()).getScene().getWindow().hide();

				try {
					(new ViewCatalog()).start(Main.getCompanyWorkerMainControl());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			public void clickLogoutButton() {

				logoutApplicationClient(btnLogout);
			}
			
			public static EditCatalog getEditCatalogControl() {
				return EditCatalogControl;
			}

			public static void setEditCatalogControl(EditCatalog editCatalogControl) {
				EditCatalogControl = editCatalogControl;
			}
			
			
}

