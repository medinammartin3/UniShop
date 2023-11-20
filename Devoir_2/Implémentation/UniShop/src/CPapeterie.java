import java.util.StringJoiner;

public class CPapeterie extends Categorie{

    final static String[] sousCats = new String[] {"Stylo", "Crayon", "Cahier", "Surligneur", "Calculatrice",
            "Classeur", "Papier", "Autre"};

    private final String sousCat;
    private final String marque;
    private final String modele;

    public CPapeterie(String marque, String modele, String sousCat) {
        this.sousCat = sousCat;
        this.marque = marque;
        this.modele = modele;
    }

    @Override
    public short getCatID() {
        return 2;
    }

    @Override
    public String getFormatSauvegarde() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(getCatID()));
        sj.add(marque);
        sj.add(modele);
        sj.add(sousCat);
        return sj.toString();
    }

    @Override
    public String getFormatDisplay() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("Modèle: " + modele);
        sj.add("Marque: " + marque);
        sj.add("Sous-Catégorie: " + sousCat);
        return sj.toString();
    }
}
