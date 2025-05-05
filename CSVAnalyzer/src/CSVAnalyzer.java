import java.io.*;
import java.util.*;

public class CSVAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("📊 CSV Data Analyzer");

        // Load data
        System.out.print("Enter CSV file path: ");
        String filePath = scanner.nextLine();
        List<String[]> data = loadCSV(filePath);

        if (!data.isEmpty()) {
            while (true) {
                // Enhanced main menu
                System.out.println("\nOptions:");
                System.out.println("1. Show Full Data");
                System.out.println("2. Show Statistics");
                System.out.println("3. Filter by Column");
                System.out.println("4. Search by Value");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        showFullData(data);
                        break;
                    case 2:
                        showStats(data);  // RESTORED STATS FUNCTIONALITY
                        break;
                    case 3:
                        System.out.print("Enter column number (0-indexed): ");
                        int col = scanner.nextInt();
                        scanner.nextLine();
                        filterData(data, col);
                        break;
                    case 4:
                        System.out.print("Search for: ");
                        String term = scanner.nextLine();
                        searchByValue(data, term);
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option!");
                }
            }
        }
    }

    // File loading (unchanged)
    private static List<String[]> loadCSV(String path) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
            System.out.println("✅ Loaded " + data.size() + " rows");
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
        return data;
    }

    // RESTORED STATS METHOD (with improvements)
    private static void showStats(List<String[]> data) {
        System.out.println("\n--- Statistics ---");
        System.out.println("Columns: " + data.get(0).length);
        System.out.println("Data Rows: " + (data.size() - 1));

        System.out.println("\nColumn Headers:");
        System.out.println(Arrays.toString(data.get(0)));

        // New: Numeric column detection
        System.out.println("\n[Data Types]");
        for (int col = 0; col < data.get(0).length; col++) {
            try {
                Double.parseDouble(data.get(1)[col]); // Test first data row
                System.out.println("Column " + col + ": Numeric");
            } catch (NumberFormatException e) {
                System.out.println("Column " + col + ": Text");
            }
        }
    }

    // Other methods remain unchanged
    private static void showFullData(List<String[]> data) {
        System.out.println("\n--- Full Data ---");
        for (String[] row : data) {
            System.out.println(Arrays.toString(row));
        }
    }

    private static void filterData(List<String[]> data, int col) {
        System.out.println("\n--- Filtered Column " + col + " ---");
        for (String[] row : data) {
            if (col < row.length) {
                System.out.println(row[col]);
            }
        }
    }

    private static void searchByValue(List<String[]> data, String searchTerm) {
        System.out.println("\n--- Rows containing '" + searchTerm + "' ---");
        for (String[] row : data) {
            for (String cell : row) {
                if (cell.contains(searchTerm)) {
                    System.out.println(Arrays.toString(row));
                    break;
                }
            }
        }
    }
}