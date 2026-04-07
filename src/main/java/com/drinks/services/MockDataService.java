package com.drinks.services;

import com.drinks.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MockDataService {
    private static MockDataService instance;

    public static MockDataService getInstance() {
        if (instance == null) instance = new MockDataService();
        return instance;
    }

    public ObservableList<Drink> getDrinks() {
        return FXCollections.observableArrayList(
            new Drink("Tusker Lager", "EABL", 250, 48, "/tusker.png", 5000, 24),
            new Drink("Guinness", "Diageo", 300, 8, "/guinness.png", 7200, 24),
            new Drink("Coca Cola", "Coke", 100, 120, "/coca-cola.png", 2400, 24),
            new Drink("Fanta Orange", "Coke", 100, 0, "/fanta.png", 2400, 24),
            new Drink("White Cap", "EABL", 280, 12, "/white-cap.png", 6720, 24),
            new Drink("Stoney Ginger", "Coke", 100, 45, "/stoney.png", 2400, 24),
            new Drink("Johnnie Walker", "Diageo", 450, 22, "/johnnie-walker.png", 10800, 24),
            new Drink("Smirnoff Vodka", "Diageo", 350, 15, "/smirnoff.png", 8400, 24),
            new Drink("Bell's Whisky", "Diageo", 380, 5, "/bells.png", 9120, 24),
            new Drink("Tusker Cider", "EABL", 220, 30, "/tusker-cider.png", 5280, 24)
        );
    }

    public ObservableList<Order> getAdminOrders() {
        return FXCollections.observableArrayList(
            new Order("ORD-8829", "Harrison Mbugua", "Nakuru", "Tusker x12, Guinness x4", "4,800", "Received", "24 Oct 2023, 08:15"),
            new Order("ORD-8830", "Grace Wanjiku", "Mombasa", "Coke x24, Water x12", "3,200", "In Transit", "24 Oct 2023, 09:30"),
            new Order("ORD-8831", "David Ochieng", "Kisumu", "White Cap x6", "1,680", "Processing", "24 Oct 2023, 10:45"),
            new Order("ORD-8832", "Amina Hassan", "Mombasa", "Fanta x12, Stoney x6", "1,800", "Pending", "24 Oct 2023, 11:00"),
            new Order("ORD-8833", "Peter Kamau", "Nakuru", "Tusker x24", "6,000", "Dispatched", "24 Oct 2023, 11:30"),
            new Order("ORD-8834", "Jane Nyambura", "Kisumu", "Guinness x6, Coke x12", "3,000", "Confirmed", "24 Oct 2023, 12:00"),
            new Order("ORD-8835", "Samuel Otieno", "HQ", "Water x48", "2,400", "Cancelled", "24 Oct 2023, 12:15")
        );
    }

    public ObservableList<Order> getBranchOrders() {
        return FXCollections.observableArrayList(
            new Order("ORD-9001", "Harrison Mbugua", "Nakuru", "Tusker x12, Guinness x4", "4,800", "Received", "24 Oct, 08:15", "Sarah J.", 480),
            new Order("ORD-9002", "Grace Wanjiku", "Nakuru", "Coke x24, Water x12", "3,200", "Confirmed", "24 Oct, 09:30", "Sarah J.", 320),
            new Order("ORD-9003", "David Ochieng", "Nakuru", "White Cap x6", "1,680", "Pending", "24 Oct, 10:45", "James M.", 168),
            new Order("ORD-9004", "Amina Hassan", "Nakuru", "Fanta x12, Stoney x6", "1,800", "Processing", "24 Oct, 11:00", "Sarah J.", 180)
        );
    }

    public ObservableList<StockItem> getStockItems() {
        return FXCollections.observableArrayList(
            new StockItem("Tusker Lager", "EABL", "Nakuru", 48, 20, "OK", "22 Oct"),
            new StockItem("Guinness 500ml", "Diageo", "Nakuru", 0, 10, "Out of Stock", "18 Oct"),
            new StockItem("Coca Cola", "Coke", "Mombasa", 120, 30, "OK", "23 Oct"),
            new StockItem("White Cap", "EABL", "Mombasa", 8, 15, "Low", "20 Oct"),
            new StockItem("Tusker Lager", "EABL", "Kisumu", 3, 20, "Critical", "15 Oct"),
            new StockItem("Fanta Orange", "Coke", "Kisumu", 45, 20, "OK", "21 Oct")
        );
    }

    public ObservableList<Return> getAdminReturns() {
        return FXCollections.observableArrayList(
            new Return("RTN-001", "Nakuru", "Guinness 500ml", 4, "Damaged in transit", "Pending Review", "23 Oct"),
            new Return("RTN-002", "Mombasa", "Tusker Lager", 2, "Expired stock", "Approved", "22 Oct"),
            new Return("RTN-003", "Kisumu", "Coke 500ml", 6, "Wrong delivery", "Replacement Sent", "20 Oct")
        );
    }

    public ObservableList<Return> getBranchReturns() {
        return FXCollections.observableArrayList(
            new Return("RTN-101", "Nakuru", "Guinness 500ml", 4, "Damaged in transit — 4 bottles cracked", "Pending Review", "23 Oct"),
            new Return("RTN-102", "Nakuru", "Tusker Lager", 2, "Expired stock returned", "Approved", "22 Oct", "Replacement dispatched"),
            new Return("RTN-103", "Nakuru", "Coke 500ml", 6, "Wrong delivery — ordered Fanta", "Replacement Sent", "20 Oct", "STK-5510 in transit"),
            new Return("RTN-104", "Nakuru", "White Cap", 3, "Customer returned — quality issue", "Rejected", "18 Oct", "Does not meet return policy")
        );
    }

    public ObservableList<Staff> getStaff() {
        return FXCollections.observableArrayList(
            new Staff("Sarah Juma", "DS-4421", "Nakuru", "Branch Manager", "Active", "24 Oct, 08:00"),
            new Staff("James Mwangi", "DS-4422", "Mombasa", "Cashier", "Active", "24 Oct, 07:45"),
            new Staff("Peter Ochieng", "DS-4423", "Kisumu", "Cashier", "Suspended", "20 Oct, 14:00"),
            new Staff("Grace Wanjiku", "DS-4424", "Mombasa", "Cashier", "Active", "24 Oct, 09:00"),
            new Staff("David Kamau", "DS-4425", "HQ", "Administrator", "Active", "24 Oct, 08:30"),
            new Staff("Amina Hassan", "DS-4426", "Nakuru", "Cashier", "Inactive", "15 Oct, 12:00")
        );
    }

    public ObservableList<PendingApproval> getPendingApprovals() {
        return FXCollections.observableArrayList(
            new PendingApproval("John Otieno", "Kisumu", "Cashier", "+254 712 345 678", "john@email.com", "23 Oct 2023"),
            new PendingApproval("Mary Njeri", "Nakuru", "Branch Manager", "+254 723 456 789", "mary@email.com", "22 Oct 2023")
        );
    }

    public ObservableList<Session> getActiveSessions() {
        return FXCollections.observableArrayList(
            new Session("Sarah Juma", "Nakuru", "08:00", "2 min ago"),
            new Session("James Mwangi", "Mombasa", "07:45", "5 min ago"),
            new Session("David Kamau", "HQ", "08:30", "Just now")
        );
    }

    public ObservableList<Customer> getCustomers() {
        return FXCollections.observableArrayList(
            new Customer("Harrison Mbugua", "+254 712 345 678", "LC-8829-2210", "Platinum", 12450, 85, "Diamond", 15000),
            new Customer("Grace Wanjiku", "+254 723 456 789", "LC-7712-1908", "Gold", 8200, 68, "Platinum", 12000),
            new Customer("David Ochieng", "+254 734 567 890", "LC-5540-1504", "Silver", 3100, 52, "Gold", 6000)
        );
    }

    public ObservableList<TrackedOrder> getAdminTrackedOrders() {
        String[] steps = {"Order Created", "Dispatched from HQ", "In Transit", "Confirmed by Branch", "Received & Stocked"};

        return FXCollections.observableArrayList(
            new TrackedOrder("ORD-8829", "Nakuru", "Tusker x12, Guinness x4", "Received",
                FXCollections.observableArrayList(
                    new TimelineStep(steps[0], "08:00", null, "done"),
                    new TimelineStep(steps[1], "08:15", null, "done"),
                    new TimelineStep(steps[2], "09:30", null, "done"),
                    new TimelineStep(steps[3], "11:00", null, "done"),
                    new TimelineStep(steps[4], "11:45", null, "done")
                )),
            new TrackedOrder("ORD-8830", "Mombasa", "Coke x24, Water x12", "In Transit",
                FXCollections.observableArrayList(
                    new TimelineStep(steps[0], "09:00", null, "done"),
                    new TimelineStep(steps[1], "09:30", null, "done"),
                    new TimelineStep(steps[2], "10:15", "Driver: James K. — En route to Mombasa", "active"),
                    new TimelineStep(steps[3], null, null, "pending"),
                    new TimelineStep(steps[4], null, null, "pending")
                )),
            new TrackedOrder("ORD-8831", "Kisumu", "White Cap x6", "Pending",
                FXCollections.observableArrayList(
                    new TimelineStep(steps[0], "10:45", null, "done"),
                    new TimelineStep(steps[1], null, null, "pending"),
                    new TimelineStep(steps[2], null, null, "pending"),
                    new TimelineStep(steps[3], null, null, "pending"),
                    new TimelineStep(steps[4], null, null, "pending")
                ))
        );
    }

    public ObservableList<TrackedOrder> getBranchTrackedOrders() {
        String[] steps = {"Order Created", "Dispatched from HQ", "In Transit", "Confirmed by Branch", "Received & Stocked"};

        return FXCollections.observableArrayList(
            new TrackedOrder("STK-5501", "", "Tusker x24, Guinness x12", "Received",
                FXCollections.observableArrayList(
                    new TimelineStep(steps[0], "08:00", null, "done"),
                    new TimelineStep(steps[1], "08:20", null, "done"),
                    new TimelineStep(steps[2], "10:00", null, "done"),
                    new TimelineStep(steps[3], "12:00", null, "done"),
                    new TimelineStep(steps[4], "12:15", null, "done")
                )),
            new TrackedOrder("STK-5502", "", "Coke x48, Fanta x24", "In Transit",
                FXCollections.observableArrayList(
                    new TimelineStep(steps[0], "09:00", null, "done"),
                    new TimelineStep(steps[1], "09:30", null, "done"),
                    new TimelineStep(steps[2], "10:45", "Driver: Peter K. — ETA 2hrs", "active"),
                    new TimelineStep(steps[3], null, null, "pending"),
                    new TimelineStep(steps[4], null, null, "pending")
                )),
            new TrackedOrder("STK-5503", "", "White Cap x12, Stoney x24", "Dispatched",
                FXCollections.observableArrayList(
                    new TimelineStep(steps[0], "11:00", null, "done"),
                    new TimelineStep(steps[1], "11:30", null, "done"),
                    new TimelineStep(steps[2], null, null, "pending"),
                    new TimelineStep(steps[3], null, null, "pending"),
                    new TimelineStep(steps[4], null, null, "pending")
                ))
        );
    }

    public ObservableList<String[]> getReportData(String reportType) {
        return switch (reportType) {
            case "Sales Report" -> FXCollections.observableArrayList(
                new String[]{"24 Oct", "Nakuru", "156", "KES 45,000", "KES 288"},
                new String[]{"24 Oct", "Mombasa", "220", "KES 75,000", "KES 341"},
                new String[]{"24 Oct", "Kisumu", "98", "KES 30,000", "KES 306"},
                new String[]{"24 Oct", "HQ", "310", "KES 90,000", "KES 290"}
            );
            case "Profit & Loss" -> FXCollections.observableArrayList(
                new String[]{"Nakuru", "KES 45,000", "KES 33,000", "+KES 12,000", "26.7%"},
                new String[]{"Mombasa", "KES 75,000", "KES 53,000", "+KES 22,000", "29.3%"},
                new String[]{"Kisumu", "KES 30,000", "KES 28,000", "+KES 2,000", "6.7%"},
                new String[]{"HQ", "KES 90,000", "KES 95,000", "-KES 5,000", "-5.6%"}
            );
            case "Order Report" -> FXCollections.observableArrayList(
                new String[]{"ORD-8829", "Nakuru", "Harrison M.", "KES 4,800", "Received"},
                new String[]{"ORD-8830", "Mombasa", "Grace W.", "KES 3,200", "In Transit"},
                new String[]{"ORD-8831", "Kisumu", "David O.", "KES 1,680", "Pending"}
            );
            case "Stock Report" -> FXCollections.observableArrayList(
                new String[]{"Tusker Lager", "Nakuru", "48", "20", "OK"},
                new String[]{"Guinness", "Nakuru", "0", "10", "Out of Stock"},
                new String[]{"White Cap", "Mombasa", "8", "15", "Low"}
            );
            case "Loyalty Report" -> FXCollections.observableArrayList(
                new String[]{"Harrison M.", "Platinum", "12,450", "42", "24 Oct"},
                new String[]{"Grace W.", "Gold", "8,200", "28", "23 Oct"},
                new String[]{"David O.", "Silver", "3,100", "15", "20 Oct"}
            );
            default -> FXCollections.observableArrayList();
        };
    }

    public String[] getReportColumns(String reportType) {
        return switch (reportType) {
            case "Sales Report" -> new String[]{"Date", "Branch", "Items Sold", "Revenue", "Avg Order"};
            case "Profit & Loss" -> new String[]{"Branch", "Revenue", "Cost", "Profit/Loss", "Margin %"};
            case "Order Report" -> new String[]{"Order ID", "Branch", "Customer", "Total", "Status"};
            case "Stock Report" -> new String[]{"Item", "Branch", "Stock", "Min", "Status"};
            case "Loyalty Report" -> new String[]{"Customer", "Tier", "Points", "Purchases", "Last Visit"};
            default -> new String[]{};
        };
    }
}
