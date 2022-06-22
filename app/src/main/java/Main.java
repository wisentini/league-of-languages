import javafx.application.Application;
import javafx.stage.Stage;
import util.WindowManagerUtil;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String fxmlFilename = "view/welcome.fxml";
        String stageTitle = "Welcome";
        boolean maximizedMode = false;

        WindowManagerUtil.newWindow(primaryStage, fxmlFilename, stageTitle, maximizedMode);
    }
}
