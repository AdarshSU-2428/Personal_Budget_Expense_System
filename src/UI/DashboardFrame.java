package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardFrame extends JFrame {

    private int userId;
    private String userName;

    private final Color SIDEBAR_BG = new Color(44, 62, 80);
    private final Color SIDEBAR_HOVER = new Color(52, 73, 94);
    private final Color CONTENT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_LIGHT = new Color(189, 195, 199);
    private final Color TEXT_DARK = new Color(44, 62, 80);
    private final Color PRIMARY = new Color(41, 128, 185);

    private JPanel contentPanel;
    private CardLayout contentCardLayout;

    public DashboardFrame(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Personal Expense Manager - Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 700));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel profilePanel = new JPanel();
        profilePanel.setBackground(new Color(34, 49, 63));
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        profilePanel.setMaximumSize(new Dimension(240, 100));
        profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatarLabel = new JLabel("USER");
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        avatarLabel.setForeground(new Color(174, 214, 241));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(userName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        profilePanel.add(avatarLabel);
        profilePanel.add(Box.createVerticalStrut(8));
        profilePanel.add(nameLabel);

        sidebar.add(profilePanel);
        sidebar.add(Box.createVerticalStrut(20));

        String[][] menuItems = {
                {"Home", "HOME"},
                {"Manage Budget", "BUDGET"},
                {"Add Expense", "EXPENSE"},
                {"Categories", "CATEGORY"},
                {"Reports", "REPORT"},
                {"Version History", "HISTORY"}
        };

        for (String[] item : menuItems) {
            JButton btn = createSidebarButton("  " + item[0], item[1]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("  Logout");
        logoutBtn.setActionCommand("LOGOUT");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutBtn.setForeground(TEXT_LIGHT);
        logoutBtn.setBackground(new Color(192, 57, 43));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);
        logoutBtn.setMaximumSize(new Dimension(240, 45));
        logoutBtn.setPreferredSize(new Dimension(240, 45));
        logoutBtn.setBorder(new EmptyBorder(10, 25, 10, 10));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setOpaque(true);

        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { logoutBtn.setBackground(new Color(169, 50, 38)); }
            public void mouseExited(MouseEvent e) { logoutBtn.setBackground(new Color(192, 57, 43)); }
        });

        logoutBtn.addActionListener(e -> handleNavigation("LOGOUT"));

        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(15));

        return sidebar;
    }

    private JButton createSidebarButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(TEXT_LIGHT);
        button.setBackground(SIDEBAR_BG);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(240, 45));
        button.setPreferredSize(new Dimension(240, 45));
        button.setBorder(new EmptyBorder(10, 25, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SIDEBAR_HOVER);
                button.setForeground(WHITE);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIDEBAR_BG);
                button.setForeground(TEXT_LIGHT);
            }
        });

        button.addActionListener(e -> handleNavigation(command));

        return button;
    }

    private JPanel createMainContent() {
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(CONTENT_BG);

        contentPanel.add(createHomePanel(), "HOME");
        contentPanel.add(new BudgetFrame(userId).getMainPanel(), "BUDGET");
        contentPanel.add(new ExpenseFrame(userId).getMainPanel(), "EXPENSE");
        contentPanel.add(new CategoryFrame(userId).getMainPanel(), "CATEGORY");
        contentPanel.add(new ReportFrame(userId).getMainPanel(), "REPORT");
        contentPanel.add(new VersionHistoryFrame(userId).getMainPanel(), "HISTORY");

        contentCardLayout.show(contentPanel, "HOME");

        return contentPanel;
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(CONTENT_BG);
        homePanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CONTENT_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        JLabel welcomeLabel = new JLabel("Welcome back, " + userName + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_DARK);

        JLabel dateLabel = new JLabel("Manage your finances efficiently");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(new Color(127, 140, 141));

        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(dateLabel, BorderLayout.SOUTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(CONTENT_BG);
        cardsPanel.setPreferredSize(new Dimension(800, 140));

        double totalBudget = getTotalBudget();
        double totalExpenses = getTotalExpenses();
        double remaining = totalBudget - totalExpenses;
        int categories = getTotalCategories();

        cardsPanel.add(createSummaryCard("Total Budget", "Rs. " + totalBudget, "BUDGET", PRIMARY));
        cardsPanel.add(createSummaryCard("Total Expenses", "Rs. " + totalExpenses, "EXPENSE", new Color(231, 76, 60)));
        cardsPanel.add(createSummaryCard("Remaining", "Rs. " + remaining, "SAVINGS", new Color(46, 204, 113)));
        cardsPanel.add(createSummaryCard("Categories", String.valueOf(categories), "CATEGORY", new Color(155, 89, 182)));

        JPanel actionsTitlePanel = new JPanel(new BorderLayout());
        actionsTitlePanel.setBackground(CONTENT_BG);
        actionsTitlePanel.setBorder(new EmptyBorder(25, 0, 15, 0));

        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        actionsTitle.setForeground(TEXT_DARK);
        actionsTitlePanel.add(actionsTitle, BorderLayout.WEST);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionsPanel.setBackground(CONTENT_BG);

        JButton quickBudget = createQuickActionButton("+ New Budget", PRIMARY);
        quickBudget.addActionListener(e -> handleNavigation("BUDGET"));

        JButton quickExpense = createQuickActionButton("+ Add Expense", new Color(46, 204, 113));
        quickExpense.addActionListener(e -> handleNavigation("EXPENSE"));

        JButton quickReport = createQuickActionButton("View Reports", new Color(155, 89, 182));
        quickReport.addActionListener(e -> handleNavigation("REPORT"));

        actionsPanel.add(quickBudget);
        actionsPanel.add(quickExpense);
        actionsPanel.add(quickReport);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(CONTENT_BG);
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(actionsTitlePanel, BorderLayout.CENTER);
        centerPanel.add(actionsPanel, BorderLayout.SOUTH);

        homePanel.add(headerPanel, BorderLayout.NORTH);
        homePanel.add(centerPanel, BorderLayout.CENTER);

        return homePanel;
    }

    private JPanel createSummaryCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel();
        card.setBackground(WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel("[" + icon + "]");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        iconLabel.setForeground(color);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(127, 140, 141));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(titleLabel);

        return card;
    }

    private JButton createQuickActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(WHITE);
        button.setBackground(color);
        button.setPreferredSize(new Dimension(180, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        Color hoverColor = color.darker();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { button.setBackground(color); }
        });

        return button;
    }

    private void handleNavigation(String command) {
        if ("LOGOUT".equals(command)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        } else {
            if ("HOME".equals(command)) {
            refreshHomePanel(); 
            }
            contentCardLayout.show(contentPanel, command);
        }
    }

    private double getTotalBudget() {
        double total = 0;
        try {
            java.util.List<Object[]> list = db.BudgetService.getBudgets(userId);
            for (Object[] row : list) {
                total += Double.parseDouble(row[4].toString()); // total_amount
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private double getTotalExpenses() {
        double total = 0;
        try {
            java.util.List<Object[]> list = db.ExpenseService.getExpenses(userId);
            for (Object[] row : list) {
                total += Double.parseDouble(row[2].toString()); // amount
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private int getTotalCategories() {
        try {
            return db.CategoryService.getCategoryNamesByUser(userId).size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private void refreshHomePanel() {
        contentPanel.remove(contentPanel.getComponent(0)); 
        contentPanel.add(createHomePanel(), "HOME");       
    }
}