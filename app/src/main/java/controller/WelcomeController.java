package controller;

import java.net.URL;
import java.security.SecureRandom;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import util.WindowManagerUtil;

public class WelcomeController implements Initializable {
    @FXML
    private Label welcomeLabel;

    private final static int MIN_SLEEP_TIME = 3000;
    private final static int MAX_SLEEP_TIME = 4000;

    @FXML @Override
    public void initialize(URL location, ResourceBundle resources) {
        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.start();
    }

    class WelcomeScreen extends Thread {
        @Override
        public void run() {
            SecureRandom secureRandom = new SecureRandom();
            long sleepTime = secureRandom.nextLong(MIN_SLEEP_TIME, MAX_SLEEP_TIME + 1);

            try {
                Thread.sleep(sleepTime);

                Platform.runLater(() -> {
                    Scene welcomeLabelScene = welcomeLabel.getScene();
                    Stage stage = (Stage)welcomeLabelScene.getWindow();

                    String fxmlFilename = "view/login.fxml";
                    String stageTitle = "Login";
                    boolean maximizedMode = false;

                    WindowManagerUtil.newWindow(fxmlFilename, stageTitle, maximizedMode);
                    stage.close();
                });
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }
}
