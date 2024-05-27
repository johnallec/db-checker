package mainpackage.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mainpackage.model.DatabaseHandler;
import mainpackage.model.SceneHandler;
import mainpackage.model.SheetCreator;

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
	
	private HashSet<String> notExistingOnes;
	private HashSet<String> existingOnes;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private void compareButtonClicked() {
		
		if(tableTF.getText().isEmpty() || attributeTF.getText().isEmpty() || dataTA.getText().isEmpty()) {
			SceneHandler.getSH().showError("Fill out the mandatory fields.");
		}
		else {
			compare();
		}
	}
	
	@FXML
	private void disconnectButtonClicked() {
		if(SceneHandler.getSH().showConfirmation("Are you sure you want to be disconnected from the DB?")) {
			SceneHandler.getSH().switchScene(SceneHandler.MAIN_PAGE_PATH, SceneHandler.MAIN_PAGE_TITLE, SceneHandler.MAIN_PAGE_WIDTH, SceneHandler.MAIN_PAGE_HEIGHT);
			DatabaseHandler.getInstance().disconnect();
		}
	}
	
	private void compare() {
		String schema = schemaTF.getText().isEmpty() ? "" : schemaTF.getText() + ".";
		
		String table = tableTF.getText();
		String comparisonAttribute = attributeTF.getText();
		String data = dataTA.getText();
		
		long veryStartMillis = System.currentTimeMillis();
		long startMillis = veryStartMillis;
		long endMillis;
		
		String[] dataArray = data.split(";");
		
		endMillis = System.currentTimeMillis();
		System.out.println("Data split, time spent: ");
		System.out.println(endMillis - startMillis);
		startMillis = endMillis;
		
		HashSet<String> records = null;
		if(dataArray.length > 300) {
			//selecting all the records of the table and comparing after
			records =  DatabaseHandler.getInstance().compareItems(schema + table, comparisonAttribute);
			
			endMillis = System.currentTimeMillis();
			System.out.println("Records gotten, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
			
			this.notExistingOnes = new HashSet<String>(Arrays.asList(dataArray));
			this.notExistingOnes.removeAll(records);
			
			endMillis = System.currentTimeMillis();
			System.out.println("Not existing ones created, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
			
			this.existingOnes = new HashSet<String>(Arrays.asList(dataArray));
			this.existingOnes.removeAll(this.notExistingOnes);
			
			endMillis = System.currentTimeMillis();
			System.out.println("Existing ones created, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
			
			existingTC.setCellValueFactory(mydata -> new SimpleStringProperty(mydata.getValue()));
			existingTV.setItems(FXCollections.observableArrayList(this.existingOnes));
			
			endMillis = System.currentTimeMillis();
			System.out.println("Existing ones in table, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
			
			notExistingTC.setCellValueFactory(mydata -> new SimpleStringProperty(mydata.getValue()));
			notExistingTV.setItems(FXCollections.observableArrayList(this.notExistingOnes));
			
			endMillis = System.currentTimeMillis();
			System.out.println("Not existing ones in table, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
		}
		else {
			//selecting using a where clause
			this.existingOnes = DatabaseHandler.getInstance().compareItemsWhereClause(schema + table, comparisonAttribute, dataArray);
			
			endMillis = System.currentTimeMillis();
			System.out.println("Existing ones gotten, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
			
			if(existingOnes == null) return;
			
			this.notExistingOnes = new HashSet<String>(Arrays.asList(dataArray));
			notExistingOnes.removeAll(existingOnes);
			
			endMillis = System.currentTimeMillis();
			System.out.println("Not existing ones created, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
			
			existingTC.setCellValueFactory(mydata -> new SimpleStringProperty(mydata.getValue()));
			existingTV.setItems(FXCollections.observableArrayList(this.existingOnes));
			
			endMillis = System.currentTimeMillis();
			System.out.println("Existing ones in table, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;
			
			notExistingTC.setCellValueFactory(mydata -> new SimpleStringProperty(mydata.getValue()));
			notExistingTV.setItems(FXCollections.observableArrayList(this.notExistingOnes));
			
			endMillis = System.currentTimeMillis();
			System.out.println("Not existing ones in table, time spent: ");
			System.out.println(endMillis - startMillis);
			startMillis = endMillis;

		}
		
		saveResultsButtonClicked();
		
		endMillis = System.currentTimeMillis();
		System.out.println("File created, time spent: ");
		System.out.println(endMillis - startMillis);
		startMillis = endMillis;
		
		System.out.println("Total amount of time spent:");
		System.out.println(System.currentTimeMillis() - veryStartMillis);
		System.out.println("\n****************************************************************************\n");
	}
	
	private void saveResultsButtonClicked() {
		SheetCreator sc = new SheetCreator("Results");
		sc.createRow(0);
		sc.createCell(0, "Existing");
		sc.createCell(1, "Not existing");
		
		ArrayList<String> existingOnesTemp = new ArrayList<String>(this.existingOnes);
		ArrayList<String> notExistingOnesTemp = new ArrayList<String>(this.notExistingOnes);
		
		for(int i=0; i < (existingOnesTemp.size() >= notExistingOnesTemp.size() ? existingOnesTemp.size() : notExistingOnesTemp.size()); i++){
			sc.createRow(i+1);
			if(i < existingOnesTemp.size()) {
				sc.createCell(0, existingOnesTemp.get(i));
			}
			if(i < notExistingOnesTemp.size()) {
				sc.createCell(1, notExistingOnesTemp.get(i));
			}
		}
		try {
			sc.storeFile("results.xlsx");
		} catch (IOException e) {
			SceneHandler.getSH().showError("An error occured trying to create the file: " + e.getMessage());
		}
	}

}
