/*
 * Die Klasse "Kategorie" repräsentiert eine Kategorie, die in der Haushaltsbuch-Anwendung verwendet wird.
 * Jede Kategorie hat einen Namen, der durch das Attribut "name" repräsentiert wird.
 */
public class Kategorie {

    // Attribut zur Speicherung des Namens der Kategorie
    private final String name;

    /*
     * Konstruktor der Klasse "Kategorie".
     * Der Konstruktor nimmt den Namen der Kategorie als Parameter und initialisiert das Attribut "name".
     *
     * @param name - Der Name der Kategorie, die erstellt werden soll.
     */
    public Kategorie(String name) {
        this.name = name;
    }

    /*
     * Getter-Methode, um den Namen der Kategorie zu erhalten.
     * Diese Methode wird verwendet, um auf den Namen der Kategorie zuzugreifen.
     *
     * @return name - Der Name der Kategorie.
     */
    public String getName() {
        return name;
    }
}
