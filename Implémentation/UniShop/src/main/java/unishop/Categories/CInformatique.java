package unishop.Categories;

import java.util.StringJoiner;
/**
 * Représente un objet de la catégorie informatique dans un système de gestion de catégories.
 */
public class CInformatique extends Categorie{
    public final static String[] sousCats = new String[]{"Ordinateur", "Laptop", "Souris", "Clavier", "Disque dur",
            "Écouteurs", "Autre"};
    private final String sousCat;
    private final String marque;
    private final String modele;
    private final String date;

    /**
     * Cette méthode permet de créer une instance d'objet appartenant à la catégorie informatique
     * @param marque La marque de l'objet
     * @param modele Le modèle de l'objet
     * @param sousCat La sous-catégorie de l'objet
     * @param date La date de sortie de l'objet
     */
    public CInformatique (String marque, String modele, String sousCat, String date) {
        this.sousCat = sousCat;
        this.marque = marque;
        this.modele = modele;
        this.date = date;
    }
    
    /** Cette méthode retourne le ID de la catégorie
     * @return  3 qui est l'id de la catégorie informatique
     */
    @Override
    public short getCatID() {
        return 3;
    }

    /**
     * Obtient les informations selon le format long de sauvegarde d'objet de la catégorie inforamtique
     * @return Les informations de l'objet de la catégorie
     */
    @Override
    public String getFormatSauvegarde() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(getCatID()));
        sj.add(marque);
        sj.add(modele);
        sj.add(sousCat);
        sj.add(date);
        return sj.toString();
    }
    /**
     * Obtient les informations selon le format display de sauvegarde d'objet de la catégorie informatique
     * @return Les informations display de l'objet de la catégorie
     */
    @Override
    public String getFormatDisplay() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Modèle: " + modele);
        sj.add("Marque: " + marque);
        sj.add("Date de sortie: " + date);
        sj.add("Sous-Catégorie: " + sousCat);
        return sj.toString();
    }
}
