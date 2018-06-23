/**
 * controller class of order view
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

public class OrderController implements Initializable, IController {

    private ApplicationMain applicationMain;
    private ICommandHandler commandHandler;

    private Kiosk kiosk;
    private Set<ArticleAmount> shopingBasket;


    //Master Panel
    @FXML
    private VBox vbSB;
    @FXML
    private Button btnRemove, btnChose;
    @FXML
    private ComboBox<String> cbKiosk;
    @FXML
    private Label lblTotalSB, lblKioskCash;
    @FXML
    private TableView<ArticleAmount> tbSB;
    @FXML
    private TableColumn<ArticleAmount, String> tbColSbName, tbColSbCategory, tbColSbAmount, tbColSbTotal;

    //View Panel
    @FXML
    private Label lblSupplier;
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

        this.kiosk = null;
        this.shopingBasket = new HashSet<>();

        clearMasterPanel();
        renderMasterPanel();

        clearViewPanel();
        renderViewPanel();

        clearDetailsPanel();
        renderDetailsPanel();
    }

    @FXML
    public void handleBtnChose(ActionEvent actionEvent) {
        if(!cbKiosk.getSelectionModel().getSelectedItem().isEmpty()) {
            kiosk = commandHandler.getKioskByName(cbKiosk.getSelectionModel().getSelectedItem());
        }

        clearMasterPanel();
        renderMasterPanel();

        clearViewPanel();
        renderViewPanel();

        clearDetailsPanel();
        renderDetailsPanel();
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

            commandHandler.buyArticlesFromSupplier(kiosk, articles, total);

            this.shopingBasket = new HashSet<>();


            clearMasterPanel();
            renderMasterPanel();

            clearViewPanel();
            renderViewPanel();

            clearDetailsPanel();
            renderDetailsPanel();

            applicationMain.changeToAdminStage((Stage) tbArticle.getScene().getWindow());
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
       ArticleBase selectedArticle = tbArticle.getSelectionModel().getSelectedItem();


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


        clearMasterPanel();
        renderMasterPanel();
        clearViewPanel();
        renderViewPanel();
        clearDetailsPanel();
        renderDetailsPanel();
    }

    @Override
    public void renderMasterPanel() {
        //Define bindings
        btnRemove.disableProperty().bind(tbSB.getSelectionModel().selectedItemProperty().isNotNull().not());

        for(Kiosk kiosk : commandHandler.getOpenKiosks())
            cbKiosk.getItems().add(kiosk.getName());

        if (kiosk != null){
            cbKiosk.getSelectionModel().select(kiosk.getName());
            cbKiosk.setDisable(true);
            btnChose.setVisible(false);
            lblKioskCash.setText(kiosk.getCash().toString());
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
        cbKiosk.getItems().clear();
        tbSB.getItems().clear();
        vbSB.setVisible(false);
        cbKiosk.setDisable(false);
        btnChose.setVisible(true);
        lblKioskCash.setText("");
    }

    @Override
    public void renderViewPanel() {
        try {
            if (kiosk != null) {
                Set<ArticleBase> articleList = commandHandler.getSupplierArticles(kiosk);

                tbColArticleCategory.setCellValueFactory(new PropertyValueFactory("categoryText"));
                tbColArticleName.setCellValueFactory(new PropertyValueFactory("categoryText"));
                tbColArticlePrice.setCellValueFactory(new PropertyValueFactory("price"));

                tbArticle.getItems().addAll(articleList);
            }
        } catch (Exception e) {
            generateAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void clearViewPanel() {
        tbArticle.getItems().clear();
    }

    @Override
    public void renderDetailsPanel() {
        ArticleBase selectedArticle = tbArticle.getSelectionModel().getSelectedItem();

        if(selectedArticle != null) {
            lblArticleCategory.setText(selectedArticle.getCategoryText());

            lblArticleName.setText(selectedArticle.getName());
            lblArticlePrice.setText(new Double(selectedArticle.getPrice()).toString());

            for (int x = 1; x <= 100; x++)
                cbArticleAmount.getItems().add(x);

            cbArticleAmount.getSelectionModel().selectFirst();

            cbArticleAmount.setDisable(false);

            btnBuy.setVisible(true);
        }

        if(kiosk != null) {
            lblSupplier.setText(kiosk.getSupplier().getName());
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
        lblSupplier.setText("Lieferant Artikel");
    }

    @Override
    public void generateAlert(String title, String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(text);

        alert.showAndWait();
    }

}
