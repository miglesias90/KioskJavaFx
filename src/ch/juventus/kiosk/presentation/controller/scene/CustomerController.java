/**
 * controller class of customer view
 *
 * @author Miguel Iglesias
 */
package ch.juventus.kiosk.presentation.controller.scene;

import ch.juventus.kiosk.lib.entity.article.base.ArticleBase;
import ch.juventus.kiosk.lib.entity.businessunit.impl.Kiosk;
import ch.juventus.kiosk.logic.base.ICommandHandler;
import ch.juventus.kiosk.logic.impl.CommandHandler;
import ch.juventus.kiosk.presentation.ApplicationMain;
import ch.juventus.kiosk.presentation.controller.IController;
import ch.juventus.kiosk.presentation.model.ArticleAmount;
import ch.juventus.kiosk.presentation.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class CustomerController implements Initializable, IController {

    private ApplicationMain applicationMain;
    private ICommandHandler commandHandler;

    private Customer customer;
    private Set<ArticleAmount> shopingBasket;

    //Master Panel
    @FXML
    private VBox vBoxLogin, vBoxCustomer, vbSB;
    @FXML
    private Button btnLogin, btnRemove;
    @FXML
    private TextField inputCustomerName;
    @FXML
    private ComboBox<String> cbCustomerAge, cbKiosk;
    @FXML
    private Label lblKiosk, lblCustomer, lblCustomerAge, lblTotalSB;
    @FXML
    private TableView<ArticleAmount> tbSB;
    @FXML
    private TableColumn<ArticleAmount, String> tbColSbName, tbColSbCategory, tbColSbAmount, tbColSbTotal;

    //View Panel
    @FXML
    private TableView<ArticleBase> tbArticle;
    @FXML
    private TableColumn<Kiosk,String> tbColArticleCategory, tbColArticleName, tbColArticlePrice;

    //Details Panel
    @FXML
    private Label lblArticleCategory, lblArticleName, lblArticlePrice;
    @FXML
    private ComboBox<Integer> cbArticleAmount;
    @FXML
    private Button btnBuy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applicationMain = ApplicationMain.getInstance();
        commandHandler = CommandHandler.getInstance();

        this.customer = null;
        this.shopingBasket = new HashSet<>();

        clearMasterPanel();
        renderMasterPanel();

        clearViewPanel();
        renderViewPanel();

        clearDetailsPanel();
        renderDetailsPanel();
    }

    @FXML
    public void handleBtnLogin(ActionEvent actionEvent) {
        Kiosk kiosk = commandHandler.getKioskByName(cbKiosk.getSelectionModel().getSelectedItem());
        String name = inputCustomerName.getText();
        int age = Integer.parseInt(cbCustomerAge.getSelectionModel().getSelectedItem());
        customer = new Customer(name, age, kiosk);

        this.shopingBasket = new HashSet<>();


        clearMasterPanel();
        renderMasterPanel();

        clearViewPanel();
        renderViewPanel();
    }

    @FXML
    public void handleBtnLogout(ActionEvent actionEvent) {
      applicationMain.changeToAdminStage((Stage)vBoxLogin.getScene().getWindow());
    }

    @FXML
    public void handleBtnRemove(ActionEvent actionEvent) {
        shopingBasket.remove(tbSB.getSelectionModel().getSelectedItem());

        clearMasterPanel();
        renderMasterPanel();

        clearViewPanel();
        renderViewPanel();

        clearDetailsPanel();
        renderDetailsPanel();
    }

    @FXML
    public void handleBtnPay(ActionEvent actionEvent) {

        try {
            Map<ArticleBase, Integer> articles = new HashMap<>();
            double total = 0.0;

            for (ArticleAmount articleAmount : shopingBasket) {
                articles.put(articleAmount.getArticle(), articleAmount.getAmount());
                total += articleAmount.getTotalPrice();
            }

            commandHandler.buyArticles(customer.getName(), customer.getKiosk(), articles, total);

            this.shopingBasket = new HashSet<>();


            clearMasterPanel();
            renderMasterPanel();

            clearViewPanel();
            renderViewPanel();

            clearDetailsPanel();
            renderDetailsPanel();
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleTbArticleOnClick(MouseEvent mouseEvent) {
        clearDetailsPanel();
        renderDetailsPanel();
    }

    @FXML
    public void handleBtnBuy(ActionEvent actionEvent) {

        try {
            ArticleBase selectedArticle = tbArticle.getSelectionModel().getSelectedItem();

            if (commandHandler.allowedToBuy(selectedArticle, customer.getAge())) {
                boolean found = false;

                for (ArticleAmount articleAmount : shopingBasket) {
                    if (articleAmount.getArticle() == selectedArticle) {
                        int oldAmount = articleAmount.getAmount();

                        articleAmount.setAmount(oldAmount + cbArticleAmount.getSelectionModel().getSelectedItem());

                        found = true;
                        break;
                    }
                }

                if (!found)
                    shopingBasket.add(new ArticleAmount(selectedArticle, cbArticleAmount.getSelectionModel().getSelectedItem()));

            } else {
                generateAlert("Minderjährig", "Der Kauf ist aufgrund des Alters nicht erlaubt!", Alert.AlertType.WARNING);
            }
            clearMasterPanel();
            renderMasterPanel();
            clearViewPanel();
            renderViewPanel();
            clearDetailsPanel();
            renderDetailsPanel();
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void renderMasterPanel() {
        //Define bindings
        btnLogin.disableProperty().bind(inputCustomerName.textProperty().isEmpty());
        btnRemove.disableProperty().bind(tbSB.getSelectionModel().selectedItemProperty().isNotNull().not());

        //Render Login Panel
        if (customer != null) {
            vBoxCustomer.setVisible(true);
            vBoxLogin.setVisible(false);
            lblKiosk.setText(customer.getKiosk().getName());
            lblCustomer.setText(customer.getName());
            lblCustomerAge.setText(Integer.toString(customer.getAge()));
        } else {
            for(Kiosk kiosk : commandHandler.getOpenKiosks())
                cbKiosk.getItems().add(kiosk.getName());

            cbKiosk.getSelectionModel().selectFirst();

            vBoxCustomer.setVisible(false);
            vBoxLogin.setVisible(true);

            for(int x = 14; x <= 99; x++)
                cbCustomerAge.getItems().add(Integer.toString(x));

            cbCustomerAge.getSelectionModel().selectFirst();
        }

        //Render shopping basket
        if(!shopingBasket.isEmpty()) {
            tbColSbCategory.setCellValueFactory(new PropertyValueFactory("articleCategoryText"));
            tbColSbName.setCellValueFactory(new PropertyValueFactory("articleName"));
            tbColSbAmount.setCellValueFactory(new PropertyValueFactory("amount"));
            tbColSbTotal.setCellValueFactory(new PropertyValueFactory("totalPrice"));

            tbSB.getItems().addAll(shopingBasket);

            double total = 0;

            for (ArticleAmount articleAmount : tbSB.getItems()) {
                total += articleAmount.getTotalPrice();
            }

            lblTotalSB.setText(String.valueOf(total));

            vbSB.setVisible(true);
        }
    }

    @Override
    public void clearMasterPanel() {
        vBoxLogin.setVisible(true);
        cbKiosk.getItems().clear();
        inputCustomerName.setText("");
        cbCustomerAge.getItems().clear();
        vBoxCustomer.setVisible(false);
        lblTotalSB.setText("");
        tbSB.getItems().clear();
        vbSB.setVisible(false);


    }

    @Override
    public void renderViewPanel() {
        try {
            if (customer != null) {
                Set<ArticleBase> articleList = commandHandler.getArticles(customer.getKiosk());

                tbColArticleCategory.setCellValueFactory(new PropertyValueFactory("categoryText"));
                tbColArticleName.setCellValueFactory(new PropertyValueFactory("categoryText"));
                tbColArticlePrice.setCellValueFactory(new PropertyValueFactory("price"));


                tbArticle.getItems().addAll(articleList);
            }
        }catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @Override
    public void clearViewPanel() {
        tbArticle.getItems().clear();
    }

    @Override
    public void renderDetailsPanel() {
        try {


            ArticleBase selectedArticle = tbArticle.getSelectionModel().getSelectedItem();

            if (selectedArticle != null && customer != null) {
                lblArticleCategory.setText(selectedArticle.getCategoryText());

                lblArticleName.setText(selectedArticle.getName());
                lblArticlePrice.setText(new Double(selectedArticle.getPrice()).toString());

                int amount = commandHandler.getArticleAmount(customer.getKiosk(), selectedArticle);
                for (int x = 1; x <= amount; x++)
                    cbArticleAmount.getItems().add(x);

                cbArticleAmount.getSelectionModel().selectFirst();

                cbArticleAmount.setDisable(false);

                btnBuy.setVisible(true);

            }
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @Override
    public void clearDetailsPanel() {
        lblArticleCategory.setText("");
        lblArticleName.setText("");
        lblArticlePrice.setText("");
        cbArticleAmount.getItems().clear();;
        cbArticleAmount.setDisable(true);
        btnBuy.setVisible(false);
    }

    @Override
    public void generateAlert(String title, String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(text);

        alert.showAndWait();
    }

}
