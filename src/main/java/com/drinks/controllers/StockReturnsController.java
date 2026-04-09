package com.drinks.controllers;

import com.drinks.components.Icons;
import com.drinks.components.StatusBadge;
import com.drinks.models.Return;
import com.drinks.services.MockDataService;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;

public class StockReturnsController {
    private final VBox view;
    private final TableView<Return> table;
    private final ObservableList<Return> allReturns;
    private File selectedPhotoFile;
    private Label photoFileNameLabel;

    public StockReturnsController() {
        view = new VBox(20);
        allReturns = MockDataService.getInstance().getBranchReturns();

        Label title = new Label("Return Damaged Stock to HQ");
        title.getStyleClass().add("ds-title");

        VBox returnForm = createReturnForm();
        table = createReturnsTable();

        view.getChildren().addAll(title, returnForm, table);
    }

    private VBox createReturnForm() {
        VBox card = new VBox(12);
        card.getStyleClass().add("ds-card");

        Label header = new Label("Raise New Return");
        header.getStyleClass().add("ds-heading");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(8);

        Label itemL = new Label("Item"); itemL.getStyleClass().add("ds-label");
        ComboBox<String> itemF = new ComboBox<>();
        itemF.getItems().addAll("Select item...", "Tusker Lager", "Guinness 500ml", "Coca Cola", "Fanta Orange", "White Cap", "Stoney Ginger");
        itemF.setValue("Select item...");

        Label branchL = new Label("Branch"); branchL.getStyleClass().add("ds-label");
        TextField branchF = new TextField("Nakuru");
        branchF.setEditable(false);
        branchF.getStyleClass().add("disabled-field");

        Label qtyL = new Label("Quantity"); qtyL.getStyleClass().add("ds-label");
        TextField qtyF = new TextField();
        qtyF.setPromptText("e.g. 4");

        Label photoL = new Label("Photo Evidence"); photoL.getStyleClass().add("ds-label");
        photoFileNameLabel = new Label("No file selected");
        photoFileNameLabel.getStyleClass().addAll("muted", "text-ellipsis");
        photoFileNameLabel.setMaxWidth(180);
        photoFileNameLabel.setTooltip(new Tooltip("No file selected"));
        
        Button photoBtn = new Button("Upload photo");
        photoBtn.setGraphic(Icons.upload(14, Color.web("#6B7280")));
        photoBtn.getStyleClass().add("btn-outline");
        photoBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Photo Evidence");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.webp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            if (selectedPhotoFile != null) {
                fileChooser.setInitialDirectory(selectedPhotoFile.getParentFile());
            }
            File selected = fileChooser.showOpenDialog(photoBtn.getScene().getWindow());
            if (selected != null) {
                selectedPhotoFile = selected;
                String displayName = selected.getName();
                photoFileNameLabel.setText(displayName);
                photoFileNameLabel.setTooltip(new Tooltip(selected.getAbsolutePath()));
                photoBtn.setText("Change photo");
            }
        });
        
        VBox photoBox = new VBox(6, photoBtn, photoFileNameLabel);

        Label reasonL = new Label("Reason for Return"); reasonL.getStyleClass().add("ds-label");
        TextArea reasonF = new TextArea();
        reasonF.setPromptText("Describe damage or reason for return...");
        reasonF.setPrefRowCount(3);

        form.add(itemL, 0, 0); form.add(itemF, 0, 1);
        form.add(branchL, 1, 0); form.add(branchF, 1, 1);
        form.add(qtyL, 0, 2); form.add(qtyF, 0, 3);
        form.add(photoL, 1, 2); form.add(photoBox, 1, 3);
        form.add(reasonL, 0, 4);
        form.add(reasonF, 0, 5);
        GridPane.setColumnSpan(reasonF, 2);

        Button submitBtn = new Button("Submit Return Request");
        submitBtn.setPrefWidth(200);

        card.getChildren().addAll(header, form, submitBtn);
        return card;
    }

    private TableView<Return> createReturnsTable() {
        VBox wrapper = new VBox(0);
        wrapper.getStyleClass().add("ds-card");

        Label header = new Label("My Return Requests");
        header.getStyleClass().add("ds-heading");
        header.setPadding(new Insets(12, 0, 12, 0));

        TableView<Return> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");

        TableColumn<Return, String> colId = new TableColumn<>("Return ID");
        colId.setCellValueFactory(f -> f.getValue().idProperty());
        colId.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("mono"); }
            }
        });

        TableColumn<Return, String> colItem = new TableColumn<>("Item");
        colItem.setCellValueFactory(f -> f.getValue().itemProperty());

        TableColumn<Return, Number> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(f -> f.getValue().qtyProperty());

        TableColumn<Return, String> colReason = new TableColumn<>("Reason");
        colReason.setCellValueFactory(f -> f.getValue().reasonProperty());
        colReason.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Return, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(f -> f.getValue().dateProperty());
        colDate.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Return, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(f -> f.getValue().statusProperty());
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else setGraphic(new StatusBadge(item));
            }
        });

        TableColumn<Return, String> colResponse = new TableColumn<>("HQ Response");
        colResponse.setCellValueFactory(f -> f.getValue().responseProperty());
        colResponse.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        tbl.getColumns().addAll(colId, colItem, colQty, colReason, colDate, colStatus, colResponse);
        tbl.setItems(allReturns);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(250);

        wrapper.getChildren().addAll(header, tbl);
        return tbl;
    }

    public VBox getView() { return view; }
}
