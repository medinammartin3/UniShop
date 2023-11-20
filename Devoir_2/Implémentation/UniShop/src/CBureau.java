import java.util.StringJoiner;

public class CBureau extends Categorie{

    final static String[] sousCats = new String[]{"Chaise", "Lampe", "Support", "Bureau", "Autre"};

    private final String sousCat;
    private final String marque;
    private final String modele;

    public CBureau (String marque, String modele, String sousCat) {
        this.sousCat = sousCat;
        this.marque = marque;
        this.modele = modele;
    }

    @Override
    public short getCatID() {
        return 4;
    }

    @Override
    public String getFormatSauvegarde() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(String.valueOf(getCatID()));
        sj.add(modele);
        sj.add(marque);
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
