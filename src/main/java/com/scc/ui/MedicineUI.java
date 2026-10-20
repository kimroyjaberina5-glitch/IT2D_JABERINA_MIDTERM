
package com.scc.ui;

import com.scc.dao.ScenarioDao;
import com.scc.model.Medicine;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MedicineUI {

    private final Scanner scanner = new Scanner(System.in);
    private final ScenarioDao dao = new ScenarioDao();

    public void start() {
        try {
            dao.initTable();
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\n================================");
            System.out.println(" PHARMACY MEDICINE INVENTORY");
            System.out.println("================================");
            System.out.println("[1] Add Medicine");
            System.out.println("[2] View All Medicines");
            System.out.println("[3] Search Medicine");
            System.out.println("[4] Update Medicine");
            System.out.println("[5] Delete Medicine");
            System.out.println("[0] Exit");
            System.out.println("================================");

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addMedicine();
                    case 2 -> viewMedicines();
                    case 3 -> searchMedicine();
                    case 4 -> updateMedicine();
                    case 5 -> deleteMedicine();
                    case 0 -> {
                        running = false;
                        System.out.println("Thank you for using the system!");
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private void addMedicine() throws SQLException {
        System.out.println("\n--- ADD MEDICINE ---");

        String brandName = readText("Enter brand name: ");
        String category = readText("Enter category: ");
        double price = readDouble("Enter price: ");
        int stock = readInt("Enter stock quantity: ");

        if (price < 0 || stock < 0) {
            System.out.println("Price and stock cannot be negative.");
            return;
        }

        Medicine medicine =
                new Medicine(brandName, category, price, stock);

        dao.addMedicine(medicine);

        System.out.println("Medicine added successfully!");
    }

    private void viewMedicines() throws SQLException {
        System.out.println("\n--- ALL MEDICINES ---");

        List<Medicine> medicines = dao.getAllRecords();
        displayMedicines(medicines);
    }

    private void searchMedicine() throws SQLException {
        System.out.println("\n--- SEARCH MEDICINE ---");

        String keyword = readText(
                "Enter brand name or category to search: ");

        List<Medicine> medicines = dao.searchMedicine(keyword);
        displayMedicines(medicines);
    }

    private void updateMedicine() throws SQLException {
        System.out.println("\n--- UPDATE MEDICINE ---");

        int id = readInt("Enter medicine ID to update: ");

        String brandName = readText("Enter new brand name: ");
        String category = readText("Enter new category: ");
        double price = readDouble("Enter new price: ");
        int stock = readInt("Enter new stock quantity: ");

        if (price < 0 || stock < 0) {
            System.out.println("Price and stock cannot be negative.");
            return;
        }

        Medicine medicine =
                new Medicine(id, brandName, category, price, stock);

        if (dao.updateMedicine(medicine)) {
            System.out.println("Medicine updated successfully!");
        } else {
            System.out.println("Medicine ID not found.");
        }
    }

    private void deleteMedicine() throws SQLException {
        System.out.println("\n--- DELETE MEDICINE ---");

        int id = readInt("Enter medicine ID to delete: ");

        String confirmation = readText(
                "Are you sure you want to delete it? (yes/no): ");

        if (confirmation.equalsIgnoreCase("yes")) {
            if (dao.deleteMedicine(id)) {
                System.out.println("Medicine deleted successfully!");
            } else {
                System.out.println("Medicine ID not found.");
            }
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private void displayMedicines(List<Medicine> medicines) {
        if (medicines.isEmpty()) {
            System.out.println("No medicines found.");
            return;
        }

        System.out.printf(
                "%-5s %-25s %-18s %-12s %-8s%n",
                "ID", "BRAND NAME", "CATEGORY", "PRICE", "STOCK"
        );

        System.out.println(
                "-----------------------------------------------------------------------"
        );

        for (Medicine medicine : medicines) {
            System.out.printf(
                    "%-5d %-25s %-18s %-12.2f %-8d%n",
                    medicine.id(),
                    medicine.brandName(),
                    medicine.category(),
                    medicine.price(),
                    medicine.stock()
            );
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                double value =
                        Double.parseDouble(scanner.nextLine().trim());

                if (Double.isFinite(value)) {
                    return value;
                }

                System.out.println("Please enter a finite number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price.");
            }
        }
    }
}
