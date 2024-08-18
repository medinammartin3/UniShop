package unishop;

import java.util.StringJoiner;
import static unishop.Main.*;

/**
 * Cette classe représente un billet avec ses différents attributs tels que son id, nom, produit initial.
 */
public class Billet {
    public final int id;
    final String nomAche;
    final String produitInitial;
    final String probAche;
    final boolean estRetour;
    final String produitRempla;

    private boolean iniLivre; //Revendeur peut modifier
    private String probRev;

    private boolean remplaLivre; //Acheteur

    /**
     * Crée une intance de la classe billet
     *
     * @param id  L'id du billet
     * @param nomAche Le nom de l'acheteeur qui a initié le billet
     * @param produitInitial Le produit qui est à l'origine du billet
     * @param probAche Le problème rencontré par l'acheteur
     * @param estRetour Si le produit acheté à déja été retourné ou non
     * @param iniLivre Si le produit initiale a été livré au revendeur
     * @param probRev La solution proposé par le revendeur
     * @param produitRempla Si le produit à été remplacé
     * @param remplaLivre Si le remplacement du produit initial a été livré
     */

    public Billet(int id, String nomAche, String produitInitial, String probAche, boolean estRetour, boolean iniLivre,
                  String probRev, String produitRempla, boolean remplaLivre) {
        this.id = id;
        this.nomAche = nomAche;
        this.produitInitial = produitInitial;
        this.probAche = probAche;
        this.estRetour = estRetour;
        this.iniLivre = iniLivre;
        this.probRev = probRev;
        this.produitRempla = produitRempla;
        this.remplaLivre = remplaLivre;
    }

    /**
     * Cette méthode permet d'initier un objet livre
     * @return un objet livre
     */
    public boolean isIniLivre() { return iniLivre; }

    /**
     * Recense si le remplacement du produit initial a été livré
     * @return True si le remplacement du produit initial a été livré et False sinon
     */
    public boolean isRemplaLivre() {
        return remplaLivre;
    }


    /**
     * Regroupe les informations du billet
     * @return Un string qui contient les informations capitales du bilet
     */
    public String saveFormat() {
        return String.join(",", String.valueOf(id), nomAche, produitInitial, probAche,
                String.valueOf(estRetour), String.valueOf(iniLivre), probRev, produitRempla,
                String.valueOf(remplaLivre));
    }

    /** Résume l'état du billet
     * @return l'état du billet
     */
    public String afficher(){
        StringJoiner sj = new StringJoiner("\n");
        sj.add("ID: " + id);
        if (estRetour)
            sj.add("Retour d'un produit");
        else
            sj.add("Échange d'un produit");
        sj.add("Billet créé par: " + nomAche);
        sj.add("Produit problématique: " + produitInitial);
        sj.add("Description du problème par l'acheteur: " + probAche);
        if (probRev.isEmpty())
            sj.add("Le revendeur n'a pas encore ajouté de solution.");
        else
            sj.add("Solution du revendeur: " + probRev);
        if (iniLivre)
            sj.add(produitInitial + " a été livré à l'entrepôt.");
        else
            sj.add(produitInitial + " n'a pas encore été livré à l'entrepôt");
        if(!estRetour) {
            sj.add("Produit de remplacement: " + produitRempla);
            if(remplaLivre)
                sj.add(produitRempla + " a été livré à l'acheteur.");
            else
                sj.add(produitRempla + " n'a pas encore été livré à l'acheteur");
        }
        return sj.toString();
    }
    /**
     * Affiche le menu
     * @return Le menu
     */
    public String afficherMenu(){
        return "ID: " + id + "; Acheteur: " + nomAche + "; Produit problématique: " + produitInitial +
                (estRetour ? "; Retour" : "; Échange");
    }
    /**
     * Vérfie si le revendeur a mis en ligne une solution
     * @return True si le revendeur a mis à jour une solution, False sinon
     */
    public boolean aSolution() {return !probRev.isEmpty();}

    /** Met à jour la solution du revendeur
     * @param probRev La solution que propose le revendeur
     */
    public void setProbRev(String probRev) {
        this.probRev = probRev;
        save();
    }
    /**
     * Confirme la réception du produit initial par le revendeur
     *
     * @return True si le produit a été livré, False sinon
     */

    // TEST
    public boolean comfirmerLivraisonInitial() {
        if (this.iniLivre)
            return false;
        else {
            this.iniLivre = true;
            save();
            return true;
        }
    }
    /** Confirme la réception du produit de remplacement
     * @return True si le produit a été livré, False sinon
     */
    // TEST
    public boolean comfirmerLivraisonRempla() {
        if (this.remplaLivre)
            return false;
        else {
            this.remplaLivre = true;
            save();
            return true;
        }
    }

    /**
     * Sauvegarde les informations du billet
     */
    public void save() {
        ecrireFichierEntier(BILLETS_PATH + id + CSV, saveFormat());
    }
}