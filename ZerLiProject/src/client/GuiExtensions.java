package client;

import java.io.IOException;
import java.util.ArrayList;

import clientServerCommon.PacketClass;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public abstract class GuiExtensions implements GuiIF{

	private static Window currentWindowToCloseInLogout;

	public Object createAndDefinedFxmlWindow(String fxmlPath, String windowTitle) throws Exception {

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
					if (Main.getLoginLogicControl() != null
							&& Main.getLoginLogicControl().getNewUser().getConnected()) {

						sendDisconnectStatusToServer(event);

					} else
						System.exit(0);

				} catch (Exception e) {
					System.exit(0);
				}
			}
		});

		return fxmlLoader.getController();
	}

	protected void updateStatusLabel(String message, boolean red_green, Label statusLabel) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update GUI
				System.out.println(message);
				statusLabel.setText(message);

				if (red_green)
					statusLabel.setTextFill(Paint.valueOf(Main.RED));
				else
					statusLabel.setTextFill(Paint.valueOf(Main.GREEN));

			}
		});
	}

	protected void verificateApplicationClose(PacketClass packet) {
		if (packet.getSuccessSql()) {
			System.exit(0);
		}
	}

	public void logoutApplicationClient(Button logoutBtn) {

		try {
			currentWindowToCloseInLogout = logoutBtn.getScene().getWindow();

			PacketClass packet = new PacketClass(
					Main.UPDATECommandStatement + "Users" + Main.SETCommandStatement + "Connected= 0"
							+ Main.WHERECommmandStatement + "userName='"
							+ Main.getLoginLogicControl().getNewUser().getUserName() + "' AND password='"
							+ Main.getLoginLogicControl().getNewUser().getPassword() + "';",
					Main.LoginVerificateLogoutApplication, Main.WRITE);

			try {
				Main.getClientConsolHandle().sendSqlQueryToServer(packet);
				System.out.println("User " + Main.getLoginLogicControl().getNewUser().getUserName() + " disconnected");
			} catch (Exception e) {
				System.out.println("Failed connect sever, must diconnect before exit");

				// try open client again
				try {
					Main.setClientConsolHandle(new ClientConsole(Main.clientDefaultHost, Main.DEFAULT_PORT));
					System.out.println("Success client connect");
					Main.setConnectionflag(true);
				} catch (IOException ex) {
					System.out.println("Error: Can't setup connection! Check host and port.");
					Main.setConnectionflag(false);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void logoutApplicationServer(PacketClass packet) {

		if (packet.getSuccessSql()) {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						Main.getLoginLogicControl().start(true);
						currentWindowToCloseInLogout.hide();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		} else {
			// Sql command failed
			System.out.println("Failed connect sever, must diconnect before exit");

		}
	}

	public void disableGui(boolean tf , Node...nodes) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				for (Node nd : nodes)
					nd.setDisable(tf);

			}
		});
		
	}
	
	public void visibleGui(boolean tf , Node...nodes) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				for (Node nd : nodes)
					nd.setVisible(tf);

			}
		});
		
	}
	
	
	public void addItemsToComboBox(ComboBox<String> cbItem , String...strs) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				cbItem.getItems().addAll(strs);

			}
		});
		
	}
	
	public void openNewWindowAndClosePrevious(Node currentWindowNode, GuiIF newWindow)
	{
		currentWindowNode.getScene().getWindow().hide();
		
		try {
			newWindow.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//////////////////////////////////
	// INTERNAL FUNCTIONS
	//////////////////////////////////

	private void sendDisconnectStatusToServer(WindowEvent event) {

		PacketClass packet = new PacketClass(
				Main.UPDATECommandStatement + "Users" + Main.SETCommandStatement + "Connected= 0"
						+ Main.WHERECommmandStatement + "userName='"
						+ Main.getLoginLogicControl().getNewUser().getUserName() + "' AND password='"
						+ Main.getLoginLogicControl().getNewUser().getPassword() + "';",
				Main.LoginVerificateCloseApplication, Main.WRITE);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
			System.out.println("User " + Main.getLoginLogicControl().getNewUser().getUserName() + " disconnected");
			event.consume();
		} catch (Exception e) {
			System.out.println("Failed connect sever, must diconnect before exit");
			event.consume();

			// try open client again
			try {
				Main.setClientConsolHandle(new ClientConsole(Main.clientDefaultHost, Main.DEFAULT_PORT));
				System.out.println("Success client connect");
				Main.setConnectionflag(true);
			} catch (IOException ex) {
				System.out.println("Error: Can't setup connection! Check host and port.");
				Main.setConnectionflag(false);
			}
		}

	}

	
	public abstract void start() throws Exception ;

}
