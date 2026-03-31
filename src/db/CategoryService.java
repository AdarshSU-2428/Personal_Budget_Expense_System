package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    // ✅ GET ALL (ID + NAME)
    public static List<Object[]> getCategories(int userId) {
        List<Object[]> list = new ArrayList<>();

        String query = "SELECT category_id, category_name FROM expense_categories";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ GET ONLY CATEGORY NAMES (FOR DROPDOWN)
    public static List<String> getAllCategoryNames() {
        List<String> list = new ArrayList<>();

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

    // ✅ ADD CATEGORY
    public static boolean addCategory(int userId, String name) {
        String query = "INSERT INTO expense_categories (category_name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
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
    public static int getCategoryIdByName(String name) {
        String query = "SELECT category_id FROM expense_categories WHERE category_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);

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