package unishop.Users;

import unishop.*;
import unishop.Produit;

import java.util.ArrayList;
import java.util.Stack;
import java.util.StringJoiner;

public class Acheteur extends User implements Comparable<Acheteur>{

    private String nom;
    private String prenom;
    private int points;
    private final ArrayList<String> acheteursSuivis;
    private final ArrayList<String> suiveurs;
    private final ArrayList<String> revendeursLikes;
    public final Commande panier;

    public Acheteur(String u, String p, String em, long phone, String address, String nom,
                    String prenom, int points, ArrayList<String> acheteursSuivis,
                    ArrayList<String> revendeursLikes, ArrayList<Billet> b, Commande panier, ArrayList<Commande> cmds,
                    Stack<Notification> ns){
        super(u, p, em, phone, address, b, cmds, ns);
        this.nom = nom;
        this.prenom = prenom;
        this.points = points;
        this.acheteursSuivis = new ArrayList<>(acheteursSuivis);
        this.revendeursLikes = new ArrayList<>(revendeursLikes);
        this.suiveurs = new ArrayList<>(); //Suiveurs en input
        this.panier = panier;
    }
    public ArrayList<String> getFollowers() {
        return new ArrayList<>(suiveurs);
    }
    public ArrayList<String> getSuivis() {
        return new ArrayList<>(acheteursSuivis);
    }
    public int getPoints() {return points;}

    private int getNbProduitsCommandes() {
        int i = 0;
        for (Commande c : commandes) {
            i += c.getProduits().size();
        }
        return i;
    }
    public void setNom (String nom) {
        this.nom = nom;
        save();
    }

    public void setPrenom (String prenom) {
        this.prenom = prenom;
        save();
    }

    @Override
    public boolean isAcheteur() {
        return true;
    }

    @Override
    public void save() {
        StringJoiner sj = new StringJoiner("\n");
        String[] infos = new String[] {this.password, this.email, String.valueOf(this.phone), this.address, nom,
                prenom, String.valueOf(points)};
        sj.add(String.join(",", infos));
        sj.add(String.join(",", acheteursSuivis));
        sj.add(String.join(",", revendeursLikes));
        sj.add(formatSaveCommande());
        sj.add(formatSaveBillet());
        sj.add(formatSaveNotifications());
        Main.ecrireFichierEntier(Main.ACHETEURS_PATH + this.username + "/Infos.csv", sj.toString());
    }

    @Override
    public void ajouterCommande(Commande c) {
        commandes.add(c);
        points += c.getPointsTotal();
        save();
    }

    @Override
    public String afficherMetriques() {
        return "\nNombre de points: " + points + "\nNombre de produits commandés: " + getNbProduitsCommandes() +
                "\nNombre total de commandes effectuées: " + commandes.size() +
                "\nNombre de followers: " + suiveurs.size();
    }

    @Override
    public int compareTo(Acheteur a){
        if (this.points > a.points)
            return 1;
        else if (this.points < a.points)
            return -1;
        return 0;
    }

    public short suivre(String acheteur) {
        if (this.username.equals(acheteur))
            return 2;
        else if (acheteursSuivis.contains(acheteur))
            return 1;
        else {
            acheteursSuivis.add(acheteur);
            save();
            return 0;
        }
    }

    public boolean aAcheteProduit(String nomProduit) {
        for(Commande c : commandes) {
            for(Produit p : c.getProduits()) {
                if (nomProduit.equals(p.getTitre()))
                    return true;
            }
        }
        return false;
    }

    public void ajouterPoints(int pts) {
        this.points += pts;
        save();
    }

    public int viderPoints() {
        int pts = this.points;
        this.points = 0;
        save();
        return pts;
    }

    public boolean billetExiste(int id) {
        for (Billet b : this.billets){
            if (b.id == id)
                return true;
        }
        return false;
    }
}