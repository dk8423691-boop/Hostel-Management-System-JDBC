package hostel_db;

import java.sql.*;
import java.util.Scanner;

public class hostelmanagementsystem {

    static Scanner sc = new Scanner(System.in);

    // Remaining code...

    // Database connection details
    static final String URL =
            "jdbc:mysql://localhost:3306/hostel_db";

    static final String USER = "root";
    static final String PASSWORD = "pass";

    // Read integer safely
    static int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // Read double safely
    static double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid fee.");
            }
        }
    }

    // Read text
    static String readText(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    // (a) Insert three student records
    static void insertStudents(Connection con) {

        System.out.println("\n--- INSERT THREE STUDENTS ---");

        String sql = "INSERT INTO HostelStudent "
                + "(student_id, student_name, roll_number, "
                + "hostel_name, room_number, fee) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 3; i++) {

                System.out.println("\nStudent " + i);

                int id = readInt("Student ID: ");
                String name = readText("Student Name: ");
                String roll = readText("Roll Number: ");
                String hostel = readText("Hostel Name: ");
                String room = readText("Room Number: ");
                double fee = readDouble("Hostel Fee: ");

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, roll);
                ps.setString(4, hostel);
                ps.setString(5, room);
                ps.setDouble(6, fee);

                try {
                    int result = ps.executeUpdate();

                    if (result > 0) {
                        System.out.println("Student inserted successfully!");
                    }

                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println(
                            "Duplicate Student ID! Record not inserted.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Insertion Error: " + e.getMessage());
        }
    }

    // (b) Retrieve students by hostel and fee
    static void retrieveStudents(Connection con) {

        System.out.println("\n--- SEARCH STUDENTS BY HOSTEL ---");

        String hostel = readText("Enter Hostel Name: ");

        String sql = "SELECT * FROM HostelStudent "
                + "WHERE hostel_name = ? AND fee > 50000 "
                + "ORDER BY fee DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hostel);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println("\nStudents with fee above 50000:");

                while (rs.next()) {

                    found = true;

                    System.out.println("----------------------------");
                    System.out.println("Student ID: "
                            + rs.getInt("student_id"));

                    System.out.println("Name: "
                            + rs.getString("student_name"));

                    System.out.println("Roll Number: "
                            + rs.getString("roll_number"));

                    System.out.println("Hostel: "
                            + rs.getString("hostel_name"));

                    System.out.println("Room: "
                            + rs.getString("room_number"));

                    System.out.println("Fee: "
                            + rs.getDouble("fee"));
                }

                if (!found) {
                    System.out.println("No matching students found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Retrieval Error: " + e.getMessage());
        }
    }

    // (c) Update hostel fee
    static void updateFee(Connection con) {

        System.out.println("\n--- UPDATE HOSTEL FEE ---");

        int id = readInt("Enter Student ID: ");
        double newFee = readDouble("Enter New Hostel Fee: ");

        String sql = "UPDATE HostelStudent SET fee = ? "
                + "WHERE student_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newFee);
            ps.setInt(2, id);

            int result = ps.executeUpdate();

            if (result > 0) {
                System.out.println("Hostel fee updated successfully!");
            } else {
                System.out.println("Student record does not exist.");
            }

        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    // (d) Search student and update room number
    static void updateRoom(Connection con) {

        System.out.println("\n--- UPDATE ROOM NUMBER ---");

        int id = readInt("Enter Student ID: ");

        String searchSql = "SELECT * FROM HostelStudent "
                + "WHERE student_id = ?";

        try (PreparedStatement search =
                con.prepareStatement(searchSql)) {

            search.setInt(1, id);

            try (ResultSet rs = search.executeQuery()) {

                if (rs.next()) {

                    System.out.println("\nStudent Found!");
                    System.out.println("Name: "
                            + rs.getString("student_name"));

                    System.out.println("Old Room Number: "
                            + rs.getString("room_number"));

                    String newRoom =
                            readText("Enter New Room Number: ");

                    String updateSql =
                            "UPDATE HostelStudent SET room_number = ? "
                            + "WHERE student_id = ?";

                    try (PreparedStatement update =
                            con.prepareStatement(updateSql)) {

                        update.setString(1, newRoom);
                        update.setInt(2, id);

                        update.executeUpdate();
                    }

                    // Display updated student details
                    try (PreparedStatement display =
                            con.prepareStatement(searchSql)) {

                        display.setInt(1, id);

                        try (ResultSet updated =
                                display.executeQuery()) {

                            if (updated.next()) {
                                System.out.println(
                                        "\nUpdated Student Details");

                                System.out.println("ID: "
                                        + updated.getInt("student_id"));

                                System.out.println("Name: "
                                        + updated.getString("student_name"));

                                System.out.println("Roll Number: "
                                        + updated.getString("roll_number"));

                                System.out.println("Hostel: "
                                        + updated.getString("hostel_name"));

                                System.out.println("New Room: "
                                        + updated.getString("room_number"));

                                System.out.println("Fee: "
                                        + updated.getDouble("fee"));
                            }
                        }
                    }

                } else {
                    System.out.println("Student record does not exist.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Room Update Error: " + e.getMessage());
        }
    }

    // (e) Calculate total fees and conditionally delete
    static void calculateAndDelete(Connection con) {

        System.out.println("\n--- TOTAL FEE COLLECTION ---");

        String sumSql = "SELECT SUM(fee) AS total_fee "
                + "FROM HostelStudent";

        try (PreparedStatement ps = con.prepareStatement(sumSql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                double total = rs.getDouble("total_fee");

                if (rs.wasNull()) {
                    total = 0.0;
                }

                System.out.println("Total Hostel Fee Collected: Rs. "
                        + total);
            }

        } catch (SQLException e) {
            System.out.println("Sum Error: " + e.getMessage());
            return;
        }

        int id = readInt("\nEnter Student ID to delete: ");

        String searchSql =
                "SELECT fee FROM HostelStudent WHERE student_id = ?";

        try (PreparedStatement search =
                con.prepareStatement(searchSql)) {

            search.setInt(1, id);

            try (ResultSet rs = search.executeQuery()) {

                if (rs.next()) {

                    double fee = rs.getDouble("fee");

                    if (fee > 0) {

                        String deleteSql =
                                "DELETE FROM HostelStudent "
                                + "WHERE student_id = ? AND fee > 0";

                        try (PreparedStatement delete =
                                con.prepareStatement(deleteSql)) {

                            delete.setInt(1, id);

                            int result = delete.executeUpdate();

                            if (result > 0) {
                                System.out.println(
                                        "Student record deleted successfully!");
                            } else {
                                System.out.println(
                                        "Record could not be deleted.");
                            }
                        }

                    } else {
                        System.out.println(
                                "Cannot delete! Hostel fee is not fully paid.");
                    }

                } else {
                    System.out.println("Student record does not exist.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Deletion Error: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD)) {

                System.out.println("================================");
                System.out.println(" HOSTEL MANAGEMENT SYSTEM");
                System.out.println("================================");
                System.out.println("Database Connected Successfully!");

                int choice;

                do {
                    System.out.println("\n========== MAIN MENU ==========");
                    System.out.println("1. Insert Three Student Records");
                    System.out.println("2. Retrieve Students by Hostel");
                    System.out.println("3. Update Hostel Fee");
                    System.out.println("4. Search and Update Room");
                    System.out.println("5. Total Fee and Delete Student");
                    System.out.println("0. Exit");
                    System.out.println("===============================");

                    choice = readInt("Enter Your Choice: ");

                    switch (choice) {

                        case 1:
                            insertStudents(con);
                            break;

                        case 2:
                            retrieveStudents(con);
                            break;

                        case 3:
                            updateFee(con);
                            break;

                        case 4:
                            updateRoom(con);
                            break;

                        case 5:
                            calculateAndDelete(con);
                            break;

                        case 0:
                            System.out.println("Exiting Application...");
                            break;

                        default:
                            System.out.println(
                                    "Invalid Choice! Please try again.");
                    }

                } while (choice != 0);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            System.out.println(e.getMessage());

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            System.out.println(e.getMessage());
        }

        sc.close();
    }
}
