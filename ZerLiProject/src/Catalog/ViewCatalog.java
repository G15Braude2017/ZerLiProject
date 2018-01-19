package Catalog;

import java.util.ArrayList;
import client.Main;
import clientServerCommon.PacketClass;
import Catalog.catalog;
import Login.LoginMain;
import ShopWorker.ShopWorkerMain;
import client.GuiExtensions;
import client.GuiIF;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ViewCatalog extends GuiExtensions {

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
	private Label lblProductName5;

	@FXML
	private Label lblProductName6;

	@FXML
	private Label lblProductType1;

	@FXML
	private Label lblProductType2;

	@FXML
	private Label lblProductType3;

	@FXML
	private Label lblProductType4;

	@FXML
	private Label lblProductType5;

	@FXML
	private Label lblProductType6;

	@FXML
	private Label lblProductPrice1;

	@FXML
	private Label lblProductPrice2;

	@FXML
	private Label lblProductPrice3;

	@FXML
	private Label lblProductPrice4;

	@FXML
	private Label lblProductPrice5;

	@FXML
	private Label lblProductPrice6;

	@FXML
	private Label lblProductSale1;

	@FXML
	private Label lblProductSale2;

	@FXML
	private Label lblProductSale3;

	@FXML
	private Label lblProductSale4;

	@FXML
	private Label lblProductSale5;

	@FXML
	private Label lblProductSale6;

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
	private ImageView image5;

	@FXML
	private ImageView image6;

	@FXML
	private ImageView OrderImage1;

	@FXML
	private ImageView OrderImage2;

	@FXML
	private ImageView OrderImage3;

	@FXML
	private ImageView OrderImage4;

	@FXML
	private ImageView OrderImage5;

	@FXML
	private ImageView OrderImage6;
	// Buttons
	@FXML
	private Button btnMyShopping;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnBackCatalog;

	@FXML
	private Button btnNextCatalog;

	// Previous window class
	private GuiIF pWindow = null;

	private ArrayList<catalog> itemsList = new ArrayList<catalog>();
	private int counter = 0;

	public void start(GuiIF prevWindow) throws Exception {

		Main.setViewCtalogControl((ViewCatalog) createAndDefinedFxmlWindow("Catalog.fxml", "Catalog"));

		Main.getViewCtalogControl().pWindow = prevWindow;

		getCatalogData();
	}

	public void getCatalogData() {

		String sqlCommand;
		int StoreID = Main.getLoginLogicControl().getNewUser().getStoreID();
		setAllUnvisible();

		if (StoreID == -1)
			sqlCommand = Main.SELECTCommandStatement + "*" + Main.FROMCommmandStatement + "itemcatalog";
		else
			sqlCommand = Main.SELECTCommandStatement + "*" + Main.FROMCommmandStatement + "itemcatalog"
					+ Main.WHERECommmandStatement + "storeID='" + StoreID + "'";

		PacketClass packet = new PacketClass(sqlCommand, Main.ViewCatalog, Main.READ);

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
					File index = new File(System.getProperty("Login.dir") + "\\catalog.tempimages");
					if (index.exists()) {
						String[] entries = index.list();
						for (String s : entries) {
							File currentFile = new File(index.getPath(), s);
							currentFile.delete();
						}
						index.delete();
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}

				localPath = System.getProperty("user.dir") + "\\catalog.tempimages";
				new File(localPath).mkdir();

				for (int i = 0; i < dataList.size(); i++) {
					if (!dataList.get(i).get(0).isEmpty() && !dataList.get(i).get(1).isEmpty()
							&& !dataList.get(i).get(2).isEmpty() && !dataList.get(i).get(3).isEmpty()
							&& !dataList.get(i).get(5).isEmpty()) {
						imageLocalPath = localPath + "\\" + dataList.get(i).get(0) + ".png";

						// Try save image
						try (FileOutputStream fileOuputStream = new FileOutputStream(imageLocalPath)) {
							fileOuputStream.write(packet.fileList.get(i).mybytearray);
						} catch (Exception e) {
							imageLocalPath = null;
						}

						try {
							int intConvertType, intConvertId, intConvertSale;
							float flConvert;

							intConvertType = Integer.parseInt(dataList.get(i).get(1));
							flConvert = Float.parseFloat(dataList.get(i).get(2));
							intConvertId = Integer.parseInt(dataList.get(i).get(3));
							if (dataList.get(i).get(4) != null)
								intConvertSale = Integer.parseInt(dataList.get(i).get(4));
							else
								intConvertSale = 0;

							itemsList.add(new catalog(imageLocalPath, dataList.get(i).get(0), intConvertType, flConvert,
									intConvertId, intConvertSale));
						} catch (Exception e) {
							updateStatusLabel("One or more of items is invalid", true,
									Main.getViewCtalogControl().lblNote);
						}

					}

					else
						updateStatusLabel("One or more of items is invalid", true, Main.getViewCtalogControl().lblNote);
				}

				/*try {
					Thread.sleep(1000); // wait for download image done
				} catch (InterruptedException e) {
					;
				}*/

				if (itemsList.size() <= 6)
					Display(itemsList.size());
				else {
					Display(6);
					visibleGui(true, Main.getViewCtalogControl().btnNextCatalog);
				}

			} else
				updateStatusLabel("The catalog is empty for this Login", true, Main.getViewCtalogControl().lblNote);
		} else
			updateStatusLabel("Could not open catalog", true, Main.getViewCtalogControl().lblNote);
	}

	public void Display(int itemsInPage) {
		setAllUnvisible();

		if (itemsInPage >= 1) {

			visibleGui(true, Main.getViewCtalogControl().lblProductName1, Main.getViewCtalogControl().lblProductType1,
					Main.getViewCtalogControl().lblProductPrice1, Main.getViewCtalogControl().image1,
					Main.getViewCtalogControl().OrderImage1, Main.getViewCtalogControl().btnMyShopping);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					if (itemsList.get(counter).getSale() != 0) {
						lblProductSale1.setVisible(true);
						lblProductSale1.setText("SALE: " + itemsList.get(counter).getSale() + "% !!!");
					}

					lblProductName1.setText(itemsList.get(counter).getItemName());

					lblProductType1.setText("" + itemsList.get(counter).getType());

					lblProductPrice1.setText("" + itemsList.get(counter).getPrice());

					Main.getViewCtalogControl().image1
							.setImage(new Image("file:" + itemsList.get(counter).getItemImage()));

				}
			});
		}

		if (itemsInPage >= 2) {

			visibleGui(true, Main.getViewCtalogControl().lblProductName2, Main.getViewCtalogControl().lblProductType2,
					Main.getViewCtalogControl().lblProductPrice2, Main.getViewCtalogControl().image2,
					Main.getViewCtalogControl().OrderImage2);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					if (itemsList.get(counter + 1).getSale() != 0) {
						lblProductSale2.setVisible(true);
						lblProductSale2.setText("SALE: " + itemsList.get(counter + 1).getSale() + "% !!!");
					}

					lblProductName2.setText(itemsList.get(counter + 1).getItemName());

					lblProductType2.setText("" + itemsList.get(counter + 1).getType());

					lblProductPrice2.setText("" + itemsList.get(counter + 1).getPrice());

					Main.getViewCtalogControl().image2
							.setImage(new Image("file:" + itemsList.get(counter + 1).getItemImage()));
				}
			});
		}

		if (itemsInPage >= 3) {

			visibleGui(true, Main.getViewCtalogControl().lblProductName3, Main.getViewCtalogControl().lblProductType3,
					Main.getViewCtalogControl().lblProductPrice3, Main.getViewCtalogControl().image3,
					Main.getViewCtalogControl().OrderImage3);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					if (itemsList.get(counter + 2).getSale() != 0) {
						lblProductSale3.setVisible(true);
						lblProductSale3.setText("SALE: " + itemsList.get(counter + 2).getSale() + "% !!!");
					}

					lblProductName3.setText(itemsList.get(counter + 2).getItemName());

					lblProductType3.setText("" + itemsList.get(counter + 2).getType());

					lblProductPrice3.setText("" + itemsList.get(counter + 2).getPrice());

					Main.getViewCtalogControl().image3
							.setImage(new Image("file:" + itemsList.get(counter + 2).getItemImage()));

				}
			});
		}

		if (itemsInPage >= 4) {

			visibleGui(true, Main.getViewCtalogControl().lblProductName4, Main.getViewCtalogControl().lblProductType4,
					Main.getViewCtalogControl().lblProductPrice4, Main.getViewCtalogControl().image4,
					Main.getViewCtalogControl().OrderImage4);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					if (itemsList.get(counter + 3).getSale() != 0) {
						lblProductSale4.setVisible(true);
						lblProductSale4.setText("SALE: " + itemsList.get(counter + 3).getSale() + "% !!!");
					}

					lblProductName4.setText(itemsList.get(counter + 3).getItemName());

					lblProductType4.setText("" + itemsList.get(counter + 3).getType());

					lblProductPrice4.setText("" + itemsList.get(counter + 3).getPrice());

					Main.getViewCtalogControl().image4
							.setImage(new Image("file:" + itemsList.get(counter + 3).getItemImage()));

				}
			});
		}

		if (itemsInPage >= 5) {

			visibleGui(true, Main.getViewCtalogControl().lblProductName5, Main.getViewCtalogControl().lblProductType5,
					Main.getViewCtalogControl().lblProductPrice5, Main.getViewCtalogControl().image5,
					Main.getViewCtalogControl().OrderImage5);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					if (itemsList.get(counter + 4).getSale() != 0) {
						lblProductSale5.setVisible(true);
						lblProductSale5.setText("SALE: " + itemsList.get(counter + 4).getSale() + "% !!!");
					}

					lblProductName5.setText(itemsList.get(counter + 4).getItemName());

					lblProductType5.setText("" + itemsList.get(counter + 4).getType());

					lblProductPrice5.setText("" + itemsList.get(counter + 4).getPrice());

					Main.getViewCtalogControl().image5
							.setImage(new Image("file:" + itemsList.get(counter + 4).getItemImage()));

				}
			});
		}

		if (itemsInPage >= 6) {

			visibleGui(true, Main.getViewCtalogControl().lblProductName6, Main.getViewCtalogControl().lblProductType6,
					Main.getViewCtalogControl().lblProductPrice6, Main.getViewCtalogControl().image6,
					Main.getViewCtalogControl().OrderImage6);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					if (itemsList.get(counter + 5).getSale() != 0) {
						lblProductSale6.setVisible(true);
						lblProductSale6.setText("SALE: " + itemsList.get(counter + 5).getSale() + "% !!!");
					}

					lblProductName6.setText(itemsList.get(counter + 5).getItemName());

					lblProductType6.setText("" + itemsList.get(counter + 5).getType());

					lblProductPrice6.setText("" + itemsList.get(counter + 5).getPrice());

					Main.getViewCtalogControl().image6
							.setImage(new Image("file:" + itemsList.get(counter + 5).getItemImage()));

				}
			});
		}

		if (!Main.getLoginLogicControl().getNewUser().getPermission().equals(Main.Permission.values()[2]))
			visibleGui(false, Main.getViewCtalogControl().OrderImage1, Main.getViewCtalogControl().OrderImage2,
					Main.getViewCtalogControl().OrderImage3, Main.getViewCtalogControl().OrderImage4,
					Main.getViewCtalogControl().OrderImage5, Main.getViewCtalogControl().OrderImage6,
					Main.getViewCtalogControl().btnMyShopping);

	}

	public void ClickNextCatalog() {
		// setAllUnvisible();

		counter += 6;
		int res = itemsList.size() - counter;
		if (res > 6) {
			Display(6);
		} else {
			Display(res);
			btnNextCatalog.setVisible(false);
		}
		Main.getViewCtalogControl().btnBackCatalog.setVisible(true);
	}

	public void ClickBackCatalog() {
		// setAllUnvisible();

		if (counter > 0) {
			counter -= 6;
			Display(6);
			if (counter == 0)
				btnBackCatalog.setVisible(false);
		}
		btnNextCatalog.setVisible(true);
	}

	public void click_ViewCatalog_backButton() {

		Stage stage = (Stage) Main.getViewCtalogControl().btnBack.getScene().getWindow();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stage.hide();

				try {
					Main.getViewCtalogControl().pWindow.start();
					Main.getViewCtalogControl().pWindow = null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void clickMyShopping() /// Customer
	{
		/*
		 * Stage stage = (Stage) btnBack.getScene().getWindow(); Platform.runLater(new
		 * Runnable() {
		 * 
		 * @Override public void run() { stage.hide();
		 * 
		 * try { (new CustomerMain()).start(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } });
		 */
	}

	public void clickAddOrder() /// Customer
	{
		/*
		 * Stage stage = (Stage) btnBack.getScene().getWindow(); Platform.runLater(new
		 * Runnable() {
		 * 
		 * @Override public void run() { stage.hide();
		 * 
		 * try { (new CustomerMain()).start(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } });
		 */
	}

	private void setAllUnvisible() {

		Main.getViewCtalogControl().image2.setVisible(false);
		Main.getViewCtalogControl().image1.setVisible(false);
		Main.getViewCtalogControl().image3.setVisible(false);
		Main.getViewCtalogControl().image4.setVisible(false);
		Main.getViewCtalogControl().image5.setVisible(false);
		Main.getViewCtalogControl().image6.setVisible(false);
		Main.getViewCtalogControl().lblProductName1.setVisible(false);
		Main.getViewCtalogControl().lblProductName2.setVisible(false);
		Main.getViewCtalogControl().lblProductName3.setVisible(false);
		Main.getViewCtalogControl().lblProductName4.setVisible(false);
		Main.getViewCtalogControl().lblProductName5.setVisible(false);
		Main.getViewCtalogControl().lblProductName6.setVisible(false);
		Main.getViewCtalogControl().lblProductType1.setVisible(false);
		Main.getViewCtalogControl().lblProductType2.setVisible(false);
		Main.getViewCtalogControl().lblProductType3.setVisible(false);
		Main.getViewCtalogControl().lblProductType4.setVisible(false);
		Main.getViewCtalogControl().lblProductType5.setVisible(false);
		Main.getViewCtalogControl().lblProductType6.setVisible(false);
		Main.getViewCtalogControl().lblProductPrice1.setVisible(false);
		Main.getViewCtalogControl().lblProductPrice2.setVisible(false);
		Main.getViewCtalogControl().lblProductPrice3.setVisible(false);
		Main.getViewCtalogControl().lblProductPrice4.setVisible(false);
		Main.getViewCtalogControl().lblProductPrice5.setVisible(false);
		Main.getViewCtalogControl().lblProductPrice6.setVisible(false);
		Main.getViewCtalogControl().OrderImage1.setVisible(false);
		Main.getViewCtalogControl().OrderImage2.setVisible(false);
		Main.getViewCtalogControl().OrderImage3.setVisible(false);
		Main.getViewCtalogControl().OrderImage4.setVisible(false);
		Main.getViewCtalogControl().OrderImage5.setVisible(false);
		Main.getViewCtalogControl().OrderImage6.setVisible(false);
		Main.getViewCtalogControl().lblProductSale1.setVisible(false);
		Main.getViewCtalogControl().lblProductSale2.setVisible(false);
		Main.getViewCtalogControl().lblProductSale3.setVisible(false);
		Main.getViewCtalogControl().lblProductSale4.setVisible(false);
		Main.getViewCtalogControl().lblProductSale5.setVisible(false);
		Main.getViewCtalogControl().lblProductSale6.setVisible(false);
		Main.getViewCtalogControl().btnBackCatalog.setVisible(false);
		Main.getViewCtalogControl().btnNextCatalog.setVisible(false);
		Main.getViewCtalogControl().btnMyShopping.setVisible(false);
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

	}

}
