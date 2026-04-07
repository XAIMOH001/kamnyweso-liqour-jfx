package com.drinks;

import com.drinks.controllers.PortalSelectorController;
import com.drinks.services.NavigationManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        StackPane root = new StackPane();
        root.getStylesheets().add(getClass().getResource("/com/drinks/styles/main.css").toExternalForm());

        NavigationManager nav = NavigationManager.getInstance();
        nav.setRootContainer(root);

        PortalSelectorController portal = new PortalSelectorController();
        nav.navigate("portal", portal.getView());

        Scene scene = new Scene(root, 1280, 800);
        stage.setTitle("Kamnyweso Liquor Store");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
