package unishop;

import unishop.Categories.Categorie;
import static unishop.Main.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Cette classe représente un produit selon le nom du revendeur, le titre, la description, le prix, les points, la quantité, les images, les vidéos, la catégorie, les likes, les évaluations et la note moyenne.
 */
public class Produit {

    private final String nomReven;
    private final String titre;
    private final String description;
    private final float prix;
    private int points;
    private int quantite;
    final ArrayList<String> images;
    final ArrayList<String> videos;
    final Categorie categorie;
    final ArrayList<String> likes;
    final ArrayList<Evaluation> evaluations;
    private float noteMoyenne;
    private int id;

    /**
     * Crée un produit selon le nom du revendeur, le titre, la description, le prix, les points, la quantité, les images, les vidéos, la catégorie, les likes, les évaluations et la note moyenne.
     *
     * @param nomReven  Le nom du revendeur du produit
     * @param titre Le nom du produit
     * @param description La description du produit
     * @param prix Le prix du produit
     * @param quantite La quantité de produit disponible
     * @param points Le nombre de points
     * @param images L'ensemble d'image connexes aux produits
     * @param videos L'ensemble de vidéos  connexes aux produits
     * @param categorie La catégorie du produit
     * @param likes Les likes du produit
     * @param evaluations Les évaluations du produit
     */
    public Produit(String nomReven, String titre, String description, float prix, int quantite, int points,
                   String[] images, String[] videos, Categorie categorie, ArrayList<String> likes,
                   ArrayList<Evaluation> evaluations) {
        this.nomReven = nomReven;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.quantite = quantite;
        this.points = points;
        this.images = new ArrayList<>(Arrays.asList(images));
        this.videos = new ArrayList<>(Arrays.asList(videos));
        this.categorie = categorie;
        this.likes = new ArrayList<>(likes);
        this.evaluations = new ArrayList<>(evaluations);
        this.noteMoyenne = getNoteMoyenne();
    }

    /**
     * Renvoies le titre
     * @return le titre
     */
    public String getTitre() { return titre; }

    /** Renvoie le nom du revendeur
     * @return  Le nom du revendeur
     */
    public String getNomReven() { return nomReven; }

    /**
     * Renvoie le prix du produit
     * @return Le prix du produit
     */
    public float getPrix() { return prix; }
    /**
     * Cette méthode recense le nombre de points  obtenu par le produit
     * @return le nombre de points  obtenu par le produit
     */
    public int getPoints() {return this.points;}

    /**
     * Recense les likes
     * @return Les likes
     */
    public ArrayList<String> getLikes() { return new ArrayList<>(likes); }

    /**
     * Renvoies l'Id
     * @return L'id
     */
    public int getId() { return id; }

    /** Renvoies la quantité de produit
     * @return la quantité de produit
     */
    public int getQuantite() { return quantite; }

    /**
     * Renvoies les évaluations
     * @return Les évaluations
     */
    public ArrayList<Evaluation> getEvaluations() { return new ArrayList<>(evaluations); }

    /**
     * Modifie le id du produit
     * @param id L'id qu'on aimerait modifier
     */
    public void setUniqueId(int id) { this.id = id; }

    /**
     * Vérifie si le produit est en promotion
     * @return True si le produit est en promotion
     */
    public boolean estEnPromotion() {return this.points > Math.floor(prix);}

    /**
     * Cette méthode calcule et recense la note moyenne du produit
     * @return La note moyenne du produit
     */
    public float getNoteMoyenne() {
        float n = 0;
        for (Evaluation e : evaluations) {
            n = arrondirPrix(n + e.note);
        }
        return arrondirPrix(n / evaluations.size());
    }

    /**
     * Sauvegarde le produit
     */
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
        ecrireFichierEntier(PRODUITS_PATH + titre + CSV, sj.toString());
    }
    /**
     * Obtenir le format long des informations qui caractérise l'évaluation
     * @return le format long des informations qui caractérise l'évaluation
     */
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
    /**
     * Obtenir le format display des informations qui caractérise l'évaluation
     * @return le format display des informations qui caractérise l'évaluation
     */
    public String getQuickDisplay() {
        StringJoiner sj = new StringJoiner("; ");
        sj.add(titre);
        sj.add(categorie.getCat());
        sj.add(prix + "$");
        sj.add(points + " points");
        sj.add(quantite + " disponibles");
        if (id != 0)
            sj.add("ID: " + id);
        return sj.toString();
    }


    /**
     * Cette méthode permet de liker un acheteur
     * @param nomAcheteur nom de l'acheteur  à acheter
     * @return True si l'acheteur a été ajouté et false si il avait été déja acheté
     */
    public boolean liker(String nomAcheteur) {
        if (likes.contains(nomAcheteur))
            return false;
        this.likes.add(nomAcheteur);
        save();
        return true;
    }

    /**
     * Cette méthode permet d'ajouter une évaluation au produit
     * @param e l'évaluation à acheter
     */
    public void addEvaluation(Evaluation e) {
        this.evaluations.add(e);
        this.noteMoyenne = getNoteMoyenne();
        save();
    }

    /**
     * Cette méthode recense les évaluations d'un produit
     * @return Les évaluations d'un produit
     */
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

    /**
     * Cette méthode permet de commander un produit
     */
    public void commander() {
        --this.quantite;
        save();
    }

    /**
     * Cette méthode permet d'enlever une promotion
     */
    public void enleverPromotion() {
        this.points = (int) Math.floor(prix);
        save();
    }

    /**
     * Cette méthode permet de chnager une promotion
     * @param pts nombre de points à ajouter
     */
    public void changerPromotion(int pts) {
        this.points = pts;
        save();
    }

    /**
     * Permet de restocker un produit
     * @param quantite Le nombre à ajouter
     */
    public void restocker(int quantite) {
        this.quantite += quantite;
        save();
    }

    /**
     * Cette méthode permet d'ajouter des vidéos
     * @param vids les vidéos à ajouter au produit
     */
    public void ajouterVideos(String[] vids) {
        this.videos.addAll(Arrays.asList(vids));
        save();
    }

    /**
     * Cette méthode permet d'ajouter des imaages
     * @param imgs les images  à ajouter au produit
     */
    public void ajouterImages(String[] imgs) {
        this.videos.addAll(Arrays.asList(imgs));
        save();
    }
}