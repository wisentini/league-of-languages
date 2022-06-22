package util;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.scene.control.Alert.AlertType;

public class ExceptionHandlerUtil {
    public static void handle(Throwable throwable) {
        AlertType alertType = AlertType.ERROR;
        
        String alertTitle = "Caught exception";
        Class<?> throwableClass = throwable.getClass();
        String throwableClassName = throwableClass.getSimpleName();
        String alertContent = "";

        if (throwable instanceof MalformedURLException) {
            alertContent = "No legal protocol could be found in a specification string or the string could not be parsed.";
        } else if (throwable instanceof IOException) {
            alertContent = "Failed or interrupted I/O operation. Check your internet connection.";
        } else if (throwable instanceof Exception) {
            alertContent = "Something went wrong.";
        }

        AlertUtil.show(alertType, alertTitle, throwableClassName, alertContent);
    }
}
