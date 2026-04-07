package com.drinks.components;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * Centralized SVG icon factory — replaces all emoji/text icons throughout the app.
 * SVG paths sourced from Lucide icon set to match the React version exactly.
 * All icons render at a 24×24 viewbox by default; use {@code createIcon(path, size, color)}
 * to customize.
 */
public final class Icons {
    private Icons() {}

    // --- Core paths (Lucide 24×24 viewbox) ---

    private static final String LAYOUT_DASHBOARD = "M3 3h7v9H3V3zm11 0h7v5h-7V3zm0 9h7v9h-7v-9zM3 16h7v5H3v-5z";
    private static final String SHOPPING_CART = "M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6m0 0 0 0M9 22a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm11 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2z";
    private static final String PACKAGE = "M16.5 9.4l-9-5.19M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16zM3.27 6.96 12 12.01l8.73-5.05M12 22.08V12";
    private static final String TRUCK = "M1 3h15v13H1V3zm15 5h4l3 3v5h-7V8zM5.5 21a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5zm13 0a2.5 2.5 0 1 0 0-5 2.5 2.5 0 0 0 0 5z";
    private static final String REFRESH_CW = "M23 4v6h-6M1 20v-6h6M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15";
    private static final String BAR_CHART = "M12 20V10M18 20V4M6 20v-4";
    private static final String USERS = "M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2M9 7a4 4 0 1 0 0-8 4 4 0 0 0 0 8zm14 14v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75";
    private static final String SETTINGS = "M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2zM12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6z";
    private static final String LOG_OUT = "M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4m7 14 5-5-5-5m5 5H9";
    private static final String SEARCH = "M11 19a8 8 0 1 0 0-16 8 8 0 0 0 0 16zM21 21l-4.35-4.35";
    private static final String TRASH_2 = "M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M10 11v6M14 11v6";
    private static final String SHIELD_CHECK = "M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10zM9 12l2 2 4-4";
    private static final String AWARD = "M12 15a7 7 0 1 0 0-14 7 7 0 0 0 0 14zM8.21 13.89 7 23l5-3 5 3-1.21-9.12";
    private static final String LOCK = "M19 11H5a2 2 0 0 0-2 2v7a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7a2 2 0 0 0-2-2zM7 11V7a5 5 0 0 1 10 0v4";
    private static final String MINUS = "M5 12h14";
    private static final String PLUS = "M12 5v14M5 12h14";
    private static final String FILE_TEXT = "M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8zM14 2v6h6M16 13H8M16 17H8M10 9H8";
    private static final String DOWNLOAD = "M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4m4-5 5 5 5-5m-5 5V3";
    private static final String TRENDING_UP = "M23 6l-9.5 9.5-5-5L1 18";
    private static final String ALERT_TRIANGLE = "M10.29 3.86 1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0zM12 9v4m0 4h.01";
    private static final String ALERT_CIRCLE = "M12 22a10 10 0 1 0 0-20 10 10 0 0 0 0 20zM12 8v4m0 4h.01";
    private static final String ARROW_RIGHT = "M5 12h14m-7-7 7 7-7 7";
    private static final String BUILDING = "M6 22V4a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v18zM6 12H4a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h2M18 9h2a2 2 0 0 1 2 2v9a2 2 0 0 1-2 2h-2M10 6h4M10 10h4M10 14h4M10 18h4";
    private static final String STORE = "M2 7l10-5 10 5M4 7v13h16V7M10 21V11h4v10";
    private static final String UPLOAD = "M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4m14-7-5-5-5 5m5-5v12";

    // --- Factory methods ---

    /**
     * Creates an SVG icon Node at default size 16px with the specified color.
     */
    public static Node createIcon(String svgPath, double size, Color color) {
        SVGPath path = new SVGPath();
        path.setContent(svgPath);
        path.setStroke(color);
        path.setStrokeWidth(2);
        path.setFill(Color.TRANSPARENT);

        // Scale from 24×24 viewbox to requested size
        double scale = size / 24.0;
        Group g = new Group(path);
        g.setScaleX(scale);
        g.setScaleY(scale);
        return g;
    }

    public static Node createIcon(String svgPath, double size, String hexColor) {
        return createIcon(svgPath, size, Color.web(hexColor));
    }

    /** Filled icon variant (e.g. for progress dots, badges). */
    public static Node createFilledIcon(String svgPath, double size, Color color) {
        SVGPath path = new SVGPath();
        path.setContent(svgPath);
        path.setFill(color);
        path.setStroke(Color.TRANSPARENT);

        double scale = size / 24.0;
        Group g = new Group(path);
        g.setScaleX(scale);
        g.setScaleY(scale);
        return g;
    }

    // --- Convenience creators (return Node at 16px, foreground color) ---

    public static Node dashboard(double size, Color c)      { return createIcon(LAYOUT_DASHBOARD, size, c); }
    public static Node shoppingCart(double size, Color c)    { return createIcon(SHOPPING_CART, size, c); }
    public static Node pkg(double size, Color c)             { return createIcon(PACKAGE, size, c); }
    public static Node truck(double size, Color c)           { return createIcon(TRUCK, size, c); }
    public static Node refreshCw(double size, Color c)       { return createIcon(REFRESH_CW, size, c); }
    public static Node barChart(double size, Color c)        { return createIcon(BAR_CHART, size, c); }
    public static Node users(double size, Color c)           { return createIcon(USERS, size, c); }
    public static Node settings(double size, Color c)        { return createIcon(SETTINGS, size, c); }
    public static Node logOut(double size, Color c)          { return createIcon(LOG_OUT, size, c); }
    public static Node search(double size, Color c)          { return createIcon(SEARCH, size, c); }
    public static Node trash(double size, Color c)           { return createIcon(TRASH_2, size, c); }
    public static Node shieldCheck(double size, Color c)     { return createIcon(SHIELD_CHECK, size, c); }
    public static Node award(double size, Color c)           { return createIcon(AWARD, size, c); }
    public static Node lock(double size, Color c)            { return createIcon(LOCK, size, c); }
    public static Node minus(double size, Color c)           { return createIcon(MINUS, size, c); }
    public static Node plus(double size, Color c)            { return createIcon(PLUS, size, c); }
    public static Node fileText(double size, Color c)        { return createIcon(FILE_TEXT, size, c); }
    public static Node download(double size, Color c)        { return createIcon(DOWNLOAD, size, c); }
    public static Node trendingUp(double size, Color c)      { return createIcon(TRENDING_UP, size, c); }
    public static Node alertTriangle(double size, Color c)   { return createIcon(ALERT_TRIANGLE, size, c); }
    public static Node alertCircle(double size, Color c)     { return createIcon(ALERT_CIRCLE, size, c); }
    public static Node arrowRight(double size, Color c)      { return createIcon(ARROW_RIGHT, size, c); }
    public static Node building(double size, Color c)        { return createIcon(BUILDING, size, c); }
    public static Node store(double size, Color c)           { return createIcon(STORE, size, c); }
    public static Node upload(double size, Color c)          { return createIcon(UPLOAD, size, c); }
}
