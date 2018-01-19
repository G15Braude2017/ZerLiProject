package CompanyWorker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Catalog.catalog;
import client.Main;
import client.Main.ItemType;
import clientServerCommon.MyFile;
import clientServerCommon.PacketClass;
import client.GuiExtensions;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EditCatalog extends GuiExtensions {
	// Labels
	@FXML
	private Label lblNote;

	@FXML
	private Label lblTitelName;

	@FXML
	private Label lblTitelPrice;

	@FXML
	private Label lblTitelType;

	@FXML
	private Label lblTitelSale;

	@FXML
	private Label lblTitelColor;

	@FXML
	private Label lblTitelStoreID;

	// Buttons
	@FXML
	private Button btnAddImage;

	@FXML
	private Button btnRemoveItem;

	@FXML
	private Button btnEditItem;

	@FXML
	private Button btnConfirmChanges;

	// Text field
	@FXML
	private TextField txtItemName;

	@FXML
	private TextField txtItemPrice;

	@FXML
	private TextField txtItemColor;

	@FXML
	private TextField txtSale;

	@FXML
	private TextField txtImagePath;

	@FXML
	private ComboBox<String> cmbitemID;

	@FXML
	private ComboBox<String> cmbItemType;

	@FXML
	private ComboBox<String> cmbStoreID;

	// Image View
	@FXML
	private ImageView ivUploadImage;

	private boolean guiConfirmed;
	private int workingMod; // 1 remove , 2 edit , 3 create

	public void start() throws Exception {

		Main.getCompanyWorkerMainControl()
				.setEditCatalogControl((EditCatalog) createAndDefinedFxmlWindow("EditCatalog.fxml", "Edit Catalog"));

		for (ItemType type : Main.ItemType.values()) {
			addItemsToComboBox(Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbItemType, type + "");
		}

		getCatalogData();
	}

	public void getCatalogData() {

		setAllUnvisible(false);
		guiConfirmed = true;
		workingMod = -1;

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "ItemID" + Main.FROMCommmandStatement + "itemcatalog", Main.EditCatalog,
				Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Could not open catalog", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}
	}

	public void initializeCatalog(PacketClass packet) {
		if (packet.getSuccessSql()) {
			ArrayList<ArrayList<String>> dataList;
			dataList = (ArrayList<ArrayList<String>>) packet.getResults();

			if (dataList != null) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update GUI
						Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbitemID.getItems().clear();
						Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbStoreID.getItems().clear();
					}
				});

				for (int i = 0; i < dataList.size(); i++) {
					addItemsToComboBox(Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbitemID,
							dataList.get(i).get(0));
				}

				PacketClass packetStoreID = new PacketClass(
						Main.SELECTCommandStatement + "storeID" + Main.FROMCommmandStatement + "stores",
						Main.SaveSizeStoreID, Main.READ);

				try {
					Main.getClientConsolHandle().sendSqlQueryToServer(packetStoreID);
				} catch (Exception e) {
					updateStatusLabel("Could not open list of StoreID", true,
							Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
				}
			} else
				updateStatusLabel("The list of ItemID is empty", true,
						Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		} else
			updateStatusLabel("Could not open list of ItemID", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);

	}

	public void SaveSizeStoreID(PacketClass packet) {
		if (packet.getSuccessSql()) {
			ArrayList<ArrayList<String>> dataList;
			if (packet.getResults() != null) {
				dataList = (ArrayList<ArrayList<String>>) packet.getResults();

				for (int i = 0; i < dataList.size(); i++) {
					addItemsToComboBox(Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbStoreID,
							dataList.get(i).get(0));
				}
			} else {
				updateStatusLabel("Stores list is empty", true,
						Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
				guiConfirmed = false;
			}
		} else {
			updateStatusLabel("Client connection error", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}

	}

	public void ClickComboBox() {

		setAllUnvisible(false);
		disableGui(false, Main.getCompanyWorkerMainControl().getEditCatalogControl().btnEditItem,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().btnRemoveItem);

		if (!cmbitemID.getSelectionModel().isEmpty())
			visibleGui(true, Main.getCompanyWorkerMainControl().getEditCatalogControl().btnEditItem,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().btnRemoveItem);

	}

	public void click_EditCatalog_Removebtn() {

		setAllUnvisible(false);
		Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod = 1;
		visibleGui(true, Main.getCompanyWorkerMainControl().getEditCatalogControl().btnConfirmChanges);
	}

	public void ClickRemove() {
		PacketClass packet = new PacketClass(Main.DELETECommmandStatement + Main.FROMCommmandStatement + "itemcatalog"
				+ Main.WHERECommmandStatement + "ItemID='"
				+ Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbitemID.getValue() + "'",
				Main.DoneRemoveItem, Main.WRITE);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}
	}

	public void DoneMessRemove(PacketClass packet) {
		if (packet.getSuccessSql()) {
			updateStatusLabel("Remove was completed", false,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
			getCatalogData();
		} else {
			updateStatusLabel("Remove was failed", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}
	}

	public void ClickEdit() {

		setAllUnvisible(true);
		Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod = 2;

		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "Image, ItemName, Type, Price, sale, storeID, itemColor"
						+ Main.FROMCommmandStatement + "itemcatalog" + Main.WHERECommmandStatement + "ItemID="
						+ Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbitemID.getValue(),
				Main.DisplayText, Main.READ);

		try {
			Main.getClientConsolHandle().sendSqlQueryToServer(packet);
		} catch (Exception e) {
			updateStatusLabel("Client connection error", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}
	}

	public void ClickEditDisplay(PacketClass packet) {
		if (packet.getSuccessSql()) {

			ArrayList<ArrayList<String>> dataList;
			dataList = (ArrayList<ArrayList<String>>) packet.getResults();

			if (dataList != null) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update GUI

						String imageLocalPath = System.getProperty("user.dir")
								+ "\\catalog.tempimages\\tempEditImage.png";

						// Try save image
						try (FileOutputStream fileOuputStream = new FileOutputStream(imageLocalPath)) {
							fileOuputStream.write(packet.fileList.get(0).mybytearray);
						} catch (Exception e) {
							imageLocalPath = null;
						}

						txtItemName.setText(dataList.get(0).get(0));

						try {
							Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbItemType.getSelectionModel()
									.select("" + Main.ItemType.values()[Integer.parseInt(dataList.get(0).get(1))]);
						} catch (Exception e) {
						}
						;

						txtItemPrice.setText(dataList.get(0).get(2));
						txtSale.setText(dataList.get(0).get(3));

						try {
							Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbStoreID.getSelectionModel()
									.select("" + Integer.parseInt(dataList.get(0).get(4)));
						} catch (Exception e) {
						}
						;

						txtItemColor.setText(dataList.get(0).get(5));

						if (imageLocalPath == null)
							Main.getCompanyWorkerMainControl().getEditCatalogControl().ivUploadImage
									.setImage(new Image("file:" + System.getProperty("user.dir")
											+ "\\ProjectImages\\300px-No_image_available.png"));
						else
							Main.getCompanyWorkerMainControl().getEditCatalogControl().ivUploadImage
									.setImage(new Image("file:" + imageLocalPath));
					}
				});
			} else
				updateStatusLabel("Item not exist or invalid", true,
						Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		} else
			updateStatusLabel("Invalid item data", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);

	}

	public void click_EditCatalog_ConfirmChangesBtn() {

		PacketClass packet = null;
		String STRsale = "", STRcolor = "";
		int type = 0;

		if (Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 1) {
			ClickRemove();
		} else if (Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 2
				|| Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 3) {

			try {

				if (Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemName.getText() != null) {
					if (Float.parseFloat(
							Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemPrice.getText()) > 0
							&& Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemPrice
									.getText() != null) {

						if (Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemColor.getText()
								.isEmpty()) {
							STRcolor = "NULL";
						} else
							STRcolor = "'"
									+ Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemColor.getText()
									+ "'";

						try {

							if (Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbItemType.getValue()
									.equals("FlowerArrangement"))
								type = 0;
							else if (Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbItemType.getValue()
									.equals("Pot"))
								type = 1;
							else if (Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbItemType.getValue()
									.equals("BridalBouquet"))
								type = 2;
							else if (Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbItemType.getValue()
									.equals("Bouquet"))
								type = 3;

						} catch (Exception e) {
							updateStatusLabel("Type not selected", true,
									Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
							return;
						}

						try {
							Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbStoreID.getValue().isEmpty();
						} catch (Exception e) {
							updateStatusLabel("Store not selected", true,
									Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
							return;
						}

						if (!Main.getCompanyWorkerMainControl().getEditCatalogControl().txtSale.getText().isEmpty()) {
							try {

								int sale = Integer.parseInt(
										Main.getCompanyWorkerMainControl().getEditCatalogControl().txtSale.getText());
								if (sale > 0 && sale < 100) {
									STRsale = "'" + sale + "'";
								} else {
									updateStatusLabel("Invalid sale value", true,
											Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
									return;
								}
							} catch (Exception e) {
								updateStatusLabel("Invalid sale value", true,
										Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
								return;

							}

						} else
							STRsale = null;

					} else {
						updateStatusLabel("Invalid value, 1-100 or empty", true,
								Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
						return;
					}

				} else {
					updateStatusLabel("Invalid value, enter letters only", true,
							Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
					return;
				}
			} catch (Exception e) {
				updateStatusLabel("Invalid price value", true,
						Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
				return;
			}
			;

		}

		if (Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 2) {
			packet = new PacketClass(
					Main.UPDATECommandStatement + "itemcatalog" + Main.SETCommandStatement + "ItemName='"
							+ txtItemName.getText() + "', Type='" + type + "', Price='" + txtItemPrice.getText()
							+ "', sale=" + STRsale + ", storeID='" + cmbStoreID.getValue() + "', itemColor=" + STRcolor
							+ Main.WHERECommmandStatement + "ItemID='"
							+ Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbitemID.getValue() + "'",
					Main.DoneEditItem, Main.WRITE);

		} else if (Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 3) {
			packet = new PacketClass(Main.INSERTCommmandStatement
					+ "itemcatalog (ItemName, Type, Price, sale, storeID, itemColor)" + Main.VALUESCommmandStatement
					+ "('" + Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemName.getText() + "', '"
					+ type + "' , '" + Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemPrice.getText()
					+ "', " + STRsale + ", '"
					+ Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbStoreID.getValue() + "', '"
					+ Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemColor.getText() + "');",
					Main.SaveNewItem, Main.WRITE);

		}

		if (Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 2
				|| Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 3) {

			try {
				Main.getClientConsolHandle().sendSqlQueryToServer(packet);
			} catch (Exception e) {
				updateStatusLabel("Client connection error", true,
						Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
			}

			if (Main.getCompanyWorkerMainControl().getEditCatalogControl().txtImagePath.getText() != null) {
				String LocalfilePath = Main.getCompanyWorkerMainControl().getEditCatalogControl().txtImagePath
						.getText();

				try {

					if (Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 2)
						packet = new PacketClass(Main.UPDATECommandStatement + "itemcatalog" + Main.SETCommandStatement
								+ "Image= ?" + Main.WHERECommmandStatement + "ItemID= ?;", Main.SaveImage, Main.WRITE);
					else
						packet = new PacketClass(Main.UPDATECommandStatement + "itemcatalog" + Main.SETCommandStatement
								+ "Image= ?" + Main.WHERECommmandStatement + "ItemName= ?;", Main.SaveImage,
								Main.WRITE);

					File newFile = new File(LocalfilePath);

					byte[] mybytearray = new byte[(int) newFile.length()];
					FileInputStream fis = new FileInputStream(newFile);
					BufferedInputStream bis = new BufferedInputStream(fis);

					packet.fileList.add(new MyFile(LocalfilePath));

					if (Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod == 2)
						packet.fileList.get(0).setColumName(
								Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbitemID.getValue());
					else
						packet.fileList.get(0).setColumName(
								Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemName.getText());

					packet.fileList.get(0).initArray(mybytearray.length);
					packet.fileList.get(0).setSize(mybytearray.length);

					bis.read(mybytearray, 0, mybytearray.length);

					packet.fileList.get(0).setMybytearray(mybytearray);

					try {
						Main.getClientConsolHandle().sendSqlQueryToServer(packet);
					} catch (Exception e) {
						updateStatusLabel("Client connection error", true,
								Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
					}

				} catch (Exception e) {
					updateStatusLabel("Invalid image path", true,
							Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
				}

			}
		}

	}

	public void DoneMessSave(PacketClass packet) {
		if (packet.getSuccessSql()) {
			updateStatusLabel("Update was completed", false,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		} else {
			updateStatusLabel("Update failed", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}
	}

	public void SaveImageMessageFromServer(PacketClass packet) {
		if (packet.getSuccessSql()) {
			updateStatusLabel("Image uploaded", false,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		} else {
			updateStatusLabel("Image upload failed", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}
	}

	public void ClickNewItem() {

		setAllUnvisible(true);
		Main.getCompanyWorkerMainControl().getEditCatalogControl().workingMod = 3;
		disableGui(true, Main.getCompanyWorkerMainControl().getEditCatalogControl().btnEditItem,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().btnRemoveItem);
		clearAll();
	}

	public void DoneMessSaveNewItem(PacketClass packet) {
		if (packet.getSuccessSql()) {
			updateStatusLabel("The item added successfully", false,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
			setAllUnvisible(false);
			getCatalogData();
		} else {
			updateStatusLabel("Problem adding item", true,
					Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote);
		}
	}

	public void click_EditCatalog_ChooseImage() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "jpg", "png");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String CurrentImagePath = chooser.getSelectedFile().getAbsolutePath();

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Main.getCompanyWorkerMainControl().getEditCatalogControl().ivUploadImage
							.setImage(new Image("file:" + CurrentImagePath));

					Main.getCompanyWorkerMainControl().getEditCatalogControl().txtImagePath.setText(CurrentImagePath);
				}
			});
		}
	}
	
	public void click_EditCatalog_backButton(ActionEvent event) {

		((Node) event.getSource()).getScene().getWindow().hide();
		
		try {
			Main.getCompanyWorkerMainControl().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setAllUnvisible(boolean bool) {
		visibleGui(bool, Main.getCompanyWorkerMainControl().getEditCatalogControl().txtSale,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemPrice,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemName,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().btnEditItem,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().btnRemoveItem,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbStoreID,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().cmbItemType,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().lblTitelName,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().lblTitelPrice,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().lblTitelType,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().lblTitelSale,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemColor,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().lblTitelColor,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().lblTitelStoreID,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().btnConfirmChanges,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().txtImagePath,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().btnAddImage,
				Main.getCompanyWorkerMainControl().getEditCatalogControl().ivUploadImage);

		Main.getCompanyWorkerMainControl().getEditCatalogControl().txtImagePath.clear();
	}

	private void clearAll() {
		Main.getCompanyWorkerMainControl().getEditCatalogControl().txtSale.clear();
		Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemPrice.clear();
		Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemName.clear();
		Main.getCompanyWorkerMainControl().getEditCatalogControl().txtItemColor.clear();

		Main.getCompanyWorkerMainControl().getEditCatalogControl().ivUploadImage.setImage(
				new Image("file:" + System.getProperty("user.dir") + "\\ProjectImages\\300px-No_image_available.png"));

		Main.getCompanyWorkerMainControl().getEditCatalogControl().lblNote.setText("---");
	}
}
