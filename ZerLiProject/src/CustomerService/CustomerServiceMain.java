package CustomerService;


import java.util.ArrayList;

import Catalog.ViewCatalog;
import Login.LoginMain;
import client.GuiExtensions;
import client.GuiIF;
import client.Main;
import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomerServiceMain extends GuiExtensions{
	
	//Button
	@FXML
	private Button btnLogout;
	
	@FXML
	private Label lblStatus;

	private static CreateSurvey CreateSurveyControl;
	private static SaveSurveyConclusion SaveSurveyConclusionControl;
	private static OpenNewComplaint OpenNewComplaintControl;
	private static FollowComplaint FollowComplaintControl;
	

	public void start() throws Exception {
		
		Main.setCustomerServiceMainControl((CustomerServiceMain) createAndDefinedFxmlWindow("CustomerServiceMain.fxml", "Customer service panel" ));

		CreateSurveyControl = new CreateSurvey();
		SaveSurveyConclusionControl = new SaveSurveyConclusion();
		FollowComplaintControl = new FollowComplaint();
		OpenNewComplaintControl = new OpenNewComplaint();
		
		initializeComplaintStatusLabel();
		
	}

	
	private void initializeComplaintStatusLabel()
	{
		String CurrentWorkerID = Main.getLoginLogicControl().getNewUser().getUserName();

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "COUNT(OrderID)" + Main.FROMCommmandStatement + "complaints"
						+ Main.WHERECommmandStatement + "WorkerID = '" + CurrentWorkerID + "' AND State = 0;",
				Main.UpdateComplaintNumberStatus, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("----", true,
					Main.getCustomerServiceMainControl().lblStatus);
		}
	}
	
	
	public void initialize_CustomerServiceMain_ComplaintNumberStatusServer(PacketClass packet)
	{
		ArrayList<ArrayList<String>> DataList;
		int i;
		String SurveyIDstr;

		if (packet.getSuccessSql()) {

				DataList = (ArrayList<ArrayList<String>>) packet.getResults();

				if (DataList == null) {
					updateStatusLabel("No opened complaints", false,
							Main.getCustomerServiceMainControl().lblStatus);
				} else {

					updateStatusLabel(DataList.get(0).get(0) + " opened complaints", true,
							Main.getCustomerServiceMainControl().lblStatus);

				}

		} else {
			// Sql command failed
			updateStatusLabel("----", true,
					Main.getCustomerServiceMainControl().lblStatus);

		}
	}
	
	
	
	public void click_CreateNewSurvey_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		
		try {
			CreateSurveyControl.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void click_SaveSurveyConclusion_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		
		try {
			SaveSurveyConclusionControl.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void click_FollowComplaint_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		
		try {
			FollowComplaintControl.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void click_OpenNewComplaint_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		
		try {
			OpenNewComplaintControl.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void clickViewCatalog_CustomerServiceMain_btn(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();

		try {
			(new ViewCatalog()).start(Main.getCustomerServiceMainControl());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clickLogoutButton() {
		
		logoutApplicationClient(btnLogout);
	}

	public static CreateSurvey getCreateSurveyControl() {
		return CreateSurveyControl;
	}

	public static void setCreateSurveyControl(CreateSurvey createSurveyControl) {
		CreateSurveyControl = createSurveyControl;
	}

	public static SaveSurveyConclusion getSaveSurveyConclusionControl() {
		return SaveSurveyConclusionControl;
	}

	public static void setSaveSurveyConclusionControl(SaveSurveyConclusion saveSurveyConclusionControl) {
		SaveSurveyConclusionControl = saveSurveyConclusionControl;
	}

	public static OpenNewComplaint getOpenNewComplaintControl() {
		return OpenNewComplaintControl;
	}

	public static void setOpenNewComplaintControl(OpenNewComplaint openNewComplaintControl) {
		OpenNewComplaintControl = openNewComplaintControl;
	}

	public static FollowComplaint getFollowComplaintControl() {
		return FollowComplaintControl;
	}

	public static void setFollowComplaintControl(FollowComplaint followComplaintControl) {
		FollowComplaintControl = followComplaintControl;
	}
	

}
