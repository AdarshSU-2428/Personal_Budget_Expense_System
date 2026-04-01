package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VersionHistoryFrame {

    private int userId;
    private JPanel mainPanel;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JPanel statsPanel; // ✅ made global

    private final Color BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color HISTORY_COLOR = new Color(52, 152, 219);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color SUCCESS = new Color(46, 204, 113);

    public VersionHistoryFrame(int userId) {
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

        JLabel titleLabel = new JLabel("Budget Version History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);

        JLabel subtitleLabel = new JLabel(
                "Track all changes made to your budgets (auto-captured by database trigger) — User ID: " + userId);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(127, 140, 141));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BG);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbarPanel.setBackground(BG);

        JButton refreshButton = createButton("Refresh", HISTORY_COLOR);
        refreshButton.addActionListener(e -> loadSampleData());

        JButton exportButton = createButton("Export", new Color(46, 204, 113));
        exportButton.addActionListener(e ->
                JOptionPane.showMessageDialog(mainPanel,
                        "Export feature will be available with backend integration.",
                        "Info", JOptionPane.INFORMATION_MESSAGE));

        toolbarPanel.add(refreshButton);
        toolbarPanel.add(exportButton);

        // Table
        String[] columns = {"Version ID", "Budget ID", "Category", "Month", "Year",
                "Old Amount (Rs)", "New Amount (Rs)", "Change (Rs)", "Changed At"};

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        historyTable = new JTable(tableModel);
        styleHistoryTable(historyTable);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(WHITE);

        // Stats panel
        statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(BG);
        statsPanel.setPreferredSize(new Dimension(800, 75));

        // Layout
        JPanel topSection = new JPanel(new BorderLayout(0, 10));
        topSection.setBackground(BG);
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(toolbarPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(BG);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(statsPanel, BorderLayout.SOUTH);

        panel.add(topSection, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        // ✅ LOAD DATA AT END (IMPORTANT FIX)
        loadSampleData();

        return panel;
    }

    private void loadSampleData() {
        tableModel.setRowCount(0);

        java.util.List<Object[]> list = db.BudgetService.getVersionHistory(userId);

        int increases = 0;
        int decreases = 0;

        for (Object[] row : list) {
            tableModel.addRow(row);

            String change = row[7].toString();

            if (change.startsWith("+")) increases++;
            else if (change.startsWith("-")) decreases++;
        }

        // update stats
        statsPanel.removeAll();
        statsPanel.add(createStatCard("Total Revisions", String.valueOf(list.size()), HISTORY_COLOR));
        statsPanel.add(createStatCard("Budget Increases", String.valueOf(increases), DANGER));
        statsPanel.add(createStatCard("Budget Decreases", String.valueOf(decreases), SUCCESS));

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(12, 18, 12, 18)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void styleHistoryTable(JTable table) {
        table.setRowHeight(35);

        JTableHeader header = table.getTableHeader();
        header.setBackground(HISTORY_COLOR);
        header.setForeground(Color.WHITE);

        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, s, f, r, c);
                String val = v.toString();
                comp.setForeground(val.startsWith("+") ? DANGER : SUCCESS);
                return comp;
            }
        });
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });

        return b;
    }
}