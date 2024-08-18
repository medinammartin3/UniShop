package unishop.Categories;

import java.util.StringJoiner;
/**
 * Représente un objet de la catégorie livre dans un système de gestion de catégories.
 */
public class CLivres extends Categorie{

    public final static String[] genres = new String[] {"Roman", "Documentaire", "Bande dessinée",
    "Manuel", "Autre"};

    private final String auteur;
    private final String maison;
    private final String genre;
    private final long isbn;
    private final String date;
    private final int numEdition;
    private final int numVolume;

    /** Crée un objet de la catégorie livres
     * @param auteur L'auteur du livre
     * @param maison La maison d'édition du livre
     * @param genre Le genre du livre
     * @param isbn Le isbn du livre
     * @param date La date de parution du livre
     * @param numEdition Le numéro d'édition du livre
     * @param numVolume Le numéro de volume du livre
     */

    public CLivres(String auteur, String maison, String genre, long isbn, String date, int numEdition, int numVolume) {
        this.isbn = isbn;
        this.auteur = auteur;
        this.maison = maison;
        this.genre = genre;
        this.date = date;
        this.numEdition = numEdition;
        this.numVolume = numVolume;
        
    /**
     * Cette méthode retourne le ID de la catégorie
     * @return 30 qui est l'id de la catégorie livre
     */
    }
    @Override
    public short getCatID () {
        return 0;
    }
    /**
     * Obtient les informations selon le format long de sauvegarde d'objet de la catégorie livre
     * @return Les informations de l'objet de la catégorie
     */
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
    /**
     * Obtient les informations selon le format display de sauvegarde d'objet de la catégorie livre
     * @return Les informations display de l'objet de la catégorie
     */
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
