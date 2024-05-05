package mainpackage.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import mainpackage.model.DatabaseHandler;
import mainpackage.model.SceneHandler;

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
		/*if(DatabaseHandler.DB_TEST) {
			DatabaseHandler.getInstance("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
			SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, "Search Page", 770, 500);
			return;
		}*/
		
		if(radioButtonGroup.getSelectedToggle() == classicRadioButton){
			
			if(dbHostTextField.getText().isEmpty() ||
				dbPortTextField.getText().isEmpty() ||
				dbNameTextField.getText().isEmpty() ||
				dbUsernameTextField.getText().isEmpty() ||
				dbPasswordTextField.getText().isEmpty()) {
				
				SceneHandler.getSH().showError("Fill out the mandatory fields.");
			}
			else {
				String host = dbHostTextField.getText();
				String port = dbPortTextField.getText();
				String name = dbNameTextField.getText();
				String username = dbUsernameTextField.getText();
				String password = dbPasswordTextField.getText();
				String type = dbTypeChoiceBox.getValue();
				
				try {
					DatabaseHandler.getInstance(host, port, name, type, username, password);
					SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, host, 770, 500);
				} catch (SQLException e) {
					SceneHandler.getSH().showError(e.getMessage());
				}
			}
			
		}
		else if(radioButtonGroup.getSelectedToggle() == urlRadioButton){
			
			if(dbURLTextField.getText().isEmpty() ||
				dbUsernameTextField.getText().isEmpty() ||
				dbPasswordTextField.getText().isEmpty()) {
				
				SceneHandler.getSH().showError("Fill out the mandatory fields.");
			}
			else {
				String url = dbURLTextField.getText();
				String username = dbUsernameTextField.getText();
				String password = dbPasswordTextField.getText();
				try {
					DatabaseHandler.getInstance(url, username, password);
					SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, url, 770, 500);
				} catch (SQLException e) {
					SceneHandler.getSH().showError(e.getMessage());
				}
			}

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
