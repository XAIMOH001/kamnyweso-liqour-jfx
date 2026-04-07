package com.drinks.controllers;

import com.drinks.components.StatusBadge;
import com.drinks.models.*;
import com.drinks.services.MockDataService;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class UserManagementController {
    private final VBox view;
    private final VBox contentArea;
    private final ObservableList<Staff> allStaff;
    private final ObservableList<PendingApproval> allPending;
    private final ObservableList<Session> allSessions;
    private final TableView<Staff> staffTable;

    public UserManagementController() {
        view = new VBox(16);
        allStaff = MockDataService.getInstance().getStaff();
        allPending = MockDataService.getInstance().getPendingApprovals();
        allSessions = MockDataService.getInstance().getActiveSessions();

        Label title = new Label("User Management");
        title.getStyleClass().add("ds-title");

        // Tab bar
        HBox tabBar = new HBox(4);
        tabBar.getStyleClass().add("tab-bar");
        Button staffTab = new Button("Staff Accounts");
        staffTab.getStyleClass().addAll("tab-btn");
        staffTab.getStyleClass().add("tab-active");
        Button pendingTab = new Button("Pending Approvals (" + allPending.size() + ")");
        pendingTab.getStyleClass().add("tab-btn");
        staffTab.setOnAction(e -> {
            setActiveTab(staffTab, pendingTab);
            showStaffView();
        });
        pendingTab.setOnAction(e -> {
            setActiveTab(pendingTab, staffTab);
            showPendingView();
        });
        tabBar.getChildren().addAll(staffTab, pendingTab);

        contentArea = new VBox(16);
        staffTable = createStaffTable();

        view.getChildren().addAll(title, tabBar, contentArea);
        showStaffView();
    }

    private void setActiveTab(Button active, Button inactive) {
        active.getStyleClass().add("tab-active");
        inactive.getStyleClass().remove("tab-active");
    }

    private void showStaffView() {
        contentArea.getChildren().clear();

        HBox filters = createStaffFilters();
        TableView<Session> sessionsTable = createSessionsTable();

        VBox sessionsWrapper = new VBox(0);
        sessionsWrapper.getStyleClass().add("ds-card");
        Label sessionsHeader = new Label("Active Sessions");
        sessionsHeader.getStyleClass().add("ds-heading");
        sessionsHeader.setPadding(new Insets(12, 0, 12, 0));
        sessionsWrapper.getChildren().addAll(sessionsHeader, sessionsTable);

        contentArea.getChildren().addAll(filters, staffTable, sessionsWrapper);
    }

    private HBox createStaffFilters() {
        HBox row = new HBox(12);
        row.getStyleClass().add("ds-card");
        row.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, ID, role...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        ComboBox<String> branchField = new ComboBox<>();
        branchField.getItems().addAll("All Branches", "Nakuru", "Mombasa", "Kisumu", "HQ");
        branchField.setValue("All Branches");

        ComboBox<String> statusField = new ComboBox<>();
        statusField.getItems().addAll("All Statuses", "Active", "Inactive", "Suspended");
        statusField.setValue("All Statuses");

        row.getChildren().addAll(searchField, branchField, statusField);

        searchField.textProperty().addListener((obs, o, n) -> applyStaffFilters(searchField, branchField, statusField));
        branchField.valueProperty().addListener((obs, o, n) -> applyStaffFilters(searchField, branchField, statusField));
        statusField.valueProperty().addListener((obs, o, n) -> applyStaffFilters(searchField, branchField, statusField));

        return row;
    }

    private void applyStaffFilters(TextField search, ComboBox<String> branch, ComboBox<String> status) {
        String s = search.getText().toLowerCase();
        String b = branch.getValue();
        String st = status.getValue();
        staffTable.getItems().clear();
        for (Staff staff : allStaff) {
            boolean matchSearch = s.isEmpty() || staff.getName().toLowerCase().contains(s) || staff.getId().toLowerCase().contains(s) || staff.getRole().toLowerCase().contains(s);
            boolean matchBranch = b.equals("All Branches") || staff.getBranch().equals(b);
            boolean matchStatus = st.equals("All Statuses") || staff.getStatus().equals(st);
            if (matchSearch && matchBranch && matchStatus) staffTable.getItems().add(staff);
        }
    }

    private TableView<Staff> createStaffTable() {
        TableView<Staff> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");

        TableColumn<Staff, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(f -> f.getValue().nameProperty());

        TableColumn<Staff, String> colId = new TableColumn<>("Staff ID");
        colId.setCellValueFactory(f -> f.getValue().idProperty());
        colId.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("mono"); }
            }
        });

        TableColumn<Staff, String> colBranch = new TableColumn<>("Branch");
        colBranch.setCellValueFactory(f -> f.getValue().branchProperty());

        TableColumn<Staff, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(f -> f.getValue().roleProperty());

        TableColumn<Staff, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(f -> f.getValue().statusProperty());
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else setGraphic(new StatusBadge(item));
            }
        });

        TableColumn<Staff, String> colLogin = new TableColumn<>("Last Login");
        colLogin.setCellValueFactory(f -> f.getValue().lastLoginProperty());
        colLogin.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Staff, Void> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    HBox btns = new HBox(8);
                    Button view = new Button("View"); view.getStyleClass().add("btn-link");
                    Button suspend = new Button("Suspend"); suspend.getStyleClass().addAll("btn-link", "btn-suspend");
                    Button reset = new Button("Reset"); reset.getStyleClass().add("btn-link");
                    btns.getChildren().addAll(view, suspend, reset);
                    setGraphic(btns);
                }
            }
        });

        tbl.getColumns().addAll(colName, colId, colBranch, colRole, colStatus, colLogin, colActions);
        tbl.setItems(allStaff);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(250);

        return tbl;
    }

    private TableView<Session> createSessionsTable() {
        TableView<Session> tbl = new TableView<>();
        tbl.getStyleClass().add("table-view");

        TableColumn<Session, String> colName = new TableColumn<>("Staff Name");
        colName.setCellValueFactory(f -> f.getValue().nameProperty());

        TableColumn<Session, String> colBranch = new TableColumn<>("Branch");
        colBranch.setCellValueFactory(f -> f.getValue().branchProperty());

        TableColumn<Session, String> colLogin = new TableColumn<>("Login Time");
        colLogin.setCellValueFactory(f -> f.getValue().loginProperty());

        TableColumn<Session, String> colActivity = new TableColumn<>("Last Activity");
        colActivity.setCellValueFactory(f -> f.getValue().lastActivityProperty());
        colActivity.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else { setText(item); getStyleClass().add("muted"); }
            }
        });

        TableColumn<Session, Void> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Button btn = new Button("Terminate Session");
                    btn.getStyleClass().addAll("btn-link", "btn-terminate");
                    setGraphic(btn);
                }
            }
        });

        tbl.getColumns().addAll(colName, colBranch, colLogin, colActivity, colActions);
        tbl.setItems(allSessions);
        tbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tbl.setPrefHeight(150);

        return tbl;
    }

    private void showPendingView() {
        contentArea.getChildren().clear();

        FlowPane cards = new FlowPane();
        cards.setHgap(16);
        cards.setVgap(16);

        for (PendingApproval p : allPending) {
            VBox card = new VBox(12);
            card.getStyleClass().add("ds-card");
            card.setPrefWidth(350);

            HBox header = new HBox();
            header.setAlignment(Pos.TOP_LEFT);
            VBox info = new VBox(2,
                new Label(p.getName()) {{ getStyleClass().add("ds-heading"); }},
                new Label(p.getRole() + " \u00B7 " + p.getBranch()) {{ getStyleClass().add("ds-text-muted"); }}
            );
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label date = new Label(p.getDate());
            date.getStyleClass().add("ds-text-small");
            header.getChildren().addAll(info, spacer, date);

            VBox details = new VBox(6,
                new HBox(4, new Label("Phone:") {{ getStyleClass().add("ds-text-muted"); }}, new Label(p.getPhone()) {{ getStyleClass().add("ds-heading-sm"); }}),
                new HBox(4, new Label("Email:") {{ getStyleClass().add("ds-text-muted"); }}, new Label(p.getEmail()) {{ getStyleClass().add("ds-heading-sm"); }})
            );

            HBox btns = new HBox(8);
            Button approve = new Button("Approve");
            approve.getStyleClass().add("btn-success");
            approve.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(approve, Priority.ALWAYS);
            Button reject = new Button("Reject");
            reject.getStyleClass().add("btn-danger");
            reject.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(reject, Priority.ALWAYS);
            btns.getChildren().addAll(approve, reject);

            card.getChildren().addAll(header, details, btns);
            cards.getChildren().add(card);
        }

        contentArea.getChildren().add(cards);
    }

    public VBox getView() { return view; }
}
