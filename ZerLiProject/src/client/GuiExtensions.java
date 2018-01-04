package client;

import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiExtensions {

	public Object createAndDefinedFxmlWindow(String fxmlPath, String windowTitle) throws Exception{
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle(windowTitle);
		stage.show();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				
				try {
					if (Main.getLoginLogicControl() != null && Main.getLoginLogicControl().getNewUser().getConnected()) {
						
						PacketClass packet = new PacketClass(
								Main.UPDATECommandStatement + "Users" + Main.SETCommandStatement + "Connected= 0"
										+ Main.WHERECommmandStatement + "userName='" + Main.getLoginLogicControl().getNewUser().getUserName()
										+ "' AND password='" + Main.getLoginLogicControl().getNewUser().getPassword() + "';",
								Main.UpdateStatusOfAnExistingUser, Main.WRITE);
						
						try {
							Main.getClientConsolHandle().sendSqlQueryToServer(packet);
							System.out.println("User " + Main.getLoginLogicControl().getNewUser().getUserName() + " disconnected");
						}catch(Exception e) {
							System.out.println("Failed connect sever, must diconnect before exit");
							event.consume();
						}
						
						
					} 
				} catch (Exception e) {
					; // TODO
				}
			}
		});
		
		return fxmlLoader.getController();
	}
	
	protected void updateStatusLabel(String message, boolean red_green , Label statusLabel) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				statusLabel.setText(message);
				statusLabel.setTextFill(Paint.valueOf(Main.RED));

			}
		});
	}

	
}
