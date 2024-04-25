package mainpackage.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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
	
	private final ToggleGroup radioButtonGroup = new ToggleGroup();
	
	private final double OPACITY_IF_DEACTIVATED = 0.4;
	private final double OPACITY_IF_ACTIVATED = 1.0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//group radio buttons
		urlRadioButton.setToggleGroup(radioButtonGroup);
		classicRadioButton.setToggleGroup(radioButtonGroup);
		
		classicRadioButton.setSelected(true);
		
		activateRadioButton(classicRadioButton);
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
		if(radioButtonGroup.getSelectedToggle() == classicRadioButton){ //if the classic mode is selected
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
			
			//System.out.println(host + " " + port + " " + name + " " + username + " " + password);
			
		}
		else if(radioButtonGroup.getSelectedToggle() == urlRadioButton){
			String url = dbURLTextField.getText();
			assert !url.isEmpty() : "The URL must be specified";
			
			//System.out.println(url);
		}
	}
	
	private void activateRadioButton(RadioButton rb) {
		if(rb == urlRadioButton) {
			dbHostTextField.setEditable(false);
			dbHostTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbPortTextField.setEditable(false);
			dbPortTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbNameTextField.setEditable(false);
			dbNameTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbUsernameTextField.setEditable(false);
			dbUsernameTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbPasswordTextField.setEditable(false);
			dbPasswordTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbURLTextField.setEditable(true);
			dbURLTextField.setOpacity(OPACITY_IF_ACTIVATED);
		}
		else if(rb == classicRadioButton) {
			dbURLTextField.setEditable(false);
			dbURLTextField.setOpacity(OPACITY_IF_DEACTIVATED);
			
			dbHostTextField.setEditable(true);
			dbHostTextField.setOpacity(OPACITY_IF_ACTIVATED);
			
			dbPortTextField.setEditable(true);
			dbPortTextField.setOpacity(OPACITY_IF_ACTIVATED);
			
			dbNameTextField.setEditable(true);
			dbNameTextField.setOpacity(OPACITY_IF_ACTIVATED);
			
			dbUsernameTextField.setEditable(true);
			dbUsernameTextField.setOpacity(OPACITY_IF_ACTIVATED);
			
			dbPasswordTextField.setEditable(true);
			dbPasswordTextField.setOpacity(OPACITY_IF_ACTIVATED);
		}
	}

}