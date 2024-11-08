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

## Nutzung

- **Einträge hinzufügen**: Füllen Sie den Betrag, die Info, die Kategorie und den Typ aus und klicken Sie auf "Speichern".
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20105611.png

- **Einträge bearbeiten**: Doppelklicken Sie auf eine Zelle, um deren Wert zu bearbeiten.
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20105823.png
    
- **Einträge löschen**: Wählen Sie eine oder mehrere Zeilen aus, klicken Sie mit der rechten Maustaste und wählen Sie "Löschen".
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20110107.png

- **Einträge filtern**: Verwenden Sie das Filterfeld unten, um nach bestimmten Einträgen zu suchen.
    https://github.com/dino-2602/HaushaltsBuch/blob/master/screenshots/Screenshot%202024-11-08%20110216.png

## Abhängigkeiten

- Java Swing für die GUI.
- MySQL für die Datenbank.
- JDBC für die Datenbankverbindung.

## Beitrag leisten

1. Forken Sie das Repository.
2. Erstellen Sie einen neuen Branch (`git checkout -b feature-branch`).
3. Committen Sie Ihre Änderungen (`git commit -m 'Neue Funktion hinzufügen'`).
4. Pushen Sie den Branch (`git push origin feature-branch`).
5. Öffnen Sie eine Pull-Request.

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert. Siehe die `LICENSE`-Datei für Details.

## Danksagungen

- Vielen Dank an alle Mitwirkenden und Open-Source-Bibliotheken, die in diesem Projekt verwendet wurden.
