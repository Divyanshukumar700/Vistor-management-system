import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/** Main dashboard: add visitors, view list, checkout. */
public class DashboardFrame extends JFrame {
    private VisitorDAO dao = new VisitorDAO();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nameField, phoneField, whomField, purposeField;
    private JLabel totalLbl, inLbl, outLbl;

    public DashboardFrame(String guardName) {
        setTitle("Dashboard - Guard: " + guardName);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        // ---- Stats panel ----
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        totalLbl = makeStat("Total: 0");
        inLbl = makeStat("Checked In: 0");
        outLbl = makeStat("Checked Out: 0");
        statsPanel.add(totalLbl);
        statsPanel.add(inLbl);
        statsPanel.add(outLbl);
        add(statsPanel, BorderLayout.NORTH);

        // ---- Add visitor form ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Visitor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(12);
        phoneField = new JTextField(12);
        whomField = new JTextField(12);
        purposeField = new JTextField(12);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3; formPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Whom to Visit:"), gbc);
        gbc.gridx = 1; formPanel.add(whomField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Purpose:"), gbc);
        gbc.gridx = 3; formPanel.add(purposeField, gbc);

        JButton addBtn = new JButton("Check In Visitor");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        formPanel.add(addBtn, gbc);

        // ---- Table ----
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Phone", "Whom to Visit", "Purpose", "Check-In", "Check-Out", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Visitor Log"));

        // ---- Bottom buttons ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshBtn = new JButton("Refresh");
        JButton checkoutBtn = new JButton("Checkout Selected");
        JButton logoutBtn = new JButton("Logout");
        bottomPanel.add(refreshBtn);
        bottomPanel.add(checkoutBtn);
        bottomPanel.add(logoutBtn);

        // ---- Center container ----
        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.add(formPanel, BorderLayout.NORTH);
        center.add(tableScroll, BorderLayout.CENTER);
        center.add(bottomPanel, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);

        // ---- Listeners ----
        addBtn.addActionListener(e -> addVisitor());
        refreshBtn.addActionListener(e -> loadVisitors());
        checkoutBtn.addActionListener(e -> checkoutSelected());
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        loadVisitors();
    }

    private JLabel makeStat(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        l.setOpaque(true);
        l.setBackground(new Color(240, 240, 250));
        return l;
    }

    private void addVisitor() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String whom = whomField.getText().trim();
        String purpose = purposeField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || whom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, Phone and Whom to Visit are required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            dao.addVisitor(name, phone, whom, purpose);
            JOptionPane.showMessageDialog(this, "Visitor checked in successfully.");
            nameField.setText(""); phoneField.setText("");
            whomField.setText(""); purposeField.setText("");
            loadVisitors();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadVisitors() {
        try {
            List<Visitor> visitors = dao.getAllVisitors();
            tableModel.setRowCount(0);
            int in = 0, out = 0;
            for (Visitor v : visitors) {
                tableModel.addRow(new Object[]{
                        v.getId(), v.getName(), v.getPhone(), v.getWhomToVisit(), v.getPurpose(),
                        v.getCheckInTime(), v.getCheckOutTime() == null ? "-" : v.getCheckOutTime(),
                        v.getStatus()
                });
                if ("checked-in".equals(v.getStatus())) in++; else out++;
            }
            totalLbl.setText("Total: " + visitors.size());
            inLbl.setText("Checked In: " + in);
            outLbl.setText("Checked Out: " + out);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading visitors: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkoutSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a visitor row.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 7);
        if ("checked-out".equals(status)) {
            JOptionPane.showMessageDialog(this, "Visitor already checked out.");
            return;
        }
        try {
            dao.checkoutVisitor(id);
            JOptionPane.showMessageDialog(this, "Visitor checked out.");
            loadVisitors();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
