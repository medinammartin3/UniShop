import java.util.StringJoiner;

public class CInformatique extends Categorie{
    final static String[] sousCats = new String[]{"Ordinateur", "Laptop", "Souris", "Clavier", "Disque dur",
            "Écouteurs", "Autre"};
    private final String sousCat;
    private final String marque;
    private final String modele;
    private final String date;
    public CInformatique (String marque, String modele, String sousCat, String date) {
        this.sousCat = sousCat;
        this.marque = marque;
        this.modele = modele;
        this.date = date;
    }
    @Override
    public short getCatID() {
        return 3;
    }

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
