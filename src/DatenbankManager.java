import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatenbankManager {

    /*
     * URL, Benutzername und Passwort für die Verbindung zur MySQL-Datenbank.
     * Diese Werte werden verwendet, um eine Verbindung zur Datenbank herzustellen,
     * in der die Haushaltskosten verwaltet werden.
     */
    private static final String URL = "jdbc:mysql://localhost:3306/haushaltskosten";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /*
     * Konstruktor für den DatenbankManager.
     * Der Konstruktor stellt sicher, dass die benötigten Tabellen "eintraege" und "kategorien" existieren,
     * indem er diese erstellt, falls sie nicht vorhanden sind.
     */
    public DatenbankManager() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (conn != null) {

                // SQL-Befehl zum Erstellen der Tabelle "eintraege", falls diese nicht existiert.
                String createEintraegeTableSQL = "CREATE TABLE IF NOT EXISTS eintraege ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "bezeichnung VARCHAR(255) NOT NULL, "
                        + "betrag DECIMAL(10, 2) NOT NULL, "
                        + "kategorie VARCHAR(255) NOT NULL, "
                        + "typ VARCHAR(50) NOT NULL, "
                        + "datum DATE NOT NULL, "
                        + "info TEXT"
                        + ");";
                Statement stmt = conn.createStatement();
                stmt.execute(createEintraegeTableSQL);

                // SQL-Befehl zum Erstellen der Tabelle "kategorien", falls diese nicht existiert.
                String createKategorienTableSQL = "CREATE TABLE IF NOT EXISTS kategorien ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "name VARCHAR(255) NOT NULL"
                        + ");";
                stmt.execute(createKategorienTableSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Getter-Methoden zum Abrufen der URL, des Benutzernamens und des Passworts.
     * Diese Methoden werden verwendet, um Datenbankinformationen für Verbindungen zu erhalten.
     */
    public String getUrl() {
        return URL;
    }

    public String getUser() {
        return USER;
    }

    public String getPassword() {
        return PASSWORD;
    }

    /*
     * Methode zum Hinzufügen eines neuen Eintrags zur Tabelle "eintraege".
     * Nimmt einen Eintrag als Parameter, öffnet eine Verbindung zur Datenbank und führt ein
     * PreparedStatement aus, um die Eintragsdaten in die Tabelle einzufügen.
     */
    public void addEintrag(Eintrag eintrag) {
        String sql = "INSERT INTO eintraege(bezeichnung, betrag, kategorie, typ, datum, info) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, eintrag.getBezeichnung());
            pstmt.setDouble(2, eintrag.getBetrag());
            pstmt.setString(3, eintrag.getKategorie().getName());
            pstmt.setString(4, eintrag.getTyp());
            pstmt.setDate(5, Date.valueOf(eintrag.getDatum()));
            pstmt.setString(6, eintrag.getInfo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Methode zum Abrufen aller Einträge aus der Tabelle "eintraege".
     * Führt eine SQL-Abfrage aus, um alle vorhandenen Einträge zu holen, und speichert diese
     * in einer Liste, die anschließend zurückgegeben wird.
     */
    public List<Eintrag> getAllEintraege() {
        List<Eintrag> eintraege = new ArrayList<>();
        String sql = "SELECT * FROM eintraege";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String bezeichnung = rs.getString("bezeichnung");
                double betrag = rs.getDouble("betrag");
                String kategorieName = rs.getString("kategorie");
                String typ = rs.getString("typ");
                LocalDate datum = rs.getDate("datum").toLocalDate();
                String info = rs.getString("info");
                Kategorie kategorie = new Kategorie(kategorieName);
                Eintrag eintrag = new Eintrag(id, bezeichnung, betrag, datum, info, kategorie, typ);
                eintraege.add(eintrag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eintraege;
    }

    /*
     * Methode zum Löschen aller Einträge, die am heutigen Tag erstellt wurden.
     * Diese Methode verwendet den SQL-Befehl "DELETE", um alle Einträge zu löschen,
     * deren Datum dem aktuellen Datum entspricht.
     */
    public void deleteTodayEntries() {
        String sql = "DELETE FROM eintraege WHERE datum = CURDATE()";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Methode zum Hinzufügen einer neuen Kategorie zur Tabelle "kategorien".
     * Diese Methode verwendet ein PreparedStatement, um die neue Kategorie in die Datenbank einzufügen.
     */
    public void addKategorie(Kategorie kategorie) {
        String sql = "INSERT INTO kategorien(name) VALUES(?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kategorie.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Methode zum Löschen einer bestimmten Kategorie aus der Tabelle "kategorien".
     * Diese Methode verwendet den Namen der Kategorie als Kriterium, um den entsprechenden Eintrag zu löschen.
     */
    public void deleteKategorie(String name) {
        String sql = "DELETE FROM kategorien WHERE name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Methode zum Abrufen aller Kategorien aus der Tabelle "kategorien".
     * Führt eine SQL-Abfrage aus, um alle Kategorien aus der Datenbank zu holen und gibt sie in einer Liste zurück.
     */
    public List<Kategorie> getAllKategorien() {
        List<Kategorie> kategorien = new ArrayList<>();
        String sql = "SELECT * FROM kategorien";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                Kategorie kategorie = new Kategorie(name);
                kategorien.add(kategorie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kategorien;
    }
}
