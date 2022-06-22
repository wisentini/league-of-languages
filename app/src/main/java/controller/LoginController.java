package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import task.LoginTask;
import util.AlertUtil;
import util.ExceptionHandlerUtil;
import repository.RepositoryResponse;
import util.WindowManagerUtil;

public class LoginController {
    @FXML
    private Button clearButton;

    @FXML
    private Button enterButton;

    @FXML
    private TextField userIdTextField;

    @FXML
    private Label validatingUserIdAsteriskLabel;

    @FXML
    private Label validatingUserIdLabel;

    @FXML
    private void clear(ActionEvent event) {
        this.userIdTextField.clear();
    }

    @FXML
    private void login(ActionEvent event) {
        this.setValidatingUserIdMessage(true);

        String userId = this.userIdTextField.getText();
        LoginTask loginTask = new LoginTask(userId);

        loginTask.setOnSucceeded(e -> {
            this.setValidatingUserIdMessage(false);

            RepositoryResponse repositoryResponse = loginTask.getValue();
            String repositoryResponseMessage = repositoryResponse.getMessage();
            boolean loginSuccess = repositoryResponse.getSuccess();

            if (loginSuccess) {
                this.callMenuView(userId);
            } else {
                AlertType alertType = AlertType.ERROR;
                String alertTitle = "Login error";
                String alertHeader = "Invalid userId";
                AlertUtil.show(alertType, alertTitle, alertHeader, repositoryResponseMessage);
            }
        });

        loginTask.setOnFailed(e -> {
            this.setValidatingUserIdMessage(false);
            Throwable throwable = loginTask.getException();
            ExceptionHandlerUtil.handle(throwable);
        });

        Thread thread = new Thread(loginTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void callMenuView(String userId) {
        Scene enterButtonScene = this.enterButton.getScene();
        Stage stage = (Stage)enterButtonScene.getWindow();

        String fxmlFilename = "view/menu.fxml";
        String stageTitle = "Menu";
        boolean maximizedMode = false;

        WindowManagerUtil.newWindow(fxmlFilename, stageTitle, maximizedMode);
        MenuController.setUserId(userId);

        stage.close();
    }

    @FXML
    private void onEnter(ActionEvent event) {
        this.login(event);
    }

    private void setValidatingUserIdMessage(boolean setVisible) {
        this.validatingUserIdAsteriskLabel.setVisible(setVisible);
        this.validatingUserIdLabel.setVisible(setVisible);
    }
}
