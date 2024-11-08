# Haushaltsbuch

<a name="readme-top"></a>
**Author:** Dino Haskic  
LBS Eibiswald | 2aAPC  
Erstellt am: 08.11.2024

Dieses Projekt ist eine Java-basierte Desktop-Anwendung zur Verwaltung der Haushaltsfinanzen. Es ermöglicht Benutzern, Einnahmen und Ausgaben zu verfolgen, Transaktionen zu kategorisieren und Zusammenfassungen anzuzeigen.

## Funktionen

- **Einträge hinzufügen, bearbeiten und löschen**: Benutzer können neue Einträge hinzufügen, bestehende bearbeiten und einzelne oder mehrere Einträge löschen.
- **Transaktionen kategorisieren**: Transaktionen können zur besseren Organisation kategorisiert werden.
- **Filtern und Suchen**: Benutzer können Einträge filtern und durchsuchen.
- **Finanzen zusammenfassen**: Die Anwendung berechnet und zeigt die Gesamtsumme aller Einträge an.
- **Anpassbare Benutzeroberfläche**: Die Anwendung verfügt über ein anpassbares anthrazitfarbenes Farbschema.

## Nutzung

- **Einträge hinzufügen**: Füllen Sie den Betrag, die Info, die Kategorie und den Typ aus und klicken Sie auf "Speichern".
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20105611.png

- **Einträge bearbeiten**: Doppelklicken Sie auf eine Zelle, um deren Wert zu bearbeiten.
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20105823.png
    
- **Einträge löschen**: Wählen Sie eine oder mehrere Zeilen aus, klicken Sie mit der rechten Maustaste und wählen Sie "Löschen".
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20110107.png

- **Einträge filtern**: Verwenden Sie das Filterfeld unten, um nach bestimmten Einträgen zu suchen.
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20110216.png

## Installation

1. **Repository klonen**:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```

2. **Datenbank einrichten**:
    - Erstellen Sie eine MySQL-Datenbank.
    - Importieren Sie das bereitgestellte SQL-Schema, um die notwendigen Tabellen einzurichten.

3. **Datenbankverbindung konfigurieren**:
    - Aktualisieren Sie die `DatenbankManager`-Klasse mit Ihrer Datenbank-URL, Ihrem Benutzernamen und Ihrem Passwort.

4. **Anwendung ausführen**:
    - Öffnen Sie das Projekt in IntelliJ IDEA.
    - Führen Sie die `main`-Methode in der `HaushaltsBuch`-Klasse aus.

## Abhängigkeiten

- Java Swing für die GUI.
- MySQL für die Datenbank.
- JDBC für die Datenbankverbindung.

## Technische Details

### Datenbank

Die Anwendung verwendet eine MySQL-Datenbank zur Speicherung der Einträge und Kategorien. Die `DatenbankManager`-Klasse verwaltet die Datenbankoperationen, wie das Abrufen, Hinzufügen und Löschen von Einträgen und Kategorien. Beim Start der Anwendung wird sichergestellt, dass die notwendigen Tabellen (`eintraege` und `kategorien`) existieren.

### Benutzeroberfläche

Die Benutzeroberfläche ist mit Java Swing implementiert und besteht aus einer `JTable` zur Anzeige der Einträge, verschiedenen Eingabefeldern und Buttons zur Verwaltung der Einträge und Kategorien. Die Tabelle unterstützt das Sortieren und Filtern der Einträge.

### Hauptfunktionen

- **Einträge hinzufügen**: Benutzer können neue Einträge hinzufügen, indem sie den Betrag, die Info, die Kategorie und den Typ ausfüllen und auf "Speichern" klicken.
- **Einträge bearbeiten**: Doppelklicken Sie auf eine Zelle, um deren Wert zu bearbeiten. Änderungen werden automatisch in der Datenbank gespeichert.
- **Einträge löschen**: Wählen Sie eine oder mehrere Zeilen aus, klicken Sie mit der rechten Maustaste und wählen Sie "Löschen", um die Einträge zu entfernen.
- **Einträge filtern**: Verwenden Sie das Filterfeld unten, um nach bestimmten Einträgen zu suchen. Die Tabelle wird entsprechend gefiltert.

### Code-Details

- **DatenbankManager**: Diese Klasse verwaltet die Verbindung zur MySQL-Datenbank und führt CRUD-Operationen (Create, Read, Update, Delete) für die Tabellen `eintraege` und `kategorien` aus.
- **HaushaltsBuch**: Diese Klasse ist das Hauptfenster der Anwendung und enthält die Benutzeroberfläche sowie die Logik zur Verwaltung der Einträge und Kategorien.

### Beispiel für die `updateDatabase`-Methode

Die `updateDatabase`-Methode aktualisiert einen Eintrag in der Datenbank, wenn die entsprechende Tabellenzeile bearbeitet wird. Sie stellt sicher, dass der Betrag als absoluter Wert in der Datenbank gespeichert wird, unabhängig davon, ob es sich um eine Einnahme oder Ausgabe handelt.

```java
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
        System.err.println("Fehler beim Parsen des Betrags: " + e.getMessage());
        e.printStackTrace();
        return;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDate datum;
    try {
        datum = LocalDate.parse(datumStr, formatter);
    } catch (Exception e) {
        System.err.println("Fehler beim Parsen des Datums: " + e.getMessage());
        e.printStackTrace();
        return;
    }

    String updateSql = "UPDATE eintraege SET bezeichnung=?, betrag=?, datum=?, info=?, kategorie=?, typ=? WHERE id=?";

    try (Connection conn = DriverManager.getConnection(dbManager.getUrl(), dbManager.getUser(), dbManager.getPassword());
         PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

        pstmt.setString(1, bezeichnung);
        pstmt.setDouble(2, Math.abs(betrag)); // Speichern des absoluten Betrags
        pstmt.setDate(3, Date.valueOf(datum));
        pstmt.setString(4, info);
        pstmt.setString(5, bezeichnung);
        pstmt.setString(6, typ);
        pstmt.setInt(7, id);

        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Fehler beim Aktualisieren der Datenbank: " + e.getMessage());
        e.printStackTrace();
    }
}
```

Diese Methode stellt sicher, dass der `betrag`-Wert als absoluter Wert in der Datenbank gespeichert wird, unabhängig davon, ob es sich um eine "Einnahme" oder "Ausgabe" handelt.

## Installation
```cmd
git clone https://github.com/dino-2602/Hangman
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Verwendung
Technologien im Einsatz:  
[![Java][java.com]][java-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>
Verbindung zur Datenbank (optional)
Falls die Anwendung auch Datenbank-Funktionalität benötigt, kann die Verbindung zur Datenbank mit Main.java erfolgen, wobei die Klassen Connection, DriverManager, ResultSet, SQLException und Statement genutzt werden. Dies ist jedoch optional für die Basisfunktion des Hangman-Spiels.

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[java.com]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[java-url]: https://www.java.com/de/
[product-screenshot]: Screen.png
