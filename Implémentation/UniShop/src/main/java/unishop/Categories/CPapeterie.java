package unishop.Categories;

import java.util.StringJoiner;

/**
* Représente un objet de la catégorie de Papeterie dans un système de gestion de catégories.
*/
public class CPapeterie extends Categorie{

    public final static String[] sousCats = new String[] {"Stylo", "Crayon", "Cahier", "Surligneur", "Calculatrice",
            "Classeur", "Papier", "Autre"};

    private final String sousCat;
    private final String marque;
    private final String modele;
    
    /**
     * Crée un objet de la catégorie papeterie
     * @param marque Marque de l'objet
     * @param modele Modèle de l'objet
     * @param sousCat Sous-catégorie de l'objet
     */
    public CPapeterie(String marque, String modele, String sousCat) {
        this.sousCat = sousCat;
        this.marque = marque;
        this.modele = modele;
    }
    
    /**
     * Cette méthode retourne le ID de la catégorie
     * @return 2 qui est l'id de la catégorie papeterie
     */
    @Override
    public short getCatID() {
        return 2;
    }
    /**
     * Obtient les informations selon le format long de sauvegarde d'objet de la catégorie papeterie
     * @return Les informations de l'objet de la catégorie
     */
    @Override
    public String getFormatSauvegarde() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(getCatID()));
        sj.add(marque);
        sj.add(modele);
        sj.add(sousCat);
        return sj.toString();
    }
    /**
     * Obtient les informations selon le format display de sauvegarde d'objet de la catégorie papeterie
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
