package mainpackage.model;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class SceneHandler {
	
	private Scene scene;
	private Stage stage;
	public static final String MAIN_PAGE_TITLE = "Database Checker";
	public static final String MAIN_PAGE_PATH = "/mainpackage/view/mainPage.fxml";
	public static final String SEARCH_PAGE_PATH = "/mainpackage/view/searchPage.fxml";
	public static final int MAIN_PAGE_WIDTH = 600;
	public static final int MAIN_PAGE_HEIGHT = 500;
	private static SceneHandler sh;
	
	public static SceneHandler getSH() {
		if(sh == null)
			sh = new SceneHandler();
		return sh;
	}
	
	
	private SceneHandler() {}
	
	public void init(Stage stage) {
		this.stage = stage;
		setCurrentScene(MAIN_PAGE_PATH, MAIN_PAGE_WIDTH, MAIN_PAGE_HEIGHT);
		this.stage.setScene(scene);
		this.stage.setTitle(MAIN_PAGE_TITLE);
		this.stage.setResizable(false);
		this.stage.show();
	}
	
	private void setCurrentScene(String path, int width, int height) {
		try {
			FXMLLoader loader= new FXMLLoader(getClass().getResource(path));
			Parent root = (Parent) loader.load();
			this.scene = new Scene(root, width, height);
		} catch (IOException e) {
			e.printStackTrace(); // To be modified
		}
	}
	
	public void switchScene(String fxmlPath, String title, int width, int heigth) {
		if(fxmlPath == null || width < 0 || heigth < 0) {
			//show an error page
			return;
		}
		setCurrentScene(fxmlPath, width, heigth);
		this.stage.setScene(scene);
		this.stage.setTitle(title);
		this.stage.setResizable(false);
		this.stage.show();
	}
	
	public void showError(String message) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.show();
    }
	
	public void showWarning(String message) {
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.show();
    }

	public boolean showConfirmation(String message) {
		Alert al= new Alert(AlertType.CONFIRMATION);
		al.setTitle("Confirm");
		al.setHeaderText("");
		al.setContentText(message);
		al.showAndWait();
		if(al.getResult()==ButtonType.OK)
			return true;
		return false;
	}

}
