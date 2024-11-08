import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class HaushaltsBuch extends JFrame {

    /*
     * DefaultTableModel verwaltet die Daten für die JTable, einschließlich Spaltennamen und Zeilen mit Daten.
     * Es ermöglicht das dynamische Hinzufügen, Bearbeiten und Löschen von Datenzeilen in der Tabelle.
     */
    private final DefaultTableModel tableModel;

    /*
     * TableRowSorter wird verwendet, um die Sortierfunktionalität für die Tabelle zu aktivieren.
     * Ermöglicht es dem Benutzer, die Tabellenzeilen nach Spalten zu sortieren.
     */
    private final TableRowSorter<DefaultTableModel> sorter;

    // DatenbankManager verwaltet die Datenbankoperationen, wie das Abrufen, Hinzufügen und Löschen von Einträgen.
    private final DatenbankManager dbManager = new DatenbankManager();

    // Textfelder und andere UI-Komponenten zum Eingeben und Anzeigen von Daten.
    private final JTextField betragField;
    private final JTextArea infoField;
    private final JComboBox<String> kategorieBox;
    private final JComboBox<String> typBox;
    private final JLabel sumLabel;
    private final JTextField filterField;
    private final JComboBox<String> filterColumnBox;

    public HaushaltsBuch() {

        /*
         * Einrichtung des Hauptfensters der Haushaltsbuch-Anwendung.
         * Setzt die Größe, den Titel und das Layout des Fensters.
         */
        setTitle("Haushaltsbuch");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

         //Erstellen einer Menüleiste mit Optionen zum Löschen aller Einträge oder Kategorien.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Optionen");
        JMenuItem deleteAllItem = new JMenuItem("Alle Einträge löschen");
        deleteAllItem.setForeground(Color.RED);
        deleteAllItem.addActionListener(e -> deleteAllEntries());
        menu.add(deleteAllItem);

        JMenuItem deleteAllCategoriesItem = new JMenuItem("Alle Kategorien löschen");
        deleteAllCategoriesItem.setForeground(Color.RED);
        deleteAllCategoriesItem.addActionListener(e -> deleteAllCategories());
        menu.add(deleteAllCategoriesItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        /*
         * Definieren der Spalten für die Tabelle und Einrichten der JTable zur Anzeige der Einträge.
         */
        String[] columns = {"ID", "Bezeichnung", "Einnahmen", "Ausgaben", "Datum", "Info"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Anpassung der Spaltenbreite zur besseren Darstellung der Tabelle
        table.getColumnModel().getColumn(0).setPreferredWidth(25);
        table.getColumnModel().getColumn(5).setPreferredWidth(250);

        /*
         * Einrichten der Spaltenausrichtung für verschiedene Datentypen.
         * Die Spalten mit numerischen Werten werden rechtsbündig dargestellt, während andere links- oder zentriert ausgerichtet werden.
         */
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Anwenden der Renderer auf die entsprechenden Spalten
        table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer); // ID-Spalte
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Bezeichnung-Spalte
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer); // Einnahmen-Spalte
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Ausgaben-Spalte
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Datum-Spalte
        table.getColumnModel().getColumn(5).setCellRenderer(leftRenderer); // Info-Spalte

        /*
         * Sicherstellen, dass der Renderer auch für bearbeitete Zellen verwendet wird.
         * Dies betrifft das Layout beim Bearbeiten von Zellen.
         */
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                editor.setHorizontalAlignment(SwingConstants.RIGHT);
                return editor;
            }
        });

        // Ähnliche Konfiguration für alle anderen Spalten
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                editor.setHorizontalAlignment(SwingConstants.CENTER);
                return editor;
            }
        });

        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                editor.setHorizontalAlignment(SwingConstants.RIGHT);
                return editor;
            }
        });

        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                editor.setHorizontalAlignment(SwingConstants.RIGHT);
                return editor;
            }
        });

        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                editor.setHorizontalAlignment(SwingConstants.CENTER);
                return editor;
            }
        });

        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                editor.setHorizontalAlignment(SwingConstants.LEFT);
                return editor;
            }
        });

        /*
         * Hinzufügen eines MouseListeners zur Tabelle für das Kontextmenü bei Rechtsklick.
         * Ermöglicht es dem Benutzer, durch Rechtsklick bestimmte Aktionen wie das Löschen von Zeilen auszuführen.
         */
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int[] selectedRows = table.getSelectedRows();
                    if (selectedRows.length > 0) {
                        showContextMenu(e, selectedRows);
                    }
                }
            }
        });

        /*
         * Erstellung eines Panels für die Benutzereingabe auf der rechten Seite des Fensters.
         * Das Panel enthält Textfelder, Labels und Buttons für die Dateneingabe.
         */
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        betragField = new JTextField(15);
        infoField = new JTextArea(3, 15);
        infoField.setLineWrap(true);
        infoField.setWrapStyleWord(true);

        JLabel charLimitLabel = new JLabel("Maximal 50 Zeichen");
        charLimitLabel.setFont(new Font(charLimitLabel.getFont().getName(), Font.PLAIN, 10));
        charLimitLabel.setForeground(Color.GRAY);

        kategorieBox = new JComboBox<>();
        loadKategorien(); // Lädt Kategorien aus der Datenbank und fügt sie der ComboBox hinzu

        typBox = new JComboBox<>(new String[]{"Einnahme", "Ausgabe"}); // Definiert die Transaktionsarten

        /*
         * Erstellen von Buttons für verschiedene Aktionen im Panel.
         * Diese Buttons ermöglichen es dem Benutzer, Einträge zu speichern, Kategorien zu verwalten und Eingabefelder zu leeren.
         */
        JButton addButton = new JButton("Speichern");
        JButton deleteButton = new JButton("Heute löschen");
        JButton newCategoryButton = new JButton("Neue Kategorie");
        JButton deleteCategoryButton = new JButton("Kategorie löschen");
        JButton clearButton = new JButton("Felder leeren");

        // Event-Listener zum Hinzufügen einer neuen Kategorie
        newCategoryButton.addActionListener(e -> {
            KategorieAuswahl dialog = new KategorieAuswahl(this);
            dialog.setVisible(true);
            Kategorie neueKategorie = dialog.getNeueKategorie();
            if (neueKategorie != null) {
                dbManager.addKategorie(neueKategorie);
                kategorieBox.addItem(neueKategorie.getName());
            }
        });

        // Event-Listener zum Löschen einer ausgewählten Kategorie
        deleteCategoryButton.addActionListener(e -> {
            String selectedCategory = (String) kategorieBox.getSelectedItem();
            if (selectedCategory != null) {
                dbManager.deleteKategorie(selectedCategory);
                kategorieBox.removeItem(selectedCategory);
            }
        });

        // Hinzufügen aller Komponenten zum Panel mit den entsprechenden Layout-Einschränkungen
        rightPanel.add(new JLabel("Betrag:"), gbc);
        gbc.gridy++;
        rightPanel.add(betragField, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Info:"), gbc);
        gbc.gridy++;
        rightPanel.add(new JScrollPane(infoField), gbc);
        gbc.gridy++;
        rightPanel.add(charLimitLabel, gbc);

        gbc.gridy++;
        rightPanel.add(clearButton, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Kategorie:"), gbc);
        gbc.gridy++;
        rightPanel.add(kategorieBox, gbc);
        gbc.gridy++;
        rightPanel.add(newCategoryButton, gbc);
        gbc.gridy++;
        rightPanel.add(deleteCategoryButton, gbc);

        gbc.gridy++;
        rightPanel.add(new JLabel("Typ:"), gbc);
        gbc.gridy++;
        rightPanel.add(typBox, gbc);

        gbc.gridy++;
        rightPanel.add(addButton, gbc);
        gbc.gridy++;
        rightPanel.add(deleteButton, gbc);

        sumLabel = new JLabel("Summe: 0,00 €");
        gbc.gridy++;
        rightPanel.add(sumLabel, gbc);

        add(rightPanel, BorderLayout.EAST);

        /*
         * Event-Listener zum Speichern eines Eintrags, zum Löschen der heutigen Einträge und zum Leeren der Felder.
         * Diese Aktionen werden durch die entsprechenden Buttons ausgelöst.
         */
        addButton.addActionListener(e -> saveEntry());
        deleteButton.addActionListener(e -> deleteTodayEntries());
        clearButton.addActionListener(e -> clearFields());

        /*
         * Aktualisieren der Datenbank, wenn die Tabelle geändert wird, und Berechnen der neuen Summe.
         * Diese Funktionalität stellt sicher, dass Änderungen direkt in der Datenbank gespeichert werden.
         */
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column != 0) {
                    updateDatabase(row);
                }
            }
            updateSum();
        });

        loadEntries(); // Lädt bestehende Einträge aus der Datenbank in die Tabelle

        /*
         * Erstellen eines Filterpanels zur Suche von Einträgen.
         * Das Panel enthält ein Textfeld und eine ComboBox, um nach bestimmten Einträgen zu filtern.
         */
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterField = new JTextField(15);
        filterPanel.add(filterField);
        filterColumnBox = new JComboBox<>(columns);
        filterPanel.add(filterColumnBox);
        add(filterPanel, BorderLayout.SOUTH);

        // Listener hinzufügen, um den Filter anzuwenden, wenn sich der Text im Filterfeld ändert
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });
    }

    /*
     * Zeigt das Kontextmenü für die ausgewählten Zeilen an.
     * Ermöglicht das Löschen der ausgewählten Zeilen durch einen Rechtsklick.
     */
    private void showContextMenu(MouseEvent e, int[] rows) {
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Löschen");
        deleteItem.addActionListener(event -> deleteRows(rows));
        contextMenu.add(deleteItem);
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /*
     * Löscht die ausgewählten Zeilen aus der Datenbank und aktualisiert die Tabelle.
     * Die Änderungen werden auch in der Benutzeroberfläche sichtbar gemacht.
     */
    private void deleteRows(int[] rows) {
        try (Connection conn = DriverManager.getConnection(dbManager.getUrl(), dbManager.getUser(), dbManager.getPassword())) {
            conn.setAutoCommit(false);
            String deleteSql = "DELETE FROM eintraege WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                for (int row : rows) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    pstmt.setInt(1, id);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            conn.commit();
            for (int i = rows.length - 1; i >= 0; i--) {
                tableModel.removeRow(rows[i]);
            }
            updateSum();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Wendet einen Filter auf die Tabelle basierend auf der Benutzereingabe an.
     * Ermöglicht es dem Benutzer, die angezeigten Einträge zu durchsuchen und zu filtern.
     */
    private void applyFilter() {
        String text = filterField.getText();
        int columnIndex = filterColumnBox.getSelectedIndex();
        if (text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, columnIndex));
        }
    }

    /*
     * Lädt die Kategorien aus der Datenbank in die ComboBox.
     * Die Kategorien werden alphabetisch sortiert und der ComboBox hinzugefügt.
     */
    private void loadKategorien() {
        List<Kategorie> kategorien = dbManager.getAllKategorien();
        kategorien.sort(Comparator.comparing(Kategorie::getName));
        kategorieBox.removeAllItems();
        for (Kategorie kategorie : kategorien) {
            kategorieBox.addItem(kategorie.getName());
        }
    }

    /*
     * Speichert einen neuen Eintrag in der Datenbank und aktualisiert die Tabelle.
     * Stellt sicher, dass alle notwendigen Felder korrekt ausgefüllt sind, bevor der Eintrag gespeichert wird.
     */
    private void saveEntry() {
        if (infoField.getText().length() > 50) {
            JOptionPane.showMessageDialog(this, "Info darf maximal 50 Zeichen enthalten.", "Ungültige Eingabe", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String betragText = betragField.getText().trim().replace('.', ',');
            NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
            Number number = format.parse(betragText);
            double betrag = number.doubleValue();

            if (betrag <= 0) {
                JOptionPane.showMessageDialog(this, "Bitte geben Sie einen Betrag größer als 0 ein.", "Ungültiger Betrag", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String kategorieName = (String) kategorieBox.getSelectedItem();
            String typ = (String) typBox.getSelectedItem();

            Kategorie kategorie = new Kategorie(kategorieName);

            Eintrag eintrag = new Eintrag(0, kategorieName, betrag, LocalDate.now(), infoField.getText().trim(), kategorie, typ);
            dbManager.addEintrag(eintrag);
            loadEntries();

            betragField.setText("");
            infoField.setText("");
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Bitte geben Sie eine gültige Zahl im Format '0,00' ein.", "Ungültiger Betrag", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Löscht alle Einträge, die das aktuelle Datum aufweisen, aus der Datenbank.
     * Fordert den Benutzer zur Bestätigung der Löschung auf.
     */
    private void deleteTodayEntries() {
        int response = JOptionPane.showConfirmDialog(this, "Möchten Sie wirklich alle Einträge des heutigen Tages löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            dbManager.deleteTodayEntries();
            loadEntries();
        }
    }

    /*
     * Leert die Eingabefelder für einen neuen Eintrag.
     * Setzt die Textfelder für Betrag und Info auf leeren Inhalt zurück.
     */
    private void clearFields() {
        betragField.setText("");
        infoField.setText("");
    }

    /*
     * Lädt alle Einträge aus der Datenbank in die Tabelle und aktualisiert die Summe.
     * Jeder Eintrag wird in der Tabelle angezeigt und die Summe aller Einträge wird berechnet.
     */
    private void loadEntries() {
        List<Eintrag> eintraege = dbManager.getAllEintraege();
        tableModel.setRowCount(0);
        double sum = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (Eintrag eintrag : eintraege) {
            tableModel.addRow(new Object[]{
                    eintrag.getId(),
                    eintrag.getBezeichnung(),
                    eintrag.getTyp().equals("Einnahme") ? String.format(Locale.GERMANY, "%.2f", eintrag.getBetrag()) : "",
                    eintrag.getTyp().equals("Ausgabe") ? String.format(Locale.GERMANY, "%.2f", Math.abs(eintrag.getBetrag())) : "",
                    eintrag.getDatum().format(formatter),
                    eintrag.getInfo()
            });
            sum += eintrag.getTyp().equals("Einnahme") ? eintrag.getBetrag() : -eintrag.getBetrag();
        }
        sumLabel.setText(String.format(Locale.GERMANY, "Summe: %.2f €", sum));
    }

    /*
     * Berechnet die aktuelle Summe aller Einträge in der Tabelle.
     * Durchläuft alle Zeilen und addiert Einnahmen, während Ausgaben subtrahiert werden.
     */
    private void updateSum() {
        double sum = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String einnahme = (String) tableModel.getValueAt(i, 2);
            String ausgabe = (String) tableModel.getValueAt(i, 3);
            if (einnahme != null && !einnahme.isEmpty()) {
                sum += Double.parseDouble(einnahme.replace(',', '.'));
            }
            if (ausgabe != null && !ausgabe.isEmpty()) {
                sum -= Double.parseDouble(ausgabe.replace(',', '.'));
            }
        }
        sumLabel.setText(String.format("Summe: %.2f €", sum));
    }

    /*
     * Aktualisiert einen Eintrag in der Datenbank, wenn die entsprechende Tabellenzeile bearbeitet wird.
     * Stellt sicher, dass die Änderungen auch in der Datenbank gespeichert werden.
     */
    private void updateDatabase(int row) {
        int id = (Integer) tableModel.getValueAt(row, 0);
        String bezeichnung = (String) tableModel.getValueAt(row, 1);
        String einnahmeStr = (String) tableModel.getValueAt(row, 2);
        String ausgabeStr = (String) tableModel.getValueAt(row, 3);
        String datumStr = (String) tableModel.getValueAt(row, 4);
        String info = (String) tableModel.getValueAt(row, 5);

        double betrag;
        String typ;
        try {
            if (einnahmeStr != null && !einnahmeStr.isEmpty()) {
                betrag = Double.parseDouble(einnahmeStr.replace(',', '.'));
                typ = "Einnahme";
            } else if (ausgabeStr != null && !ausgabeStr.isEmpty()) {
                betrag = Double.parseDouble(ausgabeStr.replace(',', '.'));
                typ = "Ausgabe";
            } else {
                System.err.println("Kein gültiger Betrag angegeben.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing betrag: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate datum;
        try {
            datum = LocalDate.parse(datumStr, formatter);
        } catch (Exception e) {
            System.err.println("Error parsing datum: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        String updateSql = "UPDATE eintraege SET bezeichnung=?, betrag=?, datum=?, info=?, kategorie=?, typ=? WHERE id=?";

        try (Connection conn = DriverManager.getConnection(dbManager.getUrl(), dbManager.getUser(), dbManager.getPassword());
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            pstmt.setString(1, bezeichnung);
            pstmt.setDouble(2, Math.abs(betrag));
            pstmt.setDate(3, Date.valueOf(datum));
            pstmt.setString(4, info);
            pstmt.setString(5, bezeichnung);
            pstmt.setString(6, typ);
            pstmt.setInt(7, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Löscht alle Einträge aus der Tabelle und Datenbank.
     * Fordert den Benutzer zur Bestätigung der Löschung auf.
     */
    public void deleteAllEntries() {
        int response = JOptionPane.showConfirmDialog(null, "Möchten Sie wirklich alle Einträge unwiderruflich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            String deleteSql = "DELETE FROM eintraege";
            String resetSql = "ALTER TABLE eintraege AUTO_INCREMENT = 1";
            try (Connection conn = DriverManager.getConnection(dbManager.getUrl(), dbManager.getUser(), dbManager.getPassword());
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteSql);
                stmt.executeUpdate(resetSql);
                loadEntries();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Löscht alle Kategorien aus der Datenbank und aktualisiert die ComboBox.
     * Fordert den Benutzer zur Bestätigung der Löschung auf.
     */
    public void deleteAllCategories() {
        int response = JOptionPane.showConfirmDialog(null, "Möchten Sie wirklich alle Kategorien unwiderruflich löschen?", "Bestätigung", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            String deleteSql = "DELETE FROM kategorien";
            String resetSql = "ALTER TABLE kategorien AUTO_INCREMENT = 1";
            try (Connection conn = DriverManager.getConnection(dbManager.getUrl(), dbManager.getUser(), dbManager.getPassword());
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteSql);
                stmt.executeUpdate(resetSql);
                loadKategorien();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * Hauptmethode zum Ausführen der Haushaltsbuch-Anwendung.
     * Erstellt eine Instanz des HaushaltsBuch-Fensters und macht es sichtbar.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HaushaltsBuch app = new HaushaltsBuch();
            app.setVisible(true);
        });
    }
}
