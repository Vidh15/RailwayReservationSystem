package com.vidhya;
import java.sql.*;
import java.util.Scanner;


public class RailwayReservation {
	
	private static int loggedInUserId = -1; // Store user_id after login

	    private static final String URL = "jdbc:mysql://localhost:3306/railway_db";
	    private static final String USER = "root";
	    private static final String PASSWORD = "vidhya123";
	    private static Scanner scanner = new Scanner(System.in);

	    public static void main(String[] args) {
	        while (true) {
	            System.out.println("\n===== Railway Reservation System =====");
	            System.out.println("1. Register");
	            System.out.println("2. Login");
	            System.out.println("3. Exit");
	            System.out.print("Choose an option: ");
	            int choice = scanner.nextInt();

	            switch (choice) {
	                case 1:
	                    registerUser();
	                    break;
	                case 2:
	                    loginUser();
	                    break;
	                case 3:
	                    System.out.println("Exiting...");
	                    System.exit(0);
	                default:
	                    System.out.println("Invalid choice. Try again.");
	            }
	        }
	    }

	    private static void registerUser() {
	        System.out.print("Enter Username: ");
	        String username = scanner.next();
	        System.out.print("Enter Password: ");
	        String password = scanner.next();
	        System.out.print("Enter Email: ");
	        String email = scanner.next();

	        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";


	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	             PreparedStatement pstmt = conn.prepareStatement(query)) {

	            pstmt.setString(1, username);
	            pstmt.setString(2, password);
	            pstmt.setString(3, email);

	            int rows = pstmt.executeUpdate();
	            if (rows > 0) {
	                System.out.println("Registration successful!");
	            } else {
	                System.out.println("Registration failed.");
	            }
	        } catch (SQLException e) {
	            if (e.getErrorCode() == 1062) { // Duplicate entry error
	                System.out.println("Error: Username or email already exists.");
	            } else {
	                e.printStackTrace();
	            }
	        }
	    }


	    private static void loginUser() {
	        System.out.print("Enter Username: ");
	        String username = scanner.next();
	        System.out.print("Enter Password: ");
	        String password = scanner.next();

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	            String query = "SELECT user_id FROM users WHERE username = ? AND password = ?";
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setString(1, username);
	            pstmt.setString(2, password);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                loggedInUserId = rs.getInt("user_id");  // Store logged-in user's ID
	                System.out.println("Login successful! Welcome, " + username);
	                userMenu();  // Go to user menu
	            } else {
	                System.out.println("Invalid username or password.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }



	    private static void userMenu() {
	        while (true) {
	            System.out.println("\n===== User Menu =====");
	            System.out.println("1. View Trains");
	            System.out.println("2. Book Ticket");
	            System.out.println("3. Cancel Ticket");
	            System.out.println("4. View Ticket");
	            System.out.println("5. Update Ticket");
	            System.out.println("6. Check Seat Availability");
	            System.out.println("7. Check Ticket Price");
	            System.out.println("8. Search Ticket");
	            System.out.println("9. Delete Ticket");
	            System.out.println("10. Logout");
	            System.out.print("Choose an option: ");
	            int choice = scanner.nextInt();

	            switch (choice) {
	                case 1:
	                    viewTrains();
	                    break;
	                case 2:
	                    bookTicket();
	                    break;
	                case 3:
	                    cancelTicket();
	                    break;
	                case 4:
	                    viewTicket();
	                    break;
	                case 5:
	                    updateTicket();
	                    break;
	                case 6:
	                    checkSeatAvailability();
	                    break;
	                case 7:
	                    checkTicketPrice();
	                    break;
	                case 8:
	                    searchTicket();
	                    break;
	                case 9:
	                    deleteTicket();
	                    break;
	                case 10:
	                    System.out.println("Logging out...");
	                    return;
	                default:
	                    System.out.println("Invalid choice. Try again.");
	            }
	        }
	    }

	    private static void viewTicket() {
	        System.out.print("Enter Ticket ID to view: ");
	        int ticketId = scanner.nextInt();

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	            String query = "SELECT * FROM tickets WHERE ticket_id = ?";
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setInt(1, ticketId);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                System.out.println("Ticket ID: " + rs.getInt("ticket_id") + ", Train ID: " + rs.getInt("train_id") + ", Seat: " + rs.getString("seat_number"));
	            } else {
	                System.out.println("No ticket found with given ID.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void updateTicket() {
	        System.out.print("Enter Ticket ID to update: ");
	        int ticketId = scanner.nextInt();
	        System.out.print("Enter new seat number: ");
	        String newSeat = scanner.next();

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	            String query = "UPDATE tickets SET seat_number = ? WHERE ticket_id = ?";
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setString(1, newSeat);
	            pstmt.setInt(2, ticketId);
	            int rows = pstmt.executeUpdate();
	            if (rows > 0) {
	                System.out.println("Ticket updated successfully!");
	            } else {
	                System.out.println("Ticket not found.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    private static void checkSeatAvailability() {
	        System.out.print("Enter Train ID to check seat availability: ");
	        int trainId = scanner.nextInt();

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	            String query = "SELECT seat_number FROM seats WHERE train_id = ? AND is_booked = FALSE";
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setInt(1, trainId);
	            ResultSet rs = pstmt.executeQuery();

	            System.out.println("Available Seats for Train ID " + trainId + ":");
	            boolean hasSeats = false;
	            while (rs.next()) {
	                System.out.println(rs.getString("seat_number"));
	                hasSeats = true;
	            }

	            if (!hasSeats) {
	                System.out.println("No available seats for this train.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }


	    private static void checkTicketPrice() {
	        System.out.print("Enter Train ID to check ticket price: ");
	        int trainId = scanner.nextInt();

	        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	            String query = "SELECT price FROM trains WHERE train_id = ?";
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setInt(1, trainId);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                System.out.println("Ticket Price: " + rs.getDouble("price"));
	            } else {
	                System.out.println("No pricing data found for this train.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	    } 
	        
	        private static void viewTrains() {
	            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	                String query = "SELECT * FROM trains";
	                Statement stmt = conn.createStatement();
	                ResultSet rs = stmt.executeQuery(query);

	                while (rs.next()) {
	                    System.out.println("Train ID: " + rs.getInt("train_id") + ", Name: " + rs.getString("train_name") + ", Price: " + rs.getDouble("price"));
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }

	        private static void bookTicket() {
	            if (loggedInUserId == -1) {  // Ensure user is logged in
	                System.out.println("You must log in to book a ticket.");
	                return;
	            }

	            System.out.print("Enter Train ID to book ticket: ");
	            int trainId = scanner.nextInt();
	            System.out.print("Enter Seat Number: ");
	            String seatNumber = scanner.next();

	            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	                String query = "INSERT INTO tickets (train_id, seat_number, user_id) VALUES (?, ?, ?)";
	                PreparedStatement pstmt = conn.prepareStatement(query);
	                pstmt.setInt(1, trainId);
	                pstmt.setString(2, seatNumber);
	                pstmt.setInt(3, loggedInUserId);  // Use logged-in user's ID
	                pstmt.executeUpdate();
	                System.out.println("Ticket booked successfully!");
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }


	        private static void cancelTicket() {
	            System.out.print("Enter Ticket ID to cancel: ");
	            int ticketId = scanner.nextInt();

	            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	                String query = "DELETE FROM tickets WHERE ticket_id = ?";
	                PreparedStatement pstmt = conn.prepareStatement(query);
	                pstmt.setInt(1, ticketId);
	                int rows = pstmt.executeUpdate();
	                if (rows > 0) {
	                    System.out.println("Ticket canceled successfully!");
	                } else {
	                    System.out.println("Ticket not found.");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }

	        private static void searchTicket() {
	            System.out.print("Enter Ticket ID to search: ");
	            int ticketId = scanner.nextInt();

	            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	                String query = "SELECT * FROM tickets WHERE ticket_id = ?";
	                PreparedStatement pstmt = conn.prepareStatement(query);
	                pstmt.setInt(1, ticketId);
	                ResultSet rs = pstmt.executeQuery();

	                if (rs.next()) {
	                    System.out.println("Ticket ID: " + rs.getInt("ticket_id") + ", Train ID: " + rs.getInt("train_id") + ", Seat: " + rs.getString("seat_number"));
	                } else {
	                    System.out.println("No ticket found with given ID.");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }

	        private static void deleteTicket() {
	            System.out.print("Enter Ticket ID to delete: ");
	            int ticketId = scanner.nextInt();

	            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	                String query = "DELETE FROM tickets WHERE ticket_id = ?";
	                PreparedStatement pstmt = conn.prepareStatement(query);
	                pstmt.setInt(1, ticketId);
	                int rows = pstmt.executeUpdate();
	                if (rows > 0) {
	                    System.out.println("Ticket deleted successfully!");
	                } else {
	                    System.out.println("Ticket not found.");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	    }
	}

	
	
	
	

