package ShopWorker;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShopWorkerMain {

	private static FillCustomerAnswers fillCustomerAnswersHandle;
	
	
	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ShopWorkerMain.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Shop worker panel");
		stage.show();

		Main.setShopWorkerMainControl(fxmlLoader.getController());
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

	public static FillCustomerAnswers getFillCustomerAnswersHandle() {
		return fillCustomerAnswersHandle;
	}


	public static void setFillCustomerAnswersHandle(FillCustomerAnswers fillCustomerAnswersHandle) {
		ShopWorkerMain.fillCustomerAnswersHandle = fillCustomerAnswersHandle;
	}

	
}
