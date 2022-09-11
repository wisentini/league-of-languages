package util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertUtil {
    public static Optional<ButtonType> show(AlertType alertType, String alertTitle, String alertHeader, String alertContent) {
        Alert alert = new Alert(alertType);

        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertContent);

        return alert.showAndWait();
    }
}
