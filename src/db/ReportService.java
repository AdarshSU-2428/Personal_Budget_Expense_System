package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportService {

    public static List<Object[]> getMonthlyReport(int userId, String month, int year) {

        List<Object[]> list = new ArrayList<>();

        String query = """
            SELECT 
                c.category_name,
                COALESCE(MAX(b.total_amount), 0) AS planned,
                COALESCE(SUM(e.amount), 0) AS actual
            FROM expense_categories c

            LEFT JOIN budgets b 
                ON c.category_id = b.category_id
                AND b.user_id = ?
                AND b.month = ?
                AND b.year = ?

            LEFT JOIN expenses e 
                ON c.category_id = e.category_id
                AND e.user_id = ?
                AND EXTRACT(MONTH FROM e.expense_date) = ?
                AND EXTRACT(YEAR FROM e.expense_date) = ?

            GROUP BY c.category_id, c.category_name
            ORDER BY c.category_name
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, month);
            ps.setInt(3, year);

            ps.setInt(4, userId);
            ps.setInt(5, getMonthNumber(month)); // convert month name → number
            ps.setInt(6, year);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String category = rs.getString("category_name");
                double planned = rs.getDouble("planned");
                double actual = rs.getDouble("actual");

                double diff = planned - actual;
                String status = diff >= 0 ? "Under Budget" : "Over Budget";

                list.add(new Object[]{
                        category,
                        String.format("%.2f", planned),
                        String.format("%.2f", actual),
                        String.format("%.2f", diff),
                        status
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static int getMonthNumber(String month) {
        switch (month.toLowerCase()) {
            case "january": return 1;
            case "february": return 2;
            case "march": return 3;
            case "april": return 4;
            case "may": return 5;
            case "june": return 6;
            case "july": return 7;
            case "august": return 8;
            case "september": return 9;
            case "october": return 10;
            case "november": return 11;
            case "december": return 12;
            default: return 1;
        }
    }
}