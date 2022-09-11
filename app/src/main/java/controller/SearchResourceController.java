package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import javafx.util.Callback;
import entity.Resource;
import repository.ResourceRepository;
import task.OpenBrowserTask;
import util.AlertUtil;
import util.ExceptionHandlerUtil;
import util.UrlValidatorUtil;
import util.WindowManagerUtil;

public class SearchResourceController implements Initializable {
    @FXML
    private RadioButton languageIdRadioButton;

    @FXML
    private RadioButton tagsIdRadioButton;

    @FXML
    private RadioButton categoryIdRadioButton;

    @FXML
    private RadioButton userIdRadioButton;
    
    @FXML
    private TextField categoryTextField;

    @FXML
    private TextField languageIdTextField;

    @FXML
    private TextField tagsTextField;

    @FXML
    private TextField userIdTextField;

    @FXML
    private Button applyFilterButton;

    @FXML
    private Button resetFiltersButton;

    @FXML
    private Button menuButton;

    @FXML
    private Button reloadTableButton;

    @FXML
    private TableView<Resource> resourceTableView;

    @FXML
    private TableColumn<Resource, String> urlColumn;

    @FXML
    private TableColumn<Resource, String> languageIdColumn;

    @FXML
    private TableColumn<Resource, String> tagsColumn;

    @FXML
    private TableColumn<Resource, String> categoryColumn;

    @FXML
    private TableColumn<Resource, String> commentColumn;

    @FXML
    private TableColumn<Resource, String> userIdColumn;

    @FXML
    private ToggleGroup radioButtons;

    @FXML
    private Label loadingTableAsteriskLabel;

    @FXML
    private Label loadingTableLabel;
    
    private static String userId;

    private List<Resource> resources;

    private List<TextField> textFields;

    private ObservableList<Resource> tableViewItems;

    private final ResourceRepository resourceRepository = new ResourceRepository();

