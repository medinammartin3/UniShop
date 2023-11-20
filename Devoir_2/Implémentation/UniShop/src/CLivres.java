import java.util.StringJoiner;

public class CLivres extends Categorie{

    final static String[] genres = new String[] {"Roman", "Documentaire", "Bande dessinée",
    "Manuel", "Autre"};

    private final String auteur;
    private final String maison;
    private final String genre;
    private final long isbn;
    private final String date;
    private final int numEdition;
    private final int numVolume;

    public CLivres(String auteur, String maison, String genre, long isbn, String date, int numEdition, int numVolume) {
        this.isbn = isbn;
        this.auteur = auteur;
        this.maison = maison;
        this.genre = genre;
        this.date = date;
        this.numEdition = numEdition;
        this.numVolume = numVolume;

    }
    @Override
    public short getCatID () {
        return 0;
    }

    @Override
    public String getFormatSauvegarde() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(getCatID()));
        sj.add(auteur);
        sj.add(maison);
        sj.add(genre);
        sj.add(String.valueOf(isbn));
        sj.add(date);
        sj.add(String.valueOf(numEdition));
        sj.add(String.valueOf(numVolume));
        return sj.toString();
    }

    @Override
    public String getFormatDisplay() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Auteur: " + auteur);
        sj.add("Maison d'édition: " + maison);
        sj.add("Genre: " + genre);
        sj.add("ISBN: " + isbn);
        sj.add("Date de parution: " + date);
        sj.add("Numéro d'édition: " + numEdition);
        sj.add("Numéro de volume: " + numVolume);
        return sj.toString();
    }
}
