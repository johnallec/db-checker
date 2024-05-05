package mainpackage.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mainpackage.model.DatabaseHandler;
import mainpackage.model.SceneHandler;

public class SearchPage implements Initializable {

	@FXML
	private TextField schemaTF;
	
	@FXML
	private TextField tableTF;
	
	@FXML
	private TextField attributeTF;
	
	@FXML
	private TextArea dataTA;
	
	@FXML
	private Button compareButton;
	
	@FXML
	private Button disconnectButton;
	
	@FXML
	private TableView<String> existingTV;
	
	@FXML
	private TableColumn<String, String> existingTC;
	
	@FXML
	private TableView<String> notExistingTV;
	
	@FXML
	private TableColumn<String, String> notExistingTC;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private void compareButtonClicked() {
		
		if(tableTF.getText().isEmpty() || attributeTF.getText().isEmpty() || dataTA.getText().isEmpty()) {
			SceneHandler.getSH().showError("Fill out the mandatory fields.");
		}
		else {
			String schema = schemaTF.getText().isEmpty() ? "" : schemaTF.getText() + ".";
			
			String table = tableTF.getText();
			String comparisonAttribute = attributeTF.getText();
			String data = dataTA.getText();
			
			String[] dataArray = data.split(";");
			
			ArrayList<String> existingOnes = DatabaseHandler.getInstance().compareItems(schema + table, comparisonAttribute, dataArray);
			
			if(existingOnes == null) return;
			
			ArrayList<String> tempNotExistingOnes = new ArrayList<String>(Arrays.asList(dataArray));
			tempNotExistingOnes.removeAll(existingOnes);
			
			existingTC.setCellValueFactory(mydata -> new SimpleStringProperty(mydata.getValue()));
			existingTV.setItems(FXCollections.observableArrayList(existingOnes));
			
			notExistingTC.setCellValueFactory(mydata -> new SimpleStringProperty(mydata.getValue()));
			notExistingTV.setItems(FXCollections.observableArrayList(tempNotExistingOnes));
		}
		
		/*String[] myData = {"1","2","3","9","10"};
		ArrayList<String> existingOnes = DatabaseHandler.getInstance().compareItems("public.stockmovement", "stock_code", myData);
		
		ArrayList<String> tempNotExistingOnes = new ArrayList<String>(Arrays.asList(myData));
		tempNotExistingOnes.removeAll(existingOnes);
		
		existingTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
		existingTV.setItems(FXCollections.observableArrayList(existingOnes));
		
		notExistingTC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
		notExistingTV.setItems(FXCollections.observableArrayList(tempNotExistingOnes));*/
	}
	
	@FXML
	private void disconnectButtonClicked() {
		if(SceneHandler.getSH().showConfirmation("Are you sure you want to be disconnected from the DB?")) {
			SceneHandler.getSH().switchScene(SceneHandler.MAIN_PAGE_PATH, SceneHandler.MAIN_PAGE_TITLE, SceneHandler.MAIN_PAGE_WIDTH, SceneHandler.MAIN_PAGE_HEIGHT);
			DatabaseHandler.getInstance().disconnect();
		}
	}

}