    @FXML @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.textFields = this.createTextFieldList();
        this.disableTextFields();
        this.setTableView();
        this.enableLoadingTableMessage(true);
        this.getResources();
    }

    @FXML
    private void applyFilter(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton)this.radioButtons.getSelectedToggle();
        
        if (selectedRadioButton == null) return;
        
        String selectedRadioButtonId = selectedRadioButton.getText();

        for (TextField textField : this.textFields) {
            String textFieldId = textField.getId();

            if (textFieldId.equals(selectedRadioButtonId)) {
                String param = textField.getText();

                if (!(param.isBlank() || param.isEmpty())) {
                    this.handleRequest(selectedRadioButtonId, param);
                }

                break;
            }
        }
    }

    @FXML
    private void resetFilters(ActionEvent event) {
        this.radioButtons.selectToggle(null);

        for (TextField textField : this.textFields) {
            textField.clear();
            textField.setDisable(true);
        }

        this.reloadTable(event);
    }

    private void getResources() {
        Task<List<Resource>> getResourcesTask = this.resourceRepository.getResourcesTask();

        getResourcesTask.setOnSucceeded(e -> {
            this.resources = getResourcesTask.getValue();
            this.updateTableView(this.resources);
        });

        getResourcesTask.setOnFailed(e -> {
            this.enableLoadingTableMessage(false);
            Throwable throwable = getResourcesTask.getException();
            ExceptionHandlerUtil.handle(throwable);
        });

        Thread thread = new Thread(getResourcesTask);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void reloadTable(ActionEvent event) {
        this.enableLoadingTableMessage(true);
        this.updateTableView(null);
        this.getResources();
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
    private void handleUnselectedTextFields(ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton)this.radioButtons.getSelectedToggle();

        if (selectedRadioButton != null) {
            for (TextField textField : this.textFields) {
                String textFieldId = textField.getId();
                String selectedRadioButtonText = selectedRadioButton.getText();
                boolean matches = textFieldId.equals(selectedRadioButtonText);
                textField.setDisable(!matches);
            }
        }
    }

    private void handleRequest(String filter, String param) {
        final Task<List<Resource>> resourcesTask = this.resourceRepository.getResourcesTask();

        this.enableLoadingTableMessage(true);

        switch (filter) {
            case "languageId" -> resourcesTask.setOnSucceeded(value -> {
                List<Resource> resources = resourcesTask.getValue();
                this.resources = ResourceRepository.filterResourcesByLanguageId(resources, param);
                this.updateTableView(this.resources);
            });
            case "tags" -> resourcesTask.setOnSucceeded(value -> {
                List<Resource> resources = resourcesTask.getValue();
                this.resources = ResourceRepository.filterResourcesByTags(resources, param);
                this.updateTableView(this.resources);
            });
            case "category" -> resourcesTask.setOnSucceeded(value -> {
                List<Resource> resources = resourcesTask.getValue();
                this.resources = ResourceRepository.filterResourcesByCategory(resources, param);
                this.updateTableView(this.resources);
            });
            case "userId" -> resourcesTask.setOnSucceeded(value -> {
                List<Resource> resources = resourcesTask.getValue();
                this.resources = ResourceRepository.filterResourcesByUserId(resources, param);
                this.updateTableView(this.resources);
            });
        }

        resourcesTask.setOnFailed(value -> {
            Throwable throwable = resourcesTask.getException();
            ExceptionHandlerUtil.handle(throwable);
            updateTableView(null);
        });
        
        Thread thread = new Thread(resourcesTask);
        thread.setDaemon(true);
        thread.start();
    }

    private List<TextField> createTextFieldList() {
        return new ArrayList<TextField>(
            Arrays.asList(
                this.categoryTextField,
                this.languageIdTextField,
                this.tagsTextField,
                this.userIdTextField
            )
        );
    }

    private void disableTextFields() {
        for (TextField textField : this.textFields) {
            textField.setDisable(true);
        }
    }

    private void setTableView() {
        TableViewSelectionModel<Resource> resourceTableViewSelectionModel = this.resourceTableView.getSelectionModel();
        resourceTableViewSelectionModel.setCellSelectionEnabled(true);

        PropertyValueFactory<Resource, String> urlPropertyValueFactory = new PropertyValueFactory<>("url");
        PropertyValueFactory<Resource, String> languageIdPropertyValueFactory = new PropertyValueFactory<>("languageId");
        PropertyValueFactory<Resource, String> tagsPropertyValueFactory = new PropertyValueFactory<>("tags");
        PropertyValueFactory<Resource, String> categoryPropertyValueFactory = new PropertyValueFactory<>("category");
        PropertyValueFactory<Resource, String> commentPropertyValueFactory = new PropertyValueFactory<>("comment");
        PropertyValueFactory<Resource, String> userIdPropertyValueFactory = new PropertyValueFactory<>("userId");

        this.urlColumn.setCellValueFactory(urlPropertyValueFactory);
        this.languageIdColumn.setCellValueFactory(languageIdPropertyValueFactory);
        this.tagsColumn.setCellValueFactory(tagsPropertyValueFactory);
        this.categoryColumn.setCellValueFactory(categoryPropertyValueFactory);
        this.commentColumn.setCellValueFactory(commentPropertyValueFactory);
        this.userIdColumn.setCellValueFactory(userIdPropertyValueFactory);

        Callback<TableColumn<Resource, String>, TableCell<Resource, String>> languageIdTextFieldTableCell = TextFieldTableCell.forTableColumn();
        Callback<TableColumn<Resource, String>, TableCell<Resource, String>> tagsTextFieldTableCell = TextFieldTableCell.forTableColumn();
        Callback<TableColumn<Resource, String>, TableCell<Resource, String>> categoryTextFieldTableCell = TextFieldTableCell.forTableColumn();
        Callback<TableColumn<Resource, String>, TableCell<Resource, String>> commentTextFieldTableCell = TextFieldTableCell.forTableColumn();
        Callback<TableColumn<Resource, String>, TableCell<Resource, String>> userIdTextFieldTableCell = TextFieldTableCell.forTableColumn();

        languageIdColumn.setCellFactory(languageIdTextFieldTableCell);
        tagsColumn.setCellFactory(tagsTextFieldTableCell);
        categoryColumn.setCellFactory(categoryTextFieldTableCell);
        commentColumn.setCellFactory(commentTextFieldTableCell);
        userIdColumn.setCellFactory(userIdTextFieldTableCell);

        resourceTableView.setOnMouseClicked(mouseEvent -> {
            Resource resource = resourceTableViewSelectionModel.getSelectedItem();

            if (resource == null || this.getSelectedCellColumnIndex() != 0 || mouseEvent.getClickCount() != 2) return;

            String url = resource.getUrl();

            AlertType confirmationAlertType = AlertType.CONFIRMATION;
            String confirmationAlertTitle = "Open URL";
            String confirmationAlertHeader = "Open resource's URL";
            String confirmationAlertContent = String.format("Do you wanna open the URL \"%s\" on your default browser?", url);

            Optional<ButtonType> confirmationAlertResponse = AlertUtil.show(confirmationAlertType, confirmationAlertTitle, confirmationAlertHeader, confirmationAlertContent);
            ButtonType buttonType = confirmationAlertResponse.orElse(null);

            if (buttonType == ButtonType.OK) {
                boolean urlValidationSuccess = UrlValidatorUtil.validate(url);

                if (urlValidationSuccess) {
                    OpenBrowserTask openBrowserTask = new OpenBrowserTask(url);

                    openBrowserTask.setOnFailed(value -> {
                        Throwable throwable = openBrowserTask.getException();
                        ExceptionHandlerUtil.handle(throwable);
                    });

                    Thread thread = new Thread(openBrowserTask);
                    thread.setDaemon(true);
                    thread.start();
                } else {
                    AlertType errorAlertType = AlertType.ERROR;
                    String errorAlertTitle = "Invalid URL";
                    String errorAlertHeader = "Couldn't open URL";
                    String errorAlertContent = String.format("\"%s\" is not a valid URL.", url);
                    AlertUtil.show(errorAlertType, errorAlertTitle, errorAlertHeader, errorAlertContent);
                }
            }
        });
    }

    private void updateTableView(List<Resource> resources) {
        this.resourceTableView.getItems().clear();

        if (resources != null) {
            this.tableViewItems = FXCollections.observableArrayList(resources);
            this.resourceTableView.setItems(this.tableViewItems);
            this.enableLoadingTableMessage(false);
        }
    }

    @SuppressWarnings("rawtypes")
    private int getSelectedCellColumnIndex() {
        // https://stackoverflow.com/questions/59634577/why-is-a-javafxml-tableposition-instance-considered-as-a-raw-type-when-type-argu

        TableViewSelectionModel<Resource> tableViewSelectionModel = resourceTableView.getSelectionModel();
        ObservableList<TablePosition> tableViewSelectedCells = tableViewSelectionModel.getSelectedCells();
        TablePosition tablePosition = tableViewSelectedCells.get(0);

        return tablePosition.getColumn();
    }

    private void enableLoadingTableMessage(boolean setVisible) {
        this.loadingTableAsteriskLabel.setVisible(setVisible);
        this.loadingTableLabel.setVisible(setVisible);
    }

    public static void setUserId(String uid) {
        userId = uid;
    }
}
