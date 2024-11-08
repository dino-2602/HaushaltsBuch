import java.time.LocalDate;

public class Eintrag {
    /*
     * ID des Eintrags, die eindeutige Identifizierung ermöglicht.
     * Diese ID wird in der Datenbank als Primärschlüssel verwendet.
     */
    private final int id;

    /*
     * Beschreibung des Eintrags, die angibt, worum es sich bei der Transaktion handelt.
     * Zum Beispiel: "Lebensmitteleinkauf" oder "Gehaltseingang".
     */
    private final String bezeichnung;

    /*
     * Betrag der Transaktion, kann sowohl positiv als auch negativ sein.
     * Einnahmen sind positive Werte und Ausgaben sind negative Werte.
     */
    private final double betrag;

    /*
     * Datum der Transaktion, gibt an, wann der Eintrag erfolgt ist.
     * Wird als LocalDate gespeichert, um Jahr, Monat und Tag zu erfassen.
     */
    private final LocalDate datum;

    /*
     * Zusätzliche Informationen zum Eintrag, wie beispielsweise Notizen.
     * Kann verwendet werden, um den Eintrag genauer zu beschreiben.
     */
    private final String info;

    /*
     * Kategorie des Eintrags, die angibt, zu welcher Kategorie die Transaktion gehört.
     * Zum Beispiel: "Lebensmittel", "Miete" oder "Freizeit".
     */
    private final Kategorie kategorie;

    /*
     * Typ der Transaktion, gibt an, ob es sich um eine "Einnahme" oder "Ausgabe" handelt.
     */
    private final String typ;


    // Konstruktor der Klasse Eintrag, der alle Eigenschaften initialisiert.
    public Eintrag(int id, String bezeichnung, double betrag, LocalDate datum, String info, Kategorie kategorie, String typ) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.betrag = betrag;
        this.datum = datum;
        this.info = info;
        this.kategorie = kategorie;
        this.typ = typ;
    }

    /*
     * Gibt die ID des Eintrags zurück.
     *
     * @return Die ID des Eintrags
     */
    public int getId() {
        return id;
    }

    /*
     * Gibt die Bezeichnung des Eintrags zurück.
     *
     * @return Die Beschreibung des Eintrags
     */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /*
     * Gibt den Betrag des Eintrags zurück.
     *
     * @return Der Betrag der Transaktion
     */
    public double getBetrag() {
        return betrag;
    }

    /*
     * Gibt das Datum des Eintrags zurück.
     *
     * @return Das Datum der Transaktion
     */
    public LocalDate getDatum() {
        return datum;
    }

    /*
     * Gibt zusätzliche Informationen zum Eintrag zurück.
     *
     * @return Die zusätzlichen Informationen zur Transaktion
     */
    public String getInfo() {
        return info;
    }

    /*
     * Gibt die Kategorie des Eintrags zurück.
     *
     * @return Die Kategorie der Transaktion
     */
    public Kategorie getKategorie() {
        return kategorie;
    }

    /*
     * Gibt den Typ des Eintrags zurück (Einnahme oder Ausgabe).
     *
     * @return Der Typ der Transaktion
     */
    public String getTyp() {
        return typ;
    }
}
