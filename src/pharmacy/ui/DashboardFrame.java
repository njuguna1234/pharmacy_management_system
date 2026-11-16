
package pharmacy.ui;

import pharmacy.dao.MedicineDao;
import pharmacy.model.Medicine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class DashboardFrame extends JFrame {

    // ================= COLORS =================

    private static final Color BLACK = new Color(18, 18, 18);
    private static final Color DARK = new Color(30, 30, 30);
    private static final Color GOLD = new Color(254, 190, 35);
    private static final Color BACKGROUND = new Color(242, 242, 242);
    private static final Color WHITE = Color.WHITE;
    private static final Color MUTED = new Color(175, 175, 175);
    private static final Color BORDER = new Color(220, 220, 220);

    private static final Color GREEN = new Color(34, 139, 84);
    private static final Color BLUE = new Color(45, 105, 190);
    private static final Color RED = new Color(205, 55, 55);
    private static final Color GREY = new Color(90, 90, 90);

    // ================= DATABASE =================

    private final MedicineDao dao = new MedicineDao();

    // ================= COMPONENTS =================

    private final JTextField search = new JTextField(25);

    private final JLabel totalValue = new JLabel("0");
    private final JLabel lowStockValue = new JLabel("0");
    private final JLabel expiredValue = new JLabel("0");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{
                    "ID",
                    "Medicine",
                    "Category",
                    "Price (KES)",
                    "Quantity",
                    "Expiry Date",
                    "Low-stock level",
                    "Status"
            }, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    // ================= CONSTRUCTOR =================

    public DashboardFrame(String username) {

        setTitle("Pharmacy Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 780);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        add(buildSidebar(), BorderLayout.WEST);
        add(buildMainContent(username), BorderLayout.CENTER);

        configureLiveSearch();

        loadMedicines("");
    }

    // ================= SIDEBAR =================

    private JPanel buildSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(BLACK);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("MEDICARE");
        logo.setFont(new Font("SansSerif", Font.BOLD, 27));
        logo.setForeground(GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("PHARMACY SYSTEM");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitle.setForeground(MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(subtitle);

        sidebar.add(Box.createVerticalStrut(45));

        sidebar.add(sidebarButton("Dashboard", true));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(sidebarButton("Inventory", false));

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("Pharmacy Management");
        footer.setForeground(MUTED);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(footer);

        return sidebar;
    }

    private JButton sidebarButton(String text, boolean active) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(190, 45));
        button.setPreferredSize(new Dimension(190, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            button.setBackground(GOLD);
            button.setForeground(BLACK);
        } else {
            button.setBackground(DARK);
            button.setForeground(WHITE);
        }

        return button;
    }

    // ================= MAIN CONTENT =================

    private JPanel buildMainContent(String username) {

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(BACKGROUND);
        main.setBorder(new EmptyBorder(20, 25, 20, 25));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BACKGROUND);

        JLabel heading = new JLabel("Dashboard");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        heading.setForeground(BLACK);

        JLabel welcome = new JLabel("Signed in as: " + username);
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 13));
        welcome.setForeground(GREY);

        top.add(heading, BorderLayout.WEST);
        top.add(welcome, BorderLayout.EAST);

        JPanel content = new JPanel(new BorderLayout(15, 20));
        content.setBackground(BACKGROUND);

        content.add(buildSummaryCards(), BorderLayout.NORTH);
        content.add(buildInventoryPanel(), BorderLayout.CENTER);

        main.add(top, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);

        return main;
    }

    // ================= SUMMARY CARDS =================

    private JPanel buildSummaryCards() {

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));
        cards.setBackground(BACKGROUND);

        cards.add(createCard(
                "TOTAL MEDICINES",
                totalValue,
                GOLD
        ));

        cards.add(createCard(
                "LOW STOCK",
                lowStockValue,
                new Color(230, 155, 25)
        ));

        cards.add(createCard(
                "EXPIRED MEDICINES",
                expiredValue,
                RED
        ));

        return cards;
    }

    private JPanel createCard(
            String title,
            JLabel value,
            Color accent
    ) {

        JPanel card = new JPanel(new BorderLayout(5, 10));

        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 12));
        heading.setForeground(GREY);

        value.setFont(new Font("SansSerif", Font.BOLD, 30));
        value.setForeground(accent);

        card.add(heading, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return card;
    }

    // ================= INVENTORY PANEL =================

    
