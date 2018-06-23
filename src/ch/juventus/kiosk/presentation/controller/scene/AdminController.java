/**
 * controller class of admin view
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.presentation.controller.scene;

import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.lib.entity.businessunit.type.KioskState;
import ch.juventus.kiosk.logic.base.ICommandHandler;
import ch.juventus.kiosk.logic.impl.CommandHandler;
import ch.juventus.kiosk.presentation.ApplicationMain;
import ch.juventus.kiosk.presentation.client.thread.ClientRunnable;
import ch.juventus.kiosk.presentation.controller.IController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class AdminController implements Initializable, IController {

    private ApplicationMain applicationMain;
    private ICommandHandler commandHandler;

    //Master Panel
    @FXML
    private TextField inputKioskName, inputKioskLocation, inputKioskCash, inputKioskEmployeeToAdd;
    @FXML
    private ListView<String> listViewKioskEmployeesToAdd;
    @FXML
    private Button btnKioskAddEmployee;
    @FXML
    private Button btnRemoveEmployee;
    @FXML
    private Button btnCreateKiosk;

    //View Panel
    @FXML
    private TableView<Kiosk> tbKiosks;
    @FXML
    private TableColumn<Kiosk,String> tbColKioskState, tbColKioskName, tbColKioskLocation, tbColKioskCash;

    //Details Panel
    @FXML
    private Label lblName, lblLocation, lblCash, lblState;
    @FXML
    private ListView<String> listViewKioskEmployees;
    @FXML
    private Button btnChangeKioskState, btnBuy, btnOrderArticles, btnIventory, btnTestThreads;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        applicationMain = ApplicationMain.getInstance();
        commandHandler = CommandHandler.getInstance();

        clearMasterPanel();
        renderMasterPanel();

        clearViewPanel();
        renderViewPanel();

        clearDetailsPanel();
        renderDetailsPanel();

    }

    @FXML
    public void handleBtnAddEmployee(ActionEvent actionEvent) {
        String employeName = inputKioskEmployeeToAdd.getText();

        if (listViewKioskEmployeesToAdd.getItems().contains(employeName)) {
            generateAlert("Warnung", "Dieser Mitarbeiter existiert bereits", Alert.AlertType.WARNING);
        } else {
            listViewKioskEmployeesToAdd.getItems().add(employeName);
        }

    }

    @FXML
    public void handleBtnRemoveEmployee(ActionEvent actionEvent) {
        listViewKioskEmployeesToAdd.getItems().remove(listViewKioskEmployeesToAdd.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void handleBtnCreateKioskAction(ActionEvent actionEvent) {
        try {
            Set<String> employees = new HashSet<>();
            for (String employee : listViewKioskEmployeesToAdd.getItems()) {
                employees.add(employee);
            }

            commandHandler.addKisok(inputKioskName.getText(), inputKioskLocation.getText(),
                    Double.parseDouble(inputKioskCash.getText()), employees);


            clearMasterPanel();

            clearViewPanel();
            renderViewPanel();

            clearDetailsPanel();
            renderDetailsPanel();
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }


    }

    @FXML
    public void handleTbKioskOnClick(MouseEvent mouseEvent) {
        clearDetailsPanel();
        renderDetailsPanel();
    }

    @FXML
    public void handleBtnChangeKioskState(ActionEvent actionEvent) {
        try {
            Kiosk selectedKiosk = tbKiosks.getSelectionModel().getSelectedItem();

            CommandHandler.getInstance().changeKioskState(selectedKiosk);

            clearViewPanel();
            renderViewPanel();
            tbKiosks.getSelectionModel().select(selectedKiosk);
            clearDetailsPanel();
            renderDetailsPanel();
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    public void handleBtnBuy(ActionEvent actionEvent) {
        applicationMain.changeToCustomerStage((Stage)inputKioskCash.getScene().getWindow());
    }

    @FXML
    public void handleBtnOrderArticles(ActionEvent actionEvent) {
        applicationMain.changeToOrderStage((Stage)inputKioskCash.getScene().getWindow());
    }

    @FXML
    public void handleBtnIventory(ActionEvent actionEvent) {
        try {
            commandHandler.generateInventory(tbKiosks.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handlebtnTestThreads(ActionEvent actionEvent) {
        try {
            for (int x = 0; x <= 10; x++) {
                Thread.sleep(1000);
                ClientRunnable clientRunnable = new ClientRunnable("Kunde " + x, tbKiosks.getSelectionModel().getSelectedItem());
                clientRunnable.run();
            }
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void renderMasterPanel() {
        //Define Bindings
        BooleanBinding bindBtnCreateKiosk = inputKioskName.textProperty().isEmpty()
                                            .or(inputKioskLocation.textProperty().isEmpty())
                                            .or(inputKioskCash.textProperty().isEmpty()
                                            .or(Bindings.isEmpty(listViewKioskEmployeesToAdd.getItems())));

        btnCreateKiosk.disableProperty().bind(bindBtnCreateKiosk);

        btnKioskAddEmployee.disableProperty().bind(inputKioskEmployeeToAdd.textProperty().isEmpty());

        btnRemoveEmployee.disableProperty().bind(listViewKioskEmployeesToAdd.getSelectionModel().selectedItemProperty().isNotNull().not());
    }

    @Override
    public void clearMasterPanel() {
        inputKioskName.setText("");
        inputKioskLocation.setText("");
        inputKioskCash.setText("");
        inputKioskEmployeeToAdd.setText("");
        listViewKioskEmployeesToAdd.getItems().clear();
    }

    @Override
    public void renderViewPanel() {
        Set<Kiosk> kioskList = commandHandler.getKiosks();

        tbColKioskState.setCellValueFactory(new PropertyValueFactory("stateText"));
        tbColKioskName.setCellValueFactory(new PropertyValueFactory("name"));
        tbColKioskLocation.setCellValueFactory(new PropertyValueFactory("location"));
        tbColKioskCash.setCellValueFactory(new PropertyValueFactory("cash"));

        tbKiosks.getItems().addAll(kioskList);
    }

    @Override
    public void clearViewPanel() {
        tbKiosks.getItems().clear();
    }

    @Override
    public void renderDetailsPanel() {
        Kiosk selectedKiosk = tbKiosks.getSelectionModel().getSelectedItem();


        if(selectedKiosk != null) {
            lblName.setText(selectedKiosk.getName());
            lblLocation.setText(selectedKiosk.getLocation());
            lblCash.setText(selectedKiosk.getCash().toString());
            lblState.setText(selectedKiosk.getStateText());

            String btnChangeKioskStateLabel = "";
            if (KioskState.CLOSED.equals(selectedKiosk.getState())) {
                btnChangeKioskStateLabel = "Öffnen";
            } else {
                btnChangeKioskStateLabel = "Schliessen";
            }
            btnChangeKioskState.setText(btnChangeKioskStateLabel);
            listViewKioskEmployees.getItems().addAll(selectedKiosk.getEmployees());

            btnChangeKioskState.setVisible(true);
            btnBuy.setVisible(true);
            btnOrderArticles.setVisible(true);
            btnIventory.setVisible(true);
            btnTestThreads.setVisible(true);

            if(selectedKiosk.getState() == KioskState.CLOSED) {
                btnBuy.setDisable(true);
                btnOrderArticles.setDisable(true);
                btnIventory.setDisable(true);
                btnTestThreads.setDisable(true);
            } else {
                btnBuy.setDisable(false);
                btnOrderArticles.setDisable(false);
                btnIventory.setDisable(false);
                btnTestThreads.setDisable(false);
            }
        }


    }

    @Override
    public void clearDetailsPanel() {
        lblName.setText("");
        lblLocation.setText("");
        lblCash.setText("");
        lblState.setText("");
        listViewKioskEmployees.getItems().clear();
        btnChangeKioskState.setVisible(false);
        btnBuy.setVisible(false);
        btnOrderArticles.setVisible(false);
        btnIventory.setVisible(false);
        btnTestThreads.setVisible(false);;
    }

    @Override
    public void generateAlert(String title, String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(text);

        alert.showAndWait();
    }

}
