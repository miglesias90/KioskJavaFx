package ch.juventus.kiosk.presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationMain extends javafx.application.Application {

    private static ApplicationMain instance;

    public static ApplicationMain getInstance() {
        if(instance == null) {
            instance = new ApplicationMain();
        }
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ch/juventus/kiosk/presentation/view/scene/admin.fxml"));
        showStage(primaryStage, root);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void changeToAdminStage(Stage stage)  {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/ch/juventus/kiosk/presentation/view/scene/admin.fxml"));
            showStage(stage, root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeToCustomerStage(Stage stage)  {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/ch/juventus/kiosk/presentation/view/scene/customer.fxml"));
            showStage(stage, root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeToOrderStage(Stage stage)  {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/ch/juventus/kiosk/presentation/view/scene/order.fxml"));
            showStage(stage, root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void showStage(Stage stage, Parent root) {
        stage.setTitle("Kiosk Simulator");
        stage.setScene(new Scene(root, 1200, 900));
        stage.show();
    }



}
