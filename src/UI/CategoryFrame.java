package UI;

import db.CategoryService;   // ✅ Correct package

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CategoryFrame {

    private int userId;
    private JPanel mainPanel;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField categoryNameField;

    private final Color BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color PURPLE = new Color(155, 89, 182);

    public CategoryFrame(int userId) {
        this.userId = userId;
        mainPanel = createMainPanel();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG);

        JLabel titleLabel = new JLabel("Category Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subtitleLabel = new JLabel("Organize your expenses — User ID: " + userId);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BG);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Content
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(BG);

        // Left panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(30, 30, 30, 30)
        ));

        JLabel formTitle = new JLabel("Add New Category");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(PURPLE);

        JLabel nameLabel = new JLabel("Category Name:");

        categoryNameField = new JTextField();
        categoryNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JButton addButton = createButton("+ Add Category", SUCCESS);
        JButton deleteButton = createButton("Delete Selected", DANGER);

        addButton.addActionListener(e -> addCategory());
        deleteButton.addActionListener(e -> deleteCategory());

        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(nameLabel);
        formPanel.add(categoryNameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(addButton);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(deleteButton);

        // Table
        String[] columns = {"ID", "Category Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        categoryTable = new JTable(tableModel);
        styleTable(categoryTable);

        JScrollPane scrollPane = new JScrollPane(categoryTable);

        loadCategoriesFromDB();   // ✅ load from database

        contentPanel.add(formPanel);
        contentPanel.add(scrollPane);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // Load categories from DB
    private void loadCategoriesFromDB() {
        tableModel.setRowCount(0);

        List<Object[]> categories = CategoryService.getCategories(userId);

        for (Object[] row : categories) {
            tableModel.addRow(row);
        }
    }

    // Add category
    private void addCategory() {
        String name = categoryNameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter a category name.");
            return;
        }

        boolean success = CategoryService.addCategory(userId, name);

        if (success) {
            JOptionPane.showMessageDialog(mainPanel, "Category added successfully!");
            categoryNameField.setText("");
            loadCategoriesFromDB();
        }
    }

    // Delete category
    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a category.");
            return;
        }

        int categoryId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                mainPanel,
                "Delete this category?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = CategoryService.deleteCategory(categoryId);

            if (success) {
                JOptionPane.showMessageDialog(mainPanel, "Category deleted.");
                loadCategoriesFromDB();
            }
        }
    }

    private JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setBackground(PURPLE);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}