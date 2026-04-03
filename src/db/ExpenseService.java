package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseService {

    public static List<Object[]> getExpenses(int userId) {

        List<Object[]> list = new ArrayList<>();

        String query = "SELECT e.expense_id, c.category_name, e.amount, e.expense_date, e.description " +
                       "FROM expenses e " +
                       "JOIN expense_categories c ON e.category_id = c.category_id " +
                       "WHERE e.user_id = ? " +
                       "ORDER BY e.expense_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("expense_id"),
                        rs.getString("category_name"),
                        rs.getDouble("amount"),
                        rs.getDate("expense_date"),
                        rs.getString("description")
                });
            }

        } catch (Exception e) {
            System.out.println("FETCH EXPENSE ERROR:");
            e.printStackTrace();
        }

        return list;
    }

    public static boolean addExpense(int userId, int categoryId,
                                    double amount, Date date, String description) {

        String query = "INSERT INTO expenses (user_id, category_id, amount, expense_date, description) " +
                       "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, categoryId);
            ps.setDouble(3, amount);
            ps.setDate(4, date);
            ps.setString(5, description);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("ADD EXPENSE ERROR:");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateExpense(int expenseId, int categoryId,
                                        double amount, Date date, String description) {

        String query = "UPDATE expenses SET category_id = ?, amount = ?, expense_date = ?, description = ? " +
                       "WHERE expense_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, categoryId);
            ps.setDouble(2, amount);
            ps.setDate(3, date);
            ps.setString(4, description);
            ps.setInt(5, expenseId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("UPDATE EXPENSE ERROR:");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteExpense(int expenseId) {

        String query = "DELETE FROM expenses WHERE expense_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, expenseId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("DELETE EXPENSE ERROR:");
            e.printStackTrace();
            return false;
        }
    }
}