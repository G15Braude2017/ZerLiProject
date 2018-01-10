package Catalog;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

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
import java.io.File;

public class ViewCtalog extends GuiExtensions {

	@FXML
	private Label lblNote;

	@FXML
	private TableView Catalog;

	@FXML
	private Button btnLoginout;

	@FXML
	private Button btnMyShopping;

	@FXML
	private Button btnBack;

	public void start() throws Exception {

		Main.setViewCtalogControl((ViewCtalog) createAndDefinedFxmlWindow("Catalog.fxml", "Catalog"));

		getCatalogData();
	}

	public void getCatalogData() {
		PacketClass packet = new PacketClass(
				Main.SELECTCommandStatement + "*" + Main.FROMCommmandStatement + "itemcatalog", Main.ViewCatalog,
				Main.READ);

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

			if (dataList != null) {
				TableColumn ImageCol = new TableColumn("Image");
				TableColumn ItemNameCol = new TableColumn("Item name");
				TableColumn ItemIDCol = new TableColumn("Item ID");
				TableColumn TypeCol = new TableColumn("Type");
				TableColumn PriceCol = new TableColumn("Price");
				TableColumn OrderCol = new TableColumn("");

				Catalog.getColumns().addAll(ImageCol, ItemNameCol, ItemIDCol, TypeCol, PriceCol, OrderCol);

				ObservableList<catalog> data = null;

				for (int i = 0; i < dataList.size(); i++) {
					data = FXCollections.observableArrayList(
							new catalog(new ImageView(dataList.get(i).get(0)), dataList.get(i).get(1),
									dataList.get(i).get(2), dataList.get(i).get(3), dataList.get(i).get(4)));
				}

				Catalog.setItems(data);
			} else
				updateStatusLabel("The catalog is empty", true, Main.getViewCtalogControl().lblNote);
		} else
			updateStatusLabel("Could not open catalog", true, Main.getViewCtalogControl().lblNote);
	}

}
