package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    // ✅ GET ALL (ID + NAME)
    public static List<Object[]> getCategories(int userId) {
        List<Object[]> list = new ArrayList<>();

        String query = "SELECT category_id, category_name FROM expense_categories WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                });
            }
             rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> getCategoryNamesByUser(int userId) {
        List<String> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT category_name FROM expense_categories WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("category_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    // ✅ ADD CATEGORY
    public static boolean addCategory(int userId, String name) {
        String query = "INSERT INTO expense_categories (user_id, category_name, created_at) VALUES (?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, name);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ DELETE CATEGORY
    public static boolean deleteCategory(int id) {
        String query = "DELETE FROM expense_categories WHERE category_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ GET CATEGORY ID BY NAME (VERY IMPORTANT)
    public static int getCategoryIdByName(int userId, String name) {
        String query = "SELECT category_id FROM expense_categories WHERE user_id = ? AND category_name = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, name);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("category_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ REQUIRED FUNCTION (FIX FOR YOUR ERROR)
    public static List<String> getCategoryNameById(int userId) {
        List<String> list = new ArrayList<>();

        // ⚠️ Your table has no user_id, so we fetch all
        String query = "SELECT category_name FROM expense_categories";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("category_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}