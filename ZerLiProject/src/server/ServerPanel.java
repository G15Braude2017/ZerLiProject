package server;

import java.net.URL;
import java.util.ResourceBundle;

import client.Main;
import gui.EditPuductInformation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ServerPanel implements Initializable{

	// Labels
	@FXML
	private Label lblINIPath;
	@FXML
	private Label lblListenPort;
	@FXML
	private Label lblSqlHost;
	@FXML
	private Label lblSqlPort;
	@FXML
	private Label lblSqlUsername;
	@FXML
	private Label lblSqlPassword;
	@FXML
	private Label lblDBName;
	
	@FXML
	private TextArea txtAreaConsol;
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// Update GUI
		lblINIPath.setText("INI Path: " + ServerMain.getCurrentPath());
		lblListenPort.setText("Listening port: " + ServerMain.getPort());
		lblSqlHost.setText("Host: " + ServerMain.getSqlHost());
		lblSqlPort.setText("Port: " + ServerMain.getSqlPort());
		lblSqlUsername.setText("Username: " + ServerMain.getUserName());
		lblSqlPassword.setText("Password: " + ServerMain.getPassword());
		lblDBName.setText("DataBase Name: " + ServerMain.getSqlDbName());
		
		txtAreaConsol.setText("");
		
	}
	
	
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("ServerPanel.fxml"));
		Parent root = (Parent) loader.load();

		ServerMain.setServerPanelControl((ServerPanel) (loader.getController()));

		Scene scene = new Scene(root);
		primaryStage.setTitle("Server Panel");
		primaryStage.setScene(scene);

		primaryStage.show();
		
		EchoServer.initializeServer();

	}
	
	
	public void UpdateConsol (String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				System.out.println(message);
				txtAreaConsol.setText(txtAreaConsol.getText() + "\n" + message);
			}
		});
	}
	
	
	
}
