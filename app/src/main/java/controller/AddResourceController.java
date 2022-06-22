package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import entity.Category;
import entity.Resource;
import repository.CategoryRepository;
import repository.ResourceRepository;
import util.AlertUtil;
import util.ExceptionHandlerUtil;
import repository.RepositoryResponse;
import util.WindowManagerUtil;

public class AddResourceController implements Initializable {
    @FXML
    private Button addResourceButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField languageIdTextField;

    @FXML
    private TextField tagsTextField;

    @FXML
    private TextField urlTextField;

    @FXML
    private Button menuButton;

    @FXML
    private Label loadingCategoriesAsteriskLabel;

    @FXML
    private Label loadingCategoriesLabel;

    @FXML
    private Label loadingDescriptionAsteriskLabel;

    @FXML
    private Label loadingDescriptionLabel;

    private List<Category> categories;

    private static String userId;

    private final ResourceRepository resourceRepository = new ResourceRepository();

    private final CategoryRepository categoryRepository = new CategoryRepository();

    @FXML @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.enableLoadingCategoriesMessage(true);

        Task<List<Category>> getCategoriesTask = this.categoryRepository.getCategoriesTask();

        getCategoriesTask.setOnSucceeded(e -> {
            this.categories = getCategoriesTask.getValue();

            List<String> categoryNames = CategoryRepository.getCategoryNames(this.categories);
            ObservableList<String> items = FXCollections.observableArrayList(categoryNames);

            ObservableList<String> categoryComboBoxItems = this.categoryComboBox.getItems();

            categoryComboBoxItems.clear();
            categoryComboBoxItems.setAll(items);

            this.enableLoadingCategoriesMessage(false);
        });

        getCategoriesTask.setOnFailed(e -> {
            this.enableLoadingCategoriesMessage(false);
            Throwable throwable = getCategoriesTask.getException();
            ExceptionHandlerUtil.handle(throwable);
        });

        Thread thread = new Thread(getCategoriesTask);
        thread.setDaemon(true);
        thread.start();

        this.commentTextArea.setWrapText(true);
        this.descriptionTextArea.setWrapText(true);
        this.descriptionTextArea.setEditable(false);
    }

    @FXML
    private void handleSelectedCategory(ActionEvent event) {
        this.enableLoadingDescriptionMessage(true);

        String selectedCategory = this.categoryComboBox.getValue();
    
        Task<List<Category>> getCategoriesTask = this.categoryRepository.getCategoriesTask();

        getCategoriesTask.setOnSucceeded(e -> {
            this.categories = getCategoriesTask.getValue();

            for (Category category : this.categories) {
                String categoryName = category.getName();

                if (selectedCategory.equals(categoryName)) {
                    String categoryDescription = category.getDescription();
                    this.descriptionTextArea.setText(categoryDescription);
                    this.enableLoadingDescriptionMessage(false);
                    break;
                }
            }
        });

        getCategoriesTask.setOnFailed(e -> {
            this.enableLoadingDescriptionMessage(false);
            Throwable throwable = getCategoriesTask.getException();
            ExceptionHandlerUtil.handle(throwable);
        });

        Thread thread = new Thread(getCategoriesTask);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void addResource(ActionEvent event) {
        String url = this.urlTextField.getText();
        String languageId = this.languageIdTextField.getText();
        String tags = this.tagsTextField.getText();
        String category = this.categoryComboBox.getValue();
        String comment = this.commentTextArea.getText();

        Resource resource = new Resource(url, languageId, tags, category, comment, userId);

        Task<RepositoryResponse> postResourceTask = this.resourceRepository.postResourceTask(resource);

        postResourceTask.setOnSucceeded(e -> {
            RepositoryResponse repositoryResponse = postResourceTask.getValue();
            String repositoryResponseMessage = repositoryResponse.getMessage();
            boolean resourcePosted = repositoryResponse.getSuccess();

            AlertType alertType = AlertType.INFORMATION;
            String alertTitle = "Add resource";
            String alertHeader;
            String alertContent;

            if (resourcePosted) {
                alertHeader = "Resource added";
                alertContent = String.format("Status: %s", repositoryResponseMessage);
            } else {
                alertHeader = "Couldn't add resource";
                alertContent = String.format("Reason: %s", repositoryResponseMessage);
            }

            AlertUtil.show(alertType, alertTitle, alertHeader, alertContent);
        });

        postResourceTask.setOnFailed(e -> {
            Throwable throwable = postResourceTask.getException();
            ExceptionHandlerUtil.handle(throwable);
        });

        Thread thread = new Thread(postResourceTask);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void callMenuView(ActionEvent event) {
        Scene menuButtonScene = this.menuButton.getScene();
        Stage stage = (Stage)menuButtonScene.getWindow();

        String fxmlFilename = "view/menu.fxml";
        String stageTitle = "Menu";
        boolean maximizedMode = false;

        WindowManagerUtil.newWindow(fxmlFilename, stageTitle, maximizedMode);
        MenuController.setUserId(userId);

        stage.close();
    }

    private void enableLoadingDescriptionMessage(boolean setVisible) {
        this.loadingDescriptionAsteriskLabel.setVisible(setVisible);
        this.loadingDescriptionLabel.setVisible(setVisible);
    }

    private void enableLoadingCategoriesMessage(boolean setVisible) {
        this.loadingCategoriesAsteriskLabel.setVisible(setVisible);
        this.loadingCategoriesLabel.setVisible(setVisible);
    }

    public static void setUserId(String uid) {
        userId = uid;
    }
}
