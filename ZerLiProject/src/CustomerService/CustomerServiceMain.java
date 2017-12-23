package CustomerService;


import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerServiceMain {

	private static CreateSurvey CreateSurveyHandle;


	public void start() throws Exception {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerServiceMain.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Customer service panel");
		stage.show();

		Main.setCustomerServiceMainControl(fxmlLoader.getController());
		CreateSurveyHandle = new CreateSurvey();
	}

	public void click_CreateNewSurvey_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		
		try {
			CreateSurveyHandle.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static CreateSurvey getCreateSurveyControl() {
		return CreateSurveyHandle;
	}

	public static void setCreateSurveyControl(CreateSurvey createSurveyControl) {
		CreateSurveyHandle = createSurveyControl;
	}

}
