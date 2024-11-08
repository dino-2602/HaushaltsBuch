import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KategorieAuswahl extends JDialog {
    /*
     * JTextField zur Eingabe des Namens der neuen Kategorie.
     * Der Name darf maximal 25 Zeichen lang sein.
     */
    private final JTextField nameField;
    /*
     * Speichert die neu erstellte Kategorie, nachdem der Benutzer sie bestätigt hat.
     */
    private Kategorie neueKategorie;

    /*
     * Konstruktor zur Erstellung des Kategorie-Auswahl-Dialogs.
     * Setzt das Layout des Fensters und fügt die notwendigen Komponenten hinzu.
     * @param parent Das übergeordnete Fenster, von dem dieser Dialog abhängt.
     */
    public KategorieAuswahl(Frame parent) {
        super(parent, "Neue Kategorie erstellen", true);
        setLayout(new GridLayout(2, 2));

        // Label für das Eingabefeld der Kategoriebezeichnung hinzufügen
        add(new JLabel("Name (max 25 Zeichen):"));
        // Textfeld für die Eingabe des Kategorienamen
        nameField = new JTextField();
        add(nameField);

        // OK-Button, um die neue Kategorie zu erstellen
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> createCategory());
        add(okButton);

        // Abbrechen-Button, um den Dialog zu schließen, ohne etwas zu speichern
        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        /*
         * Fügt einen KeyListener zum Textfeld hinzu, um die Kategorie durch Drücken der Eingabetaste zu erstellen.
         * Der Benutzer kann die Eingabetaste (Enter) drücken, um den Kategorienamen zu bestätigen.
         */
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    createCategory();
                }
            }
        });

        pack(); // Passt die Größe des Dialogs an, um alle Komponenten sichtbar zu machen
        setLocationRelativeTo(parent); // Positioniert den Dialog relativ zum übergeordneten Fenster
    }

    /*
     * Methode zum Erstellen einer neuen Kategorie basierend auf der Benutzereingabe.
     * Überprüft die Länge des Namens und zeigt eine Warnung an, wenn der Name zu lang ist.
     */
    private void createCategory() {
        String name = nameField.getText();
        if (name.length() > 25) {
            JOptionPane.showMessageDialog(this, "Der Kategoriename darf maximal 25 Zeichen lang sein.", "Ungültige Eingabe", JOptionPane.WARNING_MESSAGE);
            return;
        }
        neueKategorie = new Kategorie(name); // Speichert die neue Kategorie, wenn der Name gültig ist
        dispose(); // Schließt den Dialog
    }

    /*
     * Getter-Methode, um die neu erstellte Kategorie abzurufen.
     * @return Die neu erstellte Kategorie oder null, wenn keine Kategorie erstellt wurde.
     */
    public Kategorie getNeueKategorie() {
        return neueKategorie;
    }
}
