package unishop.Categories;

import java.util.StringJoiner;
/**
 * Représente un objet de la catégorie bureau dans un système de gestion de catégories.
 */
public class CBureau extends Categorie{

    public final static String[] sousCats = new String[]{"Chaise", "Lampe", "Support", "Bureau", "Autre"};

    private final String sousCat;
    private final String marque;
    private final String modele;
    /**
     * Cette méthode permet de créer une instance d'objet appartenant à la catégorie bureau
     * @param marque La marque de l'objet
     * @param modele Le modèle de l'objet
     * @param sousCat La sous-catégorie de l'objet
     */
    public CBureau (String marque, String modele, String sousCat) {
        this.sousCat = sousCat;
        this.marque = marque;
        this.modele = modele;
    }
    /**
     * Cette méthode retourne le ID de la catégorie
     * @return 4 qui est l'id de la catégorie Bureau
     */
    @Override
    public short getCatID() {
        return 4;
    }
    /**
     * Obtient les informations selon le format long de sauvegarde d'objet de la catégorie bureau
     * @return Les informations de l'objet de la catégorie
     */
    @Override
    public String getFormatSauvegarde() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(getCatID()));
        sj.add(modele);
        sj.add(marque);
        sj.add(sousCat);
        return sj.toString();
    }
    /**
     * Obtient les informations selon le format display de sauvegarde d'objet de la catégorie bureau
     * @return Les informations display de l'objet de la catégorie
     */
    @Override
    public String getFormatDisplay() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Modèle: " + modele);
        sj.add("Marque: " + marque);
        sj.add("Sous-Catégorie: " + sousCat);
        return sj.toString();
    }
}
