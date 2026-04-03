package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReportFrame {

    private int userId;
    private JPanel mainPanel;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> monthCombo;
    private JTextField yearField;
    private JLabel totalPlannedLabel;
    private JLabel totalActualLabel;
    private JLabel totalSavingsLabel;

    private final Color PRIMARY = new Color(41, 128, 185);
    private final Color BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color REPORT_COLOR = new Color(142, 68, 173);

    public ReportFrame(int userId) {
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

        JLabel titleLabel = new JLabel("Monthly Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subtitleLabel = new JLabel("Compare planned budget vs actual expenses — User ID: " + userId);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BG);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 20, 10, 20)
        ));

        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        monthLabel.setForeground(TEXT_DARK);

        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        monthCombo.setPreferredSize(new Dimension(150, 32));

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        yearLabel.setForeground(TEXT_DARK);

        yearField = new JTextField("2025");
        yearField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        yearField.setPreferredSize(new Dimension(80, 32));
        yearField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                new EmptyBorder(4, 8, 4, 8)
        ));

        JButton generateButton = createButton("Generate Report", REPORT_COLOR);
        generateButton.addActionListener(e -> generateReport());

        filterPanel.add(monthLabel);
        filterPanel.add(monthCombo);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(yearLabel);
        filterPanel.add(yearField);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(generateButton);

        String[] columns = {"Category", "Planned (Rs)", "Actual (Rs)", "Difference (Rs)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        reportTable = new JTable(tableModel);
        styleReportTable(reportTable);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(WHITE);

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setBackground(BG);
        summaryPanel.setPreferredSize(new Dimension(800, 80));

        totalPlannedLabel = new JLabel("Rs. 0.00");
        totalPlannedLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalPlannedLabel.setForeground(PRIMARY);

        totalActualLabel = new JLabel("Rs. 0.00");
        totalActualLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalActualLabel.setForeground(DANGER);

        totalSavingsLabel = new JLabel("Rs. 0.00");
        totalSavingsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalSavingsLabel.setForeground(SUCCESS);

        summaryPanel.add(createSummaryCard("Total Planned", totalPlannedLabel));
        summaryPanel.add(createSummaryCard("Total Actual", totalActualLabel));
        summaryPanel.add(createSummaryCard("Net Savings", totalSavingsLabel));

        generateReport();

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(BG);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(summaryPanel, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void generateReport() {
        tableModel.setRowCount(0);

        String month = monthCombo.getSelectedItem().toString();
        int year = Integer.parseInt(yearField.getText());

        double totalPlanned = 0;
        double totalActual = 0;

        java.util.List<Object[]> list = db.ReportService.getMonthlyReport(userId, month, year);

        for (Object[] row : list) {
            tableModel.addRow(row);

            totalPlanned += Double.parseDouble(row[1].toString());
            totalActual += Double.parseDouble(row[2].toString());
        }

        totalPlannedLabel.setText("Rs. " + String.format("%.2f", totalPlanned));
        totalActualLabel.setText("Rs. " + String.format("%.2f", totalActual));
        totalSavingsLabel.setText("Rs. " + String.format("%.2f", (totalPlanned - totalActual)));
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(127, 140, 141));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void styleReportTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(210, 180, 222));
        table.setSelectionForeground(TEXT_DARK);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(REPORT_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setReorderingAllowed(false);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                String status = value.toString();
                if (status.contains("Under")) {
                    c.setForeground(SUCCESS);
                } else {
                    c.setForeground(DANGER);
                }
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JButton createButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bg);
        button.setPreferredSize(new Dimension(180, 35));
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
}