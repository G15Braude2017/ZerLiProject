package client;

import gui.EditPuductInformation;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application{
	
	// Gui elements handles
	public static final int EditProductInformationSearchBtn = 1;
	public static final int EditProductInformationChangeBtn = 2;
	
	// Gui controls handles
	private static EditPuductInformation EditPuductInformationControl;
	
	// Sql command defines
	final public static String SELECTCommandStatement = "SELECT ";
	final public static String UPDATECommandStatement = "UPDATE ";
	final public static String SETCommandStatement = " SET ";
	final public static String FROMCommmandStatement = " FROM ";
	final public static String WHERECommmandStatement = " WHERE ";
	
	// Socket properties
	final public static int DEFAULT_PORT = 5555;
	
	// PacketClass defines
	public static final boolean READ = true;
	public static final boolean WRITE = false;
	
	// General defines
	private static String[] arguments;
	
	
	public static void main(String[] args) throws Exception {
		arguments = args;
		launch(args); 
	}
	
	
	@Override
	public void start(Stage arg0) throws Exception { 
								  		
				EditPuductInformation frameInstance = new EditPuductInformation(); // create StudentFrame
				
				EditPuductInformationControl = frameInstance;
				
				frameInstance.start(arg0);
	}
	
	
	public static String[] getArguments() {
		return arguments;
	}
	
	public static EditPuductInformation getFrameHandle() {
		return EditPuductInformationControl;
	}
	
	public static void setFrameHandle(EditPuductInformation handle) {
		EditPuductInformationControl = handle;
	}
	
	
}

