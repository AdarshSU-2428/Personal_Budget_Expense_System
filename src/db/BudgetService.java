package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetService {

    public static List<Object[]> getBudgets(int userId) {
        List<Object[]> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT b.budget_id, c.category_name, b.month, b.year, b.total_amount " +
                         "FROM budgets b " +
                         "JOIN expense_categories c ON b.category_id = c.category_id " +
                         "WHERE b.user_id = ? " +
                         "ORDER BY b.budget_id";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("budget_id"),
                        rs.getString("category_name"),
                        rs.getString("month"),
                        rs.getInt("year"),
                        rs.getDouble("total_amount")
                });
            }

        } catch (Exception e) {
            System.out.println("Error fetching budgets: " + e.getMessage());
        }

        return list;
    }

    public static boolean addBudget(int userId, int categoryId, String month, int year, double amount) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO budgets (user_id, category_id, month, year, total_amount) " +
                         "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setInt(2, categoryId);
            ps.setString(3, month);
            ps.setInt(4, year);
            ps.setDouble(5, amount);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error adding budget: " + e.getMessage());
            throw e;
        }
    }

    public static boolean updateBudget(int id, int categoryId, String month, int year, double amount) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {

            String sql = "UPDATE budgets SET category_id=?, month=?, year=?, total_amount=? " +
                         "WHERE budget_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, categoryId);
            ps.setString(2, month);
            ps.setInt(3, year);
            ps.setDouble(4, amount);
            ps.setInt(5, id);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error updating budget: " + e.getMessage());
            throw e;
        }
    }

    public static boolean deleteBudget(int id) {
        try (Connection conn = DBConnection.getConnection()) {

            String sql = "DELETE FROM budgets WHERE budget_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error deleting budget: " + e.getMessage());
            return false;
        }
    }

    public static List<Object[]> getVersionHistory(int userId) {
        List<Object[]> list = new ArrayList<>();

        String query =
                "SELECT v.version_id, v.budget_id, c.category_name, " +
                "b.month, b.year, " +
                "prev.planned_amount AS old_amount, " +
                "v.planned_amount AS new_amount, " +
                "v.changed_at " +
                "FROM budget_versions v " +

                "LEFT JOIN budget_versions prev " +
                "ON v.budget_id = prev.budget_id " +
                "AND v.version_number = prev.version_number + 1 " +

                "JOIN budgets b ON v.budget_id = b.budget_id " +
                "JOIN expense_categories c ON v.category_id = c.category_id " +

                "WHERE b.user_id = ? " +
                "ORDER BY v.changed_at DESC";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                double oldAmt = rs.getDouble("old_amount");
                double newAmt = rs.getDouble("new_amount");

                if (rs.wasNull()) oldAmt = 0; // first version case

                double change = newAmt - oldAmt;

                String changeStr = (change >= 0 ? "+" : "") + String.format("%.2f", change);

                list.add(new Object[]{
                        rs.getInt("version_id"),
                        rs.getInt("budget_id"),
                        rs.getString("category_name"),
                        rs.getString("month"),
                        rs.getInt("year"),
                        String.format("%.2f", oldAmt),
                        String.format("%.2f", newAmt),
                        changeStr,
                        rs.getTimestamp("changed_at").toString()
                });
            }

        } catch (Exception e) {
            System.out.println("VERSION HISTORY ERROR:");
            e.printStackTrace();
        }

        return list;
    }
}