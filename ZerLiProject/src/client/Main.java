package client;

import java.io.IOException;

import Catalog.ViewCatalog;
import CompanyManager.CompanyManagerMain;
import CompanyWorker.CompanyWorkerMain;
import Customer.CustomerInterface;
import CustomerService.CustomerServiceMain;
import Login.LoginMain;
import Login.UserMain;
import ServiceExpert.ServiceExpertMain;
import ShopWorker.ShopWorkerMain;
import StoreManager.StoreManagerMain;
import SystemManager.SystemManagerMain;
import gui.EditPuductInformation;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	// Gui elements handles
	public static final int EditProductInformationSearchBtn = 1;
	public static final int EditProductInformationChangeBtn = 2;

	public static final int CreateSurveyInitializeSurveyID = 3;
	public static final int CreateSurveyAddSurveyBtn = 4;

	public static final int FillCustomerAnswersInitializeSurveyID = 5;
	public static final int FillCustomerAnswersAddSurveyAnswersBtn = 6;
	public static final int FillCustomerAnswersCheckComboBox = 7;

	public static final int PullSurveyResultsInitializeSurveyID = 8;
	public static final int PullSurveyResultsAddConclusionBtn = 9;
	public static final int PullSurveyResultsCheckComboBoxSurveyID = 10;
	public static final int PullSurveyResultsCheckComboBoxConclusionText = 11;

	public static final int SaveSurveyConclusionCheckComboBoxSurveyID = 15;
	public static final int SaveSurveyConclusionGetConclusionText = 16;
	public static final int SaveSurveyConclusionApproveConclusionBtn = 17;

	public static final int OpenNewComplaintInitializeSurveyID = 20;
	public static final int OpenNewComplaintInitializeOrderID = 21;
	public static final int OpenNewComplaintCheckComboBoxOrderID = 22;
	public static final int OpenNewComplaintOpenComplaintBtn = 23;

	public static final int FollowComplaintInitializeOrderID = 30;
	public static final int FollowComplaintCheckComboBoxOrderID = 31;
	public static final int FollowComplaintConfirmComplaintBtn = 32;
	
	public static final int UpdateComplaintNumberStatus = 35;

	public static final int SystemManagerInitializeUserID = 40;
	public static final int SystemManagerInitializeStoreID = 41;
	public static final int SystemManagerClickUserID = 42;
	public static final int SystemManagerClickConfirmChanges = 43;

	public static final int InitializeCompanyManagerStoreIDcomboBox = 50;
	public static final int GetComplaintReportData = 51;
	public static final int GetIncomeReportData = 52;
	public static final int GetOrderReportData = 53;
	public static final int InitializeStoreManagerUsernamecomboBox = 55;
	public static final int CheckCustomerUsernameExist = 56;
	public static final int CreateNewCustomer = 57;

	public static final int SelfdefindItemFlowers = 60;
	public static final int SelfdefindItemBouqut =61;
	
	public static final int LoginCheckIfUserExists = 70;
	public static final int LoginUpdateStatusOfAnExistingUser = 71;
	public static final int LoginVerificateCloseApplication = 72;
	public static final int LoginVerificateLogoutApplication = 73;
	
	public static final int createOrderStoreIDCB =80;

	public static final int ViewCatalog = 90;
	
	public static final int EditCatalog = 100;
	public static final int DoneEditItem = 101;
	public static final int DoneRemoveItem = 102;
	public static final int DisplayText = 103;
	public static final int SaveNewItem = 104;
	public static final int SaveSizeStoreID = 105;
	public static final int SaveImage = 106;

	// Gui controls handles
	private static EditPuductInformation EditPuductInformationControl;
	private static CustomerServiceMain CustomerServiceMainControl;
	private static ShopWorkerMain ShopWorkerMainControl;
	private static CompanyManagerMain CompanyManagerMainControl;
	private static ServiceExpertMain ServiceExpertMainControl;
	private static StoreManagerMain StoreManagerMainControl;
	private static SystemManagerMain SystemManagerMainControl;
	private static ViewCatalog ViewCtalogControl;
	private static CustomerInterface CustomerMainControl;
	private static CompanyWorkerMain CompanyWorkerMainControl;
	private static LoginMain LoginLogicControl;
	private static UserMain UserMainControl;

	// Client control handle
	private static ClientConsole clientConsolHandle;

	// Sql command defines
	final public static String SELECTCommandStatement = "SELECT ";
	final public static String UPDATECommandStatement = "UPDATE ";
	final public static String SETCommandStatement = " SET ";
	final public static String FROMCommmandStatement = " FROM ";
	final public static String WHERECommmandStatement = " WHERE ";
	final public static String INSERTCommmandStatement = "INSERT INTO ";
	final public static String VALUESCommmandStatement = " VALUES ";
	final public static String DISTINCTCommandStatement = "DISTINCT ";
	final public static String DELETECommmandStatement = "DELETE ";

	// Socket properties
	final public static int DEFAULT_PORT = 5555;

	// PacketClass defines
	public static final boolean READ = true;
	public static final boolean WRITE = false;

	// enums
	public enum GreetingCardIs {
		yes, no
	};

	public enum OrderType {
		SelfDefined, CatalogType
	};

	public enum ItemType {
		FlowerArrangement, Pot, BridalBouquet, Bouquet
	};

	public enum IsOnSale {
		OnSale, NotOnSale
	};

	public enum PickUpOrShipping {shipping,Pickup};

	public enum ReceiptStatus {
		active, canceled_before_refund
	};

	public enum Permission {
		user, ShopWorker, Customer, Expert, CustomerService, SystemManager, CompanyManager, StoreManager, CompanyEmployee
	};

	// General defines
	private static String[] arguments;
	public static final String clientDefaultHost = "localhost";

	// Messages color
	public static final String RED = "red";
	public static final String GREEN = "green";

	private static boolean connectionflag = false;

	public static void main(String[] args) throws Exception {

		String host = "";
		int port; // The port number

		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}

		try {
			port = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			port = Main.DEFAULT_PORT;
		}

		try {

			clientConsolHandle = new ClientConsole(host, port);
			System.out.println("Success client connect");
			connectionflag = true;
		} catch (IOException ex) {
			System.out.println("Error: Can't setup connection! Check host and port.");
			Main.setConnectionflag(false);
		}

		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {

		LoginMain frameInstance = new LoginMain();

		LoginLogicControl = frameInstance;

		frameInstance.start(connectionflag);

	}

	public static CustomerServiceMain getCustomerServiceMainControl() {
		return CustomerServiceMainControl;
	}

	public static void setCustomerServiceMainControl(CustomerServiceMain customerServiceMainControl) {
		CustomerServiceMainControl = customerServiceMainControl;
	}

	public static String[] getArguments() {
		return arguments;
	}

	public static ClientConsole getClientConsolHandle() {
		return clientConsolHandle;
	}

	public static void setClientConsolHandle(ClientConsole clientConsolHandle) {
		Main.clientConsolHandle = clientConsolHandle;
	}

	public static ShopWorkerMain getShopWorkerMainControl() {
		return ShopWorkerMainControl;
	}

	public static void setShopWorkerMainControl(ShopWorkerMain shopWorkerMainControl) {
		ShopWorkerMainControl = shopWorkerMainControl;
	}

	public static CompanyManagerMain getCompanyManagerMainControl() {
		return CompanyManagerMainControl;
	}

	public static void setCompanyManagerMainControl(CompanyManagerMain companyManagerMainControl) {
		CompanyManagerMainControl = companyManagerMainControl;
	}

	public static ServiceExpertMain getServiceExpertMainControl() {
		return ServiceExpertMainControl;
	}

	public static void setServiceExpertMainControl(ServiceExpertMain serviceExpertMainControl) {
		ServiceExpertMainControl = serviceExpertMainControl;
	}

	public static StoreManagerMain getStoreManagerMainControl() {
		return StoreManagerMainControl;
	}

	public static void setStoreManagerMainControl(StoreManagerMain storeManagerMainControl) {
		StoreManagerMainControl = storeManagerMainControl;
	}

	public static LoginMain getLoginLogicControl() {
		return LoginLogicControl;
	}

	public static void setLoginLogicControl(LoginMain loginLogicControl) {
		LoginLogicControl = loginLogicControl;
	}

	public static boolean isConnectionflag() {
		return connectionflag;
	}

	public static void setConnectionflag(boolean connectionflag) {
		Main.connectionflag = connectionflag;
	}

	public static SystemManagerMain getSystemManagerMainControl() {
		return SystemManagerMainControl;
	}

	public static void setSystemManagerMainControl(SystemManagerMain systemManagerMainControl) {
		SystemManagerMainControl = systemManagerMainControl;
	}

	public static ViewCatalog getViewCtalogControl() {
		return ViewCtalogControl;
	}

	public static void setViewCtalogControl(ViewCatalog viewCtalogControl) {
		ViewCtalogControl = viewCtalogControl;
	}

	public static CustomerInterface getCustomerMainControl() {
		return CustomerMainControl;
	}

	public static void setCustomerMainControl(CustomerInterface CostumereMainControl) {
		CustomerMainControl = CostumereMainControl;
	}

	public static CompanyWorkerMain getCompanyWorkerMainControl() {
		return CompanyWorkerMainControl;
	}

	public static void setCompanyWorkerMainControl(CompanyWorkerMain companyWorkerMainControl) {
		CompanyWorkerMainControl = companyWorkerMainControl;
	}
	
	public static UserMain getUserMainControl() {
		return UserMainControl;
	}

	public static void setUserMainControl(UserMain userMainControl) {
		UserMainControl = userMainControl;
	}

}
