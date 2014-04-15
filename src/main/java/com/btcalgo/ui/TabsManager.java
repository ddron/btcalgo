package com.btcalgo.ui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TabsManager {

    public void init() {

        Stage primaryStage = null;
        Group root = new Group();
        Scene scene = new Scene(root, 400, 300, Color.WHITE);

        TabPane tabPane = new TabPane();
        BorderPane mainPane = new BorderPane();

        //Create Tabs
        Tab tabA = new Tab();
        tabA.setText("Tab A");
        //Add something in Tab
        Button tabA_button = new Button("Button@Tab A");
        tabA.setContent(tabA_button);
        tabPane.getTabs().add(tabA);

        Tab tabB = new Tab();
        tabB.setText("Tab B");
        //Add something in Tab
        StackPane tabB_stack = new StackPane();
        tabB_stack.setAlignment(Pos.CENTER);
        tabB_stack.getChildren().add(new Label("Label@Tab B"));
        tabB.setContent(tabB_stack);
        tabPane.getTabs().add(tabB);

        Tab tabC = new Tab();
        tabC.setText("Tab C");
        //Add something in Tab
        VBox tabC_vBox = new VBox();
        tabC_vBox.getChildren().addAll(
                new Button("Button 1@Tab C"),
                new Button("Button 2@Tab C"),
                new Button("Button 3@Tab C"),
                new Button("Button 4@Tab C"));
        tabC.setContent(tabC_vBox);
        tabPane.getTabs().add(tabC);

        mainPane.setCenter(tabPane);

        mainPane.prefHeightProperty().bind(scene.heightProperty());
        mainPane.prefWidthProperty().bind(scene.widthProperty());

        root.getChildren().add(mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
