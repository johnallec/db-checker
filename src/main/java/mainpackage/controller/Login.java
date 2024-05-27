package mainpackage.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import mainpackage.model.DatabaseHandler;
import mainpackage.model.ProfileCredential;
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
	
	private HashMap<String,ProfileCredential> profiles;
	
	private final ToggleGroup radioButtonGroup = new ToggleGroup();
	
	private final double OPACITY_IF_DEACTIVATED = 0.4;
	private final double OPACITY_IF_ACTIVATED = 1.0;
	private final String PROFILES_NOF = "profiles.txt";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//group radio buttons
		urlRadioButton.setToggleGroup(radioButtonGroup);
		classicRadioButton.setToggleGroup(radioButtonGroup);
		presetRadioButton.setToggleGroup(radioButtonGroup);
		
		classicRadioButton.setSelected(true);
		
		//activate the classic access mode as default
		activateRadioButton(classicRadioButton);
		
		dbTypeChoiceBox.setItems(FXCollections.observableArrayList(Arrays.asList(DatabaseHandler.dbTypes)));
		dbTypeChoiceBox.setValue(DatabaseHandler.dbTypes[0]);
		dbTypeChoiceBox.setValue("postgresql");
		
		profiles = new HashMap<String,ProfileCredential>();
		
		//initialize profiles
		File profileFile = new File(PROFILES_NOF);
		try {
			Scanner reader = new Scanner(profileFile);
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				Pattern pattern = Pattern.compile("(.*)%(.*)%(.*)%(.*)");
				Matcher matcher = pattern.matcher(line);
				if(matcher.find()) {
					ProfileCredential profile = new ProfileCredential(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4));
					this.profiles.put(profile.getName(),profile);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			File file = new File(PROFILES_NOF);
		    try {
				file.createNewFile();
			} catch (IOException e1) {
				SceneHandler.getSH().showError("The creation of profile.txt has encoutered an error.");
				e.printStackTrace();
			}
		}
		
		dbProfileChoiceBox.setItems(FXCollections.observableArrayList(profiles.keySet()));
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
					if(saveProfileCheckBox.isSelected()) {
						if(dbProfileNameTextField.getText().isEmpty()) {
							SceneHandler.getSH().showError("Set a profile name.");
							return;
						}
						DatabaseHandler.getInstance(host, port, name, type, username, password);
						saveProfile(dbProfileNameTextField.getText(), username, password);
					}
					else {
						DatabaseHandler.getInstance(host, port, name, type, username, password);
					}
					SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, DatabaseHandler.getInstance().getURL(), 770, 500);
				} catch (Exception e) {
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
					if(saveProfileCheckBox.isSelected()) {
						if(dbProfileNameTextField.getText().isEmpty()) {
							SceneHandler.getSH().showError("Set a profile name.");
							return;
						}
						DatabaseHandler.getInstance(url, username, password);
						saveProfile(dbProfileNameTextField.getText(), username, password);
					}
					else {
						DatabaseHandler.getInstance(url, username, password);
					}
					SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, url, 770, 500);
				} catch (Exception e) {
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
			ProfileCredential profile = profiles.get(selectedProfile);
			try {
				DatabaseHandler.getInstance(profile.getUrl(), profile.getUsername(), profile.getPassword());
				SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, selectedProfile, 770, 500);
			}
			catch(SQLException e) {
				SceneHandler.getSH().showError(e.getMessage());
			}
		}
		
	}
	
	private void saveProfile(String profileName, String username, String password) throws IOException {
		File file = new File(PROFILES_NOF);
	    file.createNewFile();
	    FileWriter writer = new FileWriter(file,true);
	    writer.write(profileName + "%" + DatabaseHandler.getInstance().getURL() + "%" + username + "%" + password + "\n");
	    writer.close();
	}
	
	@FXML
	private void saveProfileCheckBoxClicked() {
		if(saveProfileCheckBox.isSelected()) enableGraphicComponent(dbProfileNameTextField);
		else disableGraphicComponent(dbProfileNameTextField);
	}
	
	private void activateRadioButton(RadioButton rb) {
		if(rb == urlRadioButton) {
			disableGraphicComponent(dbHostTextField);
			disableGraphicComponent(dbPortTextField);
			disableGraphicComponent(dbNameTextField);
			disableGraphicComponent(dbTypeChoiceBox);
			disableGraphicComponent(dbProfileChoiceBox);
			disableGraphicComponent(dbProfileNameTextField);
			
			enableGraphicComponent(dbURLTextField);
			
			enableGraphicComponent(dbUsernameTextField);
			enableGraphicComponent(dbPasswordTextField);
			enableGraphicComponent(saveProfileCheckBox);
			saveProfileCheckBox.setSelected(false);
		}
		else if(rb == classicRadioButton) {
			disableGraphicComponent(dbURLTextField);
			disableGraphicComponent(dbProfileChoiceBox);
			disableGraphicComponent(dbProfileNameTextField);
			
			enableGraphicComponent(dbHostTextField);
			enableGraphicComponent(dbPortTextField);
			enableGraphicComponent(dbNameTextField);
			enableGraphicComponent(dbTypeChoiceBox);
			
			enableGraphicComponent(dbUsernameTextField);
			enableGraphicComponent(dbPasswordTextField);
			enableGraphicComponent(saveProfileCheckBox);
			saveProfileCheckBox.setSelected(false);
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
			saveProfileCheckBox.setSelected(false);
			
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
