package server;

import java.io.FileInputStream;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Application{

	
	final public static int DEFAULT_PORT = 5555;

	private static String currentPath;
	private static int port = 0; // Port to listen on
	private static String userName;
	private static String password;
	private static String sqlHost;
	private static String sqlPort;
	private static String sqlDbName;
	
	private static ServerPanel ServerPanelControl;
	

	public static void main(String[] args) {
		
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT;
		}
		
		try {
			userName = args[1]; // Get port from command line
		} catch (Throwable t) {
			userName = "root";
		}
		
		try {
			password = args[2]; // Get port from command line
		} catch (Throwable t) {
			password = "Braude";
		}
		
		try {
			sqlHost = args[3]; // Get port from command line
		} catch (Throwable t) {
			sqlHost = "localhost";
		}
		
		try {
			sqlPort = args[4]; // Get port from command line
		} catch (Throwable t) {
			sqlPort = "3306";
		}
		
		try {
			sqlDbName = args[5]; // Get port from command line
		} catch (Throwable t) {
			sqlDbName = "sakila";
		}
		
		Properties p = new Properties();
		
	    try {
			p.load(new FileInputStream("ServerProperties.ini"));
			currentPath = System.getProperty("Login.dir");
			userName = p.getProperty("userName");
			password = p.getProperty("password");
			sqlHost = p.getProperty("sqlHost");
			sqlPort = p.getProperty("sqlPort");
			sqlDbName = p.getProperty("sqlDbName");
			port = Integer.parseInt(p.getProperty("port"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    finally {
	    	// Run gui
	    	launch(args);
	    }
		
		
	}

	@Override
	public void start(Stage arg0) throws Exception { 
								  		
				ServerPanel frameInstance = new ServerPanel(); // create StudentFrame
				
				ServerPanelControl = frameInstance;
				
				frameInstance.start(arg0);
	}
	
	
	public static String getCurrentPath() {
		return currentPath;
	}
	
	public static ServerPanel getServerPanelControl() {
		return ServerPanelControl;
	}

	public static void setServerPanelControl(ServerPanel serverPanelControl) {
		ServerPanelControl = serverPanelControl;
	}
	
	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		ServerMain.port = port;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		ServerMain.userName = userName;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		ServerMain.password = password;
	}

	public static String getSqlHost() {
		return sqlHost;
	}

	public static void setSqlHost(String sqlHost) {
		ServerMain.sqlHost = sqlHost;
	}

	public static String getSqlPort() {
		return sqlPort;
	}

	public static void setSqlPort(String sqlPort) {
		ServerMain.sqlPort = sqlPort;
	}

	public static String getSqlDbName() {
		return sqlDbName;
	}

	public static void setSqlDbName(String sqlDbName) {
		ServerMain.sqlDbName = sqlDbName;
	}
	
	
	

}
