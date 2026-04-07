# Kamnyweso Liquor Store - JavaFX Desktop Application

A complete JavaFX desktop conversion of the React web application for the Kamnyweso Distributed Drinks Management System.

## Project Structure

```
src/
  main/
    java/com/drinks/
      App.java                          ← JavaFX entry point
      components/
        OTPInput.java                   ← Multi-digit OTP input component
        StatCard.java                   ← Metric card with accent color
        StatusBadge.java                ← Color-coded status labels
      controllers/
        AdminDashboardController.java   ← HQ dashboard with stats, charts, alerts
        AdminLayoutController.java      ← Admin portal layout with sidebar
        AdminPOSController.java         ← HQ point of sale terminal
        BranchLayoutController.java     ← Branch portal layout with tabs
        LoginController.java            ← Staff login screen
        LoyaltyController.java          ← Loyalty programme management
        OrderHistoryController.java     ← Branch order history with receipts
        OrderManagementController.java  ← Admin order management with filters
        OrderToHQController.java        ← Branch stock ordering from HQ
        OrderTrackingController.java    ← Order timeline tracking
        OTPVerifyController.java        ← Two-factor authentication
        PlaceOrderController.java       ← Branch POS for customer orders
        PortalSelectorController.java   ← Portal selection (Admin/Branch)
        RegisterController.java         ← Multi-step registration
        ReportsController.java          ← 5 report types with preview/export
        StockManagementController.java  ← Stock inventory and returns
        StockReturnsController.java     ← Branch damaged stock returns
        TrackStockOrdersController.java ← Branch stock order tracking
        UserManagementController.java   ← Staff accounts and sessions
      models/
        Customer.java                   ← Loyalty customer model
        Drink.java                      ← Drink/stock item model
        Order.java                      ← Order model
        PendingApproval.java            ← Pending staff approval model
        Return.java                     ← Stock return model
        Session.java                    ← Active session model
        Staff.java                      ← Staff member model
        StockItem.java                  ← Stock inventory item model
        TimelineStep.java               ← Order tracking timeline step
        TrackedOrder.java               ← Order with timeline
      services/
        MockDataService.java            ← All mock data (replaces API calls)
        NavigationManager.java          ← View navigation singleton
      utils/
        Formatters.java                 ← KES formatting utilities
    resources/com/drinks/
      styles/
        main.css                        ← Complete JavaFX CSS (from Tailwind)
pom.xml                                 ← Maven build configuration
```

## Conversion Mapping

| React Concept | JavaFX Equivalent |
|---|---|
| `div` / flex container | `HBox`, `VBox`, `BorderPane` |
| `grid` layout | `GridPane`, `FlowPane` |
| Card component | `VBox` with CSS border-radius + drop-shadow |
| Button | `Button` with CSS class |
| Input / text field | `TextField`, `PasswordField`, `ComboBox` |
| Table | `TableView<Model>` with custom cell factories |
| Modal / dialog | `Dialog<Void>` with custom content |
| Tab panel | Custom tab buttons with content swapping |
| Chart (bar/pie) | JavaFX `BarChart`, `PieChart` |
| React Router pages | `NavigationManager` with view swapping in `StackPane` |
| `useState` | JavaFX `SimpleXxxProperty` bindings |
| `useEffect` / polling | JavaFX `Timeline` with `KeyFrame` |
| fetch/axios API calls | `MockDataService` (ready for `HttpClient` replacement) |
| Tailwind CSS | JavaFX CSS in `main.css` |
| StatusBadge component | `StatusBadge.java` extending `Label` |
| StatCard component | `StatCard.java` extending `VBox` |
| OTPInput component | `OTPInput.java` extending `HBox` |

## Colors (Converted from Tailwind HSL)

| CSS Variable | Hex | Usage |
|---|---|---|
| `--background` | `#F7F8FA` | Page background |
| `--foreground` | `#0F172A` | Primary text |
| `--primary` | `#2563EB` | Primary buttons, links |
| `--primary-foreground` | `#FFFFFF` | Text on primary |
| `--muted` | `#F1F5F9` | Muted backgrounds |
| `--muted-foreground` | `#6B7280` | Muted text |
| `--destructive` | `#E11D48` | Error/destructive |
| `--border` | `#E5E7EB` | Borders |
| `--navy` | `#0F172A` | Header/sidebar bg |
| `--ds-blue` | `#2563EB` | Brand blue |
| `--ds-green` | `#059669` | Success |
| `--ds-amber` | `#F59E0B` | Warning |
| `--ds-red` | `#E11D48` | Error |
| `--ds-purple` | `#7C3AED` | Purple accent |

## Features Preserved

- **Portal Selection** → Admin vs Branch portal choice
- **Authentication Flow** → Login → OTP → Portal entry
- **Registration** → 3-step form with password rules and OTP verification
- **Admin Dashboard** → Revenue stats, profit/loss table, dispatched orders, stock alerts
- **Admin POS** → Customer details, drink grid, cart with loyalty points, supervisor PIN for deletions
- **Order Management** → Searchable/filterable order table with status badges
- **Order Tracking** → Timeline cards with 5-step order progress
- **Stock Management** → Inventory table, damaged returns, restock dialog
- **Reports** → 5 report types (Sales, P&L, Orders, Stock, Loyalty) with config and preview
- **User Management** → Staff accounts, pending approvals, active sessions
- **Branch POS** → Same as admin POS but submits orders to HQ
- **Order to HQ** → Case-based stock ordering with supervisor PIN
- **Order History** → Branch orders with receipt modal
- **Track Stock** → Stock order timeline from HQ perspective
- **Loyalty** → Customer search, loyalty card display, purchase history, new registration, points rules
- **Stock Returns** → Return form with photo upload, return requests table

## Running the App

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run

```bash
# Clean build and run
mvn clean javafx:run

# Or build first, then run
mvn clean compile
mvn javafx:run
```

### IDE Setup
- **IntelliJ IDEA**: Open as Maven project, run `App.java`
- **Eclipse**: Import as Maven project, run `App.java` as Java Application
- **VS Code**: Use Java Extension Pack, run `App.java`

### Build JAR

```bash
mvn clean package
```

## Architecture Notes

- **No FXML files**: All UI is built programmatically for better type safety and dynamic layout control
- **Mock Data**: All data is served from `MockDataService` — replace with `HttpClient` calls for production
- **Navigation**: `NavigationManager` singleton handles view switching (replaces React Router)
- **State Management**: JavaFX properties provide reactive bindings (replaces useState)
- **CSS Styling**: All styles in `main.css` — no inline styles except for dynamic values

## Next Steps for Production

1. Replace `MockDataService` with `HttpClient` calls to your backend API
2. Add authentication service with real login/OTP verification
3. Add `Timeline` polling for real-time order updates (every 5 seconds)
4. Add print functionality for receipts
5. Add database persistence for orders and stock
6. Add export functionality for reports (PDF/CSV using Apache PDFBox/OpenCSV)
