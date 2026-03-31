package db;

import java.sql.*;

public class UserService {

    // ✅ REGISTER USER
    public static boolean registerUser(String username, String email, String password) {

        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {

            // 🔥 HANDLE UNIQUE ERRORS (EMAIL / USERNAME)
            if (e.getMessage().contains("users_email_key")) {
                System.out.println("❌ Email already exists");
            } else if (e.getMessage().contains("users_username_key")) {
                System.out.println("❌ Username already exists");
            } else {
                e.printStackTrace();
            }

            return false;
        }
    }

    // ✅ LOGIN USER (EMAIL + PASSWORD)
    public static int loginUser(String email, String password) {

        String query = "SELECT user_id FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (Exception e) {
            System.out.println("LOGIN ERROR:");
            e.printStackTrace();
        }

        return -1; // ❌ invalid login
    }

    // ✅ GET USERNAME BY ID (IMPORTANT FOR DASHBOARD)
    public static String getUsernameById(int userId) {

        String query = "SELECT username FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "User";
    }
}