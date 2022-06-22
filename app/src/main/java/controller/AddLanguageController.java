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
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import javafx.util.Callback;
import entity.Language;
import repository.LanguageRepository;
import util.AlertUtil;
import util.ExceptionHandlerUtil;
import repository.RepositoryResponse;
import util.WindowManagerUtil;

public class AddLanguageController implements Initializable {
    @FXML
    private TableView<Language> languageTableView;

    private ObservableList<Language> tableViewItems;

    @FXML
    private TableColumn<Language, String> languageIdColumn;

    @FXML
    private TableColumn<Language, String> firstAppearedColumn;

    @FXML
    private TableColumn<Language, String> paradigmColumn;

    @FXML
    private TableColumn<Language, String> userIdColumn;

    @FXML
    private TextField languageIdTextField;

    @FXML
    private TextField firstAppearedTextField;

    @FXML
    private TextArea paradigmTextArea;

    @FXML
    private Button menuButton;

    @FXML
    private Button addLanguageButton;

    @FXML
    private Button reloadTableButton;

    @FXML
    private Label loadingTableAsteriskLabel;

    @FXML
    private Label loadingTableLabel;

    private List<Language> languages;

    private static String userId;

    private final LanguageRepository languageRepository = new LanguageRepository();

    @FXML @Override
    public void initialize(URL location, ResourceBundle resources) {
        paradigmTextArea.setWrapText(true);
        this.setTableView();
        this.enableLoadingTableMessage(true);
        this.reloadTable(null);
    }

    @FXML
    private void addLanguage(ActionEvent event) {
        String languageId = this.languageIdTextField.getText();
        String firstAppeared = this.firstAppearedTextField.getText();
        String paradigm = this.paradigmTextArea.getText();

        Language language = new Language(languageId, firstAppeared, paradigm, userId);

        Task<RepositoryResponse> postLanguageTask = this.languageRepository.postLanguageTask(language);

        postLanguageTask.setOnSucceeded(e -> {
            RepositoryResponse repositoryResponse = postLanguageTask.getValue();
            String repositoryResponseMessage = repositoryResponse.getMessage();
            boolean languagePosted = repositoryResponse.getSuccess();

            AlertType alertType = AlertType.INFORMATION;
            String alertTitle = "Add language";
            String alertHeader;
            String alertContent;

            if (languagePosted) {
                this.reloadTable(null);
                alertHeader = "Language added";
                alertContent = String.format("Status: %s", repositoryResponseMessage);
                this.clearInputs();
            } else {
                alertHeader = "Couldn't add language";
                alertContent = String.format("Reason: %s", repositoryResponseMessage);
            }

            AlertUtil.show(alertType, alertTitle, alertHeader, alertContent);
        });

        postLanguageTask.setOnFailed(e -> {
            Throwable throwable = postLanguageTask.getException();
            ExceptionHandlerUtil.handle(throwable);
        });

        Thread thread = new Thread(postLanguageTask);
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

    @FXML
    private void reloadTable(ActionEvent event) {
        ObservableList<Language> languageTableViewItems = this.languageTableView.getItems();
        languageTableViewItems.clear();

        this.enableLoadingTableMessage(true);

        Task<List<Language>> getLanguagesTask = this.languageRepository.getLanguagesTask();
        
        getLanguagesTask.setOnSucceeded(e -> {
            this.languages = getLanguagesTask.getValue();
            this.updateTableView(this.languages);
        });

        getLanguagesTask.setOnFailed(e -> {
            this.enableLoadingTableMessage(false);
            Throwable throwable = getLanguagesTask.getException();
            ExceptionHandlerUtil.handle(throwable);
        });

        Thread thread = new Thread(getLanguagesTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void setTableView() {
        TableView.TableViewSelectionModel<Language> languageTableViewSelectionModel = this.languageTableView.getSelectionModel();
        languageTableViewSelectionModel.setCellSelectionEnabled(true);

        PropertyValueFactory<Language, String> languageIdPropertyValueFactory = new PropertyValueFactory<>("languageId");
        PropertyValueFactory<Language, String> firstAppearedPropertyValueFactory = new PropertyValueFactory<>("firstAppeared");
        PropertyValueFactory<Language, String> paradigmPropertyValueFactory = new PropertyValueFactory<>("paradigm");
        PropertyValueFactory<Language, String> userIdPropertyValueFactory = new PropertyValueFactory<>("userId");

        this.languageIdColumn.setCellValueFactory(languageIdPropertyValueFactory);
        this.firstAppearedColumn.setCellValueFactory(firstAppearedPropertyValueFactory);
        this.paradigmColumn.setCellValueFactory(paradigmPropertyValueFactory);
        this.userIdColumn.setCellValueFactory(userIdPropertyValueFactory);

        Callback<TableColumn<Language, String>, TableCell<Language, String>> languageIdTextFieldTableCell = TextFieldTableCell.forTableColumn();
        Callback<TableColumn<Language, String>, TableCell<Language, String>> firstAppearedTextFieldTableCell = TextFieldTableCell.forTableColumn();
        Callback<TableColumn<Language, String>, TableCell<Language, String>> paradigmTextFieldTableCell = TextFieldTableCell.forTableColumn();
        Callback<TableColumn<Language, String>, TableCell<Language, String>> userIdTextFieldTableCell = TextFieldTableCell.forTableColumn();

        this.languageIdColumn.setCellFactory(languageIdTextFieldTableCell);
        this.firstAppearedColumn.setCellFactory(firstAppearedTextFieldTableCell);
        this.paradigmColumn.setCellFactory(paradigmTextFieldTableCell);
        this.userIdColumn.setCellFactory(userIdTextFieldTableCell);
    }

    private void updateTableView(List<Language> languages) {
        ObservableList<Language> languageTableViewItems = this.languageTableView.getItems();
        languageTableViewItems.clear();

        if (languages != null) {
            this.tableViewItems = FXCollections.observableArrayList(languages);
            this.languageTableView.setItems(this.tableViewItems);
            this.enableLoadingTableMessage(false);
        }
    }

    private void clearInputs() {
        this.languageIdTextField.clear();
        this.firstAppearedTextField.clear();
        this.paradigmTextArea.clear();
    }

    private void enableLoadingTableMessage(boolean setVisible) {
        this.loadingTableAsteriskLabel.setVisible(setVisible);
        this.loadingTableLabel.setVisible(setVisible);
    }

    public static void setUserId(String uid) {
        userId = uid;
    }
}
