package util;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManagerUtil {
    public static void newWindow(String fxmlFilename, String stageTitle, boolean maximizedMode)     {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL location = classLoader.getResource(fxmlFilename);
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        Parent root;

        try {
            root = fxmlLoader.load();
        } catch (IOException ioException) {
            String message = String.format("\nERROR: couldn't load \"%s\".\n", location);
            System.err.println(message);
            return;
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.setMaximized(maximizedMode);
        stage.show();
    }

    public static void newWindow(Stage stage, String fxmlFilename, String stageTitle, boolean maximizedMode) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL location = classLoader.getResource(fxmlFilename);
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        Parent root;

        try {
            root = fxmlLoader.load();
        } catch (IOException ioException) {
            String message = String.format("\nERROR: couldn't load \"%s\".\n", location);
            System.err.println(message);
            return;
        }

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle(stageTitle);
        stage.setMaximized(maximizedMode);
        stage.show();
    }
}
