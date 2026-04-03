package UI;

import db.BudgetService;
import db.CategoryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BudgetFrame {

    private int userId;
    private JPanel mainPanel;
    private JTable budgetTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> monthCombo;
    private JTextField yearField;
    private JTextField amountField;

    private final Color PRIMARY = new Color(41, 128, 185);
    private final Color BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color WARNING = new Color(243, 156, 18);

    public BudgetFrame(int userId) {
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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG);

        JLabel titleLabel = new JLabel("Budget Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subtitleLabel = new JLabel("Create and manage your monthly budgets — User ID: " + userId);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BG);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);


        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Category:"), gbc);

        gbc.gridx = 1;

        List<String> categories = CategoryService.getCategoryNamesByUser(userId);
        categoryCombo = new JComboBox<>(categories.toArray(new String[0]));
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryCombo.setPreferredSize(new Dimension(180, 32));
        formPanel.add(categoryCombo, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Month:"), gbc);

        gbc.gridx = 3;
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        monthCombo.setPreferredSize(new Dimension(150, 32));
        formPanel.add(monthCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Year:"), gbc);

        gbc.gridx = 1;
        yearField = createFormTextField("2026");
        formPanel.add(yearField, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Planned Amount (Rs):"), gbc);

        gbc.gridx = 3;
        amountField = createFormTextField("");
        formPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(WHITE);

        JButton addButton = createButton("+ Add Budget", SUCCESS);
        JButton updateButton = createButton("Edit Update", WARNING);
        JButton deleteButton = createButton("X Delete", DANGER);
        JButton clearButton = createButton("Clear", PRIMARY);

        addButton.addActionListener(e -> addBudget());
        updateButton.addActionListener(e -> updateBudget());
        deleteButton.addActionListener(e -> deleteBudget());
        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        formPanel.add(buttonPanel, gbc);

        String[] columns = {"ID", "Category", "Month", "Year", "Planned Amount (Rs)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        budgetTable = new JTable(tableModel);
        styleTable(budgetTable);

        budgetTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = budgetTable.getSelectedRow();
                if (row >= 0) {
                    categoryCombo.setSelectedItem(tableModel.getValueAt(row, 1).toString());

                    String monthVal = tableModel.getValueAt(row, 2).toString();
                    monthCombo.setSelectedItem(monthVal);

                    yearField.setText(tableModel.getValueAt(row, 3).toString());
                    amountField.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(budgetTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(WHITE);

        loadBudgetsFromDB();

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(BG);
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshCategories();   
                loadBudgetsFromDB();
            }
        });

        return panel;
    }

    private void loadBudgetsFromDB() {
        tableModel.setRowCount(0);
        List<Object[]> budgets = BudgetService.getBudgets(userId);
        for (Object[] row : budgets) {
            tableModel.addRow(row);
        }
    }

    private void addBudget() {
        try {
            String categoryName = categoryCombo.getSelectedItem().toString();
            int categoryId = CategoryService.getCategoryIdByName(userId,categoryName);

            String month = monthCombo.getSelectedItem().toString();
            int year = Integer.parseInt(yearField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());

            boolean success = BudgetService.addBudget(userId, categoryId, month, year, amount);

            if (success) {
                JOptionPane.showMessageDialog(mainPanel, "Budget added!");
                loadBudgetsFromDB();
                clearForm();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid input");
        }
    }

    private void updateBudget() {
        int row = budgetTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Select a row");
            return;
        }

        try {
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

            String categoryName = categoryCombo.getSelectedItem().toString();
            int categoryId = CategoryService.getCategoryIdByName(userId,categoryName);

            String month = monthCombo.getSelectedItem().toString(); 
            int year = Integer.parseInt(yearField.getText().trim());
            double amount = Double.parseDouble(amountField.getText().trim());

            System.out.println("Updating ID: " + id); 

            boolean success = BudgetService.updateBudget(id, categoryId, month, year, amount);

            if (success) {
                JOptionPane.showMessageDialog(mainPanel, "Updated!");
                loadBudgetsFromDB();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Update failed!");
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(mainPanel, "Error updating: " + e.getMessage());
        }
    }

    private void deleteBudget() {
        int row = budgetTable.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Select a row");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        boolean success = BudgetService.deleteBudget(id);

        if (success) {
            JOptionPane.showMessageDialog(mainPanel, "Deleted");
            loadBudgetsFromDB();
        }
    }

    private void clearForm() {
        categoryCombo.setSelectedIndex(0);
        monthCombo.setSelectedIndex(0);
        yearField.setText("2025");
        amountField.setText("");
        budgetTable.clearSelection();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JTextField createFormTextField(String defaultText) {
        JTextField field = new JTextField(defaultText);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(150, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return field;
    }

    private JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setPreferredSize(new Dimension(140, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        Color hover = bg.darker();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
            public void mouseExited(MouseEvent e) { button.setBackground(bg); }
        });

        return button;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(TEXT_DARK);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    private void refreshCategories() {
        String selected = (String) categoryCombo.getSelectedItem();

        List<String> categories = CategoryService.getCategoryNamesByUser(userId);

        categoryCombo.removeAllItems();

        for (String cat : categories) {
            categoryCombo.addItem(cat);
        }

        if (selected != null) {
            categoryCombo.setSelectedItem(selected);
        }
    }
}