package mainpackage.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import mainpackage.model.DatabaseHandler;

public class Login implements Initializable {

	@FXML
	private TextField dbHostTextField;
	@FXML
	private TextField dbPortTextField;
	@FXML
	private TextField dbNameTextField;
	@FXML
	private TextField dbUsernameTextField;
	@FXML
	private TextField dbPasswordTextField;
	@FXML
	private TextField dbURLTextField;
	@FXML
	private RadioButton urlRadioButton;
	@FXML
	private RadioButton classicRadioButton;
	@FXML
	private ChoiceBox<String> dbTypeChoiceBox;
	
	private final String[] allowedDBTypes = {"postgresql", "oracle"};
	
	private final ToggleGroup radioButtonGroup = new ToggleGroup();
	
	private final double OPACITY_IF_DEACTIVATED = 0.4;
	private final double OPACITY_IF_ACTIVATED = 1.0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//group radio buttons
		urlRadioButton.setToggleGroup(radioButtonGroup);
		classicRadioButton.setToggleGroup(radioButtonGroup);
		
		classicRadioButton.setSelected(true);
		
		//activate the classic access mode as default
		activateRadioButton(classicRadioButton);
		
		dbTypeChoiceBox.setItems(FXCollections.observableArrayList(allowedDBTypes));
		dbTypeChoiceBox.setValue(allowedDBTypes[0]);
		dbTypeChoiceBox.setValue("postgresql");
	}
	
	@FXML
	private void urlRadioButtonClicked() {
		activateRadioButton(urlRadioButton);
	}
	
	@FXML
	private void classicRadioButtonClicked() {
		activateRadioButton(classicRadioButton);
	}
	
	@FXML
	private void connectButtonClicked() {
		if(radioButtonGroup.getSelectedToggle() == classicRadioButton){
			String host = dbHostTextField.getText();
			assert !host.isEmpty() : "The host must be specified.";
			
			String port = dbPortTextField.getText();
			assert !port.isEmpty() : "The port must be specified.";
			
			String name = dbNameTextField.getText();
			assert !name.isEmpty() : "The DB name must be specified.";
			
			String username = dbUsernameTextField.getText();
			assert !username.isEmpty() : "The username must be specified.";
			
			String password = dbPasswordTextField.getText();
			assert !password.isEmpty() : "The password be specified.";
			
			String type = dbTypeChoiceBox.getValue();
			assert !type.isEmpty() : "Select a database type.";
			
			DatabaseHandler.getInstance(host, port, name, type, username, password);
			//System.out.println(host + " " + port + " " + name + " " + username + " " + password);
			
		}
		else if(radioButtonGroup.getSelectedToggle() == urlRadioButton){
			String url = dbURLTextField.getText();
			assert !url.isEmpty() : "The URL must be specified";
			
			String username = dbUsernameTextField.getText();
			assert !username.isEmpty() : "The username must be specified.";
			
			String password = dbPasswordTextField.getText();
			assert !password.isEmpty() : "The password be specified.";
			
			DatabaseHandler.getInstance(url, username, password);
			//System.out.println(url);
		}
	}
	
	private void activateRadioButton(RadioButton rb) {
		if(rb == urlRadioButton) {
			dbHostTextField.setDisable(true);
			dbHostTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbPortTextField.setDisable(true);
			dbPortTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbNameTextField.setDisable(true);
			dbNameTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbTypeChoiceBox.setDisable(true);
			dbTypeChoiceBox.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbURLTextField.setDisable(false);
			dbURLTextField.setOpacity(OPACITY_IF_ACTIVATED);
		}
		else if(rb == classicRadioButton) {
			dbURLTextField.setDisable(true);
			dbURLTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbHostTextField.setDisable(false);
			dbHostTextField.setOpacity(OPACITY_IF_ACTIVATED);
			
			dbPortTextField.setDisable(false);
			dbPortTextField.setOpacity(OPACITY_IF_ACTIVATED);
			
			dbNameTextField.setDisable(false);
			dbNameTextField.setOpacity(OPACITY_IF_ACTIVATED);
			
			dbTypeChoiceBox.setDisable(false);
			dbTypeChoiceBox.setOpacity(OPACITY_IF_ACTIVATED);
		}
	}

}
