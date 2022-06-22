package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import util.WindowManagerUtil;

public class MenuController {
    @FXML
    private Button addLanguageButton;

    @FXML
    private Button addResourceButton;

    @FXML
    private Button searchResourceButton;

    @FXML
    private Button goBackToLoginButton;

    private static String userId;

    @FXML
    private void callAddLanguageView(ActionEvent event) {
        Scene addLanguageButtonScene = this.addLanguageButton.getScene();
        Stage stage = (Stage)addLanguageButtonScene.getWindow();

        String fxmlFilename = "view/add-language.fxml";
        String stageTitle = "Add Language";
        boolean maximizedMode = true;

        WindowManagerUtil.newWindow(fxmlFilename, stageTitle, maximizedMode);
        AddLanguageController.setUserId(userId);

        stage.close();
    }

    @FXML
    private void callAddResourceView(ActionEvent event) {
        Scene addResourceButtonScene = this.addResourceButton.getScene();
        Stage stage = (Stage)addResourceButtonScene.getWindow();

        String fxmlFilename = "view/add-resource.fxml";
        String stageTitle = "Add Resource";
        boolean maximizedMode = true;

        WindowManagerUtil.newWindow(fxmlFilename, stageTitle, maximizedMode);
        AddResourceController.setUserId(userId);

        stage.close();
    }

    @FXML
    private void callSearchResourceView(ActionEvent event) {
        Scene searchResourceButtonScene = this.searchResourceButton.getScene();
        Stage stage = (Stage)searchResourceButtonScene.getWindow();

        String fxmlFilename = "view/search-resource.fxml";
        String stageTitle = "Search Resource";
        boolean maximizedMode = true;

        WindowManagerUtil.newWindow(fxmlFilename, stageTitle, maximizedMode);
        SearchResourceController.setUserId(userId);

        stage.close();
    }

    @FXML
    private void goBackToLogin(ActionEvent event) {
        Scene goBackToLoginButtonScene = this.goBackToLoginButton.getScene();
        Stage stage = (Stage)goBackToLoginButtonScene.getWindow();

        String fxmlFilename = "view/login.fxml";
        String stageTitle = "Login";
        boolean maximizedMode = false;

        WindowManagerUtil.newWindow(fxmlFilename, stageTitle, maximizedMode);

        stage.close();
    }

    public static void setUserId(String uid) {
        userId = uid;
    }
}
