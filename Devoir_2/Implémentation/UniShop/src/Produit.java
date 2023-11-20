import java.util.ArrayList;
import java.util.StringJoiner;

public class Produit {

    final String nomReven;
    final String titre;
    final String description;
    final float prix;
    final int points;
    private int quantite;
    final String[] images;
    final String[] videos;
    final Categorie categorie;
    final ArrayList<String> likes;
    final ArrayList<Evaluation> evaluations;
    private float noteMoyenne;
    private int id;

    public Produit(String nomReven, String titre, String description, float prix, int quantite, int points,
                   String[] images, String[] videos, Categorie categorie, ArrayList<String> likes,
                   ArrayList<Evaluation> evaluations) {
        this.nomReven = nomReven;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.quantite = quantite;
        this.points = points;
        this.images = images.clone();
        this.videos = videos.clone();
        this.categorie = categorie;
        this.likes = new ArrayList<>(likes);
        this.evaluations = new ArrayList<>(evaluations);
        this.noteMoyenne = getNoteMoyenne();
    }
    public void save() {
        StringJoiner sj = new StringJoiner(",");
        sj.add(nomReven);
        sj.add(titre);
        sj.add(description);
        sj.add(String.valueOf(prix));
        sj.add(String.valueOf(quantite));
        sj.add(String.valueOf(points));
        sj.add(String.valueOf(noteMoyenne));
        String fst = sj.toString();

        sj = new StringJoiner("\n");
        sj.add(fst);
        sj.add(String.join(",", images));
        sj.add(String.join(",", videos));
        sj.add(categorie.getFormatSauvegarde());
        sj.add(String.join(",", likes));
        if(evaluations.isEmpty())
            sj.add("");
        for(Evaluation e : evaluations)
            sj.add(e.getSaveFormat());
        Main.ecrireFichierEntier(Main.PRODUITS_PATH + titre + Main.CSV, sj.toString());
    }
    public String getFormatDisplay() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(titre);
        sj.add("Images: " + String.join(",", images));
        sj.add("Vidéos: " + String.join(",", videos));
        sj.add(description);
        sj.add("Revendeur: " + nomReven);
        sj.add("Prix: " + prix + "$");
        sj.add(points + " points par unité");
        sj.add("Quantité en inventaire: " + quantite);
        sj.add(categorie.getFormatDisplay());
        sj.add(likes.size() + " likes");
        sj.add(evaluations.size() + " évaluations");
        if (!evaluations.isEmpty())
            sj.add("Note moyenne: " + noteMoyenne);
        return sj.toString();
    }
    public String getQuickDisplay() {
        StringJoiner sj = new StringJoiner("; ");
        sj.add(titre);
        sj.add(categorie.getCat());
        sj.add(prix + "$");
        sj.add(points + " points");
        sj.add(quantite + " disponibles");
        return sj.toString();
    }

    public int getId() {
        return id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setUniqueId(int id) {
        this.id = id;
    }
    public String liker(String nomAcheteur) {
        if (likes.contains(nomAcheteur))
            return "Vous avez déjà liké ce produit!";
        this.likes.add(nomAcheteur);
        save();
        return "Vous avez liké " + titre + "!";
    }
    public void addEvaluation(Evaluation e) {
        this.evaluations.add(e);
        this.noteMoyenne = getNoteMoyenne();
        save();
    }
    public float getNoteMoyenne() {
        float n = 0;
        for (Evaluation e : evaluations) {
            n = Main.arrondirPrix(n + e.note);
        }
        return Main.arrondirPrix(n / evaluations.size());
    }
    public String getEvaluationsDisplay() {
        if (evaluations.isEmpty())
            return "Ce produit n'a aucune évaluation pour le moment.";
        else {
            StringJoiner sj = new StringJoiner("\n\n");
            for (Evaluation e : evaluations)
                sj.add(e.getDisplayFormat());
            return sj.toString();
        }
    }
    public boolean aDesEvaluations() {return this.evaluations.isEmpty();}
    public ArrayList<String> voirLikes() {
        return new ArrayList<>(likes);
    }
    public String getLike(int i) {
        return likes.get(i);
    }
    public void commander() {
        --this.quantite;
        save();
    }
}
