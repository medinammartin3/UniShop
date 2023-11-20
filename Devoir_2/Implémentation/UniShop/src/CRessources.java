import java.util.StringJoiner;

public class CRessources extends Categorie{

    final static String[] types = new String[] {"Ressource papier", "Ressource en ligne"};

    private final String type;
    private final long isbn;
    private final String auteur;
    private final String organisation;
    private final String date;
    private final int numEdition;

    public CRessources(String auteur, String organisation, String type, long isbn, String date, int numEdition) {
        this.isbn = isbn;
        this.auteur = auteur;
        this.organisation = organisation;
        this.date = date;
        this.type = type;
        this.numEdition = numEdition;
    }
    @Override
    public short getCatID() {
        return 1;
    }

    @Override
    public String getFormatSauvegarde() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(getCatID()));
        sj.add(auteur);
        sj.add(organisation);
        sj.add(type);
        sj.add(String.valueOf(isbn));
        sj.add(date);
        sj.add(String.valueOf(numEdition));
        return sj.toString();
    }

    @Override
    public String getFormatDisplay() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Auteur: " + auteur);
        sj.add("Organisation: " + organisation);
        sj.add("Type: " + type);
        sj.add("ISBN: " + isbn);
        sj.add("Date de parution: " + date);
        sj.add("Numéro d'édition: " + numEdition);
        return sj.toString();
    }
}