private JPanel buildInventoryPanel() {

    JPanel panel = new JPanel(new BorderLayout(10, 15));
    panel.setBackground(WHITE);

    panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(18, 18, 18, 18)
    ));

    // ================= TITLE =================

    JLabel title = new JLabel("Medicine Inventory");
    title.setFont(new Font("SansSerif", Font.BOLD, 19));
    title.setForeground(BLACK);

    // ================= SEARCH ROW =================

    JPanel searchPanel = new JPanel(new BorderLayout(12, 5));
    searchPanel.setBackground(WHITE);

    JLabel searchLabel = new JLabel("Search Medicines:");
    searchLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

    styleTextField(search);

    search.setPreferredSize(new Dimension(300, 38));
    search.setToolTipText(
            "Search by medicine ID, name, category, status or quantity"
    );

    searchPanel.add(searchLabel, BorderLayout.WEST);
    searchPanel.add(search, BorderLayout.CENTER);

    // ================= ACTION BUTTONS ROW =================

    JPanel actions = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 5)
    );

    actions.setBackground(WHITE);

    JButton showAll = createButton("Show All", GREY);
    JButton refresh = createButton("Refresh", GOLD);
    JButton add = createButton("Add Medicine", GREEN);
    JButton edit = createButton("Edit Selected", BLUE);
    JButton delete = createButton("Delete Selected", RED);

    showAll.setForeground(WHITE);

    actions.add(showAll);
    actions.add(refresh);
    actions.add(add);
    actions.add(edit);
    actions.add(delete);

    // ================= TOP SECTION =================

    JPanel top = new JPanel();
    top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
    top.setBackground(WHITE);

    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    actions.setAlignmentX(Component.LEFT_ALIGNMENT);

    top.add(title);
    top.add(Box.createVerticalStrut(15));

    // Search gets its own row
    top.add(searchPanel);

    top.add(Box.createVerticalStrut(10));

    // Buttons get their own row
    top.add(actions);

    top.add(Box.createVerticalStrut(12));

    // ================= TABLE =================

    styleTable();

    JScrollPane scroll = new JScrollPane(table);
    scroll.setBorder(new LineBorder(BORDER, 1, true));
    scroll.getViewport().setBackground(WHITE);

    // ================= FOOTER =================

    JLabel footer = new JLabel(
            "Expiry warning: 30 days | Low stock is based on the medicine's low-stock level."
    );

    footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
    footer.setForeground(GREY);

    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
    bottom.setBackground(WHITE);
    bottom.add(footer);

    // ================= BUTTON ACTIONS =================

    showAll.addActionListener(e -> {
        search.setText("");
        loadMedicines("");
    });

    refresh.addActionListener(e -> loadMedicines(search.getText()));

    add.addActionListener(e -> showMedicineDialog(null));

    edit.addActionListener(e -> editSelected());

    delete.addActionListener(e -> deleteSelected());

    // ================= ADD COMPONENTS =================

    panel.add(top, BorderLayout.NORTH);
    panel.add(scroll, BorderLayout.CENTER);
    panel.add(bottom, BorderLayout.SOUTH);

    return panel;
}

    // ================= LIVE SEARCH =================

    private void configureLiveSearch() {

        search.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                loadMedicines(search.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                loadMedicines(search.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                loadMedicines(search.getText());
            }
        });
    }

    // ================= TABLE STYLING =================

    private void styleTable() {

        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);

        table.setShowGrid(true);
        table.setGridColor(BORDER);

        table.setSelectionBackground(new Color(255, 235, 175));
        table.setSelectionForeground(BLACK);

        table.getTableHeader().setBackground(BLACK);
        table.getTableHeader().setForeground(GOLD);
        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        table.getTableHeader().setReorderingAllowed(false);
    }

    // ================= BUTTON STYLING =================

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setBackground(color);
        button.setForeground(WHITE);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(new EmptyBorder(10, 15, 10, 15));

        return button;
    }

    // ================= TEXT FIELD STYLING =================

    private void styleTextField(JTextField field) {

        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(280, 36));

        field.setBackground(WHITE);
        field.setForeground(BLACK);
        field.setCaretColor(GOLD);

        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
    }

    // ================= LOAD MEDICINES AND SEARCH =================

    private void loadMedicines(String term) {

        try {

            model.setRowCount(0);

            // Fetch all medicines so the search can check every field.
            List<Medicine> medicines = dao.findAll("");

            String searchTerm = term == null
                    ? ""
                    : term.trim().toLowerCase(Locale.ROOT);

            for (Medicine m : medicines) {

                String status = getMedicineStatus(m);

                String id = String.valueOf(m.getId());
                String name = safeText(m.getName());
                String category = safeText(m.getCategory());
                String quantity = String.valueOf(m.getQuantity());

                boolean matches =
                        searchTerm.isEmpty()
                        || id.contains(searchTerm)
                        || name.toLowerCase(Locale.ROOT).contains(searchTerm)
                        || category.toLowerCase(Locale.ROOT).contains(searchTerm)
                        || quantity.contains(searchTerm)
                        || status.toLowerCase(Locale.ROOT).contains(searchTerm);

                if (matches) {

                    model.addRow(new Object[]{
                            m.getId(),
                            m.getName(),
                            m.getCategory(),
                            m.getPrice(),
                            m.getQuantity(),
                            m.getExpiryDate(),
                            m.getLowStockLevel(),
                            status
                    });
                }
            }

            // Summary cards always show the complete inventory totals.
            updateSummaryCards(medicines);

        } catch (Exception ex) {

            showError("Could not load medicines:\n" + ex.getMessage());
        }
    }

    // ================= MEDICINE STATUS =================

    private String getMedicineStatus(Medicine m) {

        if (m.getExpiryDate() != null
                && m.getExpiryDate().isBefore(LocalDate.now())) {

            return "Expired";
        }

        if (m.getQuantity() <= m.getLowStockLevel()) {
            return "Low Stock";
        }

        return "In Stock";
    }

    // ================= UPDATE SUMMARY CARDS =================

    private void updateSummaryCards(List<Medicine> medicines) {

        int total = medicines.size();
        int lowStock = 0;
        int expired = 0;

        for (Medicine m : medicines) {

            if (m.getExpiryDate() != null
                    && m.getExpiryDate().isBefore(LocalDate.now())) {

                expired++;

            } else if (m.getQuantity() <= m.getLowStockLevel()) {

                lowStock++;
            }
        }

        totalValue.setText(String.valueOf(total));
        lowStockValue.setText(String.valueOf(lowStock));
        expiredValue.setText(String.valueOf(expired));
    }

    private String safeText(String value) {
        return value == null ? "" : value;
    }

    // ================= SELECT MEDICINE =================

    private Medicine selectedMedicine() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine first."
            );
            return null;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        String name = model.getValueAt(row, 1).toString();
        String category = model.getValueAt(row, 2).toString();

        BigDecimal price = new BigDecimal(
                model.getValueAt(row, 3).toString()
        );

        int quantity = Integer.parseInt(
                model.getValueAt(row, 4).toString()
        );

        LocalDate expiryDate = LocalDate.parse(
                model.getValueAt(row, 5).toString()
        );

        int lowStockLevel = Integer.parseInt(
                model.getValueAt(row, 6).toString()
        );

        return new Medicine(
                id,
                name,
                category,
                price,
                quantity,
                expiryDate,
                lowStockLevel
        );
    }

    // ================= EDIT MEDICINE =================

    private void editSelected() {

        Medicine selected = selectedMedicine();

        if (selected != null) {
            showMedicineDialog(selected);
        }
    }

    // ================= DELETE MEDICINE =================

    private void deleteSelected() {

        Medicine selected = selectedMedicine();

        if (selected == null) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete "
                        + selected.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {

            try {

                dao.delete(selected.getId());

                JOptionPane.showMessageDialog(
                        this,
                        "Medicine deleted successfully."
                );

                loadMedicines(search.getText());

            } catch (Exception ex) {

                showError("Could not delete medicine:\n" + ex.getMessage());
            }
        }
    }

    // ================= ADD / EDIT FORM =================

    private void showMedicineDialog(Medicine existing) {

        boolean editing = existing != null;

        JTextField nameField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JTextField expiryField = new JTextField(20);
        JTextField lowStockField = new JTextField(20);

        if (editing) {

            nameField.setText(existing.getName());
            categoryField.setText(existing.getCategory());
            priceField.setText(existing.getPrice().toString());
            quantityField.setText(String.valueOf(existing.getQuantity()));
            expiryField.setText(existing.getExpiryDate().toString());
            lowStockField.setText(
                    String.valueOf(existing.getLowStockLevel())
            );
        }

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));

        form.add(new JLabel("Medicine Name:"));
        form.add(nameField);

        form.add(new JLabel("Category:"));
        form.add(categoryField);

        form.add(new JLabel("Price (KES):"));
        form.add(priceField);

        form.add(new JLabel("Quantity:"));
        form.add(quantityField);

        form.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        form.add(expiryField);

        form.add(new JLabel("Low-stock Level:"));
        form.add(lowStockField);

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                editing ? "Edit Medicine" : "Add Medicine",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty() || category.isEmpty()
                    || priceField.getText().trim().isEmpty()
                    || quantityField.getText().trim().isEmpty()
                    || expiryField.getText().trim().isEmpty()
                    || lowStockField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields."
                );
                return;
            }

            BigDecimal price = new BigDecimal(
                    priceField.getText().trim()
            );

            int quantity = Integer.parseInt(
                    quantityField.getText().trim()
            );

            LocalDate expiryDate = LocalDate.parse(
                    expiryField.getText().trim()
            );

            int lowStockLevel = Integer.parseInt(
                    lowStockField.getText().trim()
            );

            if (price.compareTo(BigDecimal.ZERO) < 0
                    || quantity < 0
                    || lowStockLevel < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Price, quantity and low-stock level cannot be negative."
                );
                return;
            }

            int id = editing ? existing.getId() : 0;

            Medicine medicine = new Medicine(
                    id,
                    name,
                    category,
                    price,
                    quantity,
                    expiryDate,
                    lowStockLevel
            );

            dao.save(medicine);

            JOptionPane.showMessageDialog(
                    this,
                    editing
                            ? "Medicine updated successfully."
                            : "Medicine added successfully."
            );

            loadMedicines(search.getText());

        } catch (NumberFormatException ex) {

            showError(
                    "Invalid number. Check the price, quantity and low-stock level."
            );

        } catch (DateTimeParseException ex) {

            showError(
                    "Invalid expiry date. Use YYYY-MM-DD, for example 2027-12-31."
            );

        } catch (Exception ex) {

            showError(
                    "Could not save medicine:\n" + ex.getMessage()
            );
        }
    }

    // ================= ERROR MESSAGE =================

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Pharmacy Management System",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
