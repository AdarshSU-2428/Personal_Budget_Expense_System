import db.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.io.InputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import UI.LoginFrame;

public class Main {

    public static void main(String[] args) {

        try (InputStream is = Main.class.getClassLoader()
                                        .getResourceAsStream("db/schema.sql")) {

            if (is == null) {
                throw new IOException("schema.sql not found in classpath under db/");
            }

            // Read schema.sql from classpath
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Connect to database
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement()) {

                System.out.println("Database Connected");

                // Execute each statement individually
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }

                System.out.println("Schema executed successfully");

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginFrame());    
    
    }
    
}

