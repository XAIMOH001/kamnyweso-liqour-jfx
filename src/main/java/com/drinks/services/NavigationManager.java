package com.drinks.services;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.util.HashMap;
import java.util.Map;

public class NavigationManager {
    private static NavigationManager instance;
    private StackPane rootContainer;
    private final Map<String, Node> viewCache = new HashMap<>();
    private String currentView = "";
    private String currentPortal = "";

    public static NavigationManager getInstance() {
        if (instance == null) instance = new NavigationManager();
        return instance;
    }

    public void setRootContainer(StackPane container) {
        this.rootContainer = container;
    }

    public void navigate(String viewName, Node node) {
        currentView = viewName;
        if (viewCache.containsKey(viewName)) {
            rootContainer.getChildren().setAll(viewCache.get(viewName));
        } else {
            viewCache.put(viewName, node);
            rootContainer.getChildren().setAll(node);
        }
    }

    public void setPortal(String portal) {
        currentPortal = portal;
    }

    public String getPortal() {
        return currentPortal;
    }

    public String getCurrentView() {
        return currentView;
    }

    public void clearCache() {
        viewCache.clear();
    }
}
