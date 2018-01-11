package Catalog;

import java.util.ArrayList;
import client.Main;
import clientServerCommon.PacketClass;
import Catalog.catalog;
import client.GuiExtensions;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import user.loginLogic;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewCtalog extends GuiExtensions {

	// Labels
	@FXML
	private Label lblNote;
	
	@FXML
	private Label lblProductName1;
	
	@FXML
	private Label lblProductName2;
	
	@FXML
	private Label lblProductName3;
	
	@FXML
	private Label lblProductName4;
	
	@FXML
	private Label lblProductPrice1;
	
	@FXML
	private Label lblProductPrice2;
	
	@FXML
	private Label lblProductPrice3;
	
	@FXML
	private Label lblProductPrice4;
	
	// Image View
	@FXML
	private ImageView image1;
	
	@FXML
	private ImageView image2;
	
	@FXML
	private ImageView image3;
	
	@FXML
	private ImageView image4;

	@FXML
	private ImageView imageOrder1;
	
	@FXML
	private ImageView imageOrder2;
	
	@FXML
	private ImageView imageOrder3;
	
	@FXML
	private ImageView imageOrder4;
	
	// Buttons 
	@FXML
	private Button btnLoginout;

	@FXML
	private Button btnMyShopping;

	@FXML
	private Button btnBack;
	
	@FXML
	private Button btnBackCatalog;
	
	@FXML
	private Button btnNextCatalog;
	
	
	private ArrayList<catalog> itemsList = new ArrayList<catalog>();

	
	public void start() throws Exception {

		Main.setViewCtalogControl((ViewCtalog) createAndDefinedFxmlWindow("Catalog.fxml", "Catalog"));

		getCatalogData();
	}

	public void getCatalogData() {
		PacketClass packet = new PacketClass(Main.SELECTCommandStatement + "*" + Main.FROMCommmandStatement + "test",
				Main.ViewCatalog, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Could not open catalog", true, Main.getViewCtalogControl().lblNote);
		}
	}

	public void initializeCatalog(PacketClass packet) {
		if (packet.getSuccessSql()) {
			ArrayList<ArrayList<String>> dataList;
			dataList = (ArrayList<ArrayList<String>>) packet.getResults();
			String imageLocalPath;
			String localPath = null;

			if (dataList != null) {

				try {
					Files.delete(Paths.get(System.getProperty("user.dir")+"catalog.tempimages"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				
				localPath = System.getProperty("user.dir")+"\\catalog.tempimages";
				new File(localPath).mkdir();
				
				
				for(int i=0; i<dataList.size(); i++) {
					if(!dataList.get(i).get(1).isEmpty() && !dataList.get(i).get(0).isEmpty() && !dataList.get(i).get(2).isEmpty() && !dataList.get(i).get(3).isEmpty())
					{

						imageLocalPath = localPath + "\\" + dataList.get(i).get(2) + ".jpg";
						
						// Try save image
						try (FileOutputStream fileOuputStream = new FileOutputStream(imageLocalPath)) {
							fileOuputStream.write(packet.fileList.get(i).mybytearray);
				        } catch (Exception e) {
				            imageLocalPath = null;
				        }
						
						itemsList.add(new catalog(imageLocalPath, dataList.get(i).get(1), dataList.get(i).get(0), dataList.get(i).get(2) , dataList.get(i).get(3)));
						
						
					}
					else
						updateStatusLabel("One or more of items is invalid", true, Main.getViewCtalogControl().lblNote);
				}
				

				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update GUI
						Main.getViewCtalogControl().image1.setImage(new Image("file:"+ itemsList.get(0).getItemImage()));
					}
				});

			} else
				updateStatusLabel("The catalog is empty", true, Main.getViewCtalogControl().lblNote);
		} else
			updateStatusLabel("Could not open catalog", true, Main.getViewCtalogControl().lblNote);
	}

}
