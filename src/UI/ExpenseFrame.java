package UI;

import db.ExpenseService;
import db.CategoryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.List;

public class ExpenseFrame {

    private int userId;
    private JPanel mainPanel;
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryCombo;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField descriptionField;
    private JLabel totalLabel;

    private final Color PRIMARY = new Color(41, 128, 185);
    private final Color BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color DANGER = new Color(231, 76, 60);

    public ExpenseFrame(int userId) {
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

        JLabel titleLabel = new JLabel("Expense Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subtitleLabel = new JLabel("Record and manage your daily expenses — User ID: " + userId);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BG);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);


        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
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
        styleCombo(categoryCombo);
        formPanel.add(categoryCombo, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Amount (Rs):"), gbc);

        gbc.gridx = 3;
        amountField = createFormTextField("");
        formPanel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Date (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        dateField = createFormTextField("2026-01-01");
        formPanel.add(dateField, gbc);

        gbc.gridx = 2;
        formPanel.add(createLabel("Description:"), gbc);

        gbc.gridx = 3;
        descriptionField = createFormTextField("");
        formPanel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(WHITE);

        JButton addButton = createButton("+ Add Expense", SUCCESS);
        JButton deleteButton = createButton("X Delete", DANGER);
        JButton clearButton = createButton("Clear", PRIMARY);

        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        formPanel.add(buttonPanel, gbc);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Category", "Amount (Rs)", "Date", "Description"}, 0);

        expenseTable = new JTable(tableModel);
        styleTable(expenseTable);

        expenseTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = expenseTable.getSelectedRow();
                if (row >= 0) {
                    categoryCombo.setSelectedItem(tableModel.getValueAt(row, 1));
                    amountField.setText(tableModel.getValueAt(row, 2).toString());
                    dateField.setText(tableModel.getValueAt(row, 3).toString());
                    descriptionField.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(BG);

        totalLabel = new JLabel();
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(TEXT_DARK);

        totalPanel.add(totalLabel);

        loadExpensesFromDB();

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(BG);
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(totalPanel, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshCategories();   
                loadExpensesFromDB();    
            }
        });

        return panel;
    }

    private void loadExpensesFromDB() {
        tableModel.setRowCount(0);
        List<Object[]> list = ExpenseService.getExpenses(userId);
        for (Object[] row : list) {
            tableModel.addRow(row);
        }
        recalculateTotal();
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String categoryName = categoryCombo.getSelectedItem().toString();
            int categoryId = CategoryService.getCategoryIdByName(userId,categoryName);

            Date date = Date.valueOf(dateField.getText());
            String desc = descriptionField.getText();

            if (ExpenseService.addExpense(userId, categoryId, amount, date, desc)) {
                JOptionPane.showMessageDialog(mainPanel, "Added!");
                loadExpensesFromDB();
                clearForm();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error: " + e.getMessage());
        }
    }

    private void deleteExpense() {
        int row = expenseTable.getSelectedRow();
        if (row < 0) return;

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        if (ExpenseService.deleteExpense(id)) {
            JOptionPane.showMessageDialog(mainPanel, "Deleted!");
            loadExpensesFromDB();
        }
    }

    private void clearForm() {
        amountField.setText("");
        descriptionField.setText("");
        dateField.setText("2026-01-01");
    }

    private void recalculateTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            total += Double.parseDouble(tableModel.getValueAt(i, 2).toString());
        }
        totalLabel.setText("Total Expenses: Rs. " + total);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JTextField createFormTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(150, 32));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return field;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(150, 32));
    }

    private JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setPreferredSize(new Dimension(140, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
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