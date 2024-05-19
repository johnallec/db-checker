package mainpackage.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.util.PropertyFilePropertySource;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
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
	private TextField dbProfileNameTextField;
	@FXML
	private TextField dbURLTextField;
	@FXML
	private RadioButton urlRadioButton;
	@FXML
	private RadioButton classicRadioButton;
	@FXML
	private RadioButton presetRadioButton;
	@FXML
	private ChoiceBox<String> dbTypeChoiceBox;
	@FXML
	private ChoiceBox<String> dbProfileChoiceBox;
	@FXML
	private CheckBox saveProfileCheckBox;
	
	private final String[] allowedDBTypes = {"postgresql", "oracle"};
	
	private HashSet<String> profiles;
	
	private final ToggleGroup radioButtonGroup = new ToggleGroup();
	
	private final double OPACITY_IF_DEACTIVATED = 0.4;
	private final double OPACITY_IF_ACTIVATED = 1.0;
	private final String PROFILES_NOF = "profiles.txt";
	private final String CREDENTIALS_NOF = "credentials.txt";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//group radio buttons
		urlRadioButton.setToggleGroup(radioButtonGroup);
		classicRadioButton.setToggleGroup(radioButtonGroup);
		presetRadioButton.setToggleGroup(radioButtonGroup);
		
		classicRadioButton.setSelected(true);
		
		//activate the classic access mode as default
		activateRadioButton(classicRadioButton);
		
		dbTypeChoiceBox.setItems(FXCollections.observableArrayList(allowedDBTypes));
		dbTypeChoiceBox.setValue(allowedDBTypes[0]);
		dbTypeChoiceBox.setValue("postgresql");
		
		profiles = new HashSet<String>();
		
		File profileFile = new File(PROFILES_NOF);
		try {
			if(!profileFile.createNewFile()) {
				Scanner reader = new Scanner(profileFile);
				while(reader.hasNextLine()) {
					profiles.add(reader.nextLine());
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		dbProfileChoiceBox.setItems(FXCollections.observableArrayList(profiles));
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
	private void presetRadioButtonClicked() {
		activateRadioButton(presetRadioButton);
	}
	
	@FXML
	private void connectButtonClicked() {
		
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
		else if(radioButtonGroup.getSelectedToggle() == presetRadioButton) {
			String selectedProfile = dbProfileChoiceBox.getValue();
			if(selectedProfile == null) {
				SceneHandler.getSH().showError("Choose a profile");
				return;
			}
			
			File credentials = new File(CREDENTIALS_NOF);
			try {
				Scanner reader = new Scanner(credentials);
				while(reader.hasNextLine()) {
					String line = reader.nextLine();
					Pattern pattern = Pattern.compile("(.*)%(.*)%(.*)%(.*)");
					Matcher matcher = pattern.matcher(line);
					if(matcher.find()) {
						if(matcher.group(1).equals(selectedProfile)) {
							try {
								DatabaseHandler.getInstance(matcher.group(2), matcher.group(3), matcher.group(4));
								SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, selectedProfile, 770, 500);
							} catch (SQLException e) {
								SceneHandler.getSH().showError(e.getMessage());
							}
						}
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void activateRadioButton(RadioButton rb) {
		if(rb == urlRadioButton) {
			disableGraphicComponent(dbHostTextField);
			disableGraphicComponent(dbPortTextField);
			disableGraphicComponent(dbNameTextField);
			disableGraphicComponent(dbTypeChoiceBox);
			disableGraphicComponent(dbProfileChoiceBox);
			
			enableGraphicComponent(dbURLTextField);
			
			enableGraphicComponent(dbPasswordTextField);
			enableGraphicComponent(dbProfileNameTextField);
			enableGraphicComponent(saveProfileCheckBox);
		}
		else if(rb == classicRadioButton) {
			disableGraphicComponent(dbURLTextField);
			disableGraphicComponent(dbProfileChoiceBox);
			
			enableGraphicComponent(dbHostTextField);
			enableGraphicComponent(dbPortTextField);
			enableGraphicComponent(dbNameTextField);
			enableGraphicComponent(dbTypeChoiceBox);
			
			enableGraphicComponent(dbPasswordTextField);
			enableGraphicComponent(dbProfileNameTextField);
			enableGraphicComponent(saveProfileCheckBox);
		}
		else if(rb == presetRadioButton) {
			disableGraphicComponent(dbHostTextField);
			disableGraphicComponent(dbPortTextField);
			disableGraphicComponent(dbNameTextField);
			disableGraphicComponent(dbTypeChoiceBox);
			disableGraphicComponent(dbURLTextField);
			disableGraphicComponent(dbUsernameTextField);
			disableGraphicComponent(dbPasswordTextField);
			disableGraphicComponent(dbProfileNameTextField);
			disableGraphicComponent(saveProfileCheckBox);
			
			enableGraphicComponent(dbProfileChoiceBox);
		}
	}
	
	private void disableGraphicComponent(Node node) {
		node.setDisable(true);
		node.setOpacity(OPACITY_IF_DEACTIVATED);
	}
	
	private void enableGraphicComponent(Node node) {
		node.setDisable(false);
		node.setOpacity(OPACITY_IF_ACTIVATED);
	}

}
