package Catalog;

import Catalog.ViewCtalog;

import javafx.beans.property.SimpleStringProperty;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;


public class catalog 
{
	private ImageView Image;
	private final SimpleStringProperty ItemName;
	private final SimpleStringProperty ItemID;
	private final SimpleStringProperty Type;
	private final SimpleStringProperty Price;
	private Button btnOrder;
	
	public catalog (ImageView image, String itemName, String itemID, String type, String price)
	{
		Image = image;
        ItemName = new SimpleStringProperty(itemName);
		ItemID = new SimpleStringProperty(itemID);
		Type = new SimpleStringProperty(type);
		Price = new SimpleStringProperty(price);
		btnOrder = new Button("Add");
	}
	
	public void setItemName(String NewItemName)
	{
		ItemName.set(NewItemName);
	}
	
	public void setItemID(String NewItemID)
	{
		ItemID.set(NewItemID);
	}
	
	public void setType(String NewType)
	{
		Type.set(NewType);
	}
	public void setPrice(String NewPrice)
	{
		Price.set(NewPrice);
	}	
}
