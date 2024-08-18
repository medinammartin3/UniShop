package unishop.Categories;

import java.util.StringJoiner;

/**
 * Représente un objet de la catégorie ressource dans un système de gestion de catégories.
 */

public class CRessources extends Categorie{

    public final static String[] types = new String[] {"Ressource papier", "Ressource en ligne"};

    private final String type;
    private final long isbn;
    private final String auteur;
    private final String organisation;
    private final String date;
    private final int numEdition;

    /**
     * Constructeur de la classe CRessources.
     *
     * @param auteur      L'auteur de la ressource.
     * @param organisation L'organisation liée à la ressource.
     * @param type        Le type de ressource.
     * @param isbn        Le numéro ISBN de la ressource.
     * @param date        La date de parution de la ressource.
     * @param numEdition  Le numéro d'édition de la ressource.
     */

    public CRessources(String auteur, String organisation, String type, long isbn, String date, int numEdition) {
        this.isbn = isbn;
        this.auteur = auteur;
        this.organisation = organisation;
        this.date = date;
        this.type = type;
        this.numEdition = numEdition;
    }
    /**
     * Cette méthode retourne le ID de la catégorie
     * @return 1 qui est l'id de la catégorie ressource
     */
    @Override
    public short getCatID() {
        return 1;
    }

    /**
     * Obtient les informations selon le format long de sauvegarde d'objet de la catégorie ressource
     * @return Les informations de l'objet de la catégorie
     */
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
    /**
     * Obtient les informations selon le format display de sauvegarde d'objet de la catégorie ressource
     * @return Les informations display de l'objet de la catégorie
     */
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
